package com.example.fridgeit.scripts.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class MealListResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    // Getters and setters
    public List<Meal> getMeals()
    {
        return meals;
    }
}
