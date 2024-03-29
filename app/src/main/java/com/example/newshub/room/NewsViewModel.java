package com.example.newshub.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepository newsRepository;
    private LiveData<List<NewsItem>> allNewsItems;
    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsRepository = new NewsRepository(application);
        allNewsItems = newsRepository.getAllNewsItems();
    }

    public void insert(NewsItem newsItem) {newsRepository.insert(newsItem);}

    public void delete(NewsItem newsItem) {newsRepository.delete(newsItem);}

    public void deleteAll(){newsRepository.deleteAllNewsItems();}

    public NewsItem queryByUrl(NewsItem newsItem) throws ExecutionException, InterruptedException { return newsRepository.queryByUrl(newsItem);}

    public LiveData<List<NewsItem>> getAllNewsItems() {return allNewsItems;}
}
