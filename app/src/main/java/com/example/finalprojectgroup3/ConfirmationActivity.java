package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
public class ConfirmationActivity extends AppCompatActivity {

    TextView confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        confirmation = (TextView)findViewById(R.id.confirmation);
        confirmation.setText(DateActivity.getDate() + "\n" + MessageActivity.getMessage());
    }

    public void finish(View view) {
        if (view.getId() == R.id.finish) {
            Intent call = new Intent(this,FinishActivity.class);
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