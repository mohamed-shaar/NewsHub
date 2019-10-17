package com.example.newshub.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore {
    private static FirebaseFirestore firestore = null;
    public static String collectionName = "users";
    public static String passwordFieldName = "password";
    public static String USERNAME = "user";
    public static String SHARED_PREFS = "prefs";

    public static FirebaseFirestore getFirestore(){
        if (firestore == null){
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }
}
