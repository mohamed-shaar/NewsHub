package com.example.newshub.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newshub.R;

import static com.example.newshub.firebase.Firestore.SHARED_PREFS;
import static com.example.newshub.firebase.Firestore.USERNAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        TextView tv_account_sign_in = view.findViewById(R.id.tv_account_sign_in);
        TextView tv_account_create = view.findViewById(R.id.tv_account_create);

        SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String username = preferences.getString(USERNAME, "");
        if (!username.equals("")){
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserSettingsFragment()).commit();
        }

        tv_account_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SignInFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        tv_account_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CreateAccountFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

}
