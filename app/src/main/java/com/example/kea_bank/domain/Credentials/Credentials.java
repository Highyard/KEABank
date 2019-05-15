package com.example.kea_bank.domain.Credentials;

import android.os.Parcel;
import android.os.Parcelable;


public class Credentials implements Parcelable {


    private String name;
    private String password;

    public Credentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Credentials() {
    }

    protected Credentials(Parcel in) {
        name = in.readString();
        password = in.readString();
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
