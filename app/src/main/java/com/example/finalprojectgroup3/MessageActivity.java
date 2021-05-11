package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    private static EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message = (EditText) findViewById(R.id.messageText);
    }

    public static String getMessage() {
        return "Message:\n" + message.getText().toString();
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