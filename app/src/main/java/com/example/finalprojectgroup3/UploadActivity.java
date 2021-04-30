package com.example.finalprojectgroup3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class UploadActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1; // For recording
    static final int MEDIA_PICKER_SELECT = 2; // For uploading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button record_button = findViewById(R.id.record);
        Button upload_button = findViewById(R.id.upload);

        record_button.setOnClickListener(v -> { // For recording
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            if(intent.resolveActivity(getPackageManager()) != null){
                //Set limitation to the duration of the video. 2nd param is limit in SECONDS
                //no limit, delete line below
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() { // For uploading
            @Override
            public void onClick(View v) {
                //Pick an item from the data, returning what was selected.
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*"); //video
                startActivityForResult(pickIntent, MEDIA_PICKER_SELECT);

            }
        });
    }


    // Cause the video that was just uploaded to be replayed in a dialog box.
    // Intent "data" is the video that was received from the intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_VIDEO_CAPTURE){ // From recording a video

                Intent intent = new Intent(UploadActivity.this, VideoActivity.class);
                intent.putExtra("VIDEO_URI", data.getData().toString());
                startActivity(intent);
            }
            if(requestCode == MEDIA_PICKER_SELECT){ // From selecting a video
                Uri selectedMediaUri = data.getData();
                if(selectedMediaUri.toString().contains("video")){

                    Intent intent = new Intent(UploadActivity.this, VideoActivity.class);
                    intent.putExtra("VIDEO_URI", data.getData().toString());

                    startActivity(intent);
                }
            }
        }

    }
}