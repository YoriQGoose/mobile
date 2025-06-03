package com.example.laba8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView numberTextView;
    private int number = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberTextView = findViewById(R.id.numberTextView);
        registerForContextMenu(numberTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.color_red) {
            numberTextView.setTextColor(Color.RED);
            Toast.makeText(this, "Текст красный", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.color_green) {
            numberTextView.setTextColor(Color.GREEN);
            Toast.makeText(this, "Текст зеленый", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.color_blue) {
            numberTextView.setTextColor(Color.BLUE);
            Toast.makeText(this, "Текст синий", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.increase) {
            number += 10;
            updateNumber();
            return true;
        } else if (id == R.id.decrease) {
            number -= 10;
            updateNumber();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void updateNumber() {
        numberTextView.setText(String.valueOf(number));
    }
}