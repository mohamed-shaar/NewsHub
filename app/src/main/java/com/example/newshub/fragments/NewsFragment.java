package com.example.newshub.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.newshub.BuildConfig;
import com.example.newshub.R;
import com.example.newshub.adapter.NewsAdapter;
import com.example.newshub.api.Client;
import com.example.newshub.api.NewsTitleApi;
import com.example.newshub.model.Article;
import com.example.newshub.model.RequestInformation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private String category;
    private String apiKey = BuildConfig.News_Api_Key;
    private NewsTitleApi newsTitleApi;
    private ArrayList<Article> articles = new ArrayList<>();
    private Context context;
    private SwipeRefreshLayout refreshLayout;
    private NewsAdapter newsAdapter;

    public NewsFragment(){
        newsTitleApi = Client.getRetrofit().create(NewsTitleApi.class);
    }


    public NewsFragment(Context context) {
        // Required empty public constructor
        newsTitleApi = Client.getRetrofit().create(NewsTitleApi.class);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(false);
        newsAdapter = new NewsAdapter(articles, context);
        recyclerView.setAdapter(newsAdapter);

        refreshLayout = view.findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(
                android.R.color.holo_blue_dark)
                , getResources().getColor(android.R.color.holo_blue_light)
                , getResources().getColor(android.R.color.holo_green_light)
                , getResources().getColor(android.R.color.holo_green_light));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getResults();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        category = text;
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        getResults();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    articles.clear();
                    RequestInformation requestInformation = response.body();
                    Log.d("Total", String.valueOf(requestInformation.getTotalResults()));
                    for (Article article: requestInformation.getArticles()){
                        articles.add(article);
                        Log.d("Article", article.getTitle());
                        Log.d("Article image", String.valueOf(article.getUrlToImage()));
                    }
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RequestInformation> call, Throwable t) {
                Log.d("Failure in request: ", t.getMessage());
            }
        });
    }
}
