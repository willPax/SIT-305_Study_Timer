package com.example.timer_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton, pauseButton, stopButton;
    TextView previousTaskText;
    EditText userEnteredTask;
    Chronometer timer;

    SharedPreferences sharedPreferences;
    String SAVED_TASK = "saved_task";
    String SAVED_TIME = "saved_time";
    String CURRENTTASK = "current_task";
    String CURRENTTIME = "current_time";
    String TIMINGORNOT = "timing_or_not";
    String PAUSEDTIME = "paused_time";

    boolean timingOrNot;

    long pausedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        previousTaskText = findViewById(R.id.previousTaskText);
        userEnteredTask = findViewById(R.id.enterTaskTextBox);
        timer = findViewById(R.id.chronometer);

        sharedPreferences = getSharedPreferences("com.example.timer_v2", MODE_PRIVATE);

        if (savedInstanceState != null) {

            pausedTime = savedInstanceState.getLong(PAUSEDTIME);
            timingOrNot = savedInstanceState.getBoolean(TIMINGORNOT);

            if (timingOrNot)
            {
                timer.setBase(savedInstanceState.getLong(CURRENTTIME));
                timer.start();
            }
            else{
                timer.setBase(SystemClock.elapsedRealtime() - pausedTime);
                timer.stop();
            }
        }
        checkSharedPreferences();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        long theTime = timer.getBase();

        outState.putString(CURRENTTASK, userEnteredTask.getText().toString());
        outState.putLong(CURRENTTIME, theTime);
        outState.putBoolean(TIMINGORNOT, timingOrNot);
        outState.putLong(PAUSEDTIME, pausedTime);
    }

    public void buttonClicked(View view) {

        switch (view.getId())
        {
            case R.id.playButton:
                startTimer();
                break;

            case R.id.pauseButton:
                pauseTimer();
                break;

            case R.id.stopButton:
                stopTimer();
                break;
        }
    }

    private void stopTimer() {
        String currentTask = userEnteredTask.getText().toString();
        sharedPreferences.edit().putString(SAVED_TASK, currentTask).apply();

        String taskTime = timer.getText().toString();
        sharedPreferences.edit().putString(SAVED_TIME, taskTime).apply();

        timer.setBase(SystemClock.elapsedRealtime());
        timer.stop();
        pausedTime = 0;
        timingOrNot = false;
        previousTaskText.setText("Just spent: " + taskTime + " on: " + currentTask);
    }

    private void startTimer() {
        //Start the timer
        if (!timingOrNot)
        {
            timer.setBase(SystemClock.elapsedRealtime() - pausedTime);
            timer.start();
            timingOrNot = true;
        }
    }
    private void pauseTimer() {
        //Pause the timer
        if (timingOrNot)
        {
            pausedTime = SystemClock.elapsedRealtime() - timer.getBase();
            timer.stop();
            timingOrNot = false;
        }
    }

    public void checkSharedPreferences() {
        String savedTaskOld = sharedPreferences.getString(SAVED_TASK, "");
        String savedTaskTime = sharedPreferences.getString(SAVED_TIME, "");
        previousTaskText.setText("Previously spent: " + savedTaskTime + " on: " + savedTaskOld);
    }

}



