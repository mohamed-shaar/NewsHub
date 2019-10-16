package com.example.newshub.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newshub.R;
import com.example.newshub.adapter.FavoritesAdapter;
import com.example.newshub.room.NewsItem;
import com.example.newshub.room.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment{

    private NewsViewModel newsViewModel;
    private Context context;
    private ArrayList<NewsItem> newsItemArrayList;
    private FavoritesAdapter favoritesAdapter;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    public FavoritesFragment(Context context) {
        this.context = context;
        newsItemArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView rv_favorites = view.findViewById(R.id.rv_favorites);
        rv_favorites.setLayoutManager(new LinearLayoutManager(context));
        rv_favorites.setHasFixedSize(false);


        favoritesAdapter = new FavoritesAdapter(context, newsItemArrayList);

        rv_favorites.setAdapter(favoritesAdapter);

        newsViewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);

        newsViewModel.getAllNewsItems().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                //favoritesAdapter.setNewsItems((ArrayList<NewsItem>) newsItems);
                newsItemArrayList.clear();
                for (NewsItem item: newsItems){
                    Log.d("Item", item.getUrl());
                    newsItemArrayList.add(item);
                }
                favoritesAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    /*@Override
    public void onItemClick(int position) {
        Toast.makeText(context, newsItemArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        newsViewModel.delete(newsItemArrayList.get(position));
    }*/
}
