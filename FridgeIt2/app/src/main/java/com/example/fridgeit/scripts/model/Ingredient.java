package com.example.fridgeit.scripts.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("strIngredient")
    private String name;

    // Getters and setters
    public String getName()
    {
        return name;
    }
}