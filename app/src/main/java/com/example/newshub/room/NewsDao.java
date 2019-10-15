package com.example.newshub.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NewsDao {
    @Insert
    void insert(NewsItem newsItem);

    @Delete
    void delete(NewsItem newsItem);

    @Query("SELECT * FROM news_table WHERE url = :linkurl")
    NewsItem queryByUrl(String linkurl);

    @Query("DELETE FROM  news_table")
    void deleteAll();

    @Query("SELECT * FROM news_table")
    LiveData<List<NewsItem>> getAllNewsItems();
}
