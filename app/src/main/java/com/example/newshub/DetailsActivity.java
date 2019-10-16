package com.example.newshub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newshub.model.Article;
import com.example.newshub.room.NewsItem;
import com.example.newshub.room.NewsViewModel;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class DetailsActivity extends AppCompatActivity {

    private ImageView iv_details_news;
    private TextView tv_details_source;
    private TextView tv_details_content;
    private TextView tv_details_link;
    private ImageView iv_favorite;

    private NewsViewModel newsViewModel;

    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();

        final Article article = intent.getParcelableExtra(getString(R.string.article_object));
        String source = intent.getStringExtra(getString(R.string.source_name));

        Log.d("Details", article.getTitle());
        Log.d("Details", source);

        setTitle(article.getTitle());
        iv_details_news = findViewById(R.id.iv_details_news);

        if (TextUtils.isEmpty(article.getUrlToImage())){
            Log.d("Details", "Empty Image");
            Picasso.get().load(R.drawable.ic_launcher_foreground).fit().centerCrop().into(iv_details_news);
        }
        else {
            Log.d("Details", "Link to image");
            Picasso.get().load(article.getUrlToImage()).fit().centerCrop().into(iv_details_news);
        }

        tv_details_source = findViewById(R.id.tv_details_source);
        tv_details_source.setText(source);

        tv_details_content = findViewById(R.id.tv_details_content);
        tv_details_content.setText(article.getContent());

        tv_details_link = findViewById(R.id.tv_details_link);
        tv_details_link.setText(article.getUrl());

        tv_details_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(article.getUrl());
                Intent intent1 = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent1.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent1);
                }
            }
        });

        iv_favorite = findViewById(R.id.iv_favorite);

        final NewsItem newsItem = new NewsItem(article.getUrl(), article.getTitle());

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        try {
            NewsItem queryItem = newsViewModel.queryByUrl(newsItem);
            if (queryItem != null){
                Log.d("New Item", "exists");
                //Picasso.get().load(R.drawable.ic_favorite_black_24dp).fit().into(iv_favorite);
                iv_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                state = true;
            }
            else {
                Log.d("News Item", "Does not exist");
                //Picasso.get().load(R.drawable.ic_favorite_border_black_24dp).fit().into(iv_favorite);
                iv_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                state = false;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!state){
                    newsViewModel.insert(newsItem);
                    state = true;
                    //Picasso.get().load(R.drawable.ic_favorite_black_24dp).fit().into(iv_favorite);
                    iv_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                else {
                    newsViewModel.delete(newsItem);
                    state = false;
                    //Picasso.get().load(R.drawable.ic_favorite_border_black_24dp).fit().into(iv_favorite);
                    iv_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });

    }
}
