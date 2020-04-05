package com.doiliomatsinhe.bakingapp.ui.recipeDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.doiliomatsinhe.bakingapp.model.Ingredient;
import com.doiliomatsinhe.bakingapp.model.Recipe;

import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private ActivityRecipeDetailBinding binding;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        Intent i = getIntent();
        if (i.getSerializableExtra(RECIPE) != null) {
            recipe = (Recipe) i.getSerializableExtra(RECIPE);

            populateUI(recipe);

        } else {
            // Force Close
            finish();
        }


    }

    private void populateUI(Recipe recipe) {

        if (recipe != null) {

            // Setting up the ActionBar
            Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());


            StringBuilder stringBuilder = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                String name = ingredient.getIngredient();
                String unit = ingredient.getMeasure();
                float quantity = ingredient.getQuantity();

                String text ="";
                if (quantity % 1 == 0){
                    text = String.format("* %s %s of %s\n", (int)quantity, unit, name);
                }else{
                     text = String.format("* %s %s of %s\n", quantity, unit, name);
                }

                stringBuilder.append(text);
            }

            String finalString = stringBuilder.toString().trim();
            binding.ingredientsText.setText(finalString);
            Log.d(TAG, "Texto: \n" + finalString);
        }
    }
}
