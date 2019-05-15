package com.example.kea_bank.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Credentials.Credentials;
import com.example.kea_bank.domain.accounts.BudgetAccount;
import com.example.kea_bank.domain.accounts.BusinessAccount;
import com.example.kea_bank.domain.accounts.DefaultAccount;
import com.example.kea_bank.domain.accounts.PensionAccount;
import com.example.kea_bank.domain.accounts.SavingsAccount;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;
import com.example.kea_bank.utilities.EmailValidator;

import java.util.Calendar;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TAG = "CreateUserActivity";

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
        context = getApplicationContext();
        sharedPreferencesCredentials = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        sharedPreferencesUserAccount = context.getSharedPreferences("USERACCOUNTS", MODE_PRIVATE);
        userService = new UserService(context, sharedPreferencesCredentials);

    }



    public void onClick(View view){

        Log.d(TAG, getResources().getString(R.string.onClick));
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userPassword2 = retypePassword.getText().toString();

        setUserFields(userEmail, userPassword);

        // Check if input fields are empty //
        if (email.getText().toString().trim().isEmpty()){
            email.setError(getResources().getString(R.string.fill_out_field));
        }
        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getResources().getString(R.string.fill_out_field));

        // If the input fields are fine, we check if the user exists //
        } else {
            if (userService.userExists(userEmail)) {
                Log.d(TAG, String.valueOf(userService.userExists(userEmail)));
                email.setError(getResources().getString(R.string.user_exists_msg));
            }
            // Regex check if email follows email format convention //
            else if(!EmailValidator.validate(userEmail)){
                email.setError(getResources().getString(R.string.invalid_email));
            }

            // Check if passwords match //
            else if (!userService.passwordMatch(userPassword, userPassword2)) {
                password.setError(getResources().getString(R.string.passwords_mismatch));
                retypePassword.setError(getResources().getString(R.string.passwords_mismatch));
            }

            // If all goes well, we save the user, set our result for the ActivityOnResult method and destroy the activity //
            else{
                userService.saveUser(CreateUserActivity.this, sharedPreferencesCredentials, user);
                userService.setSharedPreferences(sharedPreferencesUserAccount);
                userService.saveUser(context, sharedPreferencesUserAccount, user);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(getResources().getString(R.string.NEW_USER_KEY), user);
                setResult(RESULT_OK, resultIntent);


                // Destroy activity and free it from memory, takes us back to MainActivity //
                finish();
            }
        }

    }

    protected void setUserFields(String userEmail, String userPassword){
        user.setCredentials(new Credentials(userEmail, userPassword));
        user.setDefaultAccount(new DefaultAccount(0.0));
        user.setBudgetAccount(new BudgetAccount(0.0));
        user.setBusinessAccount(new BusinessAccount(0.0));
        user.setPensionAccount(new PensionAccount(0.0));
        user.setSavingsAccount(new SavingsAccount(0.0));
    }

    protected void init(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        retypePassword = findViewById(R.id.retypepassword);
        sign_up = findViewById(R.id.sign_up);
        mDisplayDate = findViewById(R.id.date);
    }

    protected void datePickerInit(){
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateUserActivity.this,
                        android.R.style.Widget_DeviceDefault_Light_ActionBar_Solid,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
}
