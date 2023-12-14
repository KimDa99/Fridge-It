package com.example.fridgeit.scripts;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fridgeit.R;
import com.example.fridgeit.scripts.model.Recipe;
import com.example.fridgeit.scripts.model.RecipeListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailedRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_recipe);

        String recipeId = getIntent().getStringExtra("RECIPE_ID");
        if (recipeId != null) {
            fetchRecipeDetails(recipeId);
        }
    }

    private void fetchRecipeDetails(String recipeId) {
        TheMealDBApi apiService = RetrofitClientInstance.getRetrofitInstance().create(TheMealDBApi.class);
        apiService.lookupMealById(recipeId).enqueue(new Callback<RecipeListResponse>() {
            @Override
            public void onResponse(Call<RecipeListResponse> call, Response<RecipeListResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getRecipes().isEmpty()) {
                    Recipe recipe = response.body().getRecipes().get(0);
                    updateUI(recipe);
                }
            }

            @Override
            public void onFailure(Call<RecipeListResponse> call, Throwable t) {
                // Handle the error (show error message or log it)
            }
        });
    }

    private void updateUI(Recipe recipe) {
        TextView recipeName = findViewById(R.id.recipe_name);
        ImageView recipeImage = findViewById(R.id.recipe_image);
        TextView ingredientsList = findViewById(R.id.ingredients_list);
        TextView recipeInstructions = findViewById(R.id.recipe_instructions_1);

        recipeName.setText(recipe.getName());
        // Load image using a library like Glide or Picasso
        Picasso.get().load(recipe.getImage()).into(recipeImage);

        // Format and set ingredients and instructions
        recipe.processIngredientsAndMeasures();
        List<String> ingredient_and_Measure = new ArrayList<>();
        ingredient_and_Measure.addAll(recipe.getIngredientMeasure());

        String ingredientString ="";
        for(String string : ingredient_and_Measure)
        {
            ingredientString += string + ", ";
        }
        ingredientsList.setText(ingredientString);

        recipeInstructions.setText(recipe.getInstruction());

        // Initialize VideoView
        VideoView videoView = findViewById(R.id.recipe_video);

        // Get the YouTube video URL from the Recipe object
        String videoUrl = recipe.getStrYoutubeUrl(); // Update with the actual method you have for getting the video URL

        // Check if the video URL is not null or empty
        if (videoUrl != null && !videoUrl.isEmpty()) {
            // Set the video URI
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);

            // Start playing the video
//            videoView.start();
        } else {
            // Hide the VideoView or show a message indicating no video is available
            videoView.setVisibility(View.GONE); // You can set visibility according to your UI/UX
        }
    }
}
