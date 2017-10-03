package com.chennaka.anusha.movies.utilities;


import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {


    /**
     * Builds the URL used to query GitHub.
     *
     * @return The URL to use to query the GitHub.
     */
    public static URL buildMoviesUrl(String moviesURL, String api_key, String language, String page) {
        Uri builtUri = Uri.parse(moviesURL).buildUpon()
                .appendQueryParameter("api_key", api_key)
                .appendQueryParameter("language", language)
                .appendQueryParameter("page",page)

                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }catch (FileNotFoundException e) {
            Log.e("SyncTask","File Not Found");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return null;
    }
}