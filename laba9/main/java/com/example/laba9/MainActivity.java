package com.example.laba9;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText timeInput;
    private Button setAlarmBtn;
    private Button cancelAlarmBtn;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeInput = findViewById(R.id.timeInput);
        setAlarmBtn = findViewById(R.id.setAlarmBtn);
        cancelAlarmBtn = findViewById(R.id.cancelAlarmBtn);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        setAlarmBtn.setOnClickListener(v -> setAlarm());
        cancelAlarmBtn.setOnClickListener(v -> cancelAlarm());
    }

    private void setAlarm() {
        if (timeInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите время", Toast.LENGTH_SHORT).show();
            return;
        }

        int minutes = Integer.parseInt(timeInput.getText().toString());
        long triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }

        Toast.makeText(this,
                "Будильник установлен на " + minutes + " минут",
                Toast.LENGTH_LONG
        ).show();
    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Будильник отменен", Toast.LENGTH_SHORT).show();
    }
}