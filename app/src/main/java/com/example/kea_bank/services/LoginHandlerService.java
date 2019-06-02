package com.example.kea_bank.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.repositories.UserRepository;

public class LoginHandlerService {

    private final static String TAG = "LoginHandlerService";

    private Context context;

    private UserRepository userRepository = new UserRepository();

    public LoginHandlerService(Context context){
        this.context = context;
    }


    public boolean handleLogin(Context context, SharedPreferences sharedPreferences, String userName, String password){
        Log.d(TAG, context.getResources().getString(R.string.handle_login));
        userRepository.setContext(context, sharedPreferences);
        User user = userRepository.fetchUser(userName);
        String fetchedPassword = user.getCredentials().getPassword();
        return fetchedPassword.equals(password);
    }

}
