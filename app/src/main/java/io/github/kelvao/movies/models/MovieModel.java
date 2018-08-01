package io.github.kelvao.movies.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;

class Rating {
    private String source;
    private String value;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

public class MovieModel {
    private String Title;
    private String Released;
    private String Rated;
    private String Genre;
    private String Director;
    private String Writer;
    private String Plot;
    private String Poster;
    private ArrayList<Rating> Ratings;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public ArrayList<String> getGenre() {
        return new ArrayList<>(Arrays.asList(Genre.replace(" ", "").toUpperCase().split(",")));
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public Uri getPoster() {
        return Uri.parse(Poster.replace("SX300", "SX600"));
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public ArrayList<Rating> getRatings() {
        return Ratings;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        Ratings = ratings;
    }
}
