package com.doiliomatsinhe.bakingapp.network;

import com.doiliomatsinhe.bakingapp.model.Ingredient;
import com.doiliomatsinhe.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    /**
     * Asynchronous call to the Baking API
     * @return list of recipes containing ingridients and steps
     */
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
