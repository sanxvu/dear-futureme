package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {

    ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    TextView progress_textView;
    int i = 0;
    Button backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        backToHome = findViewById(R.id.backToHome);
        // animate progressbar for about 3 seconds
        progress_textView = findViewById(R.id.finishProgressText);
        progressBar = (ProgressBar) findViewById(R.id.finishProgressBar);
        progressBar.setProgress(i);
        mCountDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                i++;
                if (i == 2) {
                    progress_textView.setText("Burying your video...");
                } else if (i == 3) {
                    progress_textView.setText("Almost there...");
                }
                progressBar.setProgress((int) i * 100 / (3000 / 1000));
            }

            @Override
            public void onFinish() {
                i++;
                progressBar.setProgress(100);
                progress_textView.setText("Completed!");
                backToHome.setVisibility(View.VISIBLE);
            }
        };
        mCountDownTimer.start();
    }

    public void home(View view) {
        if (view.getId() == R.id.backToHome) {
            Intent call = new Intent(this, StartActivity.class);
            call.putExtra("isNewUser", false);
            startActivity(call);
        }
    }

}