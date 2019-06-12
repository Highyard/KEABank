package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ResetPasswordActivity";

    private EditText newPassword1, newPassword2, oldPassword;
    private Button changePassword;

    User user;
    Intent receivedIntent;

    Context context;
    SharedPreferences sharedPreferencesCredentials;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();
        initNonViews();
        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));

    }

    protected void init(){
        newPassword1 = findViewById(R.id.password_reset1);
        newPassword2 = findViewById(R.id.password_reset2);
        oldPassword = findViewById(R.id.old_password);
        changePassword = findViewById(R.id.sign_up);
    }

    protected void initNonViews(){
        context = getApplicationContext();
        sharedPreferencesCredentials = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferencesCredentials);
    }

    public void onClick(View view) {
        Log.d(TAG, getResources().getString(R.string.onClick));
        Log.d(TAG, "USER PASS: " + user.getCredentials().getPassword());
        Log.d(TAG, "OLD PASS: " + oldPassword.getText().toString());
        if (!oldPassword.getText().toString().equalsIgnoreCase(user.getCredentials().getPassword())){
            oldPassword.setError("Old password is incorrect.");
        }

        else if (!userService.passwordMatch(newPassword1.getText().toString(), newPassword2.getText().toString())){
            newPassword1.setError("Passwords do not match.");
            newPassword2.setError("Passwords do not match.");
        }
        else {
            user.getCredentials().setPassword(newPassword1.getText().toString().trim());
            userService.saveUser(context, sharedPreferencesCredentials, user);
            Intent intent = new Intent();
            intent.putExtra("NEW_PASS", newPassword1.getText().toString());
            setResult(HomeActivity.RESET_PASSWORD_CODE, intent);
            finish();
        }

    }
}
