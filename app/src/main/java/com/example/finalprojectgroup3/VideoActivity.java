package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class VideoActivity extends AppCompatActivity {

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    String videoURL;

    String userEmail = "";
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            videoURL = bundle.getString("VIDEO_URI");
            Log.i("TAG", "RECEIVED INFO: " + videoURL);
            videoUri = Uri.parse(videoURL);

            VideoView videoView = findViewById(R.id.videoView);

            MediaController controller = new MediaController(this);
            controller.setAnchorView(videoView);
            controller.setMediaPlayer(videoView);
            videoView.setMediaController(controller);
            videoView.setVideoURI(videoUri);

            videoView.setOnPreparedListener(mp ->{
                videoView.start();
            });

            // get the Firebase  storage reference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            if(acct != null){
                userEmail = acct.getEmail();
            }

        }
    }

    public void next(View view) {
        if (view.getId() == R.id.nextToMessage) {
            // Directory name in firebase is the user's email
            StorageReference childRef = storageReference.child(userEmail+"/"+videoUri.getLastPathSegment());
            UploadTask uploadTask = childRef.putFile(videoUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("TAG", "SUCCESS UPLOAD TO FIREBASE");
                }
            });

            Intent call = new Intent(this, MessageActivity.class);
            call.putExtra("VIDEO_URI", videoURL);
            startActivity(call);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToHome) {
            Intent call = new Intent(this, HomeActivity.class);
            startActivity(call);
        }
    }
}