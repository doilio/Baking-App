package com.doiliomatsinhe.bakingapp.ui.stepDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.databinding.ActivityStepDetailBinding;
import com.doiliomatsinhe.bakingapp.databinding.FragmentStepDetailBinding;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {
    private FragmentStepDetailBinding binding;
    private SimpleExoPlayer player;
    private static final String TAG = StepDetailFragment.class.getSimpleName();
    // Exoplayer Members
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    String videoUrl = null;
    private Step step;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getSerializable(STEP) != null) {
                step = (Step) getArguments().getSerializable(STEP);
                Log.d(TAG, "STEP is not null" + step.getShortDescription());

            } else {
                Log.d(TAG, "STEP is null");
            }
        } else {
            Log.d(TAG, "getArguments is null");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepDetailBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    public static StepDetailFragment newInstance(Step selectedStep) {
        StepDetailFragment fragment = new StepDetailFragment();
        // bundle arguments for the fragment
        Bundle arguments = new Bundle();
        arguments.putSerializable(STEP, selectedStep);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI(step);
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
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getActivity()), "baking-app");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void initializePlayer() {

        //int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);
//        int orientation = getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // In landscape
//            hideSystemUi();
//        }
        player = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getActivity()));
        binding.videoView.setPlayer(player);


        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);

        //Supply state during initialization
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    @Override
    public void onStart() {
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
    public void onResume() {
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
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
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
