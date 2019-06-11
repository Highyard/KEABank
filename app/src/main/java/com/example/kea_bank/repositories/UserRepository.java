package com.example.kea_bank.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Bills.Bill;
import com.example.kea_bank.domain.Credentials.Credentials;
import com.example.kea_bank.domain.users.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private Context context;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    public UserRepository(){
    }

    public UserRepository(Context context, SharedPreferences sharedPreferences){
        this.context = context;
        this.sharedPrefs = sharedPreferences;
    }

    public void setContext(Context context, SharedPreferences sharedPreferences){
        this.context = context;
        this.sharedPrefs = sharedPreferences;

    }

    public User fetchUser(String userName){

        Log.d(TAG, context.getResources().getString(R.string.fetch_user));
        Gson gson = new Gson();
        String fetchedUserName = sharedPrefs.getString(userName, "");
        if (fetchedUserName.contains(userName)) {
            return gson.fromJson(fetchedUserName, User.class);
        } else {
            Credentials credentials = new Credentials("", "");
            return new User(0, "", credentials, null, null, null, null, null, new ArrayList<Bill>(), false);
        }
    }

    public void saveUser(User user){
        Log.d(TAG, context.getResources().getString(R.string.save_user));
        editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(user.getCredentials().getName(), json);
        editor.apply();

    }
}
