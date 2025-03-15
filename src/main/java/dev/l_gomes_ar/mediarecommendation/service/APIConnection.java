package dev.l_gomes_ar.mediarecommendation.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.l_gomes_ar.mediarecommendation.deserializer.GenreListDeserializer;
import dev.l_gomes_ar.mediarecommendation.model.SearchResult;
import dev.l_gomes_ar.mediarecommendation.deserializer.SearchResultDeserializer;
import dev.l_gomes_ar.mediarecommendation.model.GenreList;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIConnection {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String SEARCH_MOVIE_URI = BASE_URL + "search/movie?query=";
    private static final String SEARCH_SERIES_URI = BASE_URL + "search/tv?query=";
    private static final String MOVIE_GENRE_LIST = BASE_URL + "genre/movie/list";
    private static final String SERIES_GENRE_LIST = BASE_URL + "genre/tv/list";
    private static final String MOVIE_RECOMMENDATION_BASE_URL = BASE_URL + "discover/movie?vote_average.gte=7&vote_count.gte=1000";
    private static final String SERIES_RECOMMENDATION_BASE_URL = BASE_URL + "discover/tv?vote_average.gte=7&vote_count.gte=1000";

    // Builds Gson object with custom deserializer
    public final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SearchResult.class, new SearchResultDeserializer())
            .registerTypeAdapter(GenreList.class, new GenreListDeserializer())
            .create();

    // Retrieves list of media (used for the recommendations list)
    public SearchResult getRecommendations(String type, int page) {
        return getRecommendations(type, null, page);
    }

    public SearchResult getRecommendations(String type, String query, int page) {
        String uri = (type.equals("movie")) ? MOVIE_RECOMMENDATION_BASE_URL : SERIES_RECOMMENDATION_BASE_URL;
        HttpResponse<String> response = getResponse(uri + (query != null ? query : "") + "&page=" + page);
        return (response != null) ? gson.fromJson(response.body(), SearchResult.class) : null;
    }

    // Retrieves list of media (used for the search list)
    public SearchResult searchMediaByQuery(String type, String query) {
        String escapedQuery = query.replaceAll("\\s+", "+");
        String uri = (type.equals("movie")) ? SEARCH_MOVIE_URI + escapedQuery : SEARCH_SERIES_URI + escapedQuery;
        HttpResponse<String> response = getResponse(uri);
        return (response != null) ? gson.fromJson(response.body(), SearchResult.class) : null;
    }

    public SearchResult searchMediaByQuery(String type, String query, int page) {
        String escapedQuery = query.replaceAll("\\s+", "+");
        String uri = (type.equals("movie")) ?
                SEARCH_MOVIE_URI + escapedQuery + "&page=" + page :
                SEARCH_SERIES_URI + escapedQuery + "&page=" + page;
        HttpResponse<String> response = getResponse(uri);
        return (response != null)  ? gson.fromJson(response.body(), SearchResult.class) : null;
    }

    // Retrieves genre information (name and id)
    public GenreList getGenreList(String type) {
        HttpResponse<String> response;
        String uri = (type.equals("movie") ? MOVIE_GENRE_LIST : SERIES_GENRE_LIST);
        response = getResponse(uri);
        return (response != null) ? gson.fromJson(response.body(), GenreList.class) : null;
    }

    // Used to make API calls and catches errors
    private HttpResponse<String> getResponse(String uri) {
        HttpRequest httpRequest;
        try {
           httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Authorization", Constant.API_TOKEN)
                    .build();
            HttpResponse<String> response;

            HttpClient httpClient = HttpClient.newHttpClient();
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response;
            } else {
                System.err.println("API Error: Status code " + response.statusCode());
            }
        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid URI format: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        return null;
    }
}
