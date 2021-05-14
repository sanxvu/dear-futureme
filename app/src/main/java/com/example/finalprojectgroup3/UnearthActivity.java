package com.example.finalprojectgroup3;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
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
    Button nextButton;
    Button prevButton;
    Button homeButton;

    TextView noVideo_text; // In case the user has no videos
    Button noVideo_Button;

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
        noVideo_Button = findViewById(R.id.noVideo_button);

        homeButton = findViewById(R.id.unearth_home_button);
        videoView = findViewById(R.id.testing_videoView);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        prevButton.setEnabled(false); // start with first vid, no back button

        userVideosURI = (ArrayList<Uri>) getIntent().getSerializableExtra("arrList");

        if(userVideosURI.size() == 0){ // NO VIDEO TO DISPLAY!!!!! PROMPT USER TO GO BACK
            videoView.setVisibility(View.INVISIBLE); // don't show the video view
            nextButton.setVisibility(View.INVISIBLE); // dont show next button
            prevButton.setVisibility(View.INVISIBLE); // dont show prev button

            noVideo_text.setVisibility(View.VISIBLE); // show the go back buttons and text
            noVideo_Button.setVisibility(View.VISIBLE);

        } else { // At least 1 video to display
            if(userVideosURI.size() == 1){ // If the user only has 1 vid, must not show next button
                nextButton.setEnabled(false); // no video to show next
            }

            controller = new MediaController(this);
            controller.setAnchorView(videoView);
            controller.setMediaPlayer(videoView);
            videoView.setMediaController(controller);

            displayVideo(0);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            userEmail = "";
            if(acct != null){
                userEmail = acct.getEmail();
            }
        }

    }

    public void displayVideo(int currCounter){ // Display the video with URI at currCounter
        videoView.setVideoURI(userVideosURI.get(currCounter));
        videoView.setOnPreparedListener(mp ->{
            videoView.start();
        });
    }

    public void onClickMessagePopup(View v){
        String key = userVideosURI.get(counter).getLastPathSegment().substring(userEmail.length()+1);

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

    public void videoDiaryNext(View v){
        counter++;
        if(counter == userVideosURI.size() - 1){
            nextButton.setEnabled(false);
        }
        else if(counter < userVideosURI.size() - 1){
            nextButton.setEnabled(true);
        }
        prevButton.setEnabled(true);
        displayVideo(counter);

    }

    public void videoDiaryPrev(View v){
        counter--;
        if(counter == 0){
            prevButton.setEnabled(false);;
        }
        else if(counter > 0){
            prevButton.setEnabled(true);;
        }
        nextButton.setEnabled(true);
        displayVideo(counter);
    }

    public void homeFromUnearth(View view) {
        if (view.getId() == R.id.unearth_home_button || view.getId() == R.id.noVideo_button) {
            Intent call = new Intent(this, HomeActivity.class);
            call.putExtra("isNewUser", false);
            call.putExtra("intentSource", "UnearthActivity");
            startActivity(call);
        }
    }
}
