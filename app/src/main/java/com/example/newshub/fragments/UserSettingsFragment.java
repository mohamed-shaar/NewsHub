package com.example.newshub.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newshub.R;
import com.example.newshub.firebase.Firestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.newshub.firebase.Firestore.SHARED_PREFS;
import static com.example.newshub.firebase.Firestore.USERNAME;
import static com.example.newshub.firebase.Firestore.collectionName;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingsFragment extends Fragment {

    private TextView tv_log_out;
    private TextView tv_clear_backup;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private FirebaseFirestore firebaseFirestore;

    private String name;

    public UserSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        preferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = preferences.edit();

        tv_log_out = view.findViewById(R.id.tv_log_out);
        tv_clear_backup = view.findViewById(R.id.tv_clear_backup);

        name = preferences.getString(USERNAME, "");

        firebaseFirestore = Firestore.getFirestore();

        tv_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().apply();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
            }
        });

        tv_clear_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("links", FieldValue.delete());

                firebaseFirestore
                        .collection(collectionName)
                        .document(name)
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), getContext().getString(R.string.online_backup_deleted), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

}
