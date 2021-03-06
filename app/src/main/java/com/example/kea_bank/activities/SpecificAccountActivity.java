package com.example.kea_bank.activities;

import android.annotation.SuppressLint;
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
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;
import com.example.kea_bank.utilities.NemIDDialogInflater;

public class SpecificAccountActivity extends AppCompatActivity {

    private final static String TAG = "SpecificAccountActivity";

    private final static String CUSTOM_CODE_HOLDER = "";

    TextView tvSpecificAccount, tvBalance;
    Button sendMoney, depositMoney, payBills;

    Intent receivedIntent;
    User user;
    Account currentAccount;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    int customCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_account);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();
        initNonViews();
        instantiateUser();

        customCode = receivedIntent.getIntExtra("CUSTOM_CODE", -1);

        if (savedInstanceState != null){
            Log.d(TAG, "INSTANCESTATE RAN");
            Log.d(TAG, "INSTANCE STATE INT VALUE :" + savedInstanceState.getInt(CUSTOM_CODE_HOLDER));
            customCode = savedInstanceState.getInt(CUSTOM_CODE_HOLDER);
        }

        setTextViews(customCode);

    }

    protected void init(){
        Log.d(TAG, getResources().getString(R.string.init));
        tvSpecificAccount = findViewById(R.id.tvSpecificAccount);
        tvBalance = findViewById(R.id.tvBalance);
        sendMoney = findViewById(R.id.sendMoney);
        depositMoney = findViewById(R.id.depositMoney);
        payBills = findViewById(R.id.payBills);
    }

    @SuppressLint("SetTextI18n")
    protected void setTextViews(int code){
        Log.d(TAG, "setTextViews() called");

        switch (code){

            case AccountsActivity.DEF:
                tvSpecificAccount.setText(getResources().getString(R.string.default_account) + " " + user.getCredentials().getName());
                tvBalance.setText(getResources().getString(R.string.balance) + user.getDefaultAccount().getBalance());
                currentAccount = user.getDefaultAccount();
                break;

            case AccountsActivity.BUD:
                tvSpecificAccount.setText(getResources().getString(R.string.budget_account) + " " + user.getCredentials().getName());
                tvBalance.setText(getResources().getString(R.string.balance) + user.getBudgetAccount().getBalance());
                payBills.setVisibility(View.GONE);
                currentAccount = user.getBudgetAccount();
                break;

            case AccountsActivity.BUSI:
                tvSpecificAccount.setText(getResources().getString(R.string.business_account) + " " + user.getCredentials().getName());
                tvBalance.setText(String.valueOf(user.getBusinessAccount().getBalance()));
                payBills.setVisibility(View.GONE);
                currentAccount = user.getBusinessAccount();
                break;

            case AccountsActivity.PEN:
                tvSpecificAccount.setText(getResources().getString(R.string.pension_account) + " " + user.getCredentials().getName());
                tvBalance.setText(String.valueOf(user.getPensionAccount().getBalance()));
                if (user.getAge() < 77) {
                    sendMoney.setVisibility(View.GONE);
                }
                payBills.setVisibility(View.GONE);
                currentAccount = user.getPensionAccount();
                break;

            case AccountsActivity.SAV:
                tvSpecificAccount.setText(getResources().getString(R.string.savings_account) + " " + user.getCredentials().getName());
                tvBalance.setText(String.valueOf(user.getSavingsAccount().getBalance()));
                payBills.setVisibility(View.GONE);
                currentAccount = user.getSavingsAccount();
                break;

            default:
                finish();


        }

    }

    public void onClick(View view){
        Log.d(TAG, getResources().getString(R.string.onClick));

        switch (view.getId()){

            case R.id.sendMoney:
                Intent intent = new Intent(this, SendMoneyActivity.class);
                intent.putExtra(getResources().getString(R.string.existing_user), user);
                intent.putExtra("ACCOUNT", currentAccount);
                startActivity(intent);
                break;
            case R.id.depositMoney:
                Intent depositMoney = new Intent(this, DepositMoneyActivity.class);
                startActivity(depositMoney);
                break;
            case R.id.payBills:
                break;

        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CUSTOM_CODE_HOLDER, customCode);
    }
}
