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
import com.example.kea_bank.domain.Bills.Bill;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

public class SpecificBillActivity extends AppCompatActivity {

    private static final String TAG = "SpecificBillActivity";

    private TextView billName, billAmount, tvHeader;
    private Button payManually, payAuto;

    Intent receivedIntent;
    User user;
    Bill customBill;

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
        instantiateBill();

        setTextViews();
        Log.d(TAG, "USER AUTO PAY: "+ user.isAutoPay());
    }



    public void onClick(View view){
        switch (view.getId()){

            case R.id.payMan:
                if (userService.checkAndPayBill(user, customBill)) {
                    userService.saveUser(context, sharedPreferences, user);
                    Toast.makeText(context, "Bill was paid!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
                break;


        }
    }

    protected void init(){
        tvHeader = findViewById(R.id.textView3);
        billName = findViewById(R.id.billName);
        billAmount = findViewById(R.id.billAmount);
        payManually = findViewById(R.id.payMan);
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

    protected void instantiateBill(){
        customBill = receivedIntent.getParcelableExtra("SPECIFIC_BILL");
    }

    private void setTextViews() {
        billName.setText(customBill.getSender());
        billAmount.setText(String.format("%.2f", customBill.getAmount()));
    }
}
