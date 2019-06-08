package com.example.kea_bank.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;

public class SpecificAccountActivity extends AppCompatActivity {

    private final static String TAG = "SpecificAccountActivity";

    TextView tvSpecificAccount, tvBalance;
    Button sendMoney, depositMoney, payBills;

    Intent receivedIntent;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_account);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();

        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));
        int customCode = receivedIntent.getIntExtra("CUSTOM_CODE", -1);
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
                break;

            case AccountsActivity.BUD:
                tvSpecificAccount.setText(getResources().getString(R.string.budget_account) + " " + user.getCredentials().getName());
                tvBalance.setText(getResources().getString(R.string.balance) + user.getBudgetAccount().getBalance());
                payBills.setVisibility(View.GONE);
                break;

            case AccountsActivity.BUSI:
                tvSpecificAccount.setText(getResources().getString(R.string.business_account) + " " + user.getCredentials().getName());
                tvBalance.setText(String.valueOf(user.getDefaultAccount().getBalance()));
                payBills.setVisibility(View.GONE);
                break;

            case AccountsActivity.PEN:
                tvSpecificAccount.setText(getResources().getString(R.string.pension_account) + " " + user.getCredentials().getName());
                tvBalance.setText(String.valueOf(user.getDefaultAccount().getBalance()));
                sendMoney.setVisibility(View.GONE);
                payBills.setVisibility(View.GONE);
                break;

            case AccountsActivity.SAV:
                tvSpecificAccount.setText(getResources().getString(R.string.savings_account) + " " + user.getCredentials().getName());
                tvBalance.setText(String.valueOf(user.getDefaultAccount().getBalance()));
                payBills.setVisibility(View.GONE);
                break;

            default:
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                SpecificAccountActivity.this.finish();

        }

    }

    public void onClick(View view){
        Log.d(TAG, getResources().getString(R.string.onClick));

        switch (view.getId()){

            case R.id.sendMoney:

                break;
            case R.id.depositMoney:
                break;
            case R.id.payBills:
                break;

        }

    }

    public void sendMoneyInflater(){

    }
}
