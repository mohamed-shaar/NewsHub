package com.example.newshub.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newshub.R;
import com.example.newshub.firebase.Analytics;
import com.example.newshub.firebase.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.newshub.firebase.Firestore.SHARED_PREFS;
import static com.example.newshub.firebase.Firestore.USERNAME;
import static com.example.newshub.firebase.Firestore.collectionName;
import static com.example.newshub.firebase.Firestore.passwordFieldName;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends Fragment {

    private EditText et_create_username;
    private EditText et_create_password;
    private EditText et_create_confirm_password;
    private Button btn_create_account;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private String username;
    private String password;
    private String confirm_password;

    private boolean usernameTaken  = false;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAnalytics firebaseAnalytics;


    public CreateAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        et_create_username = view.findViewById(R.id.et_create_username);
        et_create_password = view.findViewById(R.id.et_create_password);
        et_create_confirm_password = view.findViewById(R.id.et_create_confirm_password);
        btn_create_account = view.findViewById(R.id.btn_create_account);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseFirestore = Firestore.getFirestore();
        firebaseAnalytics = Analytics.getAnalytics();


        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractData();
                if (checkEmpty()){
                    if (confirmPassword()){
                        if (checkUsernameExist()){
                            Map<String, Object> passwordPair = new HashMap<>();
                            passwordPair.put(passwordFieldName, password);
                            firebaseFirestore
                                    .collection(collectionName)
                                    .document(username)
                            .set(passwordPair).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), getContext().getString(R.string.account_created), Toast.LENGTH_SHORT).show();
                                    editor.putString(USERNAME, username);
                                    editor.putString(passwordFieldName, password);
                                    editor.apply();

                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.METHOD, username);
                                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserSettingsFragment()).commit();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(), getContext().getString(R.string.username_taken), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        return view;
    }

    private void extractData(){
        username = et_create_username.getEditableText().toString();
        password = et_create_password.getEditableText().toString();
        confirm_password = et_create_confirm_password.getEditableText().toString();
    }

    private boolean checkEmpty(){

        if (TextUtils.isEmpty(username)){
            Toast.makeText(getContext(), "Please enter a username.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), "Please Enter a password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(confirm_password)){
            Toast.makeText(getContext(), "Please enter a confirmed password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean confirmPassword(){
        return password.equals(confirm_password);
    }

    private boolean checkUsernameExist(){
        firebaseFirestore.collection(collectionName)
                .whereEqualTo(username, true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("Username ", "found");
                        usernameTaken = true;
                    }
                });
        return usernameTaken;
    }

}
