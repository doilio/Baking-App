package com.doiliomatsinhe.bakingapp.ui.recipe;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doiliomatsinhe.bakingapp.data.BakingRepository;
import com.doiliomatsinhe.bakingapp.ui.recipe.RecipeViewModel;

public class RecipeViewModelFactory implements ViewModelProvider.Factory {
    private BakingRepository repository;

    public RecipeViewModelFactory(BakingRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (RecipeViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new RecipeViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class!");
    }
}
