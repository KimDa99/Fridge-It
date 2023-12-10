package com.example.fridgeit.scripts.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class RecipeListResponse {
    @SerializedName("meals")
    private List<Recipe> ingredients;
}
