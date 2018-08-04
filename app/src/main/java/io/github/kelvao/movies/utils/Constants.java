package io.github.kelvao.movies.utils;

public class Constants {

    public static final String BASE_URL = Params.BASE_URL;
    private static final String IMDB_ID = "imdbID";
    private static final String PARAM_ID = Params.ID;
    private static final String PARAM_SEARCH = Params.SEARCH;
    private static final String PARAM_TYPE = Params.TYPE;
    private static final String PARAM_PLOT = Params.PLOT;
    private static final String PARAM_PAGE = Params.PAGE;
    private static final String PARAM_API_KEY = Params.API_KEY;
    private static final String API_KEY = "45d162c8";
    private static final String QUERY = "query";
    public static final String MOVIE_FRAGMENT = "movie_fragment";
    private static final String IMAGE_OLD_SIZE = Image.OLD_SIZE;
    private static final String IMAGE_NEW_SIZE = Image.NEW_SIZE;
    private static final String FIRST_RUN = "fistrun";

    public static String getImdbId() {
        return IMDB_ID;
    }

    public static String getParamId() {
        return PARAM_ID;
    }

    public static String getParamSearch() {
        return PARAM_SEARCH;
    }

    public static String getParamType() {
        return PARAM_TYPE;
    }

    public static String getParamPlot() {
        return PARAM_PLOT;
    }

    public static String getParamPage() {
        return PARAM_PAGE;
    }

    public static String getParamApiKey() {
        return PARAM_API_KEY;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getQuery() {
        return QUERY;
    }

    public static String getImageOldSize() {
        return IMAGE_OLD_SIZE;
    }

    public static String getImageNewSize() {
        return IMAGE_NEW_SIZE;
    }

    public static String getFirstRun() {
        return FIRST_RUN;
    }

    private class Params {
        private static final String BASE_URL = "http://www.omdbapi.com/";
        private static final String ID = "i";
        private static final String SEARCH = "s";
        private static final String TYPE = "type";
        private static final String PLOT = "plot";
        private static final String PAGE = "page";
        private static final String API_KEY = "apikey";
    }

    private class Image {
        private static final String OLD_SIZE = "SX300";
        private static final String NEW_SIZE = "SX600";
    }
}
