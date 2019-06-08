package com.example.kea_bank.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.LoginHandlerService;
import com.example.kea_bank.services.UserService;
import com.example.kea_bank.utilities.KeyGenerator;
import com.example.kea_bank.utilities.NemIDDialogInflater;
//import com.example.kea_bank.utilities.NemIDDialogInflater;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    public final static int USER_SUCCESS = 1;

    Button sign_up, log_in, resetPassword;
    EditText email, password, nemIdInput;
    TextView key;

    User user;

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
        initNonViews();

        //NemIDDialogInflater dialog = new NemIDDialogInflater();


    }


    protected void init(){
        Log.d(TAG, getResources().getString(R.string.init));
        sign_up = findViewById(R.id.sign_up);
        log_in = findViewById(R.id.log_in);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        resetPassword = findViewById(R.id.reset_password);

//        inflater = getLayoutInflater();
//        layout = inflater.inflate(R.layout.verify_nemid, null);

//        key = layout.findViewById(R.id.keyText);
//        nemIdInput = layout.findViewById(R.id.nemIdInput);

    }

    protected void initNonViews(){
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
        loginHandlerService = new LoginHandlerService(context);

    }

    public void dialogInflater(final Intent intent, final User user) {
        Log.d(TAG, getResources().getString(R.string.dialog_inflater));
        final Random random = new Random();
        final int randomIndex = random.nextInt(KeyGenerator.keySize);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.verify_nemid, null);

        key = layout.findViewById(R.id.keyText);
        nemIdInput = layout.findViewById(R.id.nemIdInput);

        try {

            final AlertDialog.Builder nemIdDialog = new AlertDialog.Builder(this);
            nemIdDialog.setTitle("NemID");
            nemIdDialog.setView(layout);
            nemIdDialog.setIcon(R.mipmap.nemid_layer);


            final String[] keyArray = userService.fetchUser(email.getText().toString()).getKeys().get(randomIndex).split(":");
            Log.d(TAG, "This is the fetched key pair: " + Arrays.toString(keyArray));

            // Display the first half of key to user
            key.setText(keyArray[0]);
            Log.d(TAG, "This is the first  index: " + keyArray[0]);
            Log.d(TAG, "This is the second index: " + keyArray[1]);

            nemIdDialog.setPositiveButton(getResources().getString(R.string.dialog_login), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Verify if user typed in the correct key
                    if (userService.verifyKeyMatch(keyArray, nemIdInput.getText().toString().trim())) {
                        intent.putExtra(getResources().getString(R.string.existing_user), user);
                        startActivity(intent);

                    } else {
                        // If he did not, then the password is cleared, and a toast is displayed
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.invalid_key), Toast.LENGTH_SHORT).show();
                        password.setText("");
                    }
                }
            });

            nemIdDialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            //Show the dialog
            nemIdDialog.show();

        } catch (IllegalStateException e) {
            Log.e(TAG, "dialogInflater: IllegalStateException: " + e.getMessage());
        }
    }

    public void onClick(View view) {
        Log.d(TAG, getResources().getString(R.string.onClick));

        switch (view.getId()){
            case R.id.log_in:
                Log.d(TAG, getResources().getString(R.string.login_clicked_btn));
                if (userService.userExists(email.getText().toString())){
                    User user = userService.fetchUser(email.getText().toString().trim());
                    final Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);

                    if (loginHandlerService.handleLogin(
                            MainActivity.this,
                            sharedPreferences,
                            email.getText().toString(),
                            password.getText().toString()))
                    {

                        dialogInflater(loginIntent, user);

                        //NemIDDialogInflater dialog = new NemIDDialogInflater();
                        //dialog.show(getSupportFragmentManager(), "SomeName");

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
        Log.d(TAG, getResources().getString(R.string.on_activity_result));

        Log.d(TAG, "Request code" + requestCode + "// ResultCode" + resultCode);
        try {
            if (requestCode == USER_SUCCESS){

                if (resultCode == RESULT_OK) {
                    user = data.getParcelableExtra("NEW_USER");
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
            Log.e(TAG, "onActivityResult: The Intent data object came back null: Error ->", e);
            Toast.makeText(MainActivity.this, "Something went wrong. Try signing up again.", Toast.LENGTH_LONG).show();
        }

    }
}
