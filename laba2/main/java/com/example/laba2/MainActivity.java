package com.example.laba2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean lastClickedWasA = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Задание 1
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Иванов", Toast.LENGTH_SHORT).show();
            }
        });

        // Задание 2
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText("Иванов");
            }
        });

        // Задание 3 и 4
        Button button3_1 = findViewById(R.id.button3_1);
        Button button3_2 = findViewById(R.id.button3_2);
        Button button3_3 = findViewById(R.id.button3_3);

        View.OnClickListener button3Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText("Иванов");
            }
        };

        button3_1.setOnClickListener(button3Listener);
        button3_2.setOnClickListener(button3Listener);
        button3_3.setOnClickListener(button3Listener);

        // Задание 5
        Button button5_1 = findViewById(R.id.button5_1);
        Button button5_2 = findViewById(R.id.button5_2);

        View.OnClickListener button5Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button5_1) {
                    if (!lastClickedWasA) {
                        Toast.makeText(MainActivity.this, "Иванов", Toast.LENGTH_SHORT).show();
                        lastClickedWasA = true;
                    }
                } else {
                    if (lastClickedWasA) {
                        Toast.makeText(MainActivity.this, "Иванов", Toast.LENGTH_SHORT).show();
                        lastClickedWasA = false;
                    }
                }
            }
        };

        button5_1.setOnClickListener(button5Listener);
        button5_2.setOnClickListener(button5Listener);
    }
}