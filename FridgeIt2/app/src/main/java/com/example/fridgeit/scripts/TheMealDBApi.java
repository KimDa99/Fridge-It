package com.example.fridgeit.scripts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.fridgeit.scripts.model.IngredientListResponse;
import com.example.fridgeit.scripts.model.MealListResponse;
import com.example.fridgeit.scripts.model.RecipeListResponse;

public interface TheMealDBApi {
    @GET("list.php?i=list")
    Call<IngredientListResponse> listAllIngredients();

    @GET("search.php")
    Call<MealListResponse> searchMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("search.php")
    Call<RecipeListResponse> searchMealByName(@Query("s") String mealName);
}