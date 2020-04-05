package com.doiliomatsinhe.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.data.BakingRepository;
import com.doiliomatsinhe.bakingapp.databinding.ActivityRecipeBinding;
import com.doiliomatsinhe.bakingapp.model.Recipe;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private ActivityRecipeBinding binding;
    private RecipeViewModel viewModel;
    private static final String TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        initComponents();

        fetchRecipeList();

    }

    private void fetchRecipeList() {
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Log.d(TAG, "Size of List: " + recipes.size());
                Log.d(TAG, "Name of 1st recipe: " + recipes.get(0).getName());
            }
        });
    }

    private void initComponents() {

        // ViewModel
        BakingRepository repository = new BakingRepository();
        RecipeViewModelFactory factory = new RecipeViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);
    }
}
