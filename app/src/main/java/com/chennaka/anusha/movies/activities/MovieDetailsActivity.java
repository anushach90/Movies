package com.chennaka.anusha.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chennaka.anusha.movies.views.Movie;
import com.chennaka.anusha.movies.R;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView titleView = (TextView)findViewById(R.id.title);
        TextView plotView = (TextView)findViewById(R.id.plot);
        TextView ratingView = (TextView)findViewById(R.id.rating);
        TextView dateView = (TextView)findViewById(R.id.date);
        ImageView imgView = (ImageView)findViewById(R.id.thumbnail);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movie")) {
            Movie movieItem = intentThatStartedThisActivity.getParcelableExtra("movie");
            if(movieItem!= null){
                 titleView.setText(movieItem.getTitle());
                 plotView.setText(movieItem.getPlot());
                 ratingView.setText(movieItem.getRating());
                 dateView.setText(movieItem.getReleaseDate());
                 Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w185/"+ movieItem.getPosterPath())
                        .into(imgView);
            }

        }
    }

}
