package com.example.newshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newshub.DetailsActivity;
import com.example.newshub.R;
import com.example.newshub.model.Article;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private ArrayList<Article> articles = new ArrayList<>();
    private Context context;

    public NewsAdapter(ArrayList<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, final int position) {
        String title = articles.get(position).getTitle();
        String date = articles.get(position).getPublishedAt();
        String imageUrl = String.valueOf(articles.get(position).getUrlToImage());

        holder.tv_news_title.setText(title);
        holder.tv_news_date.setText(date);

        /*if (imageUrl.equals("null")) {
            Picasso.get().load(R.drawable.ic_launcher_background).fit().centerCrop().into(holder.iv_news);
        } else {
            if (position < articles.size()-1){
                Picasso.get().load(imageUrl).fit().centerCrop().placeholder(R.drawable.ic_launcher_background).into(holder.iv_news);
            }
        }*/
        if (TextUtils.isEmpty(imageUrl)){
            Picasso.get().load(R.drawable.ic_launcher_background).fit().centerCrop().into(holder.iv_news);
        }
        else {
            Picasso.get().load(imageUrl).fit().centerCrop().placeholder(R.drawable.ic_launcher_background).into(holder.iv_news);
        }

        holder.cv_item_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = articles.get(position);
                String source = article.getSource().getName();

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(context.getString(R.string.article_object), article);
                intent.putExtra(context.getString(R.string.source_name), source);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articles == null) {
            return 0;
        } else return articles.size();
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_news_title;
        public TextView tv_news_date;
        public ImageView iv_news;
        public MaterialCardView cv_item_news;

        public NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_news_title = itemView.findViewById(R.id.tv_news_title);
            tv_news_date = itemView.findViewById(R.id.tv_news_date);
            iv_news = itemView.findViewById(R.id.iv_news);
            cv_item_news = itemView.findViewById(R.id.cv_item_news);
        }
    }
}
