# Smart Pantry Manager

## 1. App Description

Smart Pantry Manager is an Android application developed in Java to help users manage household ingredients, monitor pantry stock, track expiry dates and reduce food waste.

The application allows users to:

* Add ingredients to their pantry.
* View all stored ingredients.
* Search for ingredients by name.
* Filter ingredients by category.
* Edit existing ingredients.
* Delete ingredients.
* Record ingredient quantity, unit, expiry date, category and minimum stock level.
* Receive low-stock warnings.
* Receive expired and expiring-soon warnings.
* View recipe suggestions based on ingredients available in the pantry.
* Only receive recipe suggestions when all required ingredients are available in sufficient quantities.
* View detailed recipe information, including ingredients and preparation instructions.
* Store user profile information through the Settings screen.
* Store pantry information so that it remains available after the application is closed and reopened.

## 2. Technologies Used

* Android Studio
* Java
* XML
* SQLite
* RecyclerView
* Android Intents
* SharedPreferences
* Git and GitHub

## 3. Database

The application uses **SQLite** as its local database.

SQLite was selected because it is built into Android, does not require a separate database server, works well for a small mobile application, and supports the Create, Read, Update and Delete (CRUD) operations required by the application.

The database is called `SmartPantry.db`.

The database stores ingredient information including:

* Ingredient ID
* Name
* Quantity
* Unit
* Expiry date
* Category
* Minimum stock level

The database also stores recipe information, including recipe names, descriptions, required ingredients, required quantities and preparation methods.

The application contains **20 preloaded recipes** that are available for recipe matching.

## 4. Main Features

### Pantry Management

Users can add, view, edit and delete ingredients in their pantry.

Each ingredient can contain a name, current quantity, unit, expiry date, category and minimum stock level.

### Search and Filtering

Users can search for pantry ingredients by name and filter ingredients according to their category.

A Clear button allows the user to reset the search and category filter.

### Stock Management

The application records the current quantity and minimum stock level.

When an ingredient's current quantity is equal to or below its minimum stock level, a low-stock warning is displayed.

### Expiry Monitoring

The application records the expiry date of each ingredient.

The application identifies ingredients that have already expired and ingredients that are due to expire within seven days.

### Recipe Suggestions

The application contains 20 preloaded recipes.

Recipe suggestions are generated according to the ingredients currently available in the user's pantry.

A recipe is displayed only when **all required ingredients are available in sufficient quantities**.

Partial matches are excluded. For example, if a recipe requires five units of an ingredient and the pantry contains only four units, that recipe will not be suggested.

The application also supports common unit differences such as grams and kilograms and millilitres and litres when checking ingredient quantities.

### Recipe Details

Users can select a suggested recipe to open a detailed recipe screen.

The Recipe Details screen displays:

* Recipe name
* Recipe description
* Required ingredients
* Preparation method

### Settings and Profile

The Settings screen allows the user to enter and save their name and email address.

The information is stored using Android SharedPreferences and remains available after the application is closed and reopened.

### Data Persistence

Pantry information is stored in SQLite and remains available when the application is closed and reopened.

User profile settings are stored using SharedPreferences.

## 5. Project Structure

The main activities include:

* `MainActivity` – application home screen.
* `PantryActivity` – displays and manages pantry ingredients.
* `AddEditIngredientActivity` – adds and edits ingredients.
* `RecipesActivity` – displays recipe suggestions.
* `RecipeDetailActivity` – displays detailed recipe information.
* `SettingsActivity` – manages user profile settings.

Supporting Java classes include:

* `Ingredient`
* `IngredientAdapter`
* `Recipe`
* `RecipeAdapter`
* `DatabaseHelper`

## 6. Setup and Run Instructions

1. Install Android Studio.
2. Clone or download the Smart Pantry Manager repository from GitHub.
3. Open the project in Android Studio.
4. Allow Gradle to sync and download the required dependencies.
5. Create or select an Android emulator, or connect an Android device with USB debugging enabled.
6. Build the project.
7. Run the application using the Android Studio Run button.
8. Open **My Pantry** to begin adding ingredients.

## 7. Version Control

This project is managed using Git and GitHub.

The repository contains genuine incremental commits documenting the development, debugging and improvement of the application.

GitHub repository:

https://github.com/Adriank02/SmartPantryManager

## 8. Application Summary

Smart Pantry Manager combines pantry management, stock monitoring, expiry tracking and recipe suggestions in one Android application.

The application uses SQLite to persist pantry and recipe information and applies strict ingredient and quantity matching to ensure that suggested recipes can actually be prepared using the ingredients currently available in the pantry.
