package com.example.kea_bank.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";

    Button newsFeed, myAccounts, applyAccounts, bills, resetPassword;
    User user;
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();
        try {

            receivedIntent = getIntent();
            user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));

        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }

    }

    public void onClick(View view) {

        Intent newsIntent = new Intent(this, NewsFeedActivity.class);
        Intent myAccountsIntent = new Intent(this, AccountsActivity.class);
        Intent applyIntent = new Intent(this, ApplyAccountActivity.class);
        Intent billsIntent = new Intent(this, NewsFeedActivity.class);
        Intent resetPasswordIntent = new Intent(this, ResetPasswordActivity.class);


        switch (view.getId()){

            case R.id.myAccounts:
                myAccountsIntent.putExtra(getResources().getString(R.string.existing_user), user);
                startActivity(myAccountsIntent);
                break;
            case R.id.applyAccounts:
                applyIntent.putExtra(getResources().getString(R.string.existing_user), user);
                startActivity(applyIntent);
                break;
            case R.id.bills:
                billsIntent.putExtra(getResources().getString(R.string.existing_user), user);
                startActivity(billsIntent);
                break;
            case R.id.newsFeedButton:
                startActivity(newsIntent);
                break;
            case R.id.resetPasswordButton:
                resetPasswordIntent.putExtra(getResources().getString(R.string.existing_user), user);
                startActivity(resetPasswordIntent);
                break;
        }
    }

    protected void init(){
        newsFeed = findViewById(R.id.newsFeedButton);
        myAccounts = findViewById(R.id.myAccounts);
        applyAccounts = findViewById(R.id.applyAccounts);
        bills = findViewById(R.id.bills);
        resetPassword = findViewById(R.id.resetPasswordButton);
    }
}
