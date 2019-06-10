package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

public class SpecificBillActivity extends AppCompatActivity {

    private static final String TAG = "SpecificBillActivity";

    private TextView billName, billAmount;
    private Button payManually, payAuto;

    Intent receivedIntent;
    User user;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_bill);
        init();
        initNonViews();
        instantiateUser();
        
        billName.setText("This is the bill name");
        billAmount.setText("This is the bill amount");

    }

    protected void init(){

    }

    protected void initNonViews(){
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
    }

    protected void instantiateUser(){
        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));
        String username = user.getCredentials().getName();
        user = userService.fetchUser(username);
    }
}
