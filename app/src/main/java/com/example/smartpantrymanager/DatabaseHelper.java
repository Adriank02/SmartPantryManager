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
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_INGREDIENTS = "ingredients";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_MIN_STOCK = "min_stock";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " REAL NOT NULL, " +
                COLUMN_UNIT + " TEXT NOT NULL, " +
                COLUMN_EXPIRY_DATE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_MIN_STOCK + " REAL DEFAULT 0" +
                ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    public long addIngredient(String name,
                              double quantity,
                              String unit,
                              String expiryDate,
                              String category,
                              double minStock) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_MIN_STOCK, minStock);

        long result = db.insert(TABLE_INGREDIENTS, null, values);

        db.close();

        return result;
    }

    public int updateIngredient(int id,
                                String name,
                                double quantity,
                                String unit,
                                String expiryDate,
                                String category,
                                double minStock) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_MIN_STOCK, minStock);

        int result = db.update(
                TABLE_INGREDIENTS,
                values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }

    public int deleteIngredient(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_INGREDIENTS,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }

    public List<Ingredient> getAllIngredients() {

        List<Ingredient> ingredientList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
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

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
                );

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)
                );

                double minStock = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_MIN_STOCK)
                );

                Ingredient ingredient = new Ingredient(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate,
                        category,
                        minStock
                );

                ingredientList.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredientList;
    }
}