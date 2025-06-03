package com.example.laba9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 1000, 500, 1000, 500, 1000};
            vibrator.vibrate(pattern, -1);
            Toast.makeText(context, "Время вышло!", Toast.LENGTH_LONG).show();
        }
    }
}