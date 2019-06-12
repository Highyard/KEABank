package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.accounts.Account;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;
import com.example.kea_bank.utilities.NemIDDialogInflater;

public class SendMoneyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SendMoneyActivity";

    TextView toWho, howMuch;
    Button myOwn, someoneElses, send;
    Spinner spinner;
    EditText editText;

    User user;
    Intent receivedIntent;

    Account receivedAccount;
    Account toAccount;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;
    NemIDDialogInflater nemIDDialogInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        init();
        initNonViews();
        instantiateUser();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.spinner_coiches, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    protected void init(){

        toWho = findViewById(R.id.toWho);
        myOwn = findViewById(R.id.myOwn);
        someoneElses = findViewById(R.id.someoneElses);
        howMuch = findViewById(R.id.howMuch);
        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.editText);
        send = findViewById(R.id.send);

        spinner.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        howMuch.setVisibility(View.GONE);
        send.setVisibility(View.GONE);
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

        switch (view.getId()){

            case R.id.myOwn:
                toWho.setText(myOwn.getText().toString());
                spinner.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                howMuch.setVisibility(View.VISIBLE);
                myOwn.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                break;

            case R.id.someoneElses:
                nemIDInflater();
                nemIDDialogInflater.instantiateCurrentAccount(receivedAccount);
                break;

            case R.id.send:
                Double amount = Double.parseDouble(editText.getText().toString());

                userService.assignInstanceOf(user, receivedAccount);

                userService.assignInstanceOf(user, toAccount);

                receivedAccount.setBalance(receivedAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);

                userService.saveUser(context, sharedPreferences, user);

                Intent intent = new Intent(this, AccountsActivity.class);
                intent.putExtra(getResources().getString(R.string.existing_user), user);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();

        Log.d(TAG, "STRING CHOICE: " + choice);

        for (Account chosenAccount: userService.fetchUserAccounts(user)) {

                if (choice.equalsIgnoreCase(chosenAccount.getClass().getSimpleName())){
                    toAccount = chosenAccount;


            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void nemIDInflater(){
        nemIDDialogInflater = new NemIDDialogInflater();
        nemIDDialogInflater.instantiateUser(user);
        nemIDDialogInflater.setActivityCode(NemIDDialogInflater.SEND_MONEY_ACTIVITY);
        nemIDDialogInflater.show(getSupportFragmentManager(), "nemIDDialogInflater");
    }





}
