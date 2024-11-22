package com.example.fourprakt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch themeSwitch;
    private Button historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitch = findViewById(R.id.themeSwitch);
        historyButton = findViewById(R.id.historyButton);

        SharedPreferences prefs = getSharedPreferences("theme", MODE_PRIVATE);
        boolean isDarkTheme = prefs.getBoolean("darkTheme", false);
        themeSwitch.setChecked(isDarkTheme);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("darkTheme", isChecked);
            editor.apply();

            recreate();
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}
