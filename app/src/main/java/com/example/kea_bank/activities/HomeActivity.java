package com.example.kea_bank.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kea_bank.R;

public class HomeActivity extends AppCompatActivity {

    Button newsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        newsFeed = findViewById(R.id.newsFeedButton);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, NewsFeedActivity.class);
        startActivity(intent);
    }
}
