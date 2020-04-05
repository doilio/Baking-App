package com.doiliomatsinhe.bakingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doiliomatsinhe.bakingapp.databinding.RecipeItemBinding;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    final private RecipeItemClickListener onClickListener;

    public RecipeAdapter(RecipeItemClickListener listener) {
        onClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecipeItemBinding binding;

        public ViewHolder(@NonNull RecipeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Recipe item) {

            if (!item.getImage().isEmpty()){
                Picasso.get().load(item.getImage()).into(binding.recipeImage);
            }
            binding.recipeName.setText(item.getName());
            binding.recipeServings.setText(String.format("Number of servings: %s", item.getServings()));

            binding.executePendingBindings();
        }


        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            onClickListener.onRecipeItemClick(clickedItem);
        }
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecipeItemBinding binding = RecipeItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.bind(currentRecipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface RecipeItemClickListener {
        void onRecipeItemClick(int position);
    }

}
