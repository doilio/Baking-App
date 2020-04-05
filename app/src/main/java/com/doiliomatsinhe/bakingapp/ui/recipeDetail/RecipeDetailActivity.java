package com.doiliomatsinhe.bakingapp.ui.recipeDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.adapter.RecipeAdapter;
import com.doiliomatsinhe.bakingapp.adapter.StepsAdapter;
import com.doiliomatsinhe.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.doiliomatsinhe.bakingapp.model.Ingredient;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepsItemClickListener {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private ActivityRecipeDetailBinding binding;
    private Recipe recipe;
    private List<Step> stepsList = new ArrayList<>();
    private StepsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        initComponents();

        Intent i = getIntent();
        if (i.getSerializableExtra(RECIPE) != null) {
            recipe = (Recipe) i.getSerializableExtra(RECIPE);

            populateUI(recipe);

        } else {
            // Force Close
            finish();
        }


    }

    private void initComponents() {

        // Adapter
        adapter = new StepsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerStep.setLayoutManager(layoutManager);
        binding.recyclerStep.setHasFixedSize(true);
        binding.recyclerStep.setAdapter(adapter);
        binding.recyclerStep.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void populateUI(Recipe recipe) {

        if (recipe != null) {

            // Setting up the list of steps
            stepsList = recipe.getSteps();

            adapter.setStepsList(stepsList);

            // Setting up the ActionBar
            Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());


            StringBuilder stringBuilder = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                String name = ingredient.getIngredient();
                String unit = ingredient.getMeasure();
                float quantity = ingredient.getQuantity();

                String text = "";
                if (quantity % 1 == 0) {
                    text = String.format("* %s %s of %s\n", (int) quantity, unit, name);
                } else {
                    text = String.format("* %s %s of %s\n", quantity, unit, name);
                }

                stringBuilder.append(text);
            }

            String finalString = stringBuilder.toString().trim();
            binding.ingredientsText.setText(finalString);
            Log.d(TAG, "Texto: \n" + finalString);
        }
    }

    @Override
    public void onStepsItemClick(int position) {
        Step step = stepsList.get(position);

        Toast.makeText(this, "Clicked: " + step.getShortDescription(), Toast.LENGTH_SHORT).show();
    }
}
