package com.chennaka.anusha.movies.views;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anusha on 9/17/2017.
 */

public class Movie implements Parcelable{

    private String title;
    private String posterPath;
    private String movieId;
    private String plot;
    private String rating;
    private String releaseDate;

    public Movie(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;

    }

    public void setPosterPath(String posterPath) {
        this.posterPath = (String) posterPath.subSequence(1,posterPath.length());
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {title,posterPath,movieId,plot,rating,releaseDate});

    }

    private Movie(Parcel in){

        String[] valuesObtained = in.createStringArray();

        this.title = valuesObtained[0];
        this.posterPath = valuesObtained[1];
        this.movieId = valuesObtained[2];
        this.plot = valuesObtained[3];
        this.rating = valuesObtained[4];
        this.releaseDate = valuesObtained[5];
    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
