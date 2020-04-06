package com.doiliomatsinhe.bakingapp.ui.stepDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.databinding.ActivityStepDetailBinding;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.INDEX;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.MY_RECIPE;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.NAME;

public class StepDetailActivity extends AppCompatActivity {

    private ActivityStepDetailBinding binding;
    private int listIndex;
    private List<Step> listOfSteps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        Intent i = getIntent();
        if (i.getStringExtra(NAME) != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(i.getStringExtra(NAME));
        }

        int index = i.getIntExtra(INDEX, -1);
        if (index != -1) {
            listIndex = index;
        }

        if (i.getSerializableExtra(MY_RECIPE) != null) {
            Recipe recipe = (Recipe) i.getSerializableExtra(MY_RECIPE);
            if (recipe != null) {
                listOfSteps.addAll(recipe.getSteps());
            }
        }


        if (savedInstanceState == null) {
            //Step selectedStep = (Step) getIntent().getSerializableExtra(STEP);
            Step selectedStep = listOfSteps.get(listIndex);
            StepDetailFragment fragment = StepDetailFragment.newInstance(selectedStep);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }

        if (listIndex == 0) {
            binding.buttonPrevious.setVisibility(View.INVISIBLE);
        }
        if (listIndex == listOfSteps.size() - 1) {
            binding.buttonNext.setVisibility(View.INVISIBLE);
        }
        binding.pageGuide.setText(String.format("%s/%s", listIndex, listOfSteps.size() - 1));
        setButtonClicks();
    }

    private void inflateFragmentFromClick(List<Step> listOfSteps, int listIndex) {
        Step selectedStep = listOfSteps.get(listIndex);
        StepDetailFragment fragment = StepDetailFragment.newInstance(selectedStep);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setButtonClicks() {

        binding.buttonPrevious.setOnClickListener(v -> {

            listIndex--;
            inflateFragmentFromClick(listOfSteps, listIndex);
            binding.pageGuide.setText(String.format("%s/%s", listIndex, listOfSteps.size() - 1));
            binding.buttonNext.setVisibility(View.VISIBLE);
            if (listIndex == 0) {
                binding.buttonPrevious.setVisibility(View.INVISIBLE);
            } else {
                binding.buttonPrevious.setVisibility(View.VISIBLE);
            }
        });

        binding.buttonNext.setOnClickListener(v -> {
            listIndex++;
            inflateFragmentFromClick(listOfSteps, listIndex);
            binding.pageGuide.setText(String.format("%s/%s", listIndex, listOfSteps.size() - 1));
            binding.buttonPrevious.setVisibility(View.VISIBLE);
            if (listIndex == listOfSteps.size() - 1) {
                binding.buttonNext.setVisibility(View.INVISIBLE);
            } else {
                binding.buttonNext.setVisibility(View.VISIBLE);
            }
        });

    }
}
