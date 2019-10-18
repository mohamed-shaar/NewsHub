package com.example.newshub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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

        //newsTitleApi = Client.getRetrofit().create(NewsTitleApi.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment(this)).commit();

        if (NetworkAvailability.isNetworkAvailable(MainActivity.this)){
            Log.d("Network", "is available");
        }
        else {
            Log.d("Network", "is not available");
        }

        //getResults();

    }

    /*private void getResults(){
        Call<RequestInformation> call = newsTitleApi.getNewsTitles(category, apiKey);
        call.enqueue(new Callback<RequestInformation>() {
            @Override
            public void onResponse(Call<RequestInformation> call, Response<RequestInformation> response) {
                if (!response.isSuccessful()) {
                    int code = response.code();
                    Log.d("Code: ", String.valueOf(code));
                    return;
                }
                else {
                    RequestInformation requestInformation = response.body();
                    Log.d("Total", String.valueOf(requestInformation.getTotalResults()));
                }
            }

            @Override
            public void onFailure(Call<RequestInformation> call, Throwable t) {
                Log.d("Failure in request: ", t.getMessage());
            }
        });
    }*/
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selected = null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_news:
                            selected = new NewsFragment(MainActivity.this);
                            break;
                        case R.id.nav_favorites:
                            selected = new FavoritesFragment(MainActivity.this);
                            break;
                        case R.id.nav_account:
                            selected = new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                    return true;
                }
            };

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
        super.onDestroy();
    }
}
