package com.example.newshub.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsRepository {

    private NewsDao newsDao;
    private LiveData<List<NewsItem>> allNewsItems;

    public NewsRepository(Application application) {
        NewsDatabase newsDatabase = NewsDatabase.getInstance(application);
        newsDao = newsDatabase.newsDao();
        allNewsItems = newsDao.getAllNewsItems();
    }

    public void insert(NewsItem newsItem) {
        new InsertNewsItem(newsDao).execute(newsItem);
    }

    public NewsItem queryByUrl(NewsItem newsItem) throws ExecutionException, InterruptedException {
        return new QueryByUrl(newsDao).execute(newsItem).get();
    }

    public void delete(NewsItem newsItem) {
        new DeleteNewsItem(newsDao).execute(newsItem);
    }

    public void deleteAllNewsItems() {
        new DeleteAllNewsItem(newsDao).execute();
    }

    public LiveData<List<NewsItem>> getAllNewsItems() {
        return allNewsItems;
    }

    private static class InsertNewsItem extends AsyncTask<NewsItem, Void, Void> {
        private NewsDao newsDao;

        private InsertNewsItem(NewsDao newsDao) {
            this.newsDao = newsDao;
        }

        @Override
        protected Void doInBackground(NewsItem... newsItems) {
            newsDao.insert(newsItems[0]);
            return null;
        }
    }

    private static class DeleteNewsItem extends AsyncTask<NewsItem, Void, Void> {
        private NewsDao newsDao;

        private DeleteNewsItem(NewsDao newsDao) {
            this.newsDao = newsDao;
        }

        @Override
        protected Void doInBackground(NewsItem... newsItems) {
            newsDao.delete(newsItems[0]);
            return null;
        }
    }

    private static class DeleteAllNewsItem extends AsyncTask<Void, Void, Void> {
        private NewsDao newsDao;

        private DeleteAllNewsItem(NewsDao newsDao) {
            this.newsDao = newsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newsDao.deleteAll();
            return null;
        }
    }

    private static class QueryByUrl extends AsyncTask<NewsItem, Void, NewsItem>{

        private NewsDao newsDao;

        private QueryByUrl(NewsDao newsDao){this.newsDao = newsDao;}

        @Override
        protected NewsItem doInBackground(NewsItem... newsItems) {
            return newsDao.queryByUrl(newsItems[0].getUrl());
        }
    }
}
