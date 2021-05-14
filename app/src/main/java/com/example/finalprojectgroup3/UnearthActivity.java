package com.example.finalprojectgroup3;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    TextView message;

    ArrayList<Uri> userVideosURI;
    int counter = 0;

    DatabaseReference mDatabase;
    String userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unearth);

        videoView = findViewById(R.id.testing_videoView);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        prevButton.setVisibility(View.INVISIBLE);
        message = findViewById(R.id.userMessage_textView);

        userVideosURI = (ArrayList<Uri>) getIntent().getSerializableExtra("arrList");
        System.out.println("in testvideodiary size " + userVideosURI.size());

        controller = new MediaController(this);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        videoView.setVideoURI(userVideosURI.get(counter));

        videoView.setOnPreparedListener(mp ->{
            videoView.start();
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = "";
        if(acct != null){
            userEmail = acct.getEmail();
        }
        String key = userVideosURI.get(0).getLastPathSegment().substring(userEmail.length()+1);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("messages").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i("firebase", "GOT THE DATA");
                    message.setText(task.getResult().getValue().toString());
                }
            }
        });
    }

    public void videoDiaryNext(View v){
        counter++;
        if(counter == userVideosURI.size() - 1){
            nextButton.setVisibility(View.INVISIBLE);
            prevButton.setVisibility(View.VISIBLE);
            videoView.setVideoURI(userVideosURI.get(counter));

            String key = userVideosURI.get(counter).getLastPathSegment().substring(userEmail.length()+1);
            mDatabase.child("messages").child(key).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i("firebase", "GOT THE DATA");
                    message.setText(task.getResult().getValue().toString());
                }
            });
        }
        else if(counter < userVideosURI.size() - 1){
            Log.i("VIDEO DIARY", "Taking URI" + userVideosURI.get(counter).toString());

            nextButton.setVisibility(View.VISIBLE);
            videoView.setVideoURI(userVideosURI.get(counter));
            prevButton.setVisibility(View.VISIBLE);

            videoView.setOnPreparedListener(mp -> videoView.start());

            String key = userVideosURI.get(counter).getLastPathSegment().substring(userEmail.length()+1);

            mDatabase.child("messages").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("firebase", "GOT THE DATA");
                        message.setText(task.getResult().getValue().toString());
                    }
                }
            });

            if(counter == userVideosURI.size()-1){
                nextButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void videoDiaryPrev(View v){
        counter--;
        if(counter == 0){
            prevButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            videoView.setVideoURI(userVideosURI.get(counter));

            videoView.setOnPreparedListener(mp ->{
                videoView.start();
            });

            String key = userVideosURI.get(counter).getLastPathSegment().substring(userEmail.length()+1);
            mDatabase.child("messages").child(key).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i("firebase", "GOT THE DATA");
                    message.setText(task.getResult().getValue().toString());
                }
            });
        }
        else if(counter > 0){

            prevButton.setVisibility(View.VISIBLE);
            videoView.setVideoURI(userVideosURI.get(counter)); // Decrement counter
            nextButton.setVisibility(View.VISIBLE);

            Log.i("VIDEO DIARY", "Taking URI" + userVideosURI.get(counter).toString());

            videoView.setOnPreparedListener(mp ->{
                videoView.start();
            });

            String key = userVideosURI.get(counter).getLastPathSegment().substring(userEmail.length()+1);

            mDatabase.child("messages").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("firebase", "GOT THE DATA");
                        message.setText(task.getResult().getValue().toString());
                    }
                }
            });

        }
    }
}
