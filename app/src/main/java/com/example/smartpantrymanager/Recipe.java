package com.example.smartpantrymanager;

public class Recipe {

    private String name;
    private String description;
    private String ingredients;

    public Recipe(
            String name,
            String description,
            String ingredients) {

        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }
}