package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.accounts.Account;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

public class SomeoneElsesActivity extends AppCompatActivity {

    TextView tvRecipient, tvAmount;
    EditText etRecipient, etAmount;
    Button send;

    User user;
    Intent receivedIntent;
    Account receivedAccount;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someone_elses);
        init();
        initNonViews();
        instantiateUser();

    }

    protected void init(){
        tvRecipient = findViewById(R.id.tvRecipient);
        tvAmount = findViewById(R.id.tvAmount);
        etRecipient = findViewById(R.id.etRecipient);
        etAmount = findViewById(R.id.etAmount);
        send = findViewById(R.id.send);
    }


    protected void initNonViews(){
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
    }

    protected void instantiateUser() {
        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));
        receivedAccount = receivedIntent.getParcelableExtra("ACCOUNT");
        String username = user.getCredentials().getName();
        user = userService.fetchUser(username);

    }

    public void onClick(View view){
        User receiver = userService.fetchUser(etRecipient.getText().toString().trim());
        userService.sendMoneySomeoneElse(user, receivedAccount, receiver, Double.parseDouble(etAmount.getText().toString()));
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.existing_user), user);
        setResult(201);
        finish();
    }

}
