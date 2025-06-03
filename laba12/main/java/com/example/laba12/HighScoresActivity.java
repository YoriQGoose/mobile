package com.example.laba12;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        TextView lastScoreText = findViewById(R.id.lastScoreText);
        TextView bestScoreText = findViewById(R.id.bestScoreText);
        Button btnBackToMain = findViewById(R.id.btnBackToMain);

        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        int lastScore = prefs.getInt("last_score", 0);
        int bestScore = prefs.getInt("best_score", 0);

        if (lastScore > bestScore) {
            bestScore = lastScore;
            prefs.edit().putInt("best_score", bestScore).apply();
        }

        lastScoreText.setText("Последний счет: " + lastScore);
        bestScoreText.setText("Лучший счет: " + bestScore);

        btnBackToMain.setOnClickListener(v -> finish());
    }
}