package com.example.smartpantrymanager;

public class Recipe {

    private String name;
    private String description;
    private String ingredients;
    private String method;

    public Recipe(
            String name,
            String description,
            String ingredients) {

        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.method = "";
    }

    public Recipe(
            String name,
            String description,
            String ingredients,
            String method) {

        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.method = method;
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

    public String getMethod() {
        return method;
    }
}