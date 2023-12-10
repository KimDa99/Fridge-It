package com.example.fridgeit.scripts.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IngredientListResponse {
    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    // Getters and setters
}
