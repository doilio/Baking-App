package com.doiliomatsinhe.bakingapp.ui.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.adapter.RecipeAdapter;
import com.doiliomatsinhe.bakingapp.data.BakingRepository;
import com.doiliomatsinhe.bakingapp.databinding.ActivityRecipeBinding;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity;
import com.doiliomatsinhe.bakingapp.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecipeAdapter.RecipeItemClickListener {

    private ActivityRecipeBinding binding;
    private RecipeViewModel viewModel;
    private RecipeAdapter adapter;
    public static final String RECIPE = "recipe";
    public static final String RECIPE_PREF = "recipe_pref";
    public static final String MY_RECIPE = "my_recipe";

    private List<Recipe> recipeList = new ArrayList<>();
    private static final String TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        initComponents();

        fetchRecipeList();

    }

    private void fetchRecipeList() {
        binding.swipeRefreshRecipe.setRefreshing(true);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Log.d(TAG, "Size of List: " + recipes.size());
                Log.d(TAG, "Name of 1st recipe: " + recipes.get(0).getName());

                adapter.setRecipeList(recipes);
                recipeList = recipes;
                binding.swipeRefreshRecipe.setRefreshing(false);
            }
        });


    }

    private void initComponents() {

        // Swipe Refresh
        binding.swipeRefreshRecipe.setOnRefreshListener(this);

        // ViewModel
        BakingRepository repository = new BakingRepository();
        RecipeViewModelFactory factory = new RecipeViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // Adapter
        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);
        adapter = new RecipeAdapter(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, gridColumnCount);
        binding.recyclerRecipe.setLayoutManager(layoutManager);
        binding.recyclerRecipe.setHasFixedSize(true);
        binding.recyclerRecipe.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        fetchRecipeList();
    }

    @Override
    public void onRecipeItemClick(int position) {
        Recipe recipe = recipeList.get(position);

        //Save the last clicked recipe as a JSON file for the Widget
        String myRecipe = JsonUtil.getJsonFromObject(recipe);
        Log.d(TAG, "My recipe" + myRecipe);
        saveToSharedPreferences(myRecipe);

        Intent i = new Intent(this, RecipeDetailActivity.class);
        i.putExtra(RECIPE, recipe);
        startActivity(i);
    }

    private void saveToSharedPreferences(String myRecipe) {
        clearSharedPref();
        SharedPreferences sharedPref = getSharedPreferences(RECIPE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MY_RECIPE, myRecipe);

        editor.apply();
        Log.d(TAG, "Shared Pref saved");
    }

    private void clearSharedPref() {
        SharedPreferences sharedPref = getSharedPreferences(RECIPE_PREF, MODE_PRIVATE);

        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            Log.d(TAG, "Shared Pref cleared");
        } else {
            Log.d(TAG, "Shared Pref is currently null");
        }
    }


}
