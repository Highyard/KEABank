package com.example.kea_bank.domain.accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kea_bank.domain.users.User;

public class PensionAccount extends Account implements Parcelable {

    public PensionAccount(Double balance) {
        this.balance = balance;
        this.activated = false;
    }

    protected PensionAccount(Parcel in) {
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        activated = in.readByte() != 0;
    }

    public static final Creator<PensionAccount> CREATOR = new Creator<PensionAccount>() {
        @Override
        public PensionAccount createFromParcel(Parcel in) {
            return new PensionAccount(in);
        }

        @Override
        public PensionAccount[] newArray(int size) {
            return new PensionAccount[size];
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
