package com.example.fridgeit.scripts.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    @SerializedName("strMeal")
    private String name;
    public String idMeal;
    public String strMeal;
    @SerializedName("strMealThumb")
    private String image;

    public List<String> strIngredientList = new ArrayList<>();
    private List<String> strMeasures = new ArrayList<>();

    public Recipe(String mealId, String mealName, List<String> ingredientList, List<String> measureList)
    {
        idMeal = mealId;
        strMeal = mealName;
        strIngredientList = ingredientList;
        strMeasures = measureList;
    }
    public void setImage(String imgUrl)
    {
        image = imgUrl;
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
            int containedIndex = strIngredientList.indexOf(includedIngredients.get(i));
            if (containedIndex >= 0)
            {
                if(strMeasures.size() > containedIndex)
                    targetArray.add(strIngredientList.get(containedIndex) + " : "+ strMeasures.get(containedIndex));
                else
                    targetArray.add(strIngredientList.get(containedIndex));
            }
        }
        return targetArray;
    }
    public List<String> getExcludedIngredientMeasure(List<String> excludedIngredients) {
        List<String> targetArray = new ArrayList<>();
        for (int i = 0; i < strIngredientList.size(); i++) {
            if (!excludedIngredients.contains(strIngredientList.get(i))) {
                if (strMeasures.size() > i) {
                    targetArray.add(strIngredientList.get(i) + " : " + strMeasures.get(i));
                } else {
                    targetArray.add(strIngredientList.get(i));
                }
            }
        }
        return targetArray;
    }

}
