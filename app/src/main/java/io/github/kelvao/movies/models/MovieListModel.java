package io.github.kelvao.movies.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class MovieListModel {

    @SerializedName("Search")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public class Movie {
        private String Title;
        private String Year;
        private String imdbID;
        private String Poster;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String year) {
            Year = year;
        }

        public String getImdbID() {
            return imdbID;
        }

        public void setImdbID(String imdbID) {
            this.imdbID = imdbID;
        }

        public Uri getPoster() {
            return Uri.parse(Poster);
        }

        public void setPoster(String poster) {
            Poster = poster;
        }
    }
}
