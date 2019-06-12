package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";

    Button newsFeed, myAccounts, applyAccounts, bills, resetPassword;
    User user;
    Intent receivedIntent;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    private final int customRequestCode = 100;
    public final static int RESET_PASSWORD_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();
        initNonViews();
        instantiateUser();

        if (user.isAutoPay()){
            userService.autoPayAllBills(user);
        }

        Log.d(TAG, "USER AUTO PAY: "+ user.isAutoPay());
    }

    public void onClick(View view) {

        Intent newsIntent = new Intent(this, NewsFeedActivity.class);
        Intent myAccountsIntent = new Intent(this, AccountsActivity.class);
        Intent applyIntent = new Intent(this, ApplyAccountActivity.class);
        Intent billsIntent = new Intent(this, BillsActivity.class);
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
                startActivityForResult(billsIntent, 100);
                break;
            case R.id.newsFeedButton:
                startActivity(newsIntent);
                break;
            case R.id.resetPasswordButton:
                resetPasswordIntent.putExtra(getResources().getString(R.string.existing_user), user);
                startActivityForResult(resetPasswordIntent, customRequestCode);
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

    protected void instantiateUser() {
        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));
        String username = user.getCredentials().getName();
        user = userService.fetchUser(username);

    }

    protected void initNonViews(){
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (user.isAutoPay()){
            userService.autoPayAllBills(user);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "DATA: " + data);

        Log.d(TAG, getResources().getString(R.string.on_activity_result));

        Log.d(TAG, "Request code" + requestCode + "// ResultCode" + resultCode);
        try {
            if (data != null) {

                if (requestCode == customRequestCode) {
                    Log.d(TAG, "" + resultCode);
                    if (resultCode == RESULT_OK) {
                        user = data.getParcelableExtra(getResources().getString(R.string.existing_user));
                        userService.saveUser(context, sharedPreferences, user);
                    }

                    if (resultCode == BillsActivity.CUSTOM_RESULT_CODE) {
                        user = data.getParcelableExtra(getResources().getString(R.string.existing_user));
                        userService.saveUser(context, sharedPreferences, user);
                    }

                    if (resultCode == RESET_PASSWORD_CODE){
                        user.getCredentials().setPassword(data.getStringExtra(("NEW_PASS")));
                        userService.saveUser(context, sharedPreferences, user);
                    }


                }
            }

        } catch (NullPointerException e){
            Log.e(TAG, "onActivityResult: If the intent object came back null, then a bill was paid manually. Error ->", e);
        }
    }


}
