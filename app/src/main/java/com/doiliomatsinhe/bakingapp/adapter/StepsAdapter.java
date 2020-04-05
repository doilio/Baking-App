package com.doiliomatsinhe.bakingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.databinding.StepItemBinding;
import com.doiliomatsinhe.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private List<Step> stepsList = new ArrayList<>();
    private StepsAdapter.StepsItemClickListener onClickListener;

    public StepsAdapter(StepsItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private StepItemBinding binding;

        public ViewHolder(@NonNull StepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Step item) {
            int stepNr = item.getId();
            String description = item.getShortDescription();
            if (stepNr == 0) {
                binding.stepNumber.setText(R.string.intro);
            } else {
                binding.stepNumber.setText(String.format("Step %s", stepNr));
            }

            binding.stepDescription.setText(description);

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            onClickListener.onStepsItemClick(clickedItem);
        }
    }

    public void setStepsList(List<Step> stepsList) {
        this.stepsList = stepsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StepItemBinding binding = StepItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, int position) {
        Step currentStep = stepsList.get(position);
        holder.bind(currentStep);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public interface StepsItemClickListener {
        void onStepsItemClick(int position);
    }


}
