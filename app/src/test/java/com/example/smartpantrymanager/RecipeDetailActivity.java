
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeDetailName;
    private TextView textRecipeDetailDescription;
    private TextView textRecipeDetailIngredients;
    private TextView textRecipeDetailMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textRecipeDetailName = findViewById(R.id.textRecipeDetailName);
        textRecipeDetailDescription = findViewById(R.id.textRecipeDetailDescription);
        textRecipeDetailIngredients = findViewById(R.id.textRecipeDetailIngredients);
        textRecipeDetailMethod = findViewById(R.id.textRecipeDetailMethod);

        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeDescription = getIntent().getStringExtra("recipe_description");
        String recipeIngredients = getIntent().getStringExtra("recipe_ingredients");
        String recipeMethod = getIntent().getStringExtra("recipe_method");

        textRecipeDetailName.setText(
                recipeName != null ? recipeName : "Recipe Details"
        );

        textRecipeDetailDescription.setText(
                recipeDescription != null ? recipeDescription : ""
        );

        textRecipeDetailIngredients.setText(
                recipeIngredients != null ? recipeIngredients : "No ingredients listed."
        );

        textRecipeDetailMethod.setText(
                recipeMethod != null && !recipeMethod.trim().isEmpty()
                        ? recipeMethod
                        : "Preparation instructions are not available yet."
        );
    }
}