package com.example.fridgeit.scripts.model;

import androidx.collection.CircularArray;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class RecipeListResponse {
    @SerializedName("meals")
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
