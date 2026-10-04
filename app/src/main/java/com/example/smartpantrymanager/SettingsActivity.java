package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        btnBackToMain =
                findViewById(R.id.btnBackToMain);

        btnBackToMain.setOnClickListener(v -> {
            finish();
        });
    }
}