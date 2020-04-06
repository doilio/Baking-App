package com.doiliomatsinhe.bakingapp.ui.stepDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.model.Step;

import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.NAME;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.STEP;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            Step selectedStep = (Step) getIntent().getSerializableExtra(STEP);
            StepDetailFragment fragment = StepDetailFragment.newInstance(selectedStep);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
        Intent i = getIntent();
        if (i.getStringExtra(NAME) != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(i.getStringExtra(NAME));
        }
    }
}
