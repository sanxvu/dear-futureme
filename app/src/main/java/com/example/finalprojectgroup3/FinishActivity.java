package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    public void home(View view) {
        if (view.getId() == R.id.backToHome) {
            Intent call = new Intent(this, StartActivity.class);
            call.putExtra("isNewUser", false);
            startActivity(call);
        }
    }

}