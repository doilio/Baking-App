package com.doiliomatsinhe.bakingapp.ui.recipe;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doiliomatsinhe.bakingapp.data.BakingRepository;
import com.doiliomatsinhe.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {

    private BakingRepository repository;
    private MutableLiveData<List<Recipe>> _recipeList = new MutableLiveData<>();
    private static final String TAG = RecipeViewModel.class.getSimpleName();

    public RecipeViewModel(BakingRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Recipe>> getRecipes() {

        repository.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.body() != null) {
                    List<Recipe> recipeList = response.body();

                    _recipeList.setValue(recipeList);
                } else {
                    Log.d(TAG, "onResponse: Error fetching Recipes");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });

        return _recipeList;
    }
}
