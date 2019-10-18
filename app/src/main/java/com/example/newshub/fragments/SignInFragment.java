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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.newshub.firebase.Firestore.SHARED_PREFS;
import static com.example.newshub.firebase.Firestore.USERNAME;
import static com.example.newshub.firebase.Firestore.collectionName;
import static com.example.newshub.firebase.Firestore.passwordFieldName;

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

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAnalytics firebaseAnalytics;


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

        preferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();

        firebaseFirestore = Firestore.getFirestore();
        firebaseAnalytics = Analytics.getAnalytics();

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_username.getEditableText().toString())) {
                    username = et_username.getEditableText().toString();
                    //Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
                    username_present = true;

                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show();
                }

                if (!TextUtils.isEmpty(et_password.getEditableText().toString())){
                    password = et_password.getEditableText().toString();
                    password_present = true;
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                }

                if (username_present && password_present){
                    firebaseFirestore
                            .collection(collectionName)
                            .document(username)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String queryPassword = documentSnapshot.getString(passwordFieldName);
                                        if (password.equals(queryPassword)){
                                            editor.putString(USERNAME, username);
                                            editor.apply();
                                            editor.putString(passwordFieldName, password);
                                            editor.apply();
                                            Bundle bundle = new Bundle();
                                            bundle.putString(FirebaseAnalytics.Param.METHOD, username);
                                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserSettingsFragment()).commit();
                                        }
                                        else {
                                            Toast.makeText(getContext(), getContext().getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), getContext().getString(R.string.failure), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return view;
    }



}
