package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private EditText editExpiryDate;
    private EditText editMinStock;

    private Spinner spinnerCategory;

    private Button btnSaveIngredient;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        editIngredientName =
                findViewById(R.id.editIngredientName);

        editQuantity =
                findViewById(R.id.editQuantity);

        editUnit =
                findViewById(R.id.editUnit);

        editExpiryDate =
                findViewById(R.id.editExpiryDate);

        editMinStock =
                findViewById(R.id.editMinStock);

        spinnerCategory =
                findViewById(R.id.spinnerCategory);

        btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        btnCancel =
                findViewById(R.id.btnCancel);

        databaseHelper =
                new DatabaseHelper(this);

        setupCategorySpinner();

        loadIngredientData();

        btnSaveIngredient.setOnClickListener(v ->
                saveIngredient()
        );

        btnCancel.setOnClickListener(v ->
                finish()
        );
    }

    private void setupCategorySpinner() {

        String[] categories = {
                "Fruits",
                "Vegetables",
                "Dairy",
                "Meat",
                "Grains",
                "Canned Goods",
                "Frozen Foods",
                "Snacks",
                "Beverages",
                "Other"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(adapter);
    }

    private void loadIngredientData() {

        if (getIntent().hasExtra("ingredient_id")) {

            ingredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String name =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double quantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            0
                    );

            String unit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String expiryDate =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );

            String category =
                    getIntent().getStringExtra(
                            "ingredient_category"
                    );

            double minStock =
                    getIntent().getDoubleExtra(
                            "ingredient_min_stock",
                            0
                    );

            editIngredientName.setText(name);
            editQuantity.setText(
                    String.valueOf(quantity)
            );
            editUnit.setText(unit);
            editExpiryDate.setText(expiryDate);
            editMinStock.setText(
                    String.valueOf(minStock)
            );

            if (category != null) {

                ArrayAdapter<String> adapter =
                        (ArrayAdapter<String>)
                                spinnerCategory.getAdapter();

                int categoryPosition =
                        adapter.getPosition(category);

                if (categoryPosition >= 0) {

                    spinnerCategory.setSelection(
                            categoryPosition
                    );
                }
            }

            btnSaveIngredient.setText(
                    "Update Ingredient"
            );
        }
    }

    private void saveIngredient() {

        String name =
                editIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                editQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                editUnit
                        .getText()
                        .toString()
                        .trim();

        String expiryDate =
                editExpiryDate
                        .getText()
                        .toString()
                        .trim();

        String minStockText =
                editMinStock
                        .getText()
                        .toString()
                        .trim();

        String category =
                spinnerCategory
                        .getSelectedItem()
                        .toString();

        if (name.isEmpty()) {

            editIngredientName.setError(
                    "Enter an ingredient name"
            );

            editIngredientName.requestFocus();

            return;
        }

        if (quantityText.isEmpty()) {

            editQuantity.setError(
                    "Enter the quantity"
            );

            editQuantity.requestFocus();

            return;
        }

        if (unit.isEmpty()) {

            editUnit.setError(
                    "Enter the unit"
            );

            editUnit.requestFocus();

            return;
        }

        if (minStockText.isEmpty()) {

            editMinStock.setError(
                    "Enter the minimum stock level"
            );

            editMinStock.requestFocus();

            return;
        }

        double quantity;
        double minStock;

        try {

            quantity =
                    Double.parseDouble(quantityText);

            minStock =
                    Double.parseDouble(minStockText);

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (ingredientId == -1) {

            long result =
                    databaseHelper.addIngredient(
                            name,
                            quantity,
                            unit,
                            expiryDate,
                            category,
                            minStock
                    );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to save ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            int result =
                    databaseHelper.updateIngredient(
                            ingredientId,
                            name,
                            quantity,
                            unit,
                            expiryDate,
                            category,
                            minStock
                    );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}