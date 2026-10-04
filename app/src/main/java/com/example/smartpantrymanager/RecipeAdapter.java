package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe =
                recipeList.get(position);

        holder.textRecipeName.setText(
                recipe.getName()
        );

        holder.textRecipeDescription.setText(
                recipe.getDescription()
        );

        holder.textRecipeIngredients.setText(
                "Uses: " +
                        recipe.getIngredients()
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView textRecipeName;
        TextView textRecipeDescription;
        TextView textRecipeIngredients;

        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);

            textRecipeName =
                    itemView.findViewById(
                            R.id.textRecipeName
                    );

            textRecipeDescription =
                    itemView.findViewById(
                            R.id.textRecipeDescription
                    );

            textRecipeIngredients =
                    itemView.findViewById(
                            R.id.textRecipeIngredients
                    );
        }
    }
}