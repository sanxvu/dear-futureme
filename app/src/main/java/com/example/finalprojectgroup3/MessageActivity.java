package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    public void next(View view) {
        if (view.getId() == R.id.nextToConfirm) {
            Intent call = new Intent(this,ConfirmationActivity.class);
            startActivity(call);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToDate) {
            Intent call = new Intent(this,DateActivity.class);
            startActivity(call);
        }
    }
}