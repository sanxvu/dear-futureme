package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class FinishActivity extends AppCompatActivity {

    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    String userEmail;

    //From Bundle
    String videoURL;
    Uri videoUri;
    String userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct != null){
            userEmail = acct.getEmail();
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            videoURL = bundle.getString("VIDEO_URI");
            userMessage = bundle.getString("Message");
            uploadVideo();
            uploadMessage();
        }
    }

    private void uploadVideo(){
        videoUri = Uri.parse(videoURL);

        // Directory name in firebase is the user's email
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference childRef = storageReference.child(userEmail+"/"+videoUri.getLastPathSegment());
        UploadTask uploadTask = childRef.putFile(videoUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("TAG", "SUCCESS UPLOAD TO FIREBASE");
            }
        });
    }

    private void uploadMessage(){
        Map<String, Object> videoToMessage = new HashMap<>();
        videoToMessage.put(videoUri.getLastPathSegment(), userMessage);

        DatabaseReference ref = firebaseDatabase.getReference("messages");
        ref.updateChildren(videoToMessage);
    }

    public void home(View view) {
        if (view.getId() == R.id.backToHome) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent call = new Intent(view.getContext(), StartActivity.class);
                    call.putExtra("isNewUser", false);
                    startActivity(call);
                }
            }, 4000);


        }
    }

}