package com.example.newshub;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newshub.api.Client;
import com.example.newshub.api.NewsTitleApi;
import com.example.newshub.model.RequestInformation;
import com.example.newshub.utils.NetworkAvailability;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String category = "technology";
    private String apiKey = BuildConfig.News_Api_Key;
    private NewsTitleApi newsTitleApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsTitleApi = Client.getRetrofit().create(NewsTitleApi.class);

        if (NetworkAvailability.isNetworkAvailable(MainActivity.this)){
            Log.d("Network", "is available");
        }
        else {
            Log.d("Network", "is not available");
        }

        getResults();

    }

    private void getResults(){
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
    }


}
