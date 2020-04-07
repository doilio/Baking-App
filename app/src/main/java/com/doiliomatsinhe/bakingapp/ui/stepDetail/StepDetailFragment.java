package com.doiliomatsinhe.bakingapp.ui.stepDetail;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doiliomatsinhe.bakingapp.databinding.FragmentStepDetailBinding;
import com.doiliomatsinhe.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {
    private FragmentStepDetailBinding binding;
    private SimpleExoPlayer player;
    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String videoUrl = null;
    private Step step;
    private static final String STEP = "step";

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getSerializable(STEP) != null) {
                step = (Step) getArguments().getSerializable(STEP);
                if (step != null) {
                    Log.d(TAG, "STEP is not null" + step.getShortDescription());
                }

            } else {
                Log.d(TAG, "STEP is null");
            }
        } else {
            Log.d(TAG, "getArguments is null");
        }


    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepDetailBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    /**
     * StepDetailFragment factory
     */
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

    /**
     * Populates the UI when this fragment is created
     */
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

    /**
     * Exoplayer Method to build a Media Source
     */
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getActivity()), "baking-app");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    /**
     * Exoplayer player initialization
     */
    private void initializePlayer() {
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

    /**
     * Exoplayer releases the player when done
     */
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
