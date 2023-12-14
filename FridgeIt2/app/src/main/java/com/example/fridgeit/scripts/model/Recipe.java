package com.example.fridgeit.scripts.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    @SerializedName("strMeal")
    private String name;
    @SerializedName("idMeal")
    public String idMeal;
    @SerializedName("strMealThumb")
    private String image;
    @SerializedName("strInstructions") private String strInstruction;

    @SerializedName("strYoutube") private String strYoutubeUrl;

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

    private List<String> strIngredientList = new ArrayList<>();
    private List<String> strMeasures = new ArrayList<>();

    public void setImage(String imgUrl)
    {
        image = imgUrl;
    }

    public String getStrYoutubeUrl(){ return strYoutubeUrl; }
    public String getImage()
    {
        return image;
    }
    public List<String> getIngredientMeasure()
    {
        List<String> targetArray = new ArrayList<>();
        for(int i = 0; i < strIngredientList.size(); i++)
        {
            if(strMeasures.size() > i)
                targetArray.add(strIngredientList.get(i) + " : "+ strMeasures.get(i));
            else
                targetArray.add(strIngredientList.get(i));
        }
        return targetArray;
    }

    public String getName() {
        return name;
    }
    public String getInstruction(){ return strInstruction; }
    public void processIngredientsAndMeasures() {
        // Clear the lists to avoid duplications if this method is called multiple times
        strIngredientList.clear();
        strMeasures.clear();

        // Ingredients
        if (strIngredient1 != null && !strIngredient1.trim().isEmpty()) { strIngredientList.add(strIngredient1); }
        if (strIngredient2 != null && !strIngredient2.trim().isEmpty()) { strIngredientList.add(strIngredient2); }
        if (strIngredient3 != null && !strIngredient3.trim().isEmpty()) { strIngredientList.add(strIngredient3); }
        if (strIngredient4 != null && !strIngredient4.trim().isEmpty()) { strIngredientList.add(strIngredient4); }
        if (strIngredient5 != null && !strIngredient5.trim().isEmpty()) { strIngredientList.add(strIngredient5); }
        if (strIngredient6 != null && !strIngredient6.trim().isEmpty()) { strIngredientList.add(strIngredient6); }
        if (strIngredient7 != null && !strIngredient7.trim().isEmpty()) { strIngredientList.add(strIngredient7); }
        if (strIngredient8 != null && !strIngredient8.trim().isEmpty()) { strIngredientList.add(strIngredient8); }
        if (strIngredient9 != null && !strIngredient9.trim().isEmpty()) { strIngredientList.add(strIngredient9); }
        if (strIngredient10 != null && !strIngredient10.trim().isEmpty()) { strIngredientList.add(strIngredient10); }
        if (strIngredient11 != null && !strIngredient11.trim().isEmpty()) { strIngredientList.add(strIngredient11); }
        if (strIngredient12 != null && !strIngredient12.trim().isEmpty()) { strIngredientList.add(strIngredient12); }
        if (strIngredient13 != null && !strIngredient13.trim().isEmpty()) { strIngredientList.add(strIngredient13); }
        if (strIngredient14 != null && !strIngredient14.trim().isEmpty()) { strIngredientList.add(strIngredient14); }
        if (strIngredient15 != null && !strIngredient15.trim().isEmpty()) { strIngredientList.add(strIngredient15); }
        if (strIngredient16 != null && !strIngredient16.trim().isEmpty()) { strIngredientList.add(strIngredient16); }
        if (strIngredient17 != null && !strIngredient17.trim().isEmpty()) { strIngredientList.add(strIngredient17); }
        if (strIngredient18 != null && !strIngredient18.trim().isEmpty()) { strIngredientList.add(strIngredient18); }
        if (strIngredient19 != null && !strIngredient19.trim().isEmpty()) { strIngredientList.add(strIngredient19); }
        if (strIngredient20 != null && !strIngredient20.trim().isEmpty()) { strIngredientList.add(strIngredient20); }

// Measures
        if (strMeasure1 != null) { strMeasures.add(strMeasure1); } else { strMeasures.add(""); }
        if (strMeasure2 != null) { strMeasures.add(strMeasure2); } else { strMeasures.add(""); }
        if (strMeasure3 != null) { strMeasures.add(strMeasure3); } else { strMeasures.add(""); }
        if (strMeasure4 != null) { strMeasures.add(strMeasure4); } else { strMeasures.add(""); }
        if (strMeasure5 != null) { strMeasures.add(strMeasure5); } else { strMeasures.add(""); }
        if (strMeasure6 != null) { strMeasures.add(strMeasure6); } else { strMeasures.add(""); }
        if (strMeasure7 != null) { strMeasures.add(strMeasure7); } else { strMeasures.add(""); }
        if (strMeasure8 != null) { strMeasures.add(strMeasure8); } else { strMeasures.add(""); }
        if (strMeasure9 != null) { strMeasures.add(strMeasure9); } else { strMeasures.add(""); }
        if (strMeasure10 != null) { strMeasures.add(strMeasure10); } else { strMeasures.add(""); }
        if (strMeasure11 != null) { strMeasures.add(strMeasure11); } else { strMeasures.add(""); }
        if (strMeasure12 != null) { strMeasures.add(strMeasure12); } else { strMeasures.add(""); }
        if (strMeasure13 != null) { strMeasures.add(strMeasure13); } else { strMeasures.add(""); }
        if (strMeasure14 != null) { strMeasures.add(strMeasure14); } else { strMeasures.add(""); }
        if (strMeasure15 != null) { strMeasures.add(strMeasure15); } else { strMeasures.add(""); }
        if (strMeasure16 != null) { strMeasures.add(strMeasure16); } else { strMeasures.add(""); }
        if (strMeasure17 != null) { strMeasures.add(strMeasure17); } else { strMeasures.add(""); }
        if (strMeasure18 != null) { strMeasures.add(strMeasure18); } else { strMeasures.add(""); }
        if (strMeasure19 != null) { strMeasures.add(strMeasure19); } else { strMeasures.add(""); }
        if (strMeasure20 != null) { strMeasures.add(strMeasure20); } else { strMeasures.add(""); }

    }
    
}
