package com.example.newshub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.newshub.firebase.Analytics;
import com.example.newshub.fragments.AccountFragment;
import com.example.newshub.fragments.FavoritesFragment;
import com.example.newshub.fragments.NewsFragment;
import com.example.newshub.utils.NetworkAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.example.newshub.firebase.Firestore.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {

    /*private String category = "technology";
    private String apiKey = BuildConfig.News_Api_Key;
    private NewsTitleApi newsTitleApi;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAnalytics analytics = Analytics.setUpAnalytics(this);
        analytics.logEvent("APP_Open", new Bundle());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if (getIntent().getAction().equals(getString(R.string.open_favorites))){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoritesFragment(this)).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment(this)).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selected = null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_news:
                            selected = new NewsFragment(MainActivity.this);
                            NetworkCheck();
                            break;
                        case R.id.nav_favorites:
                            selected = new FavoritesFragment(MainActivity.this);
                            NetworkCheck();
                            break;
                        case R.id.nav_account:
                            selected = new AccountFragment();
                            NetworkCheck();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                    return true;
                }
            };

    private void NetworkCheck(){
        if (!NetworkAvailability.isNetworkAvailable(MainActivity.this)){
            Toast.makeText(this, "Network is unavailable.", Toast.LENGTH_SHORT).show();
        }
    }
}
