package com.example.kea_bank.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Credentials.Credentials;
import com.example.kea_bank.domain.accounts.BudgetAccount;
import com.example.kea_bank.domain.accounts.BusinessAccount;
import com.example.kea_bank.domain.accounts.DefaultAccount;
import com.example.kea_bank.domain.accounts.PensionAccount;
import com.example.kea_bank.domain.accounts.SavingsAccount;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.repositories.UserRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

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

    public void setFields(User user, String email, String password, int age) {
        Credentials credentials = new Credentials(email, password);
        user.setAge(age);
        user.setCredentials(credentials);
        user.setDefaultAccount(new DefaultAccount(0.0));
        user.setBudgetAccount(new BudgetAccount(0.0));
        user.setBusinessAccount(new BusinessAccount(0.0));
        user.setPensionAccount(new PensionAccount(0.0));
        user.setSavingsAccount(new SavingsAccount(0.0));
    }

    public int calculateUserAge(Date birthDate){
        Date currentDate = new Date();

        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int date1 = Integer.parseInt(formatter.format(birthDate));
        int date2 = Integer.parseInt(formatter.format(currentDate));
        return (date2 - date1) / 10000;
    }

    public boolean verifyKeyMatch(){
        return true;
    }

}
