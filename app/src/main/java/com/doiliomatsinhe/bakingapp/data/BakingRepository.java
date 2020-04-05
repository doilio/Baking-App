package com.doiliomatsinhe.bakingapp.data;

import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.network.APIService;
import com.doiliomatsinhe.bakingapp.network.ServiceBuilder;

import java.util.List;

import retrofit2.Call;

public class BakingRepository {
    private static APIService service = ServiceBuilder.BuildService(APIService.class);

    public Call<List<Recipe>> getRecipes() {
        return  service.getRecipes();
    }
}
