package com.doiliomatsinhe.bakingapp.ui.stepDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.databinding.ActivityStepDetailBinding;
import com.doiliomatsinhe.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.NAME;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.STEP;

public class StepDetailActivity extends AppCompatActivity {

    private Step step;
    private ActivityStepDetailBinding binding;
    private SimpleExoPlayer player;

    // Exoplayer Members
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    String videoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);
        Intent i = getIntent();
        setActionBar(i);

        if (i.getSerializableExtra(STEP) != null) {
            step = (Step) i.getSerializableExtra(STEP);

            populateUI(step);

        } else {
            // Force Close
            finish();
            Toast.makeText(this, "Empty Intent", Toast.LENGTH_SHORT).show();
        }
    }

    private void setActionBar(Intent i) {
        if (i.getStringExtra(NAME) != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(i.getStringExtra(NAME));
        }
    }

    private void populateUI(Step step) {
        if (step != null) {

            binding.stepDescription.setText(step.getDescription());
            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
                videoUrl = step.getVideoURL();
            } else if (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()) {
                videoUrl = step.getThumbnailURL();
            } else {
                videoUrl = null;
            }

        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "baking-app");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        binding.videoView.setPlayer(player);


        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);

        //Supply state during initialization
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            if (videoUrl != null) {
                initializePlayer();
            } else {
                binding.videoView.setVisibility(View.GONE);
                binding.videoError.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null)) {
            if (videoUrl != null) {
                initializePlayer();
            } else {
                binding.videoView.setVisibility(View.GONE);
                binding.videoError.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        binding.videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

}
