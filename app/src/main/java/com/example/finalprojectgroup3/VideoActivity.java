package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
    }

    public void next(View view) {
        if (view.getId() == R.id.nextToDate) {
            Intent call = new Intent(this,DateActivity.class);
            startActivity(call);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToUpload) {
            Intent call = new Intent(this,UploadActivity.class);
            startActivity(call);
        }
    }
}