# Smart Pantry Manager

## 1. App Description

Smart Pantry Manager is an Android application developed in Java to help users manage household ingredients and reduce food waste.

The application allows users to:

* Add ingredients to their pantry.
* View all stored ingredients.
* Search for ingredients.
* Filter ingredients by category.
* Edit existing ingredients.
* Delete ingredients.
* Record ingredient quantity, unit, expiry date, category and minimum stock level.
* Receive low-stock and expiry-related warnings.
* View recipe suggestions based on ingredients available in the pantry.
* Store pantry information so that it remains available after the application is closed and reopened.

## 2. Technologies Used

* Android Studio
* Java
* XML
* SQLite
* RecyclerView
* Android Intents
* Git and GitHub

## 3. Database

The application uses **SQLite** as its database.

SQLite was selected because it is built into Android, does not require a separate database server, works well for a small mobile application, and allows pantry data to be stored locally on the device. It also supports the CRUD operations required by the application: Create, Read, Update and Delete.

The database is called `SmartPantry.db` and stores ingredient information including:

* Ingredient ID
* Name
* Quantity
* Unit
* Expiry date
* Category
* Minimum stock level

## 4. Main Features

### Pantry Management

Users can add, view, edit and delete ingredients.

### Search and Filtering

Users can search for an ingredient by name and filter ingredients according to their category.

### Stock Management

The application records the current quantity and minimum stock level. Ingredients can be identified when their quantity reaches or falls below the minimum stock level.

### Expiry Monitoring

The application displays expiry information and provides warnings for expired or soon-to-expire ingredients.

### Recipe Suggestions

The application provides recipe suggestions based on ingredients available in the user's pantry.

### Data Persistence

Ingredient information is stored in SQLite and remains available when the application is closed and reopened.

## 5. Project Structure

The main activities include:

* `MainActivity` – application home screen.
* `PantryActivity` – displays and manages pantry ingredients.
* `AddEditIngredientActivity` – adds and edits ingredients.
* `RecipesActivity` – displays recipe suggestions.
* `SettingsActivity` – displays application information and settings.

Supporting Java classes include:

* `Ingredient`
* `IngredientAdapter`
* `Recipe`
* `RecipeAdapter`
* `DatabaseHelper`

## 6. Setup and Run Instructions

1. Install Android Studio.
2. Clone or download the SmartPantryManager repository from GitHub.
3. Open the project in Android Studio.
4. Allow Gradle to sync and download the required dependencies.
5. Create or select an Android emulator, or connect an Android device with USB debugging enabled.
6. Build the project.
7. Run the application using the Android Studio Run button.
8. Open **My Pantry** to begin adding ingredients.

## 7. Version Control

This project is managed using Git and GitHub.

The repository contains incremental commits documenting the development and improvement of the application.

GitHub repository:

https://github.com/Adriank02/SmartPantryManager
