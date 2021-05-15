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
    Boolean isNewUser;

    long timeDifference;
    boolean[] value = new boolean[1];
    ArrayList<Uri> userVideosURI = new ArrayList<>();

    ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    int i = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // animate progressbar for about 3 seconds
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(i);
        mCountDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                i++;
                progressBar.setProgress((int) i * 100 / (5000 / 1000));
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                progressBar.setProgress(100);
            }
        };
        mCountDownTimer.start();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            userName = acct.getDisplayName();
            userEmail = acct.getEmail();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isNewUser = bundle.getBoolean("isNewUser");
            Handler handler = new Handler();
            isCorrectTime();
            retrieveVideoURI();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent call = new Intent(LoadingActivity.this, HomeActivity.class);
                    call.putExtra("isNewUser", isNewUser);
                    call.putExtra("userVideosURI", userVideosURI);

                    Log.i("isCorrectTime in start activity", String.valueOf(value[0]));
                    call.putExtra("isCorrectTime", value);
                    startActivity(call);
                }
            }, 3000);
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
            value[0] = false;
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users_date").child(userName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy, HH:mm", Locale.getDefault());
                    try {
                        Date userSetDateTime = sdf.parse(task.getResult().getValue().toString());

                        Log.i("user set time", task.getResult().getValue().toString());

                        //timeDifference = userSetDateTime.getTime() - System.currentTimeMillis();

                        timeDifference = TimeUnit.SECONDS.convert(userSetDateTime.getTime() - System.currentTimeMillis(),
                                TimeUnit.SECONDS);
                        Log.i("difference value in try catch", String.valueOf(timeDifference));
                        Log.i("difference in the try catch", String.valueOf(timeDifference <= 0));

                        value[0] = timeDifference <= 0;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
