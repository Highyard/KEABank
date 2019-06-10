package com.example.kea_bank.domain.accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kea_bank.domain.users.User;

public class BudgetAccount extends Account implements Parcelable {

    public BudgetAccount(Double balance) {
        this.balance = balance;
        this.activated = true;
    }

    protected BudgetAccount(Parcel in) {
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        activated = in.readByte() != 0;
    }

    public static final Creator<BudgetAccount> CREATOR = new Creator<BudgetAccount>() {
        @Override
        public BudgetAccount createFromParcel(Parcel in) {
            return new BudgetAccount(in);
        }

        @Override
        public BudgetAccount[] newArray(int size) {
            return new BudgetAccount[size];
        }
    };



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
