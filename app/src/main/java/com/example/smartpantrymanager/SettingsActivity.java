package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText editUserName;
    private EditText editEmail;
    private Button btnSaveSettings;

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editUserName = findViewById(R.id.editUserName);
        editEmail = findViewById(R.id.editEmail);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        SharedPreferences preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        String savedName = preferences.getString(KEY_NAME, "");
        String savedEmail = preferences.getString(KEY_EMAIL, "");

        editUserName.setText(savedName);
        editEmail.setText(savedEmail);

        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {

        String name = editUserName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editUserName.setError("Please enter your name");
            editUserName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editEmail.setError("Please enter your email");
            editEmail.requestFocus();
            return;
        }

        SharedPreferences preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);

        editor.apply();

        Toast.makeText(
                SettingsActivity.this,
                "Settings saved successfully",
                Toast.LENGTH_SHORT
        ).show();
    }
}