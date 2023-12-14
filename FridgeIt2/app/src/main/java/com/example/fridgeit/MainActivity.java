package com.example.fridgeit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.fridgeit.scripts.DetailedRecipeActivity;
import com.example.fridgeit.scripts.RetrofitClientInstance;
import com.example.fridgeit.scripts.TheMealDBApi;
import com.example.fridgeit.scripts.model.Ingredient;
import com.example.fridgeit.scripts.model.IngredientListResponse;
import com.example.fridgeit.scripts.model.Meal;
import com.example.fridgeit.scripts.model.Recipe;
import com.example.fridgeit.scripts.model.MealListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private List<String> allIngredients = new ArrayList<>();
//    String[] allIngredients = {"Tomatoes", "Cheese", "Basil", "Olive Oil", "Garlic", "Onions", "Chicken", "Beef"};
    boolean[] checkedIngredients;
    ArrayList<String> userSelectedIngredients = new ArrayList<>();

    private List<Meal> meals = new ArrayList<>();
    private List<Meal> recommendedMeals = new ArrayList<>();

    private int apiCallCounter = 0;
    private final int totalApiCalls = 26; // For letters A to Z

    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* [Setting up Ingredients] */

        // get all ingredients from api
        updateAllIngredient();

        // Initialize the boolean array
        checkedIngredients = new boolean[allIngredients.size()];

        // Set up the Select Ingredients Button
        Button selectIngredientsButton = findViewById(R.id.select_Ingredients_Button);
        selectIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredientSelectionDialog();
            }
        });

        /* [Setting up possible Recipes] */
        // fetch meal info from apis
        meals.clear();
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++)
        {
            fetchMealsByFirstLetter(alphabet);
        }

    }

    private void updateAllIngredient()
    {
        // Initialize API service
        TheMealDBApi apiService = RetrofitClientInstance.getRetrofitInstance().create(TheMealDBApi.class);

        // Fetch ingredients
        apiService.listAllIngredients().enqueue(new Callback<IngredientListResponse>() {
            @Override
            public void onResponse(Call<IngredientListResponse> call, Response<IngredientListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allIngredients.clear();
                    for(Ingredient ingredient : response.body().getIngredients())
                    {
                        allIngredients.add(ingredient.getName());
                    }

                    checkedIngredients = new boolean[allIngredients.size()];
                }
            }

            @Override
            public void onFailure(Call<IngredientListResponse> call, Throwable t) {
                // TODO: Handle failure
            }
        });
    }

    private void fetchMealsByFirstLetter(char letter) {
        TheMealDBApi apiService = RetrofitClientInstance.getRetrofitInstance().create(TheMealDBApi.class);

        apiService.searchMealsByFirstLetter(String.valueOf(letter)).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> fetchedMeals = response.body().getMeals();
                    if (fetchedMeals != null) { // Check if fetchedMeals is not null
                        meals.addAll(fetchedMeals);
                    }
                }
                apiCallCompleted();
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                apiCallCompleted();
            }
        });
    }
    private void showIngredientSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Ingredients");

        String[] ingredientArray = allIngredients.toArray(new String[allIngredients.size()]);

        builder.setMultiChoiceItems(ingredientArray, checkedIngredients, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Update the current focused item's checked status
                checkedIngredients[which] = isChecked;

                // Get the current focused item
                String currentItem = ingredientArray[which];

                // Add or remove the item from the list of selected items
                if (isChecked) {
                    userSelectedIngredients.add(currentItem);
                } else {
                    userSelectedIngredients.remove(currentItem);
                }
            }
        });

        // Set the action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, update the selected ingredients view
                updateSelectedIngredientsView();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, no action needed
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateSelectedIngredientsView() {
        TextView selectedIngredientsText = findViewById(R.id.selected_Ingredients_text);
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (String ingredient : userSelectedIngredients) {
            ingredientsBuilder.append(ingredient).append(", ");
        }
        if (ingredientsBuilder.length() > 0) {
            ingredientsBuilder.setLength(ingredientsBuilder.length() - 2);
        }
        selectedIngredientsText.setText(ingredientsBuilder.toString());

        processMealsAndPopulateRecipes();
    }

    private void populateRecipePanels()
    {
        LinearLayout recipesLayout = findViewById(R.id.recommended_recipies_Layout);
        recipesLayout.removeAllViews();

        for (Meal meal : recommendedMeals) {
            View recipePanel = LayoutInflater.from(this).inflate(R.layout.recipe_panel_layout, recipesLayout, false);

            TextView recipeName = recipePanel.findViewById(R.id.recipe_name);
            ImageView recipeImage = recipePanel.findViewById(R.id.recipe_image);
            TextView recipeIngredients = recipePanel.findViewById(R.id.recipe_ingredients);
            TextView recipeExcludedIngredients = recipePanel.findViewById(R.id.recipe_ingredients_excluded);

            recipeName.setText(meal.getName());
            // Load image using a library like Glide or Picasso
            // Glide.with(this).load(recipe.strMealThumb).into(recipeImage);
            String strRecipeIngredients = "";
            for (String string : meal.getIncludedIngredientMeasure(userSelectedIngredients)) {
                strRecipeIngredients += string + ", ";
            }
            recipeIngredients.setText(strRecipeIngredients);

            String strRecipeExcludedIngredients = "";
            for (String string : meal.getExcludedIngredientMeasure(userSelectedIngredients)) {
                strRecipeExcludedIngredients += string + ", ";
            }
            recipeExcludedIngredients.setText(strRecipeExcludedIngredients);

            Picasso.get().load(meal.getImage()).into(recipeImage);

            recipePanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent to start DetailedRecipeActivity
                    Intent intent = new Intent(MainActivity.this, DetailedRecipeActivity.class);
                    intent.putExtra("RECIPE_ID", meal.idMeal);
                    startActivity(intent);
                }
            });

            recipesLayout.addView(recipePanel);
        }
    }
    private void apiCallCompleted() {
        apiCallCounter++;
        if (apiCallCounter == totalApiCalls) {
            processMealsAndPopulateRecipes();
        }
    }

    private void processMealsAndPopulateRecipes() {
        for(Meal meal : meals){ meal.processIngredientsAndMeasures();}

        recommendedMeals.clear();
        recommendedMeals.addAll(meals);

        populateRecipePanels();
    }
}
