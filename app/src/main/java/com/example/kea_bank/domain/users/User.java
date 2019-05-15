package com.example.kea_bank.domain.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kea_bank.domain.Credentials.Credentials;
import com.example.kea_bank.domain.accounts.BudgetAccount;
import com.example.kea_bank.domain.accounts.BusinessAccount;
import com.example.kea_bank.domain.accounts.DefaultAccount;
import com.example.kea_bank.domain.accounts.PensionAccount;
import com.example.kea_bank.domain.accounts.SavingsAccount;

public class User implements Parcelable {

    private Integer age                     = null;
    private String branch                   = null;
    private Credentials credentials         = null;
    private DefaultAccount defaultAccount   = null;
    private BudgetAccount budgetAccount     = null;
    private BusinessAccount businessAccount = null;
    private PensionAccount pensionAccount   = null;
    private SavingsAccount savingsAccount   = null;

    public User() {
    }

    public User(Integer age, String branch, Credentials credentials, DefaultAccount defaultAccount, BudgetAccount budgetAccount, BusinessAccount businessAccount, PensionAccount pensionAccount, SavingsAccount savingsAccount) {
        this.age = age;
        this.branch = branch;
        this.credentials = credentials;
        this.defaultAccount = defaultAccount;
        this.budgetAccount = budgetAccount;
        this.businessAccount = businessAccount;
        this.pensionAccount = pensionAccount;
        this.savingsAccount = savingsAccount;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            age = null;
        } else {
            age = in.readInt();
        }
        branch = in.readString();
        credentials = in.readParcelable(Credentials.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (age == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(age);
        }
        dest.writeString(branch);
        dest.writeParcelable(credentials, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public DefaultAccount getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(DefaultAccount defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public BudgetAccount getBudgetAccount() {
        return budgetAccount;
    }

    public void setBudgetAccount(BudgetAccount budgetAccount) {
        this.budgetAccount = budgetAccount;
    }

    public BusinessAccount getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(BusinessAccount businessAccount) {
        this.businessAccount = businessAccount;
    }

    public PensionAccount getPensionAccount() {
        return pensionAccount;
    }

    public void setPensionAccount(PensionAccount pensionAccount) {
        this.pensionAccount = pensionAccount;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    public void setSavingsAccount(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }



}
