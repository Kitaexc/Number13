package com.example.fourprakt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    private TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTextView = findViewById(R.id.historyTextView);
        loadHistory();
        loadStatistics();
    }


    private void loadStatistics() {
        SharedPreferences prefs = getSharedPreferences("gameStats", MODE_PRIVATE);

        int winsX = prefs.getInt("winsX", 0);
        int winsO = prefs.getInt("winsO", 0);
        int draws = prefs.getInt("draws", 0);

        String statistics = "Статистика игр:\n" +
                "Победы крестиков: " + winsX + "\n" +
                "Победы ноликов: " + winsO + "\n" +
                "Ничьи: " + draws;

        historyTextView.setText(statistics);
    }


    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences("gameHistory", MODE_PRIVATE);
        String history = prefs.getString("history", "История игр пуста");
        historyTextView.setText(history);
    }
}
