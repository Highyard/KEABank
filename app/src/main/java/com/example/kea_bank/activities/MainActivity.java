package com.example.kea_bank.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.LoginHandlerService;
import com.example.kea_bank.services.UserService;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    public final static int USER_SUCCESS = 1;

    Button sign_up, log_in;
    EditText email, password;

    Context context;
    SharedPreferences sharedPreferences;
    UserService userService;
    LoginHandlerService loginHandlerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, getResources().getString(R.string.on_create));

        // Initialize views //
        init();

        // Initialize non-views //
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
        loginHandlerService = new LoginHandlerService(context);

    }

    protected void init(){
        sign_up = findViewById(R.id.sign_up);
        log_in = findViewById(R.id.log_in);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


    }

    public void onClick(View view) {
        Log.d(TAG, getResources().getString(R.string.onClick));

        switch (view.getId()){
            case R.id.log_in:
                Log.d(TAG, getResources().getString(R.string.login_clicked_btn));
                if (userService.userExists(email.getText().toString())){
                    Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);

                    if (loginHandlerService.handleLogin(
                            MainActivity.this,
                            sharedPreferences,
                            email.getText().toString(),
                            password.getText().toString()))
                    {
                        startActivity(loginIntent);
                        break;
                    } else {

                        password.setError(getResources().getString(R.string.invalid_password_string));
                        break;
                    }
                } else {
                    email.setError(getResources().getString(R.string.user_does_not_exist));
                    break;
                }

            case R.id.sign_up:
                Log.d(TAG, getResources().getString(R.string.sign_up_clicked_btn));
                Intent signUpRedirect = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivityForResult(signUpRedirect, USER_SUCCESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Request code" + requestCode + "// ResultCode" + resultCode);
        try {
            if (requestCode == USER_SUCCESS){

                if (resultCode == RESULT_OK) {
                    User user = data.getParcelableExtra("NEW_USER");
                    email.setText(user.getCredentials().getName());
                    password.setText(user.getCredentials().getPassword());
                }

                if (resultCode == RESULT_CANCELED) {

                    email.setText("");
                    password.setText("");
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                }
            }


        } catch (NullPointerException e){

        }

    }
}
