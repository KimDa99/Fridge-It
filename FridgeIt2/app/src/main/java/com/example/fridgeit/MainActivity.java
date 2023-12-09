package com.example.fridgeit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.example.fridgeit.scripts.DetailedRecipeActivity;
import com.example.fridgeit.scripts.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] allIngredients = {"Tomatoes", "Cheese", "Basil", "Olive Oil", "Garlic", "Onions", "Chicken", "Beef"};
    boolean[] checkedIngredients;
    ArrayList<String> userSelectedIngredients = new ArrayList<>();

    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of dynamically adding views to the recommended recipes layout
        LinearLayout recipesLayout = findViewById(R.id.recommended_recipies_Layout);

        // You will populate this dynamically based on selected ingredients and available recipes
        for (int i = 0; i < 5; i++) {
            TextView recipeView = new TextView(this);
            recipeView.setText("Recipe " + (i + 1));
            recipeView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            recipesLayout.addView(recipeView);
        }

        // Initialize the boolean array
        checkedIngredients = new boolean[allIngredients.length];

        // Set up the Select Ingredients Button
        Button selectIngredientsButton = findViewById(R.id.select_Ingredients_Button);
        selectIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredientSelectionDialog();
            }
        });

        List<String> ingredients = new ArrayList<>();
        ingredients.add("Tomato");
        ingredients.add("potatoes");
        Recipe testRecipe = new Recipe("id", "name", ingredients);
        recipes.add(testRecipe);
        recipes.add(testRecipe);
        recipes.add(testRecipe);
        recipes.add(testRecipe);
        recipes.add(testRecipe);

        populateRecipePanels(recipes);

    }
    private void showIngredientSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Ingredients");

        builder.setMultiChoiceItems(allIngredients, checkedIngredients, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Update the current focused item's checked status
                checkedIngredients[which] = isChecked;

                // Get the current focused item
                String currentItem = allIngredients[which];

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
    }

    private void populateRecipePanels(List<Recipe> recipes)
    {
        LinearLayout recipesLayout = findViewById(R.id.recommended_recipies_Layout);
        recipesLayout.removeAllViews();

        for (Recipe recipe : recipes) {
            View recipePanel = LayoutInflater.from(this).inflate(R.layout.recipe_panel_layout, recipesLayout, false);

            TextView recipeName = recipePanel.findViewById(R.id.recipe_name);
            ImageView recipeImage = recipePanel.findViewById(R.id.recipe_image);
            TextView recipeIngredients = recipePanel.findViewById(R.id.recipe_ingredients);

            recipeName.setText(recipe.strMeal);
            // Load image using a library like Glide or Picasso
            // Glide.with(this).load(recipe.strMealThumb).into(recipeImage);
            recipeIngredients.setText("/* Concatenate ingredients here */");

            recipePanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent to start DetailedRecipeActivity
                    Intent intent = new Intent(MainActivity.this, DetailedRecipeActivity.class);
                    intent.putExtra("RECIPE_ID", recipe.idMeal);
                    startActivity(intent);
                }
            });

            recipesLayout.addView(recipePanel);
        }
    }
}
