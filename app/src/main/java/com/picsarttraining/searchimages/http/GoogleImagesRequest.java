package com.picsarttraining.searchimages.http;

/**
 * Created by Arsen on 04.04.2016.
 */
public class GoogleImagesRequest {

    private final static String REQUEST_URL = "https://www.googleapis.com/customsearch/v1";
    private final static String API_KEY = "AIzaSyATCj77g2EDTPfHO3FGRa2TdNo4s_GFxv4";
    private final static String CX = "017023422004467372455:9u9vphjxkk0";
    private final static String ALT = "json";
    private final static String FILE_TYPE = "jpg";
    private final static String SEARCH_TYPE = "image";
    private final static String IMAGE_SIZE = "large";
    private final static int RESULTS_PER_QUERY = 10;

    public static String  getUrlFor(String query, int page) {
        StringBuilder sb = new StringBuilder();
        query = query.replaceAll(" ", "+").toLowerCase();
        String start = page!=0?"&start=" + (page * RESULTS_PER_QUERY+1):"";

        sb.append(REQUEST_URL);
        sb.append("?key=" + API_KEY);
        sb.append("&cx=" + CX);
        sb.append("&q=" + query);
        sb.append("&alt=" + ALT);
        sb.append("&imgSize=" + IMAGE_SIZE);
        sb.append("&fileType=" + FILE_TYPE);
        sb.append("&searchType=" + SEARCH_TYPE);
        sb.append(start);
        return sb.toString();
    }
}