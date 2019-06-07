package com.example.kea_bank.domain.accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kea_bank.domain.users.User;

public class SavingsAccount extends Account implements Parcelable {

    public SavingsAccount(Double balance) {
        this.balance = balance;
        this.activated = false;
    }

    protected SavingsAccount(Parcel in) {
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        activated = in.readByte() != 0;
    }

    public static final Creator<SavingsAccount> CREATOR = new Creator<SavingsAccount>() {
        @Override
        public SavingsAccount createFromParcel(Parcel in) {
            return new SavingsAccount(in);
        }

        @Override
        public SavingsAccount[] newArray(int size) {
            return new SavingsAccount[size];
        }
    };

    @Override
    public void increment(User userAccount) {

    }

    @Override
    public void send(User sender, User receiver, Double amount) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(balance);
        }
        dest.writeByte((byte) (activated ? 1 : 0));
    }
}
