package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.accounts.Account;
import com.example.kea_bank.domain.accounts.BusinessAccount;
import com.example.kea_bank.domain.accounts.PensionAccount;
import com.example.kea_bank.domain.accounts.SavingsAccount;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

import java.util.ArrayList;

public class ApplyAccountActivity extends AppCompatActivity {

    private static final String TAG = "ApplyAccountActivity";

    private TextView tv;
    private Button businessAccount, pensionAccount, savingsAccount;

    Intent receivedIntent;
    Intent updatedUser;
    User user;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_account);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();
        initNonViews();
        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));

        tv.setText("Hello " + user.getCredentials().getName());
        displayDeactivatedAccounts(user);

        updatedUser = new Intent();
    }

    protected void init(){
        Log.d(TAG, getResources().getString(R.string.init));
        tv = findViewById(R.id.tv);
        businessAccount = findViewById(R.id.businessAccount);
        pensionAccount = findViewById(R.id.pensionAccount);
        savingsAccount = findViewById(R.id.savingsAccount);
    }

    protected void initNonViews(){
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
    }

    public void onClick(View view){
        Log.d(TAG, getResources().getString(R.string.onClick));
        switch (view.getId()){

            case R.id.businessAccount:
                user.getBusinessAccount().setActivated(true);
                updateUserApplication(user);
                businessAccount.setVisibility(View.GONE);
                Toast.makeText(context, "You've applied for a " + businessAccount.getText().toString().toLowerCase() + "!\n Next time you log in, it will be verified.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pensionAccount:
                user.getPensionAccount().setActivated(true);
                updateUserApplication(user);
                pensionAccount.setVisibility(View.GONE);
                Toast.makeText(context, "You've applied for a " + pensionAccount.getText().toString().toLowerCase() + "!\n Next time you log in, it will be verified.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.savingsAccount:
                user.getSavingsAccount().setActivated(true);
                updateUserApplication(user);
                savingsAccount.setVisibility(View.GONE);
                Toast.makeText(context, "You've applied for a " + savingsAccount.getText().toString().toLowerCase() + "!\n Next time you log in, it will be verified.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public void displayDeactivatedAccounts(User user){
        Log.d(TAG, "displayDeactivatedAccounts() called");

        ArrayList<Account> accounts = userService.fetchUserAccounts(user);
        for (Account account: accounts) {
            if (account instanceof BusinessAccount && account.isActivated()){
                businessAccount.setVisibility(View.GONE);
            }
            if (account instanceof PensionAccount && account.isActivated()){
                pensionAccount.setVisibility(View.GONE);
            }
            if (account instanceof SavingsAccount && account.isActivated()){
                savingsAccount.setVisibility(View.GONE);
            }
        }
        if ((View.GONE == businessAccount.getVisibility()) && (View.GONE == pensionAccount.getVisibility()) && (View.GONE == savingsAccount.getVisibility())){
            tv.setText("You've already applied for all types of accounts!");
        }
    }

    private void updateUserApplication(User user){
        userService.saveUser(context, sharedPreferences, user);
        updatedUser.putExtra(getResources().getString(R.string.existing_user), user);
    }
}
