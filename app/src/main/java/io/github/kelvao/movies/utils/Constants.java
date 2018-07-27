package io.github.kelvao.movies.utils;

public class Constants {

    public static final String BASE_URL = Params.BASE_URL;
    private static final String PARAM_ID = Params.ID;
    private static final String PARAM_SEARCH = Params.SEARCH;
    private static final String PARAM_TYPE = Params.TYPE;
    private static final String PARAM_PAGE = Params.PAGE;
    private static final String PARAM_API_KEY = Params.API_KEY;
    private static final String API_KEY = "45d162c8";

    public static String getParamId() {
        return PARAM_ID;
    }

    public static String getParamSearch() {
        return PARAM_SEARCH;
    }

    public static String getParamType() {
        return PARAM_TYPE;
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

    private class Params {
        private static final String BASE_URL = "http://www.omdbapi.com/";
        private static final String ID = "i";
        private static final String SEARCH = "s";
        private static final String TYPE = "type";
        private static final String PAGE = "page";
        private static final String API_KEY = "apikey";
    }
}
