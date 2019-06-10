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
import com.example.kea_bank.domain.Bills.Bill;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;
import com.example.kea_bank.utilities.BillsArrayAdapter;

import java.util.ArrayList;

public class BillsActivity extends AppCompatActivity {

    private static final String TAG = "BillsActivity";

    ListView billsList;
    TextView outstandingBills;

    Intent receivedIntent;
    User user;
    ArrayList<Bill> userBillsArray;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        init();
        initNonViews();

        instantiateUser();

        BillsArrayAdapter adapter = new BillsArrayAdapter(BillsActivity.this, R.layout.custom_list_view_layout, user.getBills());

        billsList.setAdapter(adapter);

        if (adapter.isEmpty()){
            outstandingBills.setText("You have no outstanding bills!");
        }

        billsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + parent.getItemAtPosition(position));
            }
        });

    }

    protected void init(){
        billsList = findViewById(R.id.billsList);
        outstandingBills = findViewById(R.id.outstandingBills);
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
        userBillsArray = user.getBills();
    }

    public void onClick(View view) {
        userBillsArray.add(new Bill());
        userBillsArray.add(new Bill());
        userService.saveUser(context, sharedPreferences, user);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

}
