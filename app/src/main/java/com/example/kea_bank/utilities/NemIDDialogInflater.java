package com.example.kea_bank.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kea_bank.R;
import com.example.kea_bank.activities.MainActivity;
import com.example.kea_bank.domain.users.User;
import com.example.kea_bank.services.UserService;

import java.util.Arrays;
import java.util.Random;

import static android.content.ContentValues.TAG;

//public class NemIDDialogInflater extends DialogFragment {
//
//    public interface OnInputListener{
//        void sendInput(User user);
//    }
//
//
//    public OnInputListener dOnInputListener;
//    private TextView key, dCancel, dActionOk;
//    private EditText nemIdInput;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.verify_nemid, container, false);
//        key = view.findViewById(R.id.keyText);
//        nemIdInput = view.findViewById(R.id.nemIdInput);
//        dCancel = view.findViewById(R.id.dCancel);
//        dActionOk = view.findViewById(R.id.dActionOk);
//
//        dActionOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dOnInputListener.sendInput();
//
//
//                getDialog().dismiss();
//
//            }
//        });
//
//        dCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            dOnInputListener = (OnInputListener) getActivity();
//        }catch (ClassCastException e){
//            Log.e(TAG, "onAttach: ClassCastException", e);
//        }
//    }
//}
