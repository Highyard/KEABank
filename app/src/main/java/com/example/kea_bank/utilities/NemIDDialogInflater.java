package com.example.kea_bank.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.activities.ApplyAccountActivity;
import com.example.kea_bank.activities.HomeActivity;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

import java.util.Arrays;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class NemIDDialogInflater extends DialogFragment {

    private static final String TAG = "NemIDDialogInflater";

    public static final int MAIN_ACTIVITY_CODE = 0;
    public static final int SPECIFIC_ACCOUNT_ACTIVITY_CODE = 1;

    private TextView key, dCancel, dActionOk;
    private EditText nemIdInput;

    private User user;
    private Intent loginIntent;
    private Intent sendMoneyIntent;
    private int activityCode = -1;

    Context context;
    SharedPreferences sharedPreferences;
    public UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_nemid, container, false);
        key = view.findViewById(R.id.keyText);
        nemIdInput = view.findViewById(R.id.nemIdInput);
        dCancel = view.findViewById(R.id.dCancel);
        dActionOk = view.findViewById(R.id.dActionOk);

        init();

        loginIntent = new Intent(context, HomeActivity.class);
        sendMoneyIntent = new Intent(context, ApplyAccountActivity.class);


        final Random random = new Random();
        final int randomIndex = random.nextInt(KeyGenerator.KEY_SIZE);

        final String[] keyArray = user.getKeys().get(randomIndex).split(":");
        Log.d(TAG, "This is the fetched key pair: " + Arrays.toString(keyArray));

        key.setText(keyArray[0]);
        Log.d(TAG, "This is the first  index: " + keyArray[0]);
        Log.d(TAG, "This is the second index: " + keyArray[1]);

        dActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (activityCode == MAIN_ACTIVITY_CODE){
                        if (userService.verifyKeyMatch(keyArray, nemIdInput.getText().toString().trim())) {
                            loginIntent.putExtra(getResources().getString(R.string.existing_user), user);
                            startActivity(loginIntent);

                        } else {
                            Toast.makeText(context, getResources().getString(R.string.invalid_key), Toast.LENGTH_SHORT).show();
                        }
                    }

                    else if (activityCode == SPECIFIC_ACCOUNT_ACTIVITY_CODE){
                        if (userService.verifyKeyMatch(keyArray, nemIdInput.getText().toString().trim())) {
                            sendMoneyIntent.putExtra(getResources().getString(R.string.existing_user), user);
                            startActivity(sendMoneyIntent);

                        } else {
                            Toast.makeText(context, getResources().getString(R.string.invalid_key), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (activityCode == -1){
                        Log.d(TAG, "No activityCode set. Call setActivityCode() with an activityCode from " + getActivity() +  " if you want to start an activity after confirmation");
                    }

                } catch (ClassCastException e) {
                    Log.e(TAG, "Class context expected before initialization " + e.getMessage());
                }

                getDialog().dismiss();

            }
        });

        dCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void init(){
        context = getActivity();
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.CREDENTIALS_KEY), MODE_PRIVATE);
        userService = new UserService(context, sharedPreferences);
    }

    public void instantiateUser(User passedUser){
        user = passedUser;
    }

    public void setActivityCode(int code){
        activityCode = code;
    }
}
