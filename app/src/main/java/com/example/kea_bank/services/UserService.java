package com.example.kea_bank.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Bills.Bill;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
        return userRepository.fetchUser(userId).getCredentials().getName().contains(userId);
    }

    public void setFields(User user, String email, String password, int age) {
        Log.d(TAG, "setFields() called");
        Credentials credentials = new Credentials(email, password);
        user.setAge(age);
        user.setCredentials(credentials);
        DefaultAccount defaultAccount = new DefaultAccount(1000000.0);
        BudgetAccount budgetAccount = new BudgetAccount(0.0);
        BusinessAccount businessAccount = new BusinessAccount(0.0);
        PensionAccount pensionAccount = new PensionAccount(0.0);
        SavingsAccount savingsAccount = new SavingsAccount(0.0);
        ArrayList<Bill> arrayList = new ArrayList<>();
        user.setDefaultAccount(defaultAccount);
        user.setBudgetAccount(budgetAccount);
        user.setBusinessAccount(businessAccount);
        user.setPensionAccount(pensionAccount);
        user.setSavingsAccount(savingsAccount);
        user.setBills(arrayList);
        user.setAutoPay(false);
    }

    public int calculateUserAge(String unformattedBirthdate) {
        Log.d(TAG, "calculateUserAge() called");
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
        // if we somehow reach this point, 0 is returned, alerting the user that they must be 18 years old,
        // they can then try again
        return 0;
    }

    public boolean verifyKeyMatch(String[] keyArray, String input){
        Log.d(TAG, "verifyKeyMatch() called");
        String correctKey = keyArray[1];
        return input.equalsIgnoreCase(correctKey);
    }

    public ArrayList<Account> fetchUserAccounts(User user){
        Log.d(TAG, "fetchUserAccounts() called");
        ArrayList<Account> initialArrayList = new ArrayList<>();
        initialArrayList.add(user.getDefaultAccount());
        initialArrayList.add(user.getBudgetAccount());
        initialArrayList.add(user.getBusinessAccount());
        initialArrayList.add(user.getPensionAccount());
        initialArrayList.add(user.getSavingsAccount());

        ArrayList<Account> finalArrayList = new ArrayList<>();
        for (Account account: initialArrayList) {
            if (account.isActivated())
                finalArrayList.add(account);
        }

        return finalArrayList;
    }

    public ArrayList<String> getAccountNames(ArrayList<Account> accounts){
        Log.d(TAG, "getAccountNames() called");
        ArrayList<String> accountNames = new ArrayList<>();
        for (Account account: accounts) {
            accountNames.add(account.getClass().getSimpleName());
        }
        Collections.reverse(accountNames);
        return accountNames;
    }

    public boolean checkAndPayBill(User user, Bill bill){
        for (Bill userBill: user.getBills()) {
            if (userBill.getSender().equalsIgnoreCase(bill.getSender())){
                if (userBill.getAmount().equals(bill.getAmount())){
                    user.getDefaultAccount().setBalance(user.getDefaultAccount().getBalance() - bill.getAmount());
                    user.getBills().remove(userBill);
                    return true;
                }
            } else {
                Log.d(TAG, "checkAndPayBill: bill not in userBill array");
            }
        }
        return false;
    }

    public void autoPayAllBills(User user){
        ArrayList<Bill> tempArrayList = new ArrayList<>(user.getBills());
        if (user.isAutoPay()){
            for (Bill bill: tempArrayList) {
                user.getDefaultAccount().setBalance(user.getDefaultAccount().getBalance() - bill.getAmount());
                user.getBills().remove(bill);
            }
        }

        this.saveUser(context, sharedPreferences, user);
    }

    public void sendMoneySomeoneElse(User user, Account fromAccount, User receiver, Double amount){
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        receiver.getDefaultAccount().setBalance(receiver.getDefaultAccount().getBalance() + amount);
        this.saveUser(context, sharedPreferences, user);
        this.saveUser(context, sharedPreferences, receiver);
    }

    public void sendMoneyOwnAccount(User user, Account fromAccount, Account toAccount, Double amount){

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        this.saveUser(context, sharedPreferences, user);
    }
}
