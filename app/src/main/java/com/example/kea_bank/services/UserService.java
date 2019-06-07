package com.example.kea_bank.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Credentials.Credentials;
import com.example.kea_bank.domain.accounts.Account;
import com.example.kea_bank.domain.accounts.BudgetAccount;
import com.example.kea_bank.domain.accounts.BusinessAccount;
import com.example.kea_bank.domain.accounts.DefaultAccount;
import com.example.kea_bank.domain.accounts.PensionAccount;
import com.example.kea_bank.domain.accounts.SavingsAccount;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.repositories.UserRepository;
import com.example.kea_bank.utilities.KeyGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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
        DefaultAccount defaultAccount = new DefaultAccount(1000000.0);
        BudgetAccount budgetAccount = new BudgetAccount(0.0);
        BusinessAccount businessAccount = new BusinessAccount(0.0);
        PensionAccount pensionAccount = new PensionAccount(0.0);
        SavingsAccount savingsAccount = new SavingsAccount(0.0);
        user.setDefaultAccount(defaultAccount);
        user.setBudgetAccount(budgetAccount);
        user.setBusinessAccount(businessAccount);
        user.setPensionAccount(pensionAccount);
        user.setSavingsAccount(savingsAccount);
    }

    public int calculateUserAge(String unformattedBirthdate) {
        Date currentDate = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        try {
            unformattedBirthdate = unformattedBirthdate.replace("/", "-");
            Date formattedDate = simpleDateFormat.parse(unformattedBirthdate);
            int date1 = Integer.parseInt(formatter.format(formattedDate));
            int date2 = Integer.parseInt(formatter.format(currentDate));
            return (date2 - date1) / 10000;
        } catch (ParseException e) {
            Log.e(TAG, "calculateUserAge: " + e.getMessage());
        }
        return 0;
    }

    public boolean verifyKeyMatch(String[] keyArray, String input){
        String correctKey = keyArray[1];
        return input.equalsIgnoreCase(correctKey);
    }

    public ArrayList<Account> fetchUserAccounts(User user){
        ArrayList<Account> initialArrayList = new ArrayList<>();
        Log.d(TAG, "THIS IS USERSERVICE" + user.getDefaultAccount());
        initialArrayList.add(user.getBudgetAccount());
        initialArrayList.add(user.getBusinessAccount());
        initialArrayList.add(user.getDefaultAccount());
        initialArrayList.add(user.getPensionAccount());
        initialArrayList.add(user.getSavingsAccount());

        ArrayList<Account> finalArrayList = new ArrayList<>();
        for (Account account: initialArrayList) {
            if (account.isActivated())
                finalArrayList.add(account);
        }

        return finalArrayList;
    }
}
