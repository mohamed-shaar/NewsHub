package com.example.newshub.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newshub.R;
import com.example.newshub.room.NewsItem;
import com.example.newshub.room.NewsViewModel;
import com.example.newshub.utils.NetworkAvailability;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{

    private Context context;
    private ArrayList<NewsItem> newsItems;
    private NewsViewModel newsViewModel;
    private NewsItem newsItem;
    //private OnItemClickListener mListener;

    /*public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }*/

    public FavoritesAdapter(Context context, ArrayList<NewsItem> newsItems) {
        this.context = context;
        this.newsItems = newsItems;
        //this.newsViewModel = newsViewModel;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites, parent, false);
        newsViewModel = ViewModelProviders.of((FragmentActivity) context).get(NewsViewModel.class);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, final int position) {
        newsItem = newsItems.get(position);
        String title = newsItem.getTitle();
        String url = newsItem.getUrl();

        holder.tv_favorites_title.setText(title);
        holder.tv_favorites_link.setText(url);

        holder.iv_favorites_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkAvailability.isNetworkAvailable(context)){
                    //newsViewModel.delete(newsItem);
                    //notifyDataSetChanged();
                    if (newsItem == null){
                        Log.d("Item", "is null");
                    }
                    else{
                        Log.d("Item url", newsItem.getUrl());
                        newsViewModel.delete(newsItems.get(position));
                    }
                }
                else {
                    Toast.makeText(context, context.getString(R.string.no_network_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsItems == null ? 0 : newsItems.size();
    }

    public void setNewsItems(ArrayList<NewsItem> newsItems){
        this.newsItems = newsItems;
        notifyDataSetChanged();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_favorites_title;
        public TextView tv_favorites_link;
        public ImageView iv_favorites_delete;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_favorites_title = itemView.findViewById(R.id.tv_favorites_title);
            tv_favorites_link = itemView.findViewById(R.id.tv_favorites_link);
            iv_favorites_delete = itemView.findViewById(R.id.iv_favorites_delete);

            /*iv_favorites_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });*/
        }
    }
}
