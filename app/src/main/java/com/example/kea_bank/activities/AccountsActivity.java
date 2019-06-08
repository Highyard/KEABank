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
import android.widget.ListView;
import android.widget.TextView;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

import java.util.ArrayList;

public class AccountsActivity extends AppCompatActivity {

    private final static String TAG = "AccountsActivity";

    // Codes for checking which account was tapped //
    public final static int DEF = 0;
    public final static int BUD = 1;
    public final static int BUSI = 2;
    public final static int PEN = 3;
    public final static int SAV = 4;


    ListView accounts;
    TextView tvAccounts;
    ArrayList<String> accountArray;
    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    User user;
    Intent receivedIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Log.d(TAG, getResources().getString(R.string.on_create));
        init();
        initNonViews();


        receivedIntent = getIntent();
        user = receivedIntent.getParcelableExtra(getResources().getString(R.string.existing_user));

        // Assigns the account names of the users accounts that are active to ArrayList accountArray. //
        // These are used to list all the current accounts that are active for logged in user. //
        // getAccountNames takes an ArrayList as the only argument, which is retrieved from the user object //
        // with the fetchUserAccounts method //
        accountArray = userService.getAccountNames(userService.fetchUserAccounts(user));

        // Instantiating adapter //
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AccountsActivity.this, android.R.layout.simple_list_item_1, accountArray);

        // Set adapter to the ListView //
        accounts.setAdapter(adapter);


        accounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick() called. Position: " + position);
                switch (position){

                    case DEF:
                        Intent defAccount = new Intent(AccountsActivity.this, SpecificAccountActivity.class);
                        defAccount.putExtra("CUSTOM_CODE", DEF);
                        defAccount.putExtra(getResources().getString(R.string.existing_user), user);
                        startActivity(defAccount);
                        break;

                    case BUD:
                        Intent budAccount = new Intent(AccountsActivity.this, SpecificAccountActivity.class);
                        budAccount.putExtra("CUSTOM_CODE", BUD);
                        budAccount.putExtra(getResources().getString(R.string.existing_user), user);
                        startActivity(budAccount);
                        break;

                    case BUSI:
                        Intent busiAccount = new Intent(AccountsActivity.this, SpecificAccountActivity.class);
                        busiAccount.putExtra("CUSTOM_CODE", BUSI);
                        busiAccount.putExtra(getResources().getString(R.string.existing_user), user);
                        startActivity(busiAccount);
                        break;

                    case PEN:
                        Intent penAccount = new Intent(AccountsActivity.this, SpecificAccountActivity.class);
                        penAccount.putExtra("CUSTOM_CODE", PEN);
                        penAccount.putExtra(getResources().getString(R.string.existing_user), user);
                        startActivity(penAccount);
                        break;

                    case SAV:
                        Intent savAccount = new Intent(AccountsActivity.this, SpecificAccountActivity.class);
                        savAccount.putExtra("CUSTOM_CODE", SAV);
                        savAccount.putExtra(getResources().getString(R.string.existing_user), user);
                        startActivity(savAccount);
                        break;

                }

            }
        });

    }

    protected void init(){
        Log.d(TAG, getResources().getString(R.string.init));
        accounts = findViewById(R.id.accountsList);
        tvAccounts = findViewById(R.id.tvAccounts);
    }

    protected void initNonViews(){
        Log.d(TAG, "initNonViews() called");
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(this, sharedPreferences);
    }

}
