package com.example.kea_bank.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Credentials.Credentials;
import com.example.kea_bank.domain.accounts.BudgetAccount;
import com.example.kea_bank.domain.accounts.BusinessAccount;
import com.example.kea_bank.domain.accounts.DefaultAccount;
import com.example.kea_bank.domain.accounts.PensionAccount;
import com.example.kea_bank.domain.accounts.SavingsAccount;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.LatLongService;
import com.example.kea_bank.services.UserService;
import com.example.kea_bank.utilities.EmailValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TAG = "CreateUserActivity";

    private final int uniqueRequestCode = 88;
    private BroadcastReceiver broadcastReceiver;


    EditText email, password, retypePassword;
    Button sign_up;

    User user = new User();

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    Context context;
    SharedPreferences sharedPreferencesCredentials;
    SharedPreferences sharedPreferencesUserAccount;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Log.d(TAG, getResources().getString(R.string.on_create));
        // Initialize views //
        init();
        datePickerInit();

        // Initialize non-views //
        initNonViews();

        if (!checkForPermissions()){
            getUserLocation();
        }


    }


    public void onClick(View view) {

        Log.d(TAG, getResources().getString(R.string.onClick));

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userPassword2 = retypePassword.getText().toString();

        // Sets all the fields of the user. 2/5 accounts are activated, rest are disabled //
        String unformattedUserBirthdate = mDisplayDate.getText().toString();

        int userAge = userService.calculateUserAge(unformattedUserBirthdate);
        userService.setFields(user, userEmail, userPassword, userAge);

        // Check if input fields are empty //
        if (email.getText().toString().trim().isEmpty()) {
            email.setError(getResources().getString(R.string.fill_out_field));
        }
        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getResources().getString(R.string.fill_out_field));
        }
        if (userAge < 18) {
            mDisplayDate.setError(getResources().getString(R.string.must_be_18));
        }

        // If the input fields are fine, we check if the user exists //
        else {
            if (userService.userExists(userEmail)) {
                Log.d(TAG, String.valueOf(userService.userExists(userEmail)));
                email.setError(getResources().getString(R.string.user_exists_msg));
            }
            // Regex check if email follows email format convention //
            else if (!EmailValidator.validate(userEmail)) {
                email.setError(getResources().getString(R.string.invalid_email));
            }

            // Check if passwords match //
            else if (!userService.passwordMatch(userPassword, userPassword2)) {
                password.setError(getResources().getString(R.string.passwords_mismatch));
                retypePassword.setError(getResources().getString(R.string.passwords_mismatch));
            }

            // If all goes well, we save the user, set our result for the ActivityOnResult method and destroy the activity //
            else {
                userService.saveUser(CreateUserActivity.this, sharedPreferencesCredentials, user);
                userService.setSharedPreferences(sharedPreferencesUserAccount);
                userService.saveUser(context, sharedPreferencesUserAccount, user);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(getResources().getString(R.string.NEW_USER_KEY), user);
                setResult(RESULT_OK, resultIntent);

                // Stop the LatLongService class from continuously getting the user's location //
                Intent stopServiceIntent = new Intent(CreateUserActivity.this, LatLongService.class);
                stopService(stopServiceIntent);
                // Destroy activity and free it from memory, takes us back to MainActivity //
                finish();
            }
        }

    }

    protected void init(){
        Log.d(TAG, getResources().getString(R.string.init));
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        retypePassword = findViewById(R.id.retypepassword);
        sign_up = findViewById(R.id.sign_up);
        mDisplayDate = findViewById(R.id.date);
    }

    protected void initNonViews(){
        Log.d(TAG, "initNonViews() called");
        context = getApplicationContext();
        sharedPreferencesCredentials = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        sharedPreferencesUserAccount = context.getSharedPreferences("USERACCOUNTS", MODE_PRIVATE);
        userService = new UserService(context, sharedPreferencesCredentials);
    }

    protected void datePickerInit(){
        Log.d(TAG, "datePickerInit() called");
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateUserActivity.this,
                        android.R.style.Theme_Dialog,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + day + "/" + month + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

    }

    protected void getUserLocation(){
        Log.d(TAG, "getUserLocation() called");
        Intent startService = new Intent(CreateUserActivity.this, LatLongService.class);
        startService(startService);
    }

    protected boolean checkForPermissions(){
        Log.d(TAG, "checkForPermissions() called");
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, uniqueRequestCode);
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult() called");
        if (requestCode == uniqueRequestCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                getUserLocation();
            } else {
                checkForPermissions();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    double[] coords = intent.getDoubleArrayExtra("coordinates");
                    Log.d(TAG, "THESE ARE MY COORDINATES: " + coords[0] + " " + coords[1]);
                    Location location = new Location("");
                    location.setLatitude(coords[0]);
                    location.setLongitude(coords[1]);
                    user.setBranch(LatLongService.compareLocations(location));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("current_location"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");

        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }
}
