package io.github.kelvao.movies.utils;

public class Constants {

    public static final String BASE_URL = Params.BASE_URL;
    public static final String ID = Params.ID;
    public static final String SEARCH = Params.SEARCH;
    public static final String TYPE = Params.TYPE;
    public static final String PAGE = Params.PAGE;
    public static final String API_KEY = Params.API_KEY;

    private class Params {
        private static final String BASE_URL = "http://www.omdbapi.com/";
        private static final String ID = "i";
        private static final String SEARCH = "s";
        private static final String TYPE = "type";
        private static final String PAGE = "page";
        private static final String API_KEY = "apikey";
    }
}
