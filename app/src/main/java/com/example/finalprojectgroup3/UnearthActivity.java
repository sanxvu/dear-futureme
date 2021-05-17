package com.example.finalprojectgroup3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UnearthActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController controller;
    Button homeButton;
    Button nextVideo_button;
    Button prevVideo_button;

    ArrayList<Uri> userVideosURI;
    int counter = 0;

    DatabaseReference mDatabase;
    String userEmail; // Need to get user email for the database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unearth);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        homeButton = findViewById(R.id.unearth_home_button);
        nextVideo_button = findViewById(R.id.unearth_next);
        prevVideo_button = findViewById(R.id.unearth_prev);
        prevVideo_button.setEnabled(false);

        videoView = findViewById(R.id.unearth_videoView);
        userVideosURI = (ArrayList<Uri>) getIntent().getSerializableExtra("arrList");

        if(userVideosURI.size() == 1) { // Only 1 video to show, no next button
            nextVideo_button.setEnabled(false);
        }

        controller = new MediaController(this);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        displayVideo(counter);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = "";
        if (acct != null) {
            userEmail = acct.getEmail();
        }
    }

    public void nextVideo(View view){
        counter++;
        if(counter == userVideosURI.size() - 1){ // last video
            nextVideo_button.setEnabled(false);
        } else if(counter < userVideosURI.size() - 1){ // not the last video
            nextVideo_button.setEnabled(true);
        }
        displayVideo(counter);
        prevVideo_button.setEnabled(true);
    }

    public void prevVideo(View view){
        counter--;
        if(counter == 0){//first vid
            prevVideo_button.setEnabled(false);
        } else if(counter > 0){
            prevVideo_button.setEnabled(true);
        }
        displayVideo(counter);
        nextVideo_button.setEnabled(true);
    }

    public void displayVideo(int currCounter) { // Display the video with URI at currCounter
        videoView.setVideoURI(userVideosURI.get(currCounter));
        videoView.setOnPreparedListener(mp -> {
            videoView.start();
        });
    }

    public void onClickMessagePopup(View v) {
        String key = userVideosURI.get(counter).getLastPathSegment().substring(userEmail.length() + 1);

        mDatabase.child("messages").child(key).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("firebase", "GOT THE DATA: " + task.getResult().getValue().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                builder.setTitle("Dear Future Me...");
                builder.setMessage(task.getResult().getValue().toString());
                builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void homeFromUnearth(View view) {
        if (view.getId() == R.id.unearth_home_button) {
            Intent call = new Intent(this, StartActivity.class);
            call.putExtra("isNewUser", false);
            startActivity(call);
        }
    }


}
