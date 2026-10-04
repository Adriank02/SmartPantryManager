package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerPantry;
    private Button btnAddIngredient;
    private Button btnClearSearch;
    private EditText editSearchPantry;
    private Spinner spinnerCategory;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;

    private List<Ingredient> ingredientList;
    private List<Ingredient> filteredIngredientList;

    private String selectedCategory = "All Categories";
    private String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        editSearchPantry = findViewById(R.id.editSearchPantry);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        databaseHelper = new DatabaseHelper(this);

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        ingredientList = new ArrayList<>();
        filteredIngredientList = new ArrayList<>();

        ingredientAdapter = new IngredientAdapter(
                this,
                filteredIngredientList
        );

        recyclerPantry.setAdapter(ingredientAdapter);

        setupCategorySpinner();

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        btnClearSearch.setOnClickListener(v -> {

            editSearchPantry.setText("");
            spinnerCategory.setSelection(0);

        });

        editSearchPantry.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        searchText = s.toString();

                        filterIngredients();
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );

        spinnerCategory.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        selectedCategory =
                                parent.getItemAtPosition(position)
                                        .toString();

                        filterIngredients();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }

    private void setupCategorySpinner() {

        String[] categories = {
                "All Categories",
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

    private void loadIngredients() {

        ingredientList =
                databaseHelper.getAllIngredients();

        filterIngredients();
    }

    private void filterIngredients() {

        filteredIngredientList.clear();

        for (Ingredient ingredient : ingredientList) {

            boolean matchesSearch = true;
            boolean matchesCategory = true;

            if (searchText != null &&
                    !searchText.trim().isEmpty()) {

                String name =
                        ingredient.getName().toLowerCase();

                matchesSearch =
                        name.contains(
                                searchText.toLowerCase().trim()
                        );
            }

            if (!selectedCategory.equals("All Categories")) {

                String category =
                        ingredient.getCategory();

                if (category == null) {
                    category = "";
                }

                matchesCategory =
                        category.equalsIgnoreCase(
                                selectedCategory
                        );
            }

            if (matchesSearch && matchesCategory) {

                filteredIngredientList.add(ingredient);
            }
        }

        ingredientAdapter.updateList(
                filteredIngredientList
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadIngredients();
    }
}