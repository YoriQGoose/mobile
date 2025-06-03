package com.example.laba5;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PrimeCalculator";
    private Timer timer;
    private int currentNumber = 2;
    private final int endNumber = 100;
    private int sum = 0;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        Button btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {
            currentNumber = 2;
            sum = 0;
            tvResult.setText("Результат: ");
            startPrimeCalculation();
        });
    }

    private void startPrimeCalculation() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentNumber > endNumber) {
                    runOnUiThread(() -> {
                        tvResult.setText("Результат: " + sum);
                        Log.i(TAG, "Финальная сумма: " + sum);
                    });
                    timer.cancel();
                    return;
                }

                if (isPrime(currentNumber)) {
                    sum += currentNumber;
                    Log.i(TAG, "Найдено простое число: " + currentNumber + ", текущая сумма: " + sum);
                }

                currentNumber++;
            }
        }, 0, 3000);
    }

    private boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;

        for (int i = 3; i <= Math.sqrt(number); i += 2) {
            if (number % i == 0) return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}