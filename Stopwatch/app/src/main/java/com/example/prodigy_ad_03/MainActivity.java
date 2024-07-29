package com.example.prodigy_ad_03;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView timerText, pauseTimesText;
    private Button startButton, pauseButton, resetButton;

    private Handler handler = new Handler();
    private long startTime, timeInMillis, updateTime = 0L;
    private int seconds, minutes, milliseconds;
    private boolean isRunning = false;

    private List<String> pauseTimes = new ArrayList<>();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            updateTime = updateTime + timeInMillis;
            startTime = System.currentTimeMillis();

            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliseconds = (int) (updateTime % 1000);

            timerText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));

            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timer_text);
        pauseTimesText = findViewById(R.id.pause_times_text);
        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        resetButton = findViewById(R.id.reset_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis();
                    handler.postDelayed(runnable, 0);
                    isRunning = true;
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    handler.removeCallbacks(runnable);
                    isRunning = false;

                    String currentTime = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
                    pauseTimes.add(currentTime);

                    // Display pause times
                    StringBuilder pauseTimesBuilder = new StringBuilder();
                    for (String time : pauseTimes) {
                        pauseTimesBuilder.append(time).append("\n");
                    }
                    pauseTimesText.setText(pauseTimesBuilder.toString());
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                isRunning = false;
                startTime = 0L;
                timeInMillis = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliseconds = 0;
                timerText.setText("00:00:000");
                pauseTimes.clear();
                pauseTimesText.setText("");
            }
        });
    }
}
