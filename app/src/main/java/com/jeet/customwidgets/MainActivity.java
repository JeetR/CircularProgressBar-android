package com.jeet.customwidgets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.jeet.circularprogressbar.CircularProgressBar;


public class MainActivity extends AppCompatActivity {

    CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.customRoundProgressBar);
        animateProgress();
    }

    private void animateProgress() {
        mProgressBar.setMAX_PROGRESS_VALUE(60);
        mProgressBar.setProgress(40);
        final int[] currentProgress = {0};
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentProgress[0] > 60) {
                    currentProgress[0] = 0;
                }
                currentProgress[0] += 2;
                mProgressBar.setProgress(currentProgress[0]);
                h.postDelayed(this, 1500);
            }
        }, 1000);
    }
}