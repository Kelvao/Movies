package io.github.kelvao.movies.models;

import android.net.Uri;

public class MovieListModel {
    private String title;
    private String year;
    private String imdbID;
    private String poster;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public Uri getPoster() {
        return Uri.parse(poster);
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
