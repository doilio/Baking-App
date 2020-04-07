package com.doiliomatsinhe.bakingapp.ui.recipeDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.adapter.StepsAdapter;
import com.doiliomatsinhe.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.doiliomatsinhe.bakingapp.model.Ingredient;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.model.Step;
import com.doiliomatsinhe.bakingapp.ui.stepDetail.StepDetailActivity;
import com.doiliomatsinhe.bakingapp.ui.stepDetail.StepDetailFragment;
import com.doiliomatsinhe.bakingapp.widget.IngredientsWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE;
import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE_PREF;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepsItemClickListener {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    public static final String NAME = "name";
    public static final String MY_RECIPE = "recipe";
    public static final String INDEX = "index";
    public static String NAME_TEXT;
    public static String MY_INGREDIENTS = "my_ingredients";
    private ActivityRecipeDetailBinding binding;
    private List<Step> stepsList = new ArrayList<>();
    private StepsAdapter adapter;
    private boolean mTwoPane = false;
    private Recipe myRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        initComponents();

        Intent i = getIntent();
        if (i.getSerializableExtra(RECIPE) != null) {
            Recipe recipe = (Recipe) i.getSerializableExtra(RECIPE);

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
        binding.recyclerStep.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if (binding.stepDetailContainer != null) {
            mTwoPane = true;
        }
    }

    private void populateUI(Recipe recipe) {

        if (recipe != null) {

            myRecipe = recipe;
            // Setting up the list of steps
            stepsList = recipe.getSteps();

            adapter.setStepsList(stepsList);

            // Setting up the ActionBar
            Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());
            NAME_TEXT = recipe.getName();

            StringBuilder stringBuilder = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                String name = ingredient.getIngredient();
                String unit = ingredient.getMeasure();
                float quantity = ingredient.getQuantity();

                String text;
                if (quantity % 1 == 0) {
                    text = String.format("* %s %s of %s;\n", (int) quantity, unit, name);
                } else {
                    text = String.format("* %s %s of %s;\n", quantity, unit, name);
                }

                stringBuilder.append(text);
            }

            String finalString = stringBuilder.toString().trim();

            saveToSharedPref(finalString);

            binding.ingredientsText.setText(finalString);
            Log.d(TAG, "Texto: \n" + finalString);
        }
    }

    private void saveToSharedPref(String listOfIngredients) {
        SharedPreferences sharedPref = getSharedPreferences(RECIPE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MY_INGREDIENTS, listOfIngredients);
        editor.apply();
        Log.d(TAG, "List of Ingredients saved");


        // Updates the Widget
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, IngredientsWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_widget_stack);
    }

    /**
     * Provides a smooth experience when navigating Up.
     * Not Querying Again
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public void onStepsItemClick(int position) {

        if (mTwoPane) {
            Step selectedStep = stepsList.get(position);
            StepDetailFragment fragment = StepDetailFragment.newInstance(selectedStep);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent i = new Intent(this, StepDetailActivity.class);
            i.putExtra(MY_RECIPE, myRecipe);
            i.putExtra(INDEX, position);
            i.putExtra(NAME, NAME_TEXT);
            startActivity(i);

        }

    }
}
