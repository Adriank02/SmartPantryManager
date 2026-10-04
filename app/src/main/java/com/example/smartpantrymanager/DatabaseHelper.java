package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    // ---------------------------------------------------------
    // INGREDIENT TABLE
    // ---------------------------------------------------------

    private static final String TABLE_INGREDIENTS = "ingredients";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_MIN_STOCK = "min_stock";

    // ---------------------------------------------------------
    // RECIPE TABLE
    // ---------------------------------------------------------

    private static final String TABLE_RECIPES = "recipes";

    private static final String COLUMN_RECIPE_ID = "recipe_id";
    private static final String COLUMN_RECIPE_NAME = "name";
    private static final String COLUMN_RECIPE_DESCRIPTION = "description";
    private static final String COLUMN_RECIPE_METHOD = "method";

    // ---------------------------------------------------------
    // RECIPE INGREDIENT TABLE
    // ---------------------------------------------------------

    private static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    private static final String COLUMN_RECIPE_INGREDIENT_ID =
            "recipe_ingredient_id";

    private static final String COLUMN_RECIPE_REF_ID =
            "recipe_id";

    private static final String COLUMN_REQUIRED_NAME =
            "ingredient_name";

    private static final String COLUMN_REQUIRED_QUANTITY =
            "required_quantity";

    private static final String COLUMN_REQUIRED_UNIT =
            "required_unit";

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =========================================================
    // DATABASE CREATION
    // =========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        createIngredientTable(db);

        createRecipeTable(db);

        createRecipeIngredientTable(db);

        seedRecipes(db);
    }

    // =========================================================
    // DATABASE UPGRADE
    // =========================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        /*
         * IMPORTANT:
         *
         * We DO NOT delete the ingredients table.
         *
         * Existing pantry data must remain safe.
         */

        if (oldVersion < 2) {

            createRecipeTable(db);

            createRecipeIngredientTable(db);

            seedRecipes(db);
        }
    }

    // =========================================================
    // CREATE INGREDIENT TABLE
    // =========================================================

    private void createIngredientTable(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE IF NOT EXISTS "
                        + TABLE_INGREDIENTS
                        + " ("
                        + COLUMN_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + COLUMN_NAME
                        + " TEXT NOT NULL, "

                        + COLUMN_QUANTITY
                        + " REAL NOT NULL, "

                        + COLUMN_UNIT
                        + " TEXT NOT NULL, "

                        + COLUMN_EXPIRY_DATE
                        + " TEXT, "

                        + COLUMN_CATEGORY
                        + " TEXT, "

                        + COLUMN_MIN_STOCK
                        + " REAL DEFAULT 0"
                        + ")";

        db.execSQL(createTable);
    }

    // =========================================================
    // CREATE RECIPE TABLE
    // =========================================================

    private void createRecipeTable(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE IF NOT EXISTS "
                        + TABLE_RECIPES
                        + " ("

                        + COLUMN_RECIPE_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + COLUMN_RECIPE_NAME
                        + " TEXT NOT NULL, "

                        + COLUMN_RECIPE_DESCRIPTION
                        + " TEXT, "

                        + COLUMN_RECIPE_METHOD
                        + " TEXT"
                        + ")";

        db.execSQL(createTable);
    }

    // =========================================================
    // CREATE RECIPE INGREDIENT TABLE
    // =========================================================

    private void createRecipeIngredientTable(
            SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE IF NOT EXISTS "
                        + TABLE_RECIPE_INGREDIENTS
                        + " ("

                        + COLUMN_RECIPE_INGREDIENT_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + COLUMN_RECIPE_REF_ID
                        + " INTEGER NOT NULL, "

                        + COLUMN_REQUIRED_NAME
                        + " TEXT NOT NULL, "

                        + COLUMN_REQUIRED_QUANTITY
                        + " REAL NOT NULL, "

                        + COLUMN_REQUIRED_UNIT
                        + " TEXT NOT NULL, "

                        + "FOREIGN KEY("
                        + COLUMN_RECIPE_REF_ID
                        + ") REFERENCES "
                        + TABLE_RECIPES
                        + "("
                        + COLUMN_RECIPE_ID
                        + ")"
                        + ")";

        db.execSQL(createTable);
    }

    // =========================================================
    // ADD INGREDIENT
    // =========================================================

    public long addIngredient(
            String name,
            double quantity,
            String unit,
            String expiryDate,
            String category,
            double minStock) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                name
        );

        values.put(
                COLUMN_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_UNIT,
                unit
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                expiryDate
        );

        values.put(
                COLUMN_CATEGORY,
                category
        );

        values.put(
                COLUMN_MIN_STOCK,
                minStock
        );

        long result =
                db.insert(
                        TABLE_INGREDIENTS,
                        null,
                        values
                );

        db.close();

        return result;
    }

    // =========================================================
    // UPDATE INGREDIENT
    // =========================================================

    public int updateIngredient(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate,
            String category,
            double minStock) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                name
        );

        values.put(
                COLUMN_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_UNIT,
                unit
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                expiryDate
        );

        values.put(
                COLUMN_CATEGORY,
                category
        );

        values.put(
                COLUMN_MIN_STOCK,
                minStock
        );

        int result =
                db.update(
                        TABLE_INGREDIENTS,
                        values,
                        COLUMN_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }

    // =========================================================
    // DELETE INGREDIENT
    // =========================================================

    public int deleteIngredient(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_INGREDIENTS,
                        COLUMN_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }

    // =========================================================
    // GET ALL INGREDIENTS
    // =========================================================

    public List<Ingredient> getAllIngredients() {

        List<Ingredient> ingredientList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_INGREDIENTS,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_NAME + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_UNIT
                                )
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_EXPIRY_DATE
                                )
                        );

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_CATEGORY
                                )
                        );

                double minStock =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_MIN_STOCK
                                )
                        );

                Ingredient ingredient =
                        new Ingredient(
                                id,
                                name,
                                quantity,
                                unit,
                                expiryDate,
                                category,
                                minStock
                        );

                ingredientList.add(
                        ingredient
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredientList;
    }

    // =========================================================
    // ADD RECIPE
    // =========================================================

    private long addRecipe(
            SQLiteDatabase db,
            String name,
            String description,
            String method) {

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                name
        );

        values.put(
                COLUMN_RECIPE_DESCRIPTION,
                description
        );

        values.put(
                COLUMN_RECIPE_METHOD,
                method
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    // =========================================================
    // ADD RECIPE INGREDIENT
    // =========================================================

    private void addRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_REF_ID,
                recipeId
        );

        values.put(
                COLUMN_REQUIRED_NAME,
                ingredientName
        );

        values.put(
                COLUMN_REQUIRED_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_REQUIRED_UNIT,
                unit
        );

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }

    // =========================================================
    // SEED RECIPES
    // =========================================================

    private void seedRecipes(SQLiteDatabase db) {

        /*
         * 1. Creamy Rice
         */

        long recipeId =
                addRecipe(
                        db,
                        "Creamy Rice",
                        "A simple creamy rice dish made with rice and milk.",
                        "Cook the rice until tender. Heat the milk separately. "
                                + "Combine the cooked rice and milk and simmer gently "
                                + "until creamy. Serve warm."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "rice",
                1.0,
                "kg"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                500.0,
                "ml"
        );

        /*
         * 2. Vegetable Rice
         */

        recipeId =
                addRecipe(
                        db,
                        "Vegetable Rice",
                        "A simple rice dish using rice and carrots.",
                        "Cook the rice until tender. Chop the carrots. "
                                + "Cook the carrots until soft, then mix them "
                                + "with the cooked rice and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "rice",
                1.0,
                "kg"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "carrot",
                2.0,
                "unit"
        );

        /*
         * 3. Fruit and Yogurt Bowl
         */

        recipeId =
                addRecipe(
                        db,
                        "Fruit and Yogurt Bowl",
                        "A quick snack made with apple and yogurt.",
                        "Wash and slice the apple. Place the apple in a bowl "
                                + "and add the yogurt. Mix gently and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "apple",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "yogurt",
                200.0,
                "ml"
        );

        /*
         * 4. Healthy Grain Bowl
         */

        recipeId =
                addRecipe(
                        db,
                        "Healthy Grain Bowl",
                        "A simple meal combining rice, vegetables and milk.",
                        "Cook the rice. Cook the carrot until tender. "
                                + "Add the cooked ingredients to a bowl and "
                                + "serve with warm milk."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "rice",
                1.0,
                "kg"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "carrot",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                250.0,
                "ml"
        );

        /*
         * 5. Banana Smoothie
         */

        recipeId =
                addRecipe(
                        db,
                        "Banana Smoothie",
                        "A simple smoothie made with banana and milk.",
                        "Peel the bananas. Add bananas and milk to a blender. "
                                + "Blend until smooth and serve chilled."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "banana",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                250.0,
                "ml"
        );

        /*
         * 6. Egg Fried Rice
         */

        recipeId =
                addRecipe(
                        db,
                        "Egg Fried Rice",
                        "A quick meal using rice, eggs and onion.",
                        "Cook the rice. Chop the onion. Scramble the eggs "
                                + "in a pan, add the onion and cooked rice, "
                                + "then stir-fry until heated through."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "rice",
                1.0,
                "kg"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "egg",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "onion",
                1.0,
                "unit"
        );

        /*
         * 7. Tomato Pasta
         */

        recipeId =
                addRecipe(
                        db,
                        "Tomato Pasta",
                        "A simple pasta dish with fresh tomatoes and onion.",
                        "Cook the pasta according to the package instructions. "
                                + "Chop the tomatoes and onion. Cook the vegetables "
                                + "until soft and mix with the pasta."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "pasta",
                250.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "tomato",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "onion",
                1.0,
                "unit"
        );

        /*
         * 8. Vegetable Pasta
         */

        recipeId =
                addRecipe(
                        db,
                        "Vegetable Pasta",
                        "Pasta combined with carrots and tomatoes.",
                        "Cook the pasta. Chop the carrot and tomato. "
                                + "Cook the vegetables until tender and "
                                + "combine them with the pasta."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "pasta",
                250.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "carrot",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "tomato",
                1.0,
                "unit"
        );

        /*
         * 9. Pancakes
         */

        recipeId =
                addRecipe(
                        db,
                        "Pancakes",
                        "Simple pancakes made with flour, milk and egg.",
                        "Mix the flour, milk and egg into a smooth batter. "
                                + "Heat a pan and cook small portions of batter "
                                + "on both sides until golden."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "flour",
                200.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                250.0,
                "ml"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "egg",
                1.0,
                "unit"
        );

        /*
         * 10. Omelette
         */

        recipeId =
                addRecipe(
                        db,
                        "Vegetable Omelette",
                        "An omelette made with eggs, tomato and onion.",
                        "Beat the eggs. Chop the tomato and onion. "
                                + "Cook the vegetables briefly, pour in the eggs "
                                + "and cook until set."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "egg",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "tomato",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "onion",
                1.0,
                "unit"
        );

        /*
         * 11. Avocado Toast
         */

        recipeId =
                addRecipe(
                        db,
                        "Avocado Toast",
                        "Toast topped with mashed avocado.",
                        "Toast the bread. Mash the avocado with a fork. "
                                + "Spread the avocado over the toast and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "bread",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "avocado",
                1.0,
                "unit"
        );

        /*
         * 12. Peanut Butter Banana Toast
         */

        recipeId =
                addRecipe(
                        db,
                        "Peanut Butter Banana Toast",
                        "Toast topped with peanut butter and banana.",
                        "Toast the bread and spread peanut butter over it. "
                                + "Slice the banana and place the slices on top."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "bread",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "banana",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "peanut butter",
                2.0,
                "tbsp"
        );

        /*
         * 13. Chicken Rice Bowl
         */

        recipeId =
                addRecipe(
                        db,
                        "Chicken Rice Bowl",
                        "A filling bowl made with rice, chicken and carrots.",
                        "Cook the rice. Cook the chicken thoroughly and cut "
                                + "into pieces. Cook the carrot until tender. "
                                + "Combine all ingredients in a bowl."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "rice",
                1.0,
                "kg"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "chicken",
                250.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "carrot",
                1.0,
                "unit"
        );

        /*
         * 14. Chicken Pasta
         */

        recipeId =
                addRecipe(
                        db,
                        "Chicken Pasta",
                        "Pasta with chicken and tomato.",
                        "Cook the pasta. Cook the chicken thoroughly and "
                                + "cut into pieces. Cook the tomatoes until soft "
                                + "and combine everything with the pasta."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "pasta",
                250.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "chicken",
                250.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "tomato",
                2.0,
                "unit"
        );

        /*
         * 15. Tomato Soup
         */

        recipeId =
                addRecipe(
                        db,
                        "Tomato Soup",
                        "A simple soup made with tomatoes, onion and carrot.",
                        "Chop the tomatoes, onion and carrot. Cook the vegetables "
                                + "until soft. Add water and simmer until the "
                                + "vegetables are tender, then blend if desired."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "tomato",
                3.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "onion",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "carrot",
                1.0,
                "unit"
        );

        /*
         * 16. Vegetable Soup
         */

        recipeId =
                addRecipe(
                        db,
                        "Vegetable Soup",
                        "A simple soup using potatoes, carrots and onion.",
                        "Chop the vegetables. Add them to a pot with water "
                                + "and simmer until soft. Season and serve warm."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "potato",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "carrot",
                2.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "onion",
                1.0,
                "unit"
        );

        /*
         * 17. Mashed Potatoes
         */

        recipeId =
                addRecipe(
                        db,
                        "Mashed Potatoes",
                        "Creamy mashed potatoes made with milk and butter.",
                        "Boil the potatoes until soft. Drain and mash them. "
                                + "Add milk and butter, mix until smooth and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "potato",
                3.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                250.0,
                "ml"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "butter",
                50.0,
                "g"
        );

        /*
         * 18. Banana Pancakes
         */

        recipeId =
                addRecipe(
                        db,
                        "Banana Pancakes",
                        "Pancakes made with banana, flour, egg and milk.",
                        "Mash the banana. Mix it with flour, egg and milk. "
                                + "Cook small portions in a heated pan until "
                                + "golden on both sides."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "banana",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "flour",
                200.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "egg",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                250.0,
                "ml"
        );

        /*
         * 19. Apple Oatmeal
         */

        recipeId =
                addRecipe(
                        db,
                        "Apple Oatmeal",
                        "Warm oatmeal made with apple and milk.",
                        "Cook the oats with milk until soft. Chop the apple "
                                + "and add it to the oatmeal. Cook briefly and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "oats",
                100.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "apple",
                1.0,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                250.0,
                "ml"
        );

        /*
         * 20. Rice Pudding
         */

        recipeId =
                addRecipe(
                        db,
                        "Rice Pudding",
                        "A simple sweet dish made with rice, milk and sugar.",
                        "Cook the rice until soft. Add the milk and sugar. "
                                + "Simmer gently while stirring until creamy. "
                                + "Serve warm or chilled."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "rice",
                200.0,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "milk",
                500.0,
                "ml"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "sugar",
                50.0,
                "g"
        );
    }

    // =========================================================
    // GET ALL RECIPES
    // =========================================================

    public List<Recipe> getAllRecipes() {

        List<Recipe> recipeList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_RECIPE_NAME + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_NAME
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_DESCRIPTION
                                )
                        );

                String method =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_METHOD
                                )
                        );

                long recipeId =
                        cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_ID
                                )
                        );

                String ingredients =
                        getRecipeIngredientsText(
                                db,
                                recipeId
                        );

                recipeList.add(
                        new Recipe(
                                name,
                                description,
                                ingredients,
                                method
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipeList;
    }

    // =========================================================
    // GET RECIPE INGREDIENTS AS TEXT
    // =========================================================

    private String getRecipeIngredientsText(
            SQLiteDatabase db,
            long recipeId) {

        StringBuilder ingredients =
                new StringBuilder();

        Cursor cursor =
                db.query(
                        TABLE_RECIPE_INGREDIENTS,
                        null,
                        COLUMN_RECIPE_REF_ID + "=?",
                        new String[]{
                                String.valueOf(recipeId)
                        },
                        null,
                        null,
                        COLUMN_RECIPE_INGREDIENT_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_UNIT
                                )
                        );

                if (ingredients.length() > 0) {
                    ingredients.append("\n");
                }

                ingredients.append(
                                formatQuantity(quantity)
                        )
                        .append(" ")
                        .append(unit)
                        .append(" ")
                        .append(name);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients.toString();
    }

    // =========================================================
    // FORMAT QUANTITY
    // =========================================================

    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {

            return String.valueOf(
                    (long) quantity
            );
        }

        return String.valueOf(quantity);
    }
}