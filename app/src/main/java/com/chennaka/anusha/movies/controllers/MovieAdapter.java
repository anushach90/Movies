package com.chennaka.anusha.movies.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chennaka.anusha.movies.R;
import com.chennaka.anusha.movies.views.Movie;
import com.chennaka.anusha.movies.interfaces.OnItemClickListener;

import java.util.List;

/**
 * Created by Anusha on 9/16/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies;
    private final Context mContext;
    private OnItemClickListener onItemClickListener;

    public MovieAdapter(Context context,List<Movie> movies){
        this.movies = movies;
        this.mContext = context;
    }

    public List<Movie> getMovies(){
        return movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (null != movies ? movies.size() : 0);
    }

    //This is used to hold the views for each item
    public class MovieViewHolder extends RecyclerView.ViewHolder{

        final ImageView imgView;

        public MovieViewHolder(View itemView){
            super(itemView);
            this.imgView = (ImageView)itemView.findViewById(R.id.movie_image);
        }

        void bind(int listIndex) {

            final Movie movieItem = movies.get(listIndex);

            Glide.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185/"+ movieItem.getPosterPath())
                    .into(imgView);


            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(movieItem);
                }
            };
            imgView.setOnClickListener(listener);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
