package com.example.newshub.fragments;


import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.newshub.firebase.Firestore;
import com.example.newshub.room.NewsItem;
import com.example.newshub.room.NewsViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.newshub.firebase.Firestore.SHARED_PREFS;
import static com.example.newshub.firebase.Firestore.USERNAME;
import static com.example.newshub.firebase.Firestore.collectionName;
import static com.example.newshub.firebase.Firestore.passwordFieldName;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment{

    private NewsViewModel newsViewModel;
    private Context context;
    private ArrayList<NewsItem> newsItemArrayList;
    private FavoritesAdapter favoritesAdapter;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String password;
    private String username;
    private List<String> links;

    private boolean loggedIn;

    private FirebaseFirestore firebaseFirestore;


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

        preferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();

        password = preferences.getString(passwordFieldName, "");
        username = preferences.getString(USERNAME, "");

        if (!password.equals("")){
            loggedIn = true;
            firebaseFirestore = Firestore.getFirestore();
        }
        else {
            loggedIn = false;
        }

        newsViewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);

        newsViewModel.getAllNewsItems().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                //favoritesAdapter.setNewsItems((ArrayList<NewsItem>) newsItems);
                newsItemArrayList.clear();
                links = new ArrayList<>();
                for (NewsItem item: newsItems){
                    Log.d("Item", item.getUrl());
                    newsItemArrayList.add(item);
                    if (loggedIn){
                        links.add(item.getUrl());
                    }
                }
                favoritesAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (loggedIn){
            Item item = new Item(password, links);
            firebaseFirestore
                    .collection(collectionName)
                    .document(username)
                    .set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (getContext() != null){
                                Toast.makeText(context, getContext().getString(R.string.backup_updated), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public class Item{
        private String password;
        private List<String> links;

        public Item(String password, List<String> links) {
            this.password = password;
            this.links = links;
        }

        public Item(List<String> links) {
            this.links = links;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<String> getLinks() {
            return links;
        }

        public void setLinks(List<String> links) {
            this.links = links;
        }
    }
}
