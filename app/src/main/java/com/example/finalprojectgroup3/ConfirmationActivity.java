package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
    }

    public void send(View view) {
        if (view.getId() == R.id.send) {
            Intent call = new Intent(this,SentActivity.class);
            startActivity(call);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToMessage) {
            Intent call = new Intent(this,MessageActivity.class);
            startActivity(call);
        }
    }
}