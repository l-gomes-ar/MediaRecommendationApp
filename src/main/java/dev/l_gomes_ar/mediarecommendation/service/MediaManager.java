package dev.l_gomes_ar.mediarecommendation.service;

import dev.l_gomes_ar.mediarecommendation.model.SearchResult;
import dev.l_gomes_ar.mediarecommendation.model.GenreList;
import dev.l_gomes_ar.mediarecommendation.model.Media;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class MediaManager {
    private final APIConnection apiConnection = new APIConnection();

    public ArrayList<Media> movieLikedList = new ArrayList<>();
    public ArrayList<Media> seriesLikedList = new ArrayList<>();
    public GenreList genreList;

    public SearchResult searchList;
    public SearchResult recommendationList;

    public MediaManager() {
        genreList = apiConnection.getGenreList("movie");
        genreList.genres.putAll(apiConnection.getGenreList("series").genres);
    }

    // Retrieves recommendations
    public void getRecommendations(String type) {
        getRecommendations(type, 1);
    }

    public void getRecommendations(String type, int page) {
        if (movieLikedList.isEmpty() && seriesLikedList.isEmpty()) {
            this.recommendationList = apiConnection.getRecommendations(type, page);
        } else {
            // Define which media list to use
            ArrayList<Media> mediaList = (type.equals("movie")) ? movieLikedList : seriesLikedList;
            String query = buildRecommendationQuery(mediaList);
            this.recommendationList = apiConnection.getRecommendations(type, query, page);
        }

        filterRecommendations(type);
    }

    // filter recommendations - remove watched list
    private void filterRecommendations(String type) {
        ArrayList<Media> comparisonList = (type.equals("movie")) ? movieLikedList : seriesLikedList;

        if (!comparisonList.isEmpty()) {
            // Create a copy of the recommendation list to apply the changes to, using gson
            SearchResult filteredRecommendationList = apiConnection
                    .gson
                    .fromJson(apiConnection.gson.toJson(this.recommendationList), SearchResult.class);

            for (Media mediaItem : recommendationList.results) {
                if (movieLikedList.contains(mediaItem) || seriesLikedList.contains(mediaItem)) {
                    filteredRecommendationList.results.remove(mediaItem);
                    filteredRecommendationList.total_results--;
                }
            }

            // Assign the filtered copy of the recommendation list
            this.recommendationList = filteredRecommendationList;
        }
    }

    public String buildRecommendationQuery(ArrayList<Media> mediaList) {
        StringBuilder query = new StringBuilder();

        // Collect unique combinations of genres with comma and |
        HashSet<String> genreIds = new HashSet<>();
        for (Media item : mediaList) {
            if (item.getGenreId() != null) {
                genreIds.addAll(Arrays.asList(item.getGenreId()));
            }
        }
        if (!genreIds.isEmpty()) {
            // Using "%7C" for | (OR) for searching for more than one genre according to TMdb API
            query.append("&with_genres=").append(String.join("%7C", genreIds));
        }

        // Collect unique original languages
        HashSet<String> languages = new HashSet<>();
        for (Media item : mediaList) {
            if (item.original_language != null && !item.original_language.isBlank()) {
                languages.add(item.original_language);
            }
        }
        if (!languages.isEmpty()) {
            // Using "%7C" for | (OR) for more than one genre according to TMdb API
            query.append("&with_original_language=").append(String.join("%7C", languages));
        }

        return query.toString();
    }

    // functionality for retrieving info (searching) by query
    public void searchMedia(String type, String query) {
        this.searchList = apiConnection.searchMediaByQuery(type, query);
    }

    public void searchMedia(String type, String query, int page) {
        this.searchList = apiConnection.searchMediaByQuery(type, query, page);
    }

    public void addMediaToLikedList(int id, SearchResult list) {
        for (Media mediaItem : list.results) {
            int mediaId = mediaItem.getId();

            if (id == mediaId) {
                if (mediaItem.getTitle() != null) {
                    movieLikedList.add(mediaItem);
                    System.out.println(mediaId + " || " + mediaItem.getTitle() + " - Added successfully");
                } else {
                    seriesLikedList.add(mediaItem);
                    System.out.println(mediaId + " || " + mediaItem.getName() + " - Added successfully");
                }
                return;
            }
        }
        System.out.println("Not a valid id!");
    }
}
