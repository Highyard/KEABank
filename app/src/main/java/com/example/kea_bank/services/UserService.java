package com.example.kea_bank.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.repositories.UserRepository;

public class UserService {

    private final static String TAG = "UserService";

    private Context context;
    private SharedPreferences sharedPreferences;

    private UserRepository userRepository = new UserRepository();

    public UserService(Context context, SharedPreferences sharedPreferences){
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveUser(Context context, SharedPreferences sharedPreferences, User user){
        userRepository.setContext(context, sharedPreferences);
        Log.d(TAG, context.getResources().getString(R.string.save_user));
        userRepository.saveUser(user);

    }

    public User fetchUser(String userId){
        userRepository.setContext(context, sharedPreferences);
        Log.d(TAG, context.getResources().getString(R.string.fetch_user));
        return userRepository.fetchUser(userId);
    }

    public boolean passwordMatch(String pass1, String pass2){
        Log.d(TAG, context.getResources().getString(R.string.password_match));
        return pass1.equals(pass2);
    }

    public boolean userExists(String userId) throws NullPointerException {
        userRepository.setContext(context, sharedPreferences);
        Log.d(TAG, context.getResources().getString(R.string.user_exists));
        //Log.d(TAG, "Name of contains: " + userRepository.fetchUser(userId).getCredentials().getName());
        return userRepository.fetchUser(userId).getCredentials().getName().contains(userId);
    }

}
