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

        recyclerRecipes =
                findViewById(R.id.recyclerRecipes);

        databaseHelper =
                new DatabaseHelper(this);

        recipeList =
                new ArrayList<>();

        recipeAdapter =
                new RecipeAdapter(this, recipeList);

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerRecipes.setAdapter(
                recipeAdapter
        );

        createRecipeSuggestions();
    }

    // =========================================================
    // CREATE STRICT RECIPE SUGGESTIONS
    // =========================================================

    private void createRecipeSuggestions() {

        recipeList.clear();

        /*
         * Get all recipes stored in the database.
         */
        List<Recipe> allRecipes =
                databaseHelper.getAllRecipes();

        /*
         * Get all ingredients currently in the pantry.
         */
        List<Ingredient> pantryIngredients =
                databaseHelper.getAllIngredients();

        /*
         * Check every database recipe.
         *
         * A recipe is added ONLY when all of its
         * required ingredients are available.
         */
        for (Recipe recipe : allRecipes) {

            if (recipeCanBeMade(
                    recipe,
                    pantryIngredients
            )) {

                recipeList.add(recipe);
            }
        }

        /*
         * If nothing can currently be made,
         * provide feedback to the user.
         */
        if (recipeList.isEmpty()) {

            recipeList.add(
                    new Recipe(
                            "No Recipe Suggestions Yet",
                            "No complete recipes match your pantry right now.",
                            "Add the required ingredients and quantities to your pantry.",
                            "Once all required ingredients are available in sufficient quantities, the recipe will appear here."
                    )
            );
        }

        recipeAdapter.notifyDataSetChanged();
    }

    // =========================================================
    // CHECK WHETHER A RECIPE CAN BE MADE
    // =========================================================

    private boolean recipeCanBeMade(
            Recipe recipe,
            List<Ingredient> pantryIngredients) {

        /*
         * The database currently returns the required
         * ingredients as text.
         *
         * Each line has this format:
         *
         * quantity unit ingredient
         *
         * Example:
         *
         * 1 kg rice
         * 500 ml milk
         *
         * We check every required ingredient.
         */
        String ingredients =
                recipe.getIngredients();

        if (ingredients == null
                || ingredients.trim().isEmpty()) {

            return false;
        }

        String[] requiredIngredients =
                ingredients.split("\\n");

        for (String requiredIngredient :
                requiredIngredients) {

            if (!requiredIngredientIsAvailable(
                    requiredIngredient,
                    pantryIngredients
            )) {

                /*
                 * One missing ingredient means
                 * the entire recipe does NOT match.
                 */
                return false;
            }
        }

        /*
         * Every required ingredient passed.
         */
        return true;
    }

    // =========================================================
    // CHECK ONE REQUIRED INGREDIENT
    // =========================================================

    private boolean requiredIngredientIsAvailable(
            String requiredIngredient,
            List<Ingredient> pantryIngredients) {

        if (requiredIngredient == null) {
            return false;
        }

        String text =
                requiredIngredient
                        .trim()
                        .toLowerCase(Locale.ROOT);

        /*
         * Split the requirement into:
         *
         * quantity
         * unit
         * ingredient name
         */
        String[] parts =
                text.split("\\s+", 3);

        if (parts.length < 3) {
            return false;
        }

        double requiredQuantity;

        try {

            requiredQuantity =
                    Double.parseDouble(parts[0]);

        } catch (NumberFormatException e) {

            return false;
        }

        String requiredUnit =
                parts[1];

        String requiredName =
                parts[2];

        /*
         * Look through the pantry for the
         * required ingredient.
         */
        for (Ingredient pantryIngredient :
                pantryIngredients) {

            String pantryName =
                    normaliseIngredientName(
                            pantryIngredient.getName()
                    );

            String wantedName =
                    normaliseIngredientName(
                            requiredName
                    );

            if (!pantryName.equals(wantedName)) {
                continue;
            }

            /*
             * Convert both quantities to a
             * common base unit.
             */
            double pantryQuantity =
                    convertToBaseUnit(
                            pantryIngredient.getQuantity(),
                            pantryIngredient.getUnit()
                    );

            double neededQuantity =
                    convertToBaseUnit(
                            requiredQuantity,
                            requiredUnit
                    );

            /*
             * The pantry must contain AT LEAST
             * the required quantity.
             */
            if (areCompatibleUnits(
                    pantryIngredient.getUnit(),
                    requiredUnit
            )
                    && pantryQuantity >= neededQuantity) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // NORMALISE INGREDIENT NAME
    // =========================================================

    private String normaliseIngredientName(
            String name) {

        if (name == null) {
            return "";
        }

        String result =
                name.trim()
                        .toLowerCase(Locale.ROOT);

        /*
         * Handle common plural forms.
         *
         * berries -> berry
         * tomatoes -> tomato
         * potatoes -> potato
         * apples -> apple
         */
        if (result.endsWith("ies")
                && result.length() > 3) {

            result =
                    result.substring(
                            0,
                            result.length() - 3
                    ) + "y";

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

    // =========================================================
    // UNIT COMPATIBILITY
    // =========================================================

    private boolean areCompatibleUnits(
            String pantryUnit,
            String requiredUnit) {

        if (pantryUnit == null
                || requiredUnit == null) {

            return false;
        }

        String pantry =
                pantryUnit.trim()
                        .toLowerCase(Locale.ROOT);

        String required =
                requiredUnit.trim()
                        .toLowerCase(Locale.ROOT);

        /*
         * Weight units.
         */
        boolean pantryWeight =
                pantry.equals("g")
                        || pantry.equals("gram")
                        || pantry.equals("grams")
                        || pantry.equals("kg")
                        || pantry.equals("kilogram")
                        || pantry.equals("kilograms");

        boolean requiredWeight =
                required.equals("g")
                        || required.equals("gram")
                        || required.equals("grams")
                        || required.equals("kg")
                        || required.equals("kilogram")
                        || required.equals("kilograms");

        if (pantryWeight && requiredWeight) {
            return true;
        }

        /*
         * Volume units.
         */
        boolean pantryVolume =
                pantry.equals("ml")
                        || pantry.equals("milliliter")
                        || pantry.equals("millilitre")
                        || pantry.equals("milliliters")
                        || pantry.equals("millilitres")
                        || pantry.equals("l")
                        || pantry.equals("liter")
                        || pantry.equals("litre")
                        || pantry.equals("liters")
                        || pantry.equals("litres");

        boolean requiredVolume =
                required.equals("ml")
                        || required.equals("milliliter")
                        || required.equals("millilitre")
                        || required.equals("milliliters")
                        || required.equals("millilitres")
                        || required.equals("l")
                        || required.equals("liter")
                        || required.equals("litre")
                        || required.equals("liters")
                        || required.equals("litres");

        if (pantryVolume && requiredVolume) {
            return true;
        }

        /*
         * Count-based units.
         */
        boolean pantryCount =
                pantry.equals("unit")
                        || pantry.equals("units")
                        || pantry.equals("item")
                        || pantry.equals("items")
                        || pantry.equals("piece")
                        || pantry.equals("pieces");

        boolean requiredCount =
                required.equals("unit")
                        || required.equals("units")
                        || required.equals("item")
                        || required.equals("items")
                        || required.equals("piece")
                        || required.equals("pieces");

        if (pantryCount && requiredCount) {
            return true;
        }

        /*
         * Cooking measurements such as tablespoons
         * are only considered compatible with the
         * same type of measurement.
         */
        if (pantry.equals(required)) {
            return true;
        }

        return false;
    }

    // =========================================================
    // CONVERT UNITS TO BASE VALUES
    // =========================================================

    private double convertToBaseUnit(
            double quantity,
            String unit) {

        if (unit == null) {
            return quantity;
        }

        String normalisedUnit =
                unit.trim()
                        .toLowerCase(Locale.ROOT);

        switch (normalisedUnit) {

            // Weight
            case "kg":
            case "kilogram":
            case "kilograms":

                return quantity * 1000.0;

            case "g":
            case "gram":
            case "grams":

                return quantity;

            // Volume
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

    // =========================================================
    // REFRESH WHEN RETURNING TO SCREEN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            createRecipeSuggestions();
        }
    }
}