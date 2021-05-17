package com.example.finalprojectgroup3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LoadingActivity extends AppCompatActivity {
    String userName;
    String userEmail;
    String userGivenName;
    Boolean isNewUser;

    String userTime;

    long timeDifference;
    boolean isCorrectTime;
    ArrayList<Uri> userVideosURI = new ArrayList<>();

    VideoView loading_videoView;
    TextView loadingMessage_textView;
    TextView progress_textView;

    ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    int i = 0;

    int delayTimeMilliseconds = 3500;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progress_textView = findViewById(R.id.progress_textView);
        loadingMessage_textView = findViewById(R.id.loadingMessage_textView);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            userName = acct.getDisplayName();
            userEmail = acct.getEmail();
            userGivenName = acct.getGivenName();
        }

        // animate progressbar for about 3 seconds
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(i);
        mCountDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                i++;
                if(i == 2){
                    progress_textView.setText("Retrieving your memories...");
                } else if (i==3){
                    progress_textView.setText("Almost there...");
                }
                progressBar.setProgress((int) i * 100 / (3000/ 1000));
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                progressBar.setProgress(100);
            }
        };
        mCountDownTimer.start();

        loading_videoView = findViewById(R.id.loading_videoView);
        loading_videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.loading_bg);
        loading_videoView.setOnPreparedListener(mp ->{
            loading_videoView.start();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isNewUser = bundle.getBoolean("isNewUser");
            if(isNewUser){
                loadingMessage_textView.setText("Welcome, " + userGivenName + "!");
            } else {
                loadingMessage_textView.setText("Hi "+ userGivenName + "!");
            }

            Handler handler = new Handler();
            if(!isNewUser){ // Returning user, calculate time
                isCorrectTime();
            }
            retrieveVideoURI();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent call = new Intent(LoadingActivity.this, HomeActivity.class);
                    call.putExtra("isNewUser", isNewUser);
                    call.putExtra("userVideosURI", userVideosURI);
                    call.putExtra("userSelectedTime", userTime);
                    call.putExtra("isCorrectTime", isCorrectTime);
                    startActivity(call);
                }
            }, delayTimeMilliseconds);
        }
    }

    public void retrieveVideoURI() {
        if (!isNewUser) { // Returning user
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference listRef = storage.getReference().child(userEmail + "/");

            listRef.listAll()
                    .addOnSuccessListener(listResult -> {

                        System.out.println(listResult.getItems().size());

                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    userVideosURI.add(uri);
                                    System.out.println("Size userVideosURI: " + userVideosURI.size());
                                }
                            });
                        }
                    });
        }
    }

    public void isCorrectTime() {
        if (isNewUser) {
            isCorrectTime = false;
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users_date").child(userName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy, HH:mm", Locale.getDefault());
                    try {
                        if(task.getResult().getValue() != null){
                            userTime = task.getResult().getValue().toString();

                            Date userSetDateTime = sdf.parse(userTime);

                            Log.i("user set time", task.getResult().getValue().toString());

                            timeDifference = TimeUnit.SECONDS.convert(userSetDateTime.getTime() - System.currentTimeMillis(),
                                    TimeUnit.SECONDS);
                            Log.i("difference value in try catch", String.valueOf(timeDifference));
                            Log.i("difference in the try catch", String.valueOf(timeDifference <= 0));

                            isCorrectTime = timeDifference <= 0;
                        } else {
                            isCorrectTime = false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        isCorrectTime = false;
                    }
                } else {
                    isCorrectTime = false;
                }
            });
        }
    }
}
