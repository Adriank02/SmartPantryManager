package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipes;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipes);

        recyclerRecipes = findViewById(R.id.recyclerRecipes);

        databaseHelper = new DatabaseHelper(this);

        recipeList = new ArrayList<>();

        recipeAdapter = new RecipeAdapter(recipeList);

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerRecipes.setAdapter(recipeAdapter);

        createRecipeSuggestions();
    }

    /**
     * Creates recipe suggestions using the strict-matching rule.
     *
     * A recipe is suggested ONLY when:
     *
     * 1. Every required ingredient is present.
     * 2. The pantry quantity is at least the required quantity.
     * 3. Ingredient names are compared using simple normalisation
     *    to handle basic singular/plural differences.
     */
    private void createRecipeSuggestions() {

        recipeList.clear();

        List<Ingredient> pantryIngredients =
                databaseHelper.getAllIngredients();

        /*
         * Recipe 1:
         * Creamy Rice requires:
         * - Rice: 1 kg
         * - Milk: 500 ml
         */
        if (hasRequiredIngredient(
                pantryIngredients,
                "rice",
                1.0,
                "kg"
        )
                && hasRequiredIngredient(
                pantryIngredients,
                "milk",
                500.0,
                "ml"
        )) {

            recipeList.add(
                    new Recipe(
                            "Creamy Rice",
                            "A simple creamy rice dish made with rice and milk.",
                            "Rice (1 kg) and milk (500 ml)"
                    )
            );
        }

        /*
         * Recipe 2:
         * Vegetable Rice requires:
         * - Rice: 1 kg
         * - Carrot: 2
         */
        if (hasRequiredIngredient(
                pantryIngredients,
                "rice",
                1.0,
                "kg"
        )
                && hasRequiredIngredient(
                pantryIngredients,
                "carrot",
                2.0,
                "unit"
        )) {

            recipeList.add(
                    new Recipe(
                            "Vegetable Rice",
                            "A simple rice dish using rice and carrots.",
                            "Rice (1 kg) and carrots (2)"
                    )
            );
        }

        /*
         * Recipe 3:
         * Fruit and Yogurt Bowl requires:
         * - Apple: 1
         * - Yogurt: 200 ml
         */
        if (hasRequiredIngredient(
                pantryIngredients,
                "apple",
                1.0,
                "unit"
        )
                && hasRequiredIngredient(
                pantryIngredients,
                "yogurt",
                200.0,
                "ml"
        )) {

            recipeList.add(
                    new Recipe(
                            "Fruit and Yogurt Bowl",
                            "A quick snack made with fruit and yogurt.",
                            "Apple (1) and yogurt (200 ml)"
                    )
            );
        }

        /*
         * Recipe 4:
         * Healthy Grain Bowl requires:
         * - Rice: 1 kg
         * - Carrot: 1
         * - Milk: 250 ml
         */
        if (hasRequiredIngredient(
                pantryIngredients,
                "rice",
                1.0,
                "kg"
        )
                && hasRequiredIngredient(
                pantryIngredients,
                "carrot",
                1.0,
                "unit"
        )
                && hasRequiredIngredient(
                pantryIngredients,
                "milk",
                250.0,
                "ml"
        )) {

            recipeList.add(
                    new Recipe(
                            "Healthy Grain Bowl",
                            "A simple meal combining rice, vegetables and milk.",
                            "Rice (1 kg), carrot (1) and milk (250 ml)"
                    )
            );
        }

        /*
         * If no complete recipe matches, show feedback
         * instead of leaving the screen blank.
         */
        if (recipeList.isEmpty()) {

            recipeList.add(
                    new Recipe(
                            "No Recipe Suggestions Yet",
                            "No recipes match your pantry yet. Add the required ingredients and quantities.",
                            "Every required ingredient must be available in your pantry."
                    )
            );
        }

        recipeAdapter.notifyDataSetChanged();
    }

    /**
     * Checks whether the pantry contains a required ingredient
     * in at least the required quantity.
     */
    private boolean hasRequiredIngredient(
            List<Ingredient> pantryIngredients,
            String requiredName,
            double requiredQuantity,
            String requiredUnit
    ) {

        for (Ingredient ingredient : pantryIngredients) {

            String pantryName =
                    normaliseIngredientName(
                            ingredient.getName()
                    );

            String wantedName =
                    normaliseIngredientName(
                            requiredName
                    );

            if (pantryName.equals(wantedName)) {

                double pantryQuantity =
                        convertToBaseUnit(
                                ingredient.getQuantity(),
                                ingredient.getUnit()
                        );

                double requiredAmount =
                        convertToBaseUnit(
                                requiredQuantity,
                                requiredUnit
                        );

                if (pantryQuantity >= requiredAmount) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Normalises simple ingredient-name differences.
     *
     * Examples:
     * tomato -> tomato
     * tomatoes -> tomato
     * potato -> potato
     * potatoes -> potato
     * apple -> apple
     * apples -> apple
     */
    private String normaliseIngredientName(String name) {

        if (name == null) {
            return "";
        }

        String result =
                name.trim()
                        .toLowerCase(Locale.ROOT);

        if (result.endsWith("ies")
                && result.length() > 3) {

            result =
                    result.substring(
                            0,
                            result.length() - 3
                    )
                            + "y";

        } else if (result.endsWith("oes")
                && result.length() > 3) {

            result =
                    result.substring(
                            0,
                            result.length() - 2
                    );

        } else if (result.endsWith("es")
                && result.length() > 3) {

            result =
                    result.substring(
                            0,
                            result.length() - 2
                    );

        } else if (result.endsWith("s")
                && result.length() > 2) {

            result =
                    result.substring(
                            0,
                            result.length() - 1
                    );
        }

        return result;
    }

    /**
     * Converts common units to a base unit so that
     * quantities can still be compared when the user
     * enters grams/kilograms or millilitres/litres.
     *
     * Weight base unit = grams.
     * Volume base unit = millilitres.
     * Count-based units remain unchanged.
     */
    private double convertToBaseUnit(
            double quantity,
            String unit
    ) {

        if (unit == null) {
            return quantity;
        }

        String normalisedUnit =
                unit.trim()
                        .toLowerCase(Locale.ROOT);

        switch (normalisedUnit) {

            case "kg":
            case "kilogram":
            case "kilograms":
                return quantity * 1000.0;

            case "g":
            case "gram":
            case "grams":
                return quantity;

            case "l":
            case "liter":
            case "litre":
            case "liters":
            case "litres":
                return quantity * 1000.0;

            case "ml":
            case "milliliter":
            case "millilitre":
            case "milliliters":
            case "millilitres":
                return quantity;

            default:
                return quantity;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            createRecipeSuggestions();
        }
    }
}