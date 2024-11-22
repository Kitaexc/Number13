package com.example.fourprakt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private boolean playerTurn = true;
    private boolean isBotMode = false;
    private Button[][] buttons = new Button[3][3];
    private int roundCount = 0;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);

        loadTheme();
        initGameBoard();

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        Switch gameModeSwitch = findViewById(R.id.gameModeSwitch);
        gameModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> isBotMode = isChecked);
    }

    private void initGameBoard() {
        GridLayout gameBoard = findViewById(R.id.gameBoard);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this::makeMove);
            }
        }
    }



    private void makeMove(View view) {
        Button button = (Button) view;
        if (!button.getText().toString().equals("")) {
            return;
        }
        if (playerTurn) {
            button.setText("X");
            roundCount++;
            if (checkForWin()) {
                showWinner("Крестики выиграли!");
                return;
            } else if (roundCount == 9) {
                showWinner("Ничья!");
                return;
            }
            playerTurn = false;
            if (isBotMode) {
                botMove();
            }
        } else {
            if (!isBotMode) {
                button.setText("O");
                roundCount++;
                if (checkForWin()) {
                    showWinner("Нолики выиграли!");
                    return;
                } else if (roundCount == 9) {
                    showWinner("Ничья!");
                    return;
                }
                playerTurn = true;
            }
        }
    }


    private void botMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("O");
                    roundCount++;
                    if (checkForWin()) {
                        showWinner("Нолики выиграли!");
                        return;
                    } else if (roundCount == 9) {
                        showWinner("Ничья!");
                        return;
                    }
                    playerTurn = true;
                    return;
                }
            }
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void showWinner(String winner) {
        resultTextView.setText(winner);
        Toast.makeText(this, winner, Toast.LENGTH_SHORT).show();
        updateStatistics(winner);
        saveHistory(winner);
        resetGameAfterWin();
    }

    private void updateStatistics(String result) {
        SharedPreferences prefs = getSharedPreferences("gameStats", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int winsX = prefs.getInt("winsX", 0);
        int winsO = prefs.getInt("winsO", 0);
        int draws = prefs.getInt("draws", 0);

        switch (result) {
            case "Крестики выиграли!":
                winsX++;
                editor.putInt("winsX", winsX);
                break;
            case "Нолики выиграли!":
                winsO++;
                editor.putInt("winsO", winsO);
                break;
            case "Ничья!":
                draws++;
                editor.putInt("draws", draws);
                break;
        }

        editor.apply();
    }

    private void saveHistory(String winner) {
        SharedPreferences prefs = getSharedPreferences("gameHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String currentHistory = prefs.getString("history", "");
        String timeStamp = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
        String newEntry = timeStamp + ": " + winner + "\n";
        editor.putString("history", currentHistory + newEntry);
        editor.apply();
    }

    private void resetGameAfterWin() {
        new android.os.Handler().postDelayed(this::resetGame, 2000);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        playerTurn = true;
        resultTextView.setText("");
    }

    private void loadTheme() {
        SharedPreferences prefs = getSharedPreferences("theme", MODE_PRIVATE);
        boolean isDarkTheme = prefs.getBoolean("darkTheme", false);
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
