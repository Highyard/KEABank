package com.example.kea_bank.domain.accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kea_bank.domain.users.User;

public class BusinessAccount extends Account implements Parcelable {

    public BusinessAccount(Double balance) {
        this.balance = balance;
        this.activated = false;
    }


    protected BusinessAccount(Parcel in) {
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        activated = in.readByte() != 0;
    }

    public static final Creator<BusinessAccount> CREATOR = new Creator<BusinessAccount>() {
        @Override
        public BusinessAccount createFromParcel(Parcel in) {
            return new BusinessAccount(in);
        }

        @Override
        public BusinessAccount[] newArray(int size) {
            return new BusinessAccount[size];
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
