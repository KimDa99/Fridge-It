package com.example.fridgeit.scripts.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Meal {

    @SerializedName("strMeal")
    private String name;
    @SerializedName("idMeal")
    public String idMeal;
    @SerializedName("strMealThumb")
    private String image;

    //region ingredients
    @SerializedName("strIngredient1") private String strIngredient1;
    @SerializedName("strIngredient2") private String strIngredient2;
    @SerializedName("strIngredient3") private String strIngredient3;
    @SerializedName("strIngredient4") private String strIngredient4;
    @SerializedName("strIngredient5") private String strIngredient5;
    @SerializedName("strIngredient6") private String strIngredient6;
    @SerializedName("strIngredient7") private String strIngredient7;
    @SerializedName("strIngredient8") private String strIngredient8;
    @SerializedName("strIngredient9") private String strIngredient9;
    @SerializedName("strIngredient10") private String strIngredient10;
    @SerializedName("strIngredient11") private String strIngredient11;
    @SerializedName("strIngredient12") private String strIngredient12;
    @SerializedName("strIngredient13") private String strIngredient13;
    @SerializedName("strIngredient14") private String strIngredient14;
    @SerializedName("strIngredient15") private String strIngredient15;
    @SerializedName("strIngredient16") private String strIngredient16;
    @SerializedName("strIngredient17") private String strIngredient17;
    @SerializedName("strIngredient18") private String strIngredient18;
    @SerializedName("strIngredient19") private String strIngredient19;
    @SerializedName("strIngredient20") private String strIngredient20;
    //endregion

    //region strMeasures
    @SerializedName("strMeasure1") private String strMeasure1;
    @SerializedName("strMeasure2") private String strMeasure2;
    @SerializedName("strMeasure3") private String strMeasure3;
    @SerializedName("strMeasure4") private String strMeasure4;
    @SerializedName("strMeasure5") private String strMeasure5;
    @SerializedName("strMeasure6") private String strMeasure6;
    @SerializedName("strMeasure7") private String strMeasure7;
    @SerializedName("strMeasure8") private String strMeasure8;
    @SerializedName("strMeasure9") private String strMeasure9;
    @SerializedName("strMeasure10") private String strMeasure10;
    @SerializedName("strMeasure11") private String strMeasure11;
    @SerializedName("strMeasure12") private String strMeasure12;
    @SerializedName("strMeasure13") private String strMeasure13;
    @SerializedName("strMeasure14") private String strMeasure14;
    @SerializedName("strMeasure15") private String strMeasure15;
    @SerializedName("strMeasure16") private String strMeasure16;
    @SerializedName("strMeasure17") private String strMeasure17;
    @SerializedName("strMeasure18") private String strMeasure18;
    @SerializedName("strMeasure19") private String strMeasure19;
    @SerializedName("strMeasure20") private String strMeasure20;
    //endregion

    private List<String> ingredients = new ArrayList<>();
    private List<String> measures = new ArrayList<>();

    private List<String> selectedIngredients = new ArrayList<>();
    private List<String> unselectedIngredients = new ArrayList<>();

    public void processIngredientsAndMeasures() {
        // Clear the lists to avoid duplications if this method is called multiple times
        ingredients.clear();
        measures.clear();

        // Ingredients
        if (strIngredient1 != null && !strIngredient1.trim().isEmpty()) { ingredients.add(strIngredient1); }
        if (strIngredient2 != null && !strIngredient2.trim().isEmpty()) { ingredients.add(strIngredient2); }
        if (strIngredient3 != null && !strIngredient3.trim().isEmpty()) { ingredients.add(strIngredient3); }
        if (strIngredient4 != null && !strIngredient4.trim().isEmpty()) { ingredients.add(strIngredient4); }
        if (strIngredient5 != null && !strIngredient5.trim().isEmpty()) { ingredients.add(strIngredient5); }
        if (strIngredient6 != null && !strIngredient6.trim().isEmpty()) { ingredients.add(strIngredient6); }
        if (strIngredient7 != null && !strIngredient7.trim().isEmpty()) { ingredients.add(strIngredient7); }
        if (strIngredient8 != null && !strIngredient8.trim().isEmpty()) { ingredients.add(strIngredient8); }
        if (strIngredient9 != null && !strIngredient9.trim().isEmpty()) { ingredients.add(strIngredient9); }
        if (strIngredient10 != null && !strIngredient10.trim().isEmpty()) { ingredients.add(strIngredient10); }
        if (strIngredient11 != null && !strIngredient11.trim().isEmpty()) { ingredients.add(strIngredient11); }
        if (strIngredient12 != null && !strIngredient12.trim().isEmpty()) { ingredients.add(strIngredient12); }
        if (strIngredient13 != null && !strIngredient13.trim().isEmpty()) { ingredients.add(strIngredient13); }
        if (strIngredient14 != null && !strIngredient14.trim().isEmpty()) { ingredients.add(strIngredient14); }
        if (strIngredient15 != null && !strIngredient15.trim().isEmpty()) { ingredients.add(strIngredient15); }
        if (strIngredient16 != null && !strIngredient16.trim().isEmpty()) { ingredients.add(strIngredient16); }
        if (strIngredient17 != null && !strIngredient17.trim().isEmpty()) { ingredients.add(strIngredient17); }
        if (strIngredient18 != null && !strIngredient18.trim().isEmpty()) { ingredients.add(strIngredient18); }
        if (strIngredient19 != null && !strIngredient19.trim().isEmpty()) { ingredients.add(strIngredient19); }
        if (strIngredient20 != null && !strIngredient20.trim().isEmpty()) { ingredients.add(strIngredient20); }

// Measures
        if (strMeasure1 != null) { measures.add(strMeasure1); } else { measures.add(""); }
        if (strMeasure2 != null) { measures.add(strMeasure2); } else { measures.add(""); }
        if (strMeasure3 != null) { measures.add(strMeasure3); } else { measures.add(""); }
        if (strMeasure4 != null) { measures.add(strMeasure4); } else { measures.add(""); }
        if (strMeasure5 != null) { measures.add(strMeasure5); } else { measures.add(""); }
        if (strMeasure6 != null) { measures.add(strMeasure6); } else { measures.add(""); }
        if (strMeasure7 != null) { measures.add(strMeasure7); } else { measures.add(""); }
        if (strMeasure8 != null) { measures.add(strMeasure8); } else { measures.add(""); }
        if (strMeasure9 != null) { measures.add(strMeasure9); } else { measures.add(""); }
        if (strMeasure10 != null) { measures.add(strMeasure10); } else { measures.add(""); }
        if (strMeasure11 != null) { measures.add(strMeasure11); } else { measures.add(""); }
        if (strMeasure12 != null) { measures.add(strMeasure12); } else { measures.add(""); }
        if (strMeasure13 != null) { measures.add(strMeasure13); } else { measures.add(""); }
        if (strMeasure14 != null) { measures.add(strMeasure14); } else { measures.add(""); }
        if (strMeasure15 != null) { measures.add(strMeasure15); } else { measures.add(""); }
        if (strMeasure16 != null) { measures.add(strMeasure16); } else { measures.add(""); }
        if (strMeasure17 != null) { measures.add(strMeasure17); } else { measures.add(""); }
        if (strMeasure18 != null) { measures.add(strMeasure18); } else { measures.add(""); }
        if (strMeasure19 != null) { measures.add(strMeasure19); } else { measures.add(""); }
        if (strMeasure20 != null) { measures.add(strMeasure20); } else { measures.add(""); }

    }
    // Getters and setters
    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public String getImage()
    {
        return image;
    }

    public List<String> getIncludedIngredientMeasure(List<String> includedIngredients)
    {
        List<String> targetArray = new ArrayList<>();
        for(int i =0; i < includedIngredients.size() ; i++)
        {
            int containedIndex = ingredients.indexOf(includedIngredients.get(i));
            if (containedIndex >= 0)
            {
                if(measures.size() > containedIndex)
                    targetArray.add(ingredients.get(containedIndex) + " : "+ measures.get(containedIndex));
                else
                    targetArray.add(ingredients.get(containedIndex));
            }
        }
        selectedIngredients = targetArray;
        return targetArray;
    }
    public List<String> getExcludedIngredientMeasure(List<String> excludedIngredients) {
        List<String> targetArray = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            if (!excludedIngredients.contains(ingredients.get(i))) {
                if (measures.size() > i) {
                    targetArray.add(ingredients.get(i) + " : " + measures.get(i));
                } else {
                    targetArray.add(ingredients.get(i));
                }
            }
        }
        unselectedIngredients = targetArray;
        return targetArray;
    }
}