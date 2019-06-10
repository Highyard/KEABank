package com.example.kea_bank.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kea_bank.R;
import com.example.kea_bank.domain.Bills.Bill;

import java.util.List;

public class BillsArrayAdapter extends ArrayAdapter<Bill> {

    private static final String TAG = "BillsArrayAdapter";

    private Context context;
    private int resource;


    public BillsArrayAdapter(Context context, int resource, List<Bill> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String businessName = getItem(position).getSender();
        Log.d(TAG, "getView: " + businessName);
        Double amount = getItem(position).getAmount();
        Log.d(TAG, "getView: " + amount);

        Bill bill = new Bill(businessName, amount);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView tvBusinessName = convertView.findViewById(R.id.textView1);
        TextView tvAmount = convertView.findViewById(R.id.textView2);

        tvBusinessName.setText(businessName);
        tvAmount.setText(String.format("%.2f", amount));

        return convertView;
    }
}
