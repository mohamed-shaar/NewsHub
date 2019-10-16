package com.example.newshub.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = NewsItem.class, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    private static NewsDatabase instance;

    public abstract NewsDao newsDao();

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "news_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDb(instance).execute();
        }
    };

    private static class PopulateDb extends AsyncTask<Void, Void, Void>{
        private NewsDao newsDao;

        private PopulateDb(NewsDatabase db){this.newsDao = db.newsDao();}
        @Override
        protected Void doInBackground(Void... voids) {
            newsDao.insert(new NewsItem("url-1", "title-1"));
            newsDao.insert(new NewsItem("url-2", "title-2"));
            return null;
        }
    }
}
