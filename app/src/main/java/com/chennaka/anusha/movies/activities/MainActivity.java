package com.chennaka.anusha.movies.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chennaka.anusha.movies.interfaces.OnMoviesFetched;
import com.chennaka.anusha.movies.views.Movie;
import com.chennaka.anusha.movies.controllers.MovieAdapter;
import com.chennaka.anusha.movies.R;
import com.chennaka.anusha.movies.interfaces.OnItemClickListener;
import com.chennaka.anusha.movies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesRView;
    private MovieAdapter popTopAdapter;
    private List<Movie> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesRView = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLay = new GridLayoutManager(this, 2);
        moviesRView.setLayoutManager(gridLay);

        if (savedInstanceState == null || !savedInstanceState.containsKey("MOVIES-LAYOUT")) {
            getMovies("popular");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("MOVIES-LAYOUT", moviesRView.getLayoutManager().onSaveInstanceState());
        if(popTopAdapter!=null) {
            moviesList = popTopAdapter.getMovies();
            for (int i = 1; i <= moviesList.size(); i++) {
                outState.putParcelable("MOVIES-LIST-" + i, popTopAdapter.getMovies().get(i - 1));
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("MOVIES-LAYOUT");

            List<Movie> listMovies = new ArrayList<>();
            for(int i=1;;i++){
                if(savedInstanceState.getParcelable("MOVIES-LIST-" + i) == null) {
                    break;
                }
                Movie m1= (savedInstanceState.getParcelable("MOVIES-LIST-" + i));
                listMovies.add(m1);
            }
            popTopAdapter = new MovieAdapter(MainActivity.this, listMovies);
            moviesRView.setAdapter(popTopAdapter);
            moviesRView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            popTopAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(Movie movieItem) {
                    Toast.makeText(MainActivity.this, movieItem.getTitle(), Toast.LENGTH_LONG).show();
                    Intent detailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                    detailsIntent.putExtra("movie", movieItem);
                    startActivity(detailsIntent);
                }
            });
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void getMovies(final String sortType) {
        if (isOnline()) {

            OnMoviesFetched moviesFetched = new OnMoviesFetched() {
                @Override
                public void ReturnMovies(List<Movie> moviesReturned) {

                    if(moviesReturned==null){
                        Toast.makeText(MainActivity.this, "Error fetching the Movies from the source", Toast.LENGTH_LONG).show();
                    }
                    else if(moviesReturned.size()>0){
                        //moviesRView.setLayoutManager(gridLay);
                        popTopAdapter = new MovieAdapter(MainActivity.this, moviesReturned);
                        moviesRView.setAdapter(popTopAdapter);
                        popTopAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Movie movieItem) {
                                Toast.makeText(MainActivity.this, movieItem.getTitle(), Toast.LENGTH_LONG).show();
                                Intent detailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                                detailsIntent.putExtra("movie", movieItem);
                                startActivity(detailsIntent);
                            }
                        });

                    }
                }
            };

            try {
                URL buildUrl = NetworkUtils.buildMoviesUrl(this.getString(R.string.popular_movie_url),
                                                           this.getString(R.string.api_key),
                                                           this.getString(R.string.language),
                                                           this.getString(R.string.page));

                if (sortType.compareTo("top_rated") == 0) {
                    buildUrl =  NetworkUtils.buildMoviesUrl(this.getString(R.string.rated_movie_url),
                                                            this.getString(R.string.api_key),
                                                            this.getString(R.string.language),
                                                            this.getString(R.string.page));
                }

                new QueryTask(moviesFetched).execute(buildUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else
            Toast.makeText(this,"No internet connection",Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_popular) {
            getMovies("popular");
            return true;
        }
        else if (itemThatWasClickedId == R.id.action_top_rated) {
            getMovies("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public class QueryTask extends AsyncTask<URL, Void, List<Movie>> {

        final OnMoviesFetched mListener;

        public QueryTask(OnMoviesFetched listener) {
            super();
            mListener = listener;
        }

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected List<Movie> doInBackground(URL... params) {


            URL searchUrl = params[0];
            List<Movie> obtainedMovies = null;
            String queryResults = null;
            try {
                queryResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                if (queryResults != null && !queryResults.equals("")) {
                    obtainedMovies = parseResult(queryResults);
                }
                return obtainedMovies;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(queryResults);
            return obtainedMovies;
        }

        // COMPLETED (3) Override onPostExecute
        @Override
        protected void onPostExecute(List<Movie> obtainedMovies) {
            mListener.ReturnMovies(obtainedMovies);
        }

        private List<Movie> parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray results = response.optJSONArray("results");
                moviesList = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieItem = results.optJSONObject(i);

                      Movie item = new Movie();
                      item.setTitle(movieItem.optString("original_title"));
                      item.setPosterPath(movieItem.optString("poster_path"));
                      item.setMovieId(movieItem.optString("id"));
                      item.setPlot(movieItem.optString("overview"));
                      item.setRating(movieItem.optString("vote_average"));
                      item.setReleaseDate(movieItem.optString("release_date"));
                      moviesList.add(item);
                }

                return moviesList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviesList;
        }
    }

}
