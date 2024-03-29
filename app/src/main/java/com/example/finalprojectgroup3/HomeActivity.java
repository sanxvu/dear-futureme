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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1; // For recording
    static final int MEDIA_PICKER_SELECT = 2; // For uploading

    // ITEMS THAT WILL BE RECEIVED FROM LOADING ACTIVITY
    public ArrayList<Uri> userVideosURI = new ArrayList<>(); // has the URI of vids
    public static boolean isNewUser;
    public Boolean isCorrectTime;
    public String userSelectedTime;

    Button unearthButton; // clickable if correct time, unclickable if not correct time/new user
    TextView unearth_hidden_text; // Text view that appears when unearth button is unclickable
    Button changeTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        unearthButton = findViewById(R.id.unearth);
        unearth_hidden_text = findViewById(R.id.unearth_hidden_textView);
        changeTimeButton = findViewById(R.id.changeTime);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isNewUser = bundle.getBoolean("isNewUser");
            isCorrectTime = bundle.getBoolean("isCorrectTime");
            userVideosURI = (ArrayList<Uri>) getIntent().getSerializableExtra("userVideosURI");

            if (!isNewUser) { // Not a new user
                if (isCorrectTime) {
                    if (userVideosURI != null || userVideosURI.size() != 0) {  // correct time and has videos to show
                        Log.i("HomeActivity", "VIDEOS ADDED, CORRECT TIME");
                        unearthButton.setEnabled(true);
                        changeTimeButton.setEnabled(true);
                        unearth_hidden_text.setText("It's time to unearth your memories!\nBury more videos or update your date & time.");
                    } else { // No video to show
                        Log.i("HomeActivity", "NO ADDED VIDEOS");
                        unearthButton.setEnabled(false);
                        changeTimeButton.setEnabled(false);
                        unearth_hidden_text.setText("EMPTY: Bury your videos into the capsule!");
                    }
                    unearth_hidden_text.setVisibility(View.VISIBLE);
                } else { // Added videos but not the correct time
                    Log.i("HomeActivity", "VIDEOS ADDED, NOT CORRECT TIME");
                    unearthButton.setEnabled(false);
                    userSelectedTime = bundle.getString("userSelectedTime");
                    if(userSelectedTime != null){
                        String[] datetime = userSelectedTime.split(", ");
                        changeTimeButton.setEnabled(true);
                        unearth_hidden_text.setText("Come back on " + datetime[0] + " at " + datetime[1] + "!\n or change your date & time.");
                    } else { // For some reason user did not set the time but closed the app
                        changeTimeButton.setEnabled(true);
                        unearth_hidden_text.setText("Please upload a video and set a time!");
                    }
                    unearth_hidden_text.setVisibility(View.VISIBLE);
                }
            } else { // New user, hide the unearth and change time button
                Log.i("HomeActivity", "Hit New user");
                unearthButton.setEnabled(false);
                changeTimeButton.setEnabled(false);
                unearth_hidden_text.setText("Bury a video before unearthing it!");
                unearth_hidden_text.setVisibility(View.VISIBLE);
            }
        } else { // Bundle == NULL, hide the unearth and change time button in case
            Log.i("HomeActivity", "Bundle null");
            unearthButton.setEnabled(false);
            changeTimeButton.setEnabled(false);
            unearth_hidden_text.setText("EMPTY: Bury your videos into the capsule!");
            unearth_hidden_text.setVisibility(View.VISIBLE);
        }
    }

    public void signout(View view) {
        if (view.getId() == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(this, StartActivity.class);
            startActivity(logoutIntent);
        }
    }

    public void bury(View view) {
        if (view.getId() == R.id.bury) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            builder.setTitle("Burying your video...");
            builder.setMessage("Would you like to upload or record?");
            // Upload button
            builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Pick an item from the data, returning what was selected.
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("video/*"); //video
                    startActivityForResult(pickIntent, MEDIA_PICKER_SELECT);
                }
            });
            // Record button
            builder.setNegativeButton("Record", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        //Set limitation to the duration of the video. 2nd param is limit in SECONDS
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            alert.setCanceledOnTouchOutside(true);
        }
    }

    public void unearth(View view) {
        if (view.getId() == R.id.unearth) {
            Intent call = new Intent(HomeActivity.this, UnearthActivity.class);
            call.putExtra("arrList", userVideosURI);
            startActivity(call);
        }
    }

    public void changeTime(View view) {
        if (view.getId() == R.id.changeTime) {
            Intent call = new Intent(HomeActivity.this, DateActivity.class);
            call.putExtra("editing", true);
            startActivity(call);
        }
    }

    // Intent "data" is the video that was received from the intent
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE) { // From recording a video
                Intent call = new Intent(HomeActivity.this, VideoActivity.class);
                call.putExtra("VIDEO_URI", data.getData().toString());

                call.putExtra("isNewUser", isNewUser);
                call.putExtra("userVideosURI", userVideosURI);
                call.putExtra("userSelectedTime", userSelectedTime);
                call.putExtra("isCorrectTime", isCorrectTime);

                startActivity(call);
            }
            if (requestCode == MEDIA_PICKER_SELECT) { // From selecting a video
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("video")) {

                    Intent call = new Intent(HomeActivity.this, VideoActivity.class);
                    call.putExtra("VIDEO_URI", data.getData().toString());

                    call.putExtra("isNewUser", isNewUser);
                    call.putExtra("userVideosURI", userVideosURI);
                    call.putExtra("userSelectedTime", userSelectedTime);
                    call.putExtra("isCorrectTime", isCorrectTime);

                    startActivity(call);
                }
            }
        }
    }
}