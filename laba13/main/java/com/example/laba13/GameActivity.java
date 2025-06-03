package com.example.laba13;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private RelativeLayout gameLayout;
    private TextView scoreText;
    private TextView timeText;
    private int score = 0;
    private CountDownTimer timer;
    private final List<View> circles = new ArrayList<>();
    private final Map<View, CountDownTimer> timerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameLayout = findViewById(R.id.gameLayout);
        scoreText = findViewById(R.id.scoreText);
        timeText = findViewById(R.id.timeText);
        Button btnBack = findViewById(R.id.btnBack);

        gameLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                handleSwipe();
            }

            @Override
            public void onSwipeRight() {
                handleSwipe();
            }

            @Override
            public void onSwipeUp() {
                handleSwipe();
            }

            @Override
            public void onSwipeDown() {
                handleSwipe();
            }
        });

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
        gameLayout.addView(circle);
        circles.add(circle);

        CountDownTimer circleTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if (gameLayout.indexOfChild(circle) != -1) {
                    gameLayout.removeView(circle);
                    circles.remove(circle);
                    timerMap.remove(circle);
                }
            }
        }.start();

        timerMap.put(circle, circleTimer);
    }

    private void handleSwipe() {
        if (!circles.isEmpty()) {
            View circle = circles.get(0);
            gameLayout.removeView(circle);
            circles.remove(circle);

            if (timerMap.containsKey(circle)) {
                timerMap.get(circle).cancel();
                timerMap.remove(circle);
            }

            score++;
            updateScore();
            Toast.makeText(this, "+1 очко!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore() {
        scoreText.setText("Счет: " + score);
    }

    private void saveScore() {
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
        for (CountDownTimer t : timerMap.values()) {
            t.cancel();
        }
    }
}