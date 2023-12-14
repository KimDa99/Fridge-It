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
import java.util.Collections;
import java.util.Comparator;
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
    boolean[] checkedIngredients;
    private ArrayList<String> userSelectedIngredients = new ArrayList<>();

    private List<Meal> meals = new ArrayList<>();
    private List<Meal> recommendedMeals = new ArrayList<>();

    private int apiCallCounter = 0;
    private final int totalApiCalls = 26; // For letters A to Z


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* [Setting up Ingredients] */

        // get all ingredients from api
        //updateAllIngredient();
        checkAndFetchIngredient();
        updateSelectedIngredientsView();
        // Initialize the boolean array
        checkedIngredients = new boolean[allIngredients.size()];
        updateCheckedIngredients(allIngredients, userSelectedIngredients);

        // Set up the Select Ingredients Button
        Button selectIngredientsButton = findViewById(R.id.select_Ingredients_Button);
        selectIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredientSelectionDialog();
            }
        });

        // fetch meal info from apis
        checkAndFetchMeals();

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
        saveIngredientsToFile(allIngredients, userSelectedIngredients);
        TextView selectedIngredientsText = findViewById(R.id.selected_Ingredients_text);
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (String ingredient : userSelectedIngredients) {
            ingredientsBuilder.append(ingredient).append(", ");
        }
        if (ingredientsBuilder.length() > 0) {
            ingredientsBuilder.setLength(ingredientsBuilder.length() - 2);
        }
        selectedIngredientsText.setText(ingredientsBuilder.toString());

        if(userSelectedIngredients.isEmpty()) selectedIngredientsText.setText("None");

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
        for(Meal meal : meals){
            meal.getIncludedIngredientMeasure(userSelectedIngredients);
            meal.getExcludedIngredientMeasure(userSelectedIngredients);
            meal.processIngredientsAndMeasures();
        }
        recommendedMeals.clear();
        recommendedMeals = sortMealsByExcludedIngredients(meals);

        populateRecipePanels();
    }

    private void checkAndFetchMeals() {
        List<Meal> loadedMeals = loadMealsFromFolder();

        if (loadedMeals.isEmpty()) {
            for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
                fetchMealsByFirstLetter(alphabet);
            }
        } else {
            // Meals are already fetched, load from the file
            showToastMessage("Meal is loaded");

            meals = loadedMeals;
            processMealsAndPopulateRecipes();
        }
    }

    private void saveMealsToFolder(List<Meal> meals) {

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

            showToastMessage("meals Saved!");
        } catch (IOException e) {
            e.printStackTrace();
            showToastMessage("meals un-Saved!");
        }

    }
    private List<Meal> loadMealsFromFolder() {

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

            showToastMessage("Meals Load!");
        } catch (IOException e) {
            e.printStackTrace();
            showToastMessage("Meals Load Failed");
        }

        return loadedMeals;
    }

    private void saveIngredientsToFile(List<String> allIngredients, List<String> selectedIngredients) {
        try {
            File ingredientsFolder = new File(getFilesDir(), "ingredients");
            if (!ingredientsFolder.exists()) {
                ingredientsFolder.mkdir();
            }

            // Save all ingredients
            File allIngredientsFile = new File(ingredientsFolder, "allIngredients.json");
            try (FileOutputStream allIngredientsFos = new FileOutputStream(allIngredientsFile)) {
                String allIngredientsJson = new Gson().toJson(allIngredients);
                allIngredientsFos.write(allIngredientsJson.getBytes());
            }

            // Save selected ingredients
            File selectedIngredientsFile = new File(ingredientsFolder, "selectedIngredients.json");
            try (FileOutputStream selectedIngredientsFos = new FileOutputStream(selectedIngredientsFile)) {
                String selectedIngredientsJson = new Gson().toJson(selectedIngredients);
                selectedIngredientsFos.write(selectedIngredientsJson.getBytes());
            }

            showToastMessage("Ingredients Saved!");
        } catch (IOException e) {
            e.printStackTrace();
            showToastMessage("Ingredients un-Saved!");
        }

    }

    private List<String> loadIngredientsFromFile(String fileName) {

        try (FileInputStream fis = openFileInput(fileName)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Type type = new TypeToken<List<String>>() {}.getType();
            showToastMessage("Load Ingredients");
            return new Gson().fromJson(json, type);
        } catch (IOException e) {
            showToastMessage("Failed to Load Ingredients!");

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void checkAndFetchIngredient()
    {
        List<String> loadedWholeIngredient = loadIngredientsFromFolder();

        if(loadedWholeIngredient.isEmpty())
        {
            showToastMessage("No Loaded Ingredients");
            updateAllIngredient();
            saveIngredientsToFile(allIngredients, userSelectedIngredients);
        }
        else
        {
            showToastMessage("Ingredients Loaded?");
            allIngredients = loadedWholeIngredient;

            List<String> loadedSelectedIngredients = loadSelectedIngredientsFromFolder();
            if (!loadedSelectedIngredients.isEmpty()) {
                userSelectedIngredients = (ArrayList<String>) loadedSelectedIngredients;
                updateSelectedIngredientsView(); // Update the selected ingredients view if needed
            }
        }
    }

    private void showToastMessage(String string)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        CharSequence text = string;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private List<String> loadSelectedIngredientsFromFolder(){
        List<String> loadedSelectedIngredients = new ArrayList<>();
        try {
            File ingredientsFolder = new File(getFilesDir(), "ingredients");
            if (ingredientsFolder.exists()) {
                // Load selected ingredients
                File selectedIngredientsFile = new File(ingredientsFolder, "selectedIngredients.json");
                try (FileInputStream selectedIngredientsFis = new FileInputStream(selectedIngredientsFile)) {
                    InputStreamReader isr = new InputStreamReader(selectedIngredientsFis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    String selectedIngredientsJson = sb.toString();
                    Type type = new TypeToken<List<String>>() {}.getType();
                    loadedSelectedIngredients = new Gson().fromJson(selectedIngredientsJson, type);
                }
            }

            showToastMessage("Ingredients Load!");
        } catch (IOException e) {
            e.printStackTrace();
            showToastMessage("Ingredients Load Failed!");
        }
        return loadedSelectedIngredients;
    }
    private List<String> loadIngredientsFromFolder() {
        List<String> loadedAllIngredients = new ArrayList<>();

        try {
            File ingredientsFolder = new File(getFilesDir(), "ingredients");
            if (ingredientsFolder.exists()) {
                // Load all ingredients
                File allIngredientsFile = new File(ingredientsFolder, "allIngredients.json");
                try (FileInputStream allIngredientsFis = new FileInputStream(allIngredientsFile)) {
                    InputStreamReader isr = new InputStreamReader(allIngredientsFis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    String allIngredientsJson = sb.toString();
                    Type type = new TypeToken<List<String>>() {}.getType();
                    loadedAllIngredients = new Gson().fromJson(allIngredientsJson, type);
                }
            }

            showToastMessage("Ingredients Load!");
        } catch (IOException e) {
            e.printStackTrace();
            showToastMessage("Ingredients Load Failed!");
        }

        return loadedAllIngredients;
    }

    private void updateCheckedIngredients(List<String> allIngredients, List<String> userSelectedIngredients) {
        checkedIngredients = new boolean[allIngredients.size()];

        for (int i = 0; i < allIngredients.size(); i++) {
            String currentIngredient = allIngredients.get(i);
            checkedIngredients[i] = userSelectedIngredients.contains(currentIngredient);
        }
    }

    private List<Meal> sortMealsByExcludedIngredients(List<Meal> meals) {
        List<Meal> sortedMeals = new ArrayList<>();

        // Calculate scores for each meal based on excluded ingredients
        for (Meal meal : meals) {
            meal.getExcludedIngredientMeasure(userSelectedIngredients);
//            meal.setExcludedIngredientScore(excludedIngredientsCount);
        }

        // Sort meals based on scores
        Collections.sort(meals, new Comparator<Meal>() {
            @Override
            public int compare(Meal meal1, Meal meal2) {
                // Ascending order, modify if you want descending
                return Integer.compare(meal1.getExcludedIngredientScore(), meal2.getExcludedIngredientScore());
            }
        });

        // Add sorted meals to the result list
        sortedMeals.addAll(meals);

        return sortedMeals;
    }

}
