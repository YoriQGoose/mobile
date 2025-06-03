package com.example.laba12;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private RelativeLayout gameLayout;
    private TextView scoreText;
    private TextView timeText;
    private int score = 0;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameLayout = findViewById(R.id.gameLayout);
        scoreText = findViewById(R.id.scoreText);
        timeText = findViewById(R.id.timeText);
        Button btnBack = findViewById(R.id.btnBack);

        startGame();

        btnBack.setOnClickListener(v -> finish());
    }

    private void startGame() {
        score = 0;
        updateScore();

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Время: " + millisUntilFinished / 1000);
                spawnCircle();
            }

            @Override
            public void onFinish() {
                timeText.setText("Время вышло!");
                saveScore();
            }
        }.start();
    }

    private void spawnCircle() {
        View circle = new View(this);
        circle.setBackgroundResource(R.drawable.circle_shape);

        int size = 150;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);

        Random random = new Random();
        params.leftMargin = random.nextInt(gameLayout.getWidth() - size);
        params.topMargin = random.nextInt(gameLayout.getHeight() - size);

        circle.setLayoutParams(params);
        circle.setOnClickListener(v -> {
            score++;
            updateScore();
            gameLayout.removeView(v);
        });

        gameLayout.addView(circle);

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                gameLayout.removeView(circle);
            }
        }.start();
    }

    private void updateScore() {
        scoreText.setText("Счет: " + score);
    }

    private void saveScore() {
        // Сохранение счета в SharedPreferences
        getSharedPreferences("GamePrefs", MODE_PRIVATE)
                .edit()
                .putInt("last_score", score)
                .apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}