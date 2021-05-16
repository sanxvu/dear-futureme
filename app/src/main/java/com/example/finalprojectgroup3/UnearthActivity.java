package com.example.finalprojectgroup3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UnearthActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController controller;
    Button homeButton;
//    Button nextVideo_button;
//    Button prevVideo_button;
    Button message_button;

    TextView noVideo_text; // In case the user has no videos

    ArrayList<Uri> userVideosURI;
    int counter = 0;

    DatabaseReference mDatabase;
    String userEmail; // Need to get user email for the database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unearth);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        noVideo_text = findViewById(R.id.noVideo_textView);

        homeButton = findViewById(R.id.unearth_home_button);
        message_button = findViewById(R.id.readMessage);
//        nextVideo_button = findViewById(R.id.unearth_nextButton);
//        prevVideo_button = findViewById(R.id.unearth_backButton);
        //prevVideo_button.setEnabled(false);

        videoView = findViewById(R.id.unearth_videoView);
        userVideosURI = (ArrayList<Uri>) getIntent().getSerializableExtra("arrList");

        if (userVideosURI.size() == 0) { // NO VIDEO TO DISPLAY!!!!! PROMPT USER TO GO BACK
            videoView.setVisibility(View.INVISIBLE); // don't show the video view
            //nextVideo_button.setEnabled(false);
            noVideo_text.setVisibility(View.VISIBLE); // show the go back buttons and text
        }


        controller = new MediaController(this);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        displayVideo(0);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = "";
        if (acct != null) {
            userEmail = acct.getEmail();
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        View touchView = findViewById(R.id.constraintLayout);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float xCoor = event.getX();
                Log.i("coordinate", String.valueOf(xCoor));
                if(xCoor > (width/2 + 100) && (counter + 1 != userVideosURI.size())){ // right click
                    counter++;
                    displayVideo(counter);
                } else if (xCoor < (width/2 - 100) && (counter - 1 >= 0)){
                    counter--;
                    displayVideo(counter);
                }

                return true;
            }
        });

    }

//    boolean areButtonsThere = false;
//
//    public void clickToDisappearAppear(View v){
//        if(areButtonsThere == true){ // The buttons are there, make disappear
//            homeButton.setVisibility(View.INVISIBLE);
//            nextVideo_button.setVisibility(View.INVISIBLE);
//            prevVideo_button.setVisibility(View.INVISIBLE);
//            message_button.setVisibility(View.INVISIBLE);
//            areButtonsThere = false;
//        } else { // Buttons disappear, make appear
//            homeButton.setVisibility(View.VISIBLE);
//            nextVideo_button.setVisibility(View.VISIBLE);
//            prevVideo_button.setVisibility(View.VISIBLE);
//            message_button.setVisibility(View.VISIBLE);
//        }
//    }

//    public void nextVideo(View view){
//        if(counter + 1 < userVideosURI.size()){
//            counter++;
//            nextVideo_button.setEnabled(true);
//            displayVideo(counter);
//        } else {
//            nextVideo_button.setEnabled(false);
//        }
//    }
//
//    public void prevVideo(View view){
//        if(counter - 1 >= 0){
//            counter--;
//            prevVideo_button.setEnabled(true);
//            displayVideo(counter);
//        } else {
//            prevVideo_button.setEnabled(false);
//        }
//    }

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
