package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
    }

    public void next(View view) {
        if (view.getId() == R.id.nextToMessage) {
            Intent call = new Intent(this,MessageActivity.class);
            startActivity(call);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToVideo) {
            Intent call = new Intent(this,VideoActivity.class);
            startActivity(call);
        }
    }
}