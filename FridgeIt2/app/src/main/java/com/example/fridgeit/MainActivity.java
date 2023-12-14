package com.example.fridgeit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        checkAndFetchMeals();
        /*
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++)
        {
            fetchMealsByFirstLetter(alphabet);
        }*/

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
            saveMealsToFolder(meals);
            processMealsAndPopulateRecipes();
        }
    }

    private void processMealsAndPopulateRecipes() {
        for(Meal meal : meals){ meal.processIngredientsAndMeasures();}

        recommendedMeals.clear();
        recommendedMeals.addAll(meals);

        populateRecipePanels();
    }

    private void saveMealsToFile(List<Meal> meals) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        CharSequence text = "!!!";


        String json = new Gson().toJson(meals);

        try (FileOutputStream fos = openFileOutput("meals.json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
            text = "Save!";
        } catch (IOException e) {
            e.printStackTrace();
            text = "Un-Save!";
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    private void checkAndFetchMeals() {
        List<Meal> loadedMeals = loadMealsFromFolder();

        if (loadedMeals.isEmpty()) {
            for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
                fetchMealsByFirstLetter(alphabet);
            }
        } else {
            // Meals are already fetched, load from the file
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            CharSequence text = "ONLY LOAD";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            meals = loadedMeals;
            processMealsAndPopulateRecipes();
        }
    }

    // Load meals from a file
    private List<Meal> loadMealsFromFile() {
        Context context = getApplicationContext();
        CharSequence text = "Load!";
        int duration = Toast.LENGTH_LONG;


        try (FileInputStream fis = openFileInput("meals.json")) {
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Type type = new TypeToken<List<Meal>>() {}.getType();
            return new Gson().fromJson(json, type);
        } catch (IOException e) {
            text = "Load Failed!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveMealsToFolder(List<Meal> meals) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        CharSequence text;

        try {
            File mealsFolder = new File(getFilesDir(), "meals");
            if (!mealsFolder.exists()) {
                mealsFolder.mkdir();
            }

            for (Meal meal : meals) {
                File mealFile = new File(mealsFolder, meal.idMeal + ".json");
                try (FileOutputStream fos = new FileOutputStream(mealFile)) {
                    String json = meal.toJson();
                    fos.write(json.getBytes());
                }
            }

            text = "Save!";
        } catch (IOException e) {
            e.printStackTrace();
            text = "Un-Save!";
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private List<Meal> loadMealsFromFolder() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        CharSequence text;

        List<Meal> loadedMeals = new ArrayList<>();

        try {
            File mealsFolder = new File(getFilesDir(), "meals");
            if (mealsFolder.exists()) {
                File[] mealFiles = mealsFolder.listFiles();

                if (mealFiles != null) {
                    for (File mealFile : mealFiles) {
                        try (FileInputStream fis = new FileInputStream(mealFile)) {
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();
                            String line;

                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                            String json = sb.toString();
                            Meal meal = Meal.fromJson(json);
                            loadedMeals.add(meal);
                        }
                    }
                }
            }

            text = "Load!";
        } catch (IOException e) {
            e.printStackTrace();
            text = "Load Failed!";
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        return loadedMeals;
    }


}
