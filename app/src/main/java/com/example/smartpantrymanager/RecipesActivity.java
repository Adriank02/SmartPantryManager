package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipes;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipes);

        recyclerRecipes =
                findViewById(
                        R.id.recyclerRecipes
                );

        databaseHelper =
                new DatabaseHelper(this);

        recipeList =
                new ArrayList<>();

        recipeAdapter =
                new RecipeAdapter(
                        recipeList
                );

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerRecipes.setAdapter(
                recipeAdapter
        );

        createRecipeSuggestions();
    }

    private void createRecipeSuggestions() {

        recipeList.clear();

        List<Ingredient> ingredients =
                databaseHelper.getAllIngredients();

        boolean hasRice = false;
        boolean hasMilk = false;
        boolean hasVegetables = false;
        boolean hasFruit = false;
        boolean hasGrains = false;
        boolean hasDairy = false;

        for (Ingredient ingredient : ingredients) {

            String name =
                    ingredient.getName()
                            .toLowerCase();

            String category =
                    ingredient.getCategory();

            if (category == null) {
                category = "";
            }

            category =
                    category.toLowerCase();

            if (name.contains("rice")) {
                hasRice = true;
            }

            if (name.contains("milk")) {
                hasMilk = true;
            }

            if (category.contains("vegetable")) {
                hasVegetables = true;
            }

            if (category.contains("fruit")) {
                hasFruit = true;
            }

            if (category.contains("grain")) {
                hasGrains = true;
            }

            if (category.contains("dairy")) {
                hasDairy = true;
            }
        }

        if (hasRice && hasVegetables) {

            recipeList.add(
                    new Recipe(
                            "Vegetable Rice",
                            "A simple meal using rice and vegetables.",
                            "Rice and vegetables"
                    )
            );
        }

        if (hasRice && hasMilk) {

            recipeList.add(
                    new Recipe(
                            "Creamy Rice",
                            "A simple creamy rice dish using milk.",
                            "Rice and milk"
                    )
            );
        }

        if (hasFruit && hasDairy) {

            recipeList.add(
                    new Recipe(
                            "Fruit and Yogurt Bowl",
                            "A quick snack using fruit and dairy ingredients.",
                            "Fruit and dairy"
                    )
            );
        }

        if (hasGrains && hasVegetables) {

            recipeList.add(
                    new Recipe(
                            "Healthy Grain Bowl",
                            "A balanced meal using grains and vegetables.",
                            "Grains and vegetables"
                    )
            );
        }

        if (recipeList.isEmpty()) {

            recipeList.add(
                    new Recipe(
                            "No Recipe Suggestions Yet",
                            "Add more ingredients to your pantry to receive recipe suggestions.",
                            "Add ingredients such as rice, vegetables, fruit or dairy"
                    )
            );
        }

        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            createRecipeSuggestions();
        }
    }
}