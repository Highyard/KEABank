package com.example.kea_bank.domain.Bills;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Bill implements Parcelable {

    private static final String[] businessNames = {"Codan", "Mærsk", "Autoglass", "Tryg", "Almindelig Brand"};

    private Double amount;
    private String sender;

    public Bill(String sender, Double amount){
        this.sender = sender;
        this.amount = amount;
    }

    public Bill() {
        this.amount = generateAmount();
        this.sender = generateBusinessName();
    }

    private Double generateAmount(){
        Random random = new Random();

        return random.nextDouble() * 1000;
    }

    private String generateBusinessName(){
        Random random = new Random();

        int ran = random.nextInt(businessNames.length);

        return businessNames[ran];
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    protected Bill(Parcel in) {
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        sender = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
        }
        dest.writeString(sender);
    }
}
