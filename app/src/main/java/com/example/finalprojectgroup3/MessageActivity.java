package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private static EditText message;
    private static Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message = (EditText) findViewById(R.id.messageText);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            String videoURL = bundle.getString("VIDEO_URI");
            videoUri = Uri.parse(videoURL);
        }
    }

    public static String getMessage() {


        Map<String, Object> videoToMessage = new HashMap<>();
        videoToMessage.put(videoUri.getLastPathSegment(), message.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("messages");

        ref.updateChildren(videoToMessage);
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