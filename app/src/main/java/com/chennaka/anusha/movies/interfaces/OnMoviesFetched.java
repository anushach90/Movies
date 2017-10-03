package com.chennaka.anusha.movies.interfaces;

import com.chennaka.anusha.movies.views.Movie;

import java.util.List;

/**
 * Created by Anusha on 9/22/2017.
 */

public interface OnMoviesFetched {
    void ReturnMovies(List<Movie> movies);
}
