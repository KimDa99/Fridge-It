package com.example.fridgeit.scripts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fridgeit.R;

public class DetailedRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_recipe);

        String recipeId = getIntent().getStringExtra("RECIPE_ID");
        // Fetch and display the recipe details using the recipeId
    }
}