package com.example.newshub.firebase;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {
    private static FirebaseAnalytics analytics = null;

    public static FirebaseAnalytics setUpAnalytics(Context context){
        if (analytics == null){
            analytics = FirebaseAnalytics.getInstance(context);
        }
        return analytics;
    }

    public static FirebaseAnalytics getAnalytics() {
        return analytics;
    }
}
