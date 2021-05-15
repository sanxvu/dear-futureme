package com.example.finalprojectgroup3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateActivity extends AppCompatActivity {

    private static String userName;
    Button btnDatePicker;
    Button btnTimePicker;

    Button back;
    boolean isEditing = false;

    EditText txtDate;
    EditText txtTime;

    private static int mYear, mMonth, mDay, mHour, mMinute;
    private static String[] MONTHS = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

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
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    back.setVisibility(View.GONE);
                }
            }, 3000);
        } else //show back button
            back.setVisibility(View.VISIBLE);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        userName = "";
        if (acct != null) {
            userName = acct.getDisplayName();
        }
    }

    public void onClick(View v) {
        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
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
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtTime.setText(hourOfDay + ":" + minute);
                            mHour = hourOfDay;
                            mMinute = minute;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public static String getDate() {
        Map<String, Object> userToDate = new HashMap<>();
        int monthVersion = mMonth + 1;
        String monthDecorator;
        if (monthVersion < 10) {
            monthDecorator = "0" + monthVersion;
        } else {
            monthDecorator = String.valueOf(monthVersion);
        }

        String dateAndTime = monthDecorator + "." + mDay + "." + mYear + ", " + mHour+ ":" + mMinute;
        userToDate.put(userName, dateAndTime);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users_date");

        ref.updateChildren(userToDate);

        String month = MONTHS[mMonth];
        return "Date: " + month + " " + mDay + ", " + mYear + "\nTime: " + mHour + ":" + mMinute;
    }

    public void next(View view) {
        if (view.getId() == R.id.nextToConfirmation) {
            Intent call;
            if (isEditing) {
                 call = new Intent(this, LoadingActivity.class);
            }
            else {
                 call = new Intent(this, FinishActivity.class);
            }
            startActivity(call);
        }
    }

    public void back(View view) {
        if (view.getId() == R.id.backToMessage) {
            Intent call = new Intent(this, MessageActivity.class);
            startActivity(call);
        }
    }
}