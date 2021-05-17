package com.example.finalprojectgroup3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private EditText message;
    String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message = findViewById(R.id.messageText);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            videoURL = bundle.getString("VIDEO_URI");
            String userMessage = bundle.getString("Message");
            if(userMessage != null){
                message.setText(userMessage);
            }
        }
    }

    public void next(View view) {
        if (view.getId() == R.id.nextToDate) {
            if(HomeActivity.isNewUser){
                intentTo(DateActivity.class, true);
            } else { // Not a new user
                intentTo(FinishActivity.class, true);
            }
        }
    }

    public void back(View view) {
        if (view.getId() == R.id.backToVideo) {
            intentTo(VideoActivity.class, false);
        }
    }

    private void intentTo(Class<?> cls, boolean next){
        Intent call = new Intent(this, cls);

        Bundle bundle = getIntent().getExtras();

        call.putExtra("isNewUser", bundle.getBoolean("isNewUser"));
        call.putExtra("userVideosURI", (ArrayList<Uri>) getIntent().getSerializableExtra("userVideosURI"));
        call.putExtra("userSelectedTime", bundle.getString("userSelectedTime"));
        call.putExtra("isCorrectTime", bundle.getBoolean("isCorrectTime"));

        call.putExtra("VIDEO_URI", videoURL);

        if(next){
            // String version, need to parse
            if(HomeActivity.isNewUser){
                call.putExtra("editing", false);
            }
            call.putExtra("Message", message.getText().toString()); // next activity cares abt message
        }
        startActivity(call);
    }
}