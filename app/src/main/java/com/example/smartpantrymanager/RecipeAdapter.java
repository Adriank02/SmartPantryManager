package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private final List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {
        Recipe recipe = recipeList.get(position);

        holder.textRecipeName.setText(recipe.getName());

        if (recipe.getDescription() != null) {
            holder.textRecipeDescription.setText(recipe.getDescription());
        } else {
            holder.textRecipeDescription.setText("");
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    RecipeDetailActivity.class
            );

            intent.putExtra(
                    "recipe_name",
                    recipe.getName()
            );

            intent.putExtra(
                    "recipe_description",
                    recipe.getDescription()
            );

            intent.putExtra(
                    "recipe_ingredients",
                    recipe.getIngredients()
            );

            intent.putExtra(
                    "recipe_method",
                    recipe.getMethod()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView textRecipeName;
        TextView textRecipeDescription;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textRecipeName = itemView.findViewById(
                    R.id.textRecipeName
            );

            textRecipeDescription = itemView.findViewById(
                    R.id.textRecipeDescription
            );
        }
    }
}