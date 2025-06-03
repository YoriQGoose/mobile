package com.example.laba4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            int imageResource = R.drawable.image1;

            if (selectedId == R.id.radioImage2) {
                imageResource = R.drawable.image2;
            } else if (selectedId == R.id.radioImage3) {
                imageResource = R.drawable.image3;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedImage", imageResource);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}