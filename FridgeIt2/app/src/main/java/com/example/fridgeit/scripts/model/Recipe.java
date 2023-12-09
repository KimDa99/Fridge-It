package com.example.fridgeit.scripts.model;

import java.util.List;

public class Recipe {
    public String idMeal;
    public String strMeal;

    public List<String> strIngredientList;

    public Recipe(String mealId, String mealName, List<String> ingredientList)
    {
        idMeal = mealId;
        strMeal = mealName;
        strIngredientList = ingredientList;
    }
}
