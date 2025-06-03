package com.example.laba10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private Button calculateBtn;
    private Button loadImageBtn;
    private ProgressBar progressBar;
    private ImageView imageView;
    private final double[] array = new double[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = findViewById(R.id.resultText);
        calculateBtn = findViewById(R.id.calculateBtn);
        loadImageBtn = findViewById(R.id.loadImageBtn);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        generateArray();
        calculateBtn.setOnClickListener(v -> calculateArray());
        loadImageBtn.setOnClickListener(v -> loadImage());
    }

    private void generateArray() {
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextDouble() * 20 - 10;
        }
    }

    private void calculateArray() {
        new CalculateTask().execute();
    }

    private void loadImage() {
        new DownloadImageTask().execute("https://picsum.photos/500");
    }

    private class CalculateTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            calculateBtn.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double sumNegative = 0;
            for (double num : array) {
                if (num < 0) sumNegative += num;
            }

            int minIndex = 0;
            int maxIndex = 0;
            for (int i = 1; i < array.length; i++) {
                if (array[i] < array[minIndex]) minIndex = i;
                if (array[i] > array[maxIndex]) maxIndex = i;
            }

            int start = Math.min(minIndex, maxIndex);
            int end = Math.max(minIndex, maxIndex);
            double product = 1;
            for (int i = start + 1; i < end; i++) {
                product *= array[i];
            }

            return "Сумма отрицательных: " + String.format("%.2f", sumNegative) +
                    "\nПроизведение между min и max: " + String.format("%.2f", product);
        }

        @Override
        protected void onPostExecute(String result) {
            resultText.setText(result);
            calculateBtn.setEnabled(true);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            loadImageBtn.setEnabled(false);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                InputStream inputStream = (InputStream) url.getContent();
                publishProgress(50);
                Thread.sleep(1000);
                publishProgress(100);
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.GONE);
            loadImageBtn.setEnabled(true);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(MainActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        }
    }
}