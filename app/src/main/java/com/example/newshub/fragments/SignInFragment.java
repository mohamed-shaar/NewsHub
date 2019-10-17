package com.example.newshub.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newshub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private EditText et_username;
    private EditText et_password;
    private Button btn_sign_in;

    private String username;
    private String password;

    private boolean username_present = false;
    private boolean password_present = false;


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);
        btn_sign_in = view.findViewById(R.id.btn_sign_in);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_username.getEditableText().toString())) {
                    username = et_username.getEditableText().toString();
                    //Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Please enter your username.", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(et_password.getEditableText().toString())){
                    password = et_password.getEditableText().toString();
                } else {
                    Toast.makeText(getContext(), "Please enter your password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
