package com.example.kea_bank.domain.accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kea_bank.domain.users.User;

public abstract class Account implements Parcelable {

    boolean activated   = false;
    Double balance      = null;
    String userId       = null;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void move(User sender, String receiver, Double amount) {

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
