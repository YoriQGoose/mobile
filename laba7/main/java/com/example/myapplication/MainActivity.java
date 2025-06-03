package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Изображения
    private ImageView imageView;
    private Button btnPrev, btnNext;
    private int currentImage = 0;
    private final int[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    // Видео
    private VideoView videoView;
    private MediaController mediaController;
    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private boolean wasPlayingBeforePause = false;

    // Аудио
    private MediaPlayer backgroundPlayer;
    private final Handler handler = new Handler();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов
        imageView = findViewById(R.id.textview_first);
        btnPrev = findViewById(R.id.button_first);
        btnNext = findViewById(R.id.button_second);
        videoView = findViewById(R.id.textview_second);
        volumeSeekBar = findViewById(R.id.toolbar);

        // Настройка переключения изображений
        btnPrev.setOnClickListener(v -> showPrevImage());
        btnNext.setOnClickListener(v -> showNextImage());

        // Настройка видео
        setupVideoPlayer();
        setupVolumeControl();

        // Настройка фонового аудио
        setupBackgroundAudio();
    }

    private void showPrevImage() {
        if (--currentImage < 0) currentImage = images.length - 1;
        imageView.setImageResource(images[currentImage]);
    }

    private void showNextImage() {
        if (++currentImage >= images.length) currentImage = 0;
        imageView.setImageResource(images[currentImage]);
    }

    private void setupVideoPlayer() {
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(videoUri);

        // Обработчики событий видео
        videoView.setOnPreparedListener(mp -> {
            mp.setOnInfoListener((mp1, what, extra) -> {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    pauseBackgroundAudio();
                    return true;
                }
                return false;
            });
        });

        videoView.setOnCompletionListener(mp -> resumeBackgroundAudioDelayed());
    }

    private void setupVolumeControl() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupBackgroundAudio() {
        backgroundPlayer = MediaPlayer.create(this, R.raw.music);
        backgroundPlayer.setLooping(true);
        backgroundPlayer.start();

        backgroundPlayer.setOnCompletionListener(mp -> {
            mp.seekTo(0);
            mp.start();
        });
    }

    private void pauseBackgroundAudio() {
        if (backgroundPlayer != null && backgroundPlayer.isPlaying()) {
            backgroundPlayer.pause();
        }
    }

    private void resumeBackgroundAudioDelayed() {
        handler.postDelayed(() -> {
            if (backgroundPlayer != null && !backgroundPlayer.isPlaying() && !videoView.isPlaying()) {
                backgroundPlayer.start();
            }
        }, 1500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasPlayingBeforePause = videoView.isPlaying();
        if (wasPlayingBeforePause) videoView.pause();
        if (backgroundPlayer != null) backgroundPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasPlayingBeforePause) {
            videoView.start();
        } else if (backgroundPlayer != null && !videoView.isPlaying()) {
            backgroundPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundPlayer != null) {
            backgroundPlayer.release();
            backgroundPlayer = null;
        }
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}