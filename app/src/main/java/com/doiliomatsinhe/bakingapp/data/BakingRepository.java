package com.doiliomatsinhe.bakingapp.data;


import com.doiliomatsinhe.bakingapp.model.Ingredient;
import com.doiliomatsinhe.bakingapp.network.APIService;
import com.doiliomatsinhe.bakingapp.network.ServiceBuilder;

import retrofit2.Call;

public class BakingRepository {
    private static APIService service = ServiceBuilder.BuildService(APIService.class);

    public Call<Ingredient> getIngredients() {
        return  service.getIngredients();
    }
}
