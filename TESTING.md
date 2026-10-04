# Smart Pantry Manager - Testing

## 1. Build Testing

The application was successfully built in Android Studio using the Java Android project configuration.

Build result:

- Build successful
- No compilation errors

## 2. Main Screen Testing

The main screen was tested successfully.

Tested functions:

- My Pantry button
- Suggested Recipes button
- Settings button

All three buttons opened their corresponding screens successfully.

## 3. Pantry Testing

The pantry functionality was tested successfully.

Tested functions:

- Add ingredient
- View ingredient
- Edit ingredient
- Delete ingredient
- Search ingredients
- Filter ingredients by category
- Clear search and category filters

All tested functions worked correctly.

## 4. Stock Warning Testing

Low-stock functionality was tested.

An ingredient was entered with a current quantity equal to or below its minimum stock level.

Result:

- Low-stock warning displayed successfully.

## 5. Expiry Testing

Expiry monitoring was tested.

The application correctly handles:

- Expired ingredients
- Ingredients expiring within seven days
- Ingredients with later expiry dates

The appropriate expiry warning is displayed when required.

## 6. Recipe Suggestion Testing

Recipe suggestions were tested using ingredients stored in the pantry.

The application successfully checks:

- Required ingredient names
- Required quantities
- Unit compatibility
- Availability of all required ingredients

Recipes with missing ingredients are excluded from the suggestions.

Recipes where the pantry quantity is below the required quantity are also excluded.

## 7. Recipe Details Testing

A suggested recipe was selected successfully.

The Recipe Details screen displayed:

- Recipe name
- Recipe description
- Required ingredients
- Preparation method

## 8. Settings Testing

The Settings screen was tested successfully.

The user was able to:

- Enter a name
- Enter an email address
- Save the information
- Close and reopen the application
- Confirm that the saved information remained available

## 9. Data Persistence Testing

Pantry data was tested after closing and reopening the application.

Previously stored pantry information remained available successfully.

## 10. Overall Result

The main application functionality was tested successfully.

The Smart Pantry Manager application is ready for the remaining submission activities, including GitHub submission, documentation and demonstration.