package com.example.finalprojectgroup3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
            intentTo(MessageActivity.class, true);
        }
    }
    public void back(View view) {
        if (view.getId() == R.id.backToHome) {
            intentTo(HomeActivity.class, false);
        }
    }

    private void intentTo(Class<?> cls, boolean next){
        Intent call = new Intent(this, cls);

        Bundle bundle = getIntent().getExtras();

        call.putExtra("isNewUser", bundle.getBoolean("isNewUser"));
        call.putExtra("userVideosURI", (ArrayList<Uri>) getIntent().getSerializableExtra("userVideosURI"));
        call.putExtra("userSelectedTime", bundle.getString("userSelectedTime"));
        call.putExtra("isCorrectTime", bundle.getBoolean("isCorrectTime"));

        if(next){
            call.putExtra("VIDEO_URI", videoURL); // String version, need to parse
        } // back button (to home activity) doesnt care abt video_uri

        startActivity(call);
    }
}