package com.doiliomatsinhe.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.adapter.RecipeAdapter;
import com.doiliomatsinhe.bakingapp.data.BakingRepository;
import com.doiliomatsinhe.bakingapp.databinding.ActivityRecipeBinding;
import com.doiliomatsinhe.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecipeAdapter.RecipeItemClickListener {

    private ActivityRecipeBinding binding;
    private RecipeViewModel viewModel;
    private RecipeAdapter adapter;

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
            }
        });

        binding.swipeRefreshRecipe.setRefreshing(false);
    }

    private void initComponents() {

        // Swipe Refresh
        binding.swipeRefreshRecipe.setOnRefreshListener(this);

        // ViewModel
        BakingRepository repository = new BakingRepository();
        RecipeViewModelFactory factory = new RecipeViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // Adapter
        adapter = new RecipeAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
        Toast.makeText(this, "clicked on:" + recipe.getName(), Toast.LENGTH_SHORT).show();
    }
}
