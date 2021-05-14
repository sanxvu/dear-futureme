package com.example.finalprojectgroup3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1; // For recording
    static final int MEDIA_PICKER_SELECT = 2; // For uploading

    ArrayList<Uri> userVideosURI = new ArrayList<>();

    boolean isNewUser = true;

    Button unearthButton;
    TextView unearth_hidden_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        unearthButton = findViewById(R.id.unearth);
        unearth_hidden_text = findViewById(R.id.unearth_hidden_textView);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String userEmail = "";
        if (acct != null) {
            userEmail = acct.getEmail();
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String source = bundle.getString("intentSource");
            if(source.equals("FinishActivity") || source.equals("UnearthActivity")){ // If source is FinishActivity, must have at least 1 vid to show
                Log.i("Hit", "Intent from FinishActivity");
                unearthButton.setEnabled(true);
                unearth_hidden_text.setVisibility(View.INVISIBLE);
            }

            isNewUser = bundle.getBoolean("isNewUser");
            if(!isNewUser){ // Not a new user, need to get their videos

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference listRef = storage.getReference().child(userEmail + "/");

                listRef.listAll()
                        .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {

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
                            }
                        });

            } else{ // NEW USER hides the unearth button
                Log.i("Hit", "New user");
                unearthButton.setEnabled(false);
                unearth_hidden_text.setVisibility(View.VISIBLE);
            }
        } else { // Bundle == NULL, just hide just in case
            Log.i("Hit", "Bundle null");
            unearthButton.setEnabled(false);
            unearth_hidden_text.setVisibility(View.VISIBLE);
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
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
        }
    }

    public void unearth(View view) {

        if (view.getId() == R.id.unearth) {
            Intent call = new Intent(HomeActivity.this, UnearthActivity.class);
            call.putExtra("arrList", userVideosURI);
            startActivity(call);
        }
    }

    // Intent "data" is the video that was received from the intent
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE) { // From recording a video

                Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
                intent.putExtra("VIDEO_URI", data.getData().toString());
                startActivity(intent);
            }
            if (requestCode == MEDIA_PICKER_SELECT) { // From selecting a video
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("video")) {

                    Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
                    intent.putExtra("VIDEO_URI", data.getData().toString());

                    startActivity(intent);
                }
            }
        }
    }
}