package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateActivity extends AppCompatActivity {

    private static String userName;
    Button btnDatePicker;
    Button btnTimePicker;

    Button back;
    boolean isEditing;
    boolean setDate;
    boolean setTime;

    EditText txtDate;
    EditText txtTime;

    String message; // from Bundle

    private static int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        btnDatePicker = (Button) findViewById(R.id.dateButton);
        btnTimePicker = (Button) findViewById(R.id.timeButton);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        back = (Button) findViewById(R.id.backToMessage);

        // don't show back button if user is just editing date/time
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEditing = bundle.getBoolean("editing");
            if (isEditing) {
                setDate = false;
                setTime = false;
                back.setVisibility(View.GONE);
            } else { //show back button
                back.setVisibility(View.VISIBLE);
                message = bundle.getString("Message");
            }
        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        userName = "";
        if (acct != null) {
            userName = acct.getDisplayName();
        }
    }

    // Show date and time picker when they click on the set date/set time
    public void onClick(View v) {
        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DateTimeDialogCustom,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            setDate = true;
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DateTimeDialogCustom,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtTime.setText(hourOfDay + ":" + minute);
                            mHour = hourOfDay;
                            mMinute = minute;
                            setTime = true;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public void next(View view) {
        Map<String, Object> userToDate = new HashMap<>();
        int monthVersion = mMonth + 1;
        String monthDecorator;
        if (monthVersion < 10) {
            monthDecorator = "0" + monthVersion;
        } else {
            monthDecorator = String.valueOf(monthVersion);
        }

        String dateAndTime = monthDecorator + "." + mDay + "." + mYear + ", " + mHour + ":" + mMinute;
        userToDate.put(userName, dateAndTime);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users_date");

        ref.updateChildren(userToDate);

        if (view.getId() == R.id.nextToConfirmation) {
            // Show alert if user didn't set date and time
            if (!setDate || !setTime) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                builder.setTitle("Error");
                builder.setMessage("Please set the date and time.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();
                alert.setCanceledOnTouchOutside(true);
            } else {
                Intent call;
                if (isEditing) {
                    call = new Intent(this, LoadingActivity.class);
                    call.putExtra("isNewUser", false); // through editing way, not new user
                } else {
                    call = new Intent(this, FinishActivity.class);
                    call.putExtra("Message", message);
                    Bundle bundle = getIntent().getExtras();
                    call.putExtra("VIDEO_URI", bundle.getString("VIDEO_URI"));
                }
                startActivity(call);
            }
        }
    }

    public void back(View view) {
        if (view.getId() == R.id.backToMessage) {
            Intent call = new Intent(this, MessageActivity.class);

            Bundle bundle = getIntent().getExtras();

            call.putExtra("isNewUser", bundle.getBoolean("isNewUser"));
            call.putExtra("userVideosURI", (ArrayList<Uri>) getIntent().getSerializableExtra("userVideosURI"));
            call.putExtra("userSelectedTime", bundle.getString("userSelectedTime"));
            call.putExtra("isCorrectTime", bundle.getBoolean("isCorrectTime"));

            // Message activity expects this
            call.putExtra("VIDEO_URI", bundle.getString("VIDEO_URI"));
            call.putExtra("Message", message);

            startActivity(call);
        }
    }
}