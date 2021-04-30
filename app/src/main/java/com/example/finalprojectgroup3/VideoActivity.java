package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle bundle = getIntent().getExtras();
        String videoURL = bundle.getString("VIDEO_URI");
        Log.i("TAG", "RECEIVED INFO: " + videoURL);
        Uri videoUri = Uri.parse(videoURL);

        VideoView videoView = findViewById(R.id.videoView);

        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(videoUri);



        videoView.setOnPreparedListener(mp ->{

            videoView.start();
        });


    }

    public void next(View view) {
        if (view.getId() == R.id.nextToDate) {
            Intent call = new Intent(this,DateActivity.class);
            startActivity(call);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToUpload) {
            Intent call = new Intent(this,UploadActivity.class);
            startActivity(call);
        }
    }
}