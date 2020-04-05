package com.doiliomatsinhe.bakingapp.ui.stepDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.model.Step;

import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.NAME;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.STEP;

public class StepDetailActivity extends AppCompatActivity {

    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent i = getIntent();
        if (i.getStringExtra(NAME) != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(i.getStringExtra(NAME));
        }
        if (i.getSerializableExtra(STEP) != null) {
            step = (Step) i.getSerializableExtra(STEP);

            populateUI(step);

        } else {
            // Force Close
            finish();
            Toast.makeText(this, "Empty Intent", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateUI(Step step) {
        if (step != null) {


        }
    }
}
