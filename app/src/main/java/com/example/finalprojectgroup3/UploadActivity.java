package com.example.finalprojectgroup3;

import androidx.annotation.NonNull;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1; // For recording
    static final int MEDIA_PICKER_SELECT = 2; // For uploading

    ArrayList<Uri> userVideosURI = new ArrayList<>();

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
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
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

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String userEmail = "";
        if(acct != null){
            userEmail = acct.getEmail();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child(userEmail+"/");
        Log.i("VIDEO DIARY", "Email: " + userEmail);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.i("VIDEO DIARY", "Hit on success list all");

                        System.out.println(listResult.getItems().size());

                        for (StorageReference item : listResult.getItems()) {
                            Log.i("VIDEO DIARY", "Hit an item in the storage");
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i("VIDEO DIARY", "Hit on success download url");
                                    userVideosURI.add(uri);
                                    System.out.println("Size userVideosURI: " + userVideosURI.size());
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("VIDEO DIARY", "FAIL TO LIST ALL");
            }
        });
    }

    public void videoDiary(View view){

        if (view.getId() == R.id.videoDiaryTEST_button) {
            Intent call = new Intent(this, TestVideoDiary.class);
            call.putExtra("arrList", userVideosURI);
            startActivity(call);
        }
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