package dev.l_gomes_ar.mediarecommendation.deserializer;

import com.google.gson.*;
import dev.l_gomes_ar.mediarecommendation.model.SearchResult;
import dev.l_gomes_ar.mediarecommendation.model.Media;
import dev.l_gomes_ar.mediarecommendation.model.Movie;
import dev.l_gomes_ar.mediarecommendation.model.Series;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchResultDeserializer implements JsonDeserializer<SearchResult> {
    @Override
    public SearchResult deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Parse 'page', 'total_page', and 'total_results'
        int page = jsonObject.get("page").getAsInt();
        int total_pages = jsonObject.get("total_pages").getAsInt();
        int total_results = jsonObject.get("total_results").getAsInt();

        // Parse 'results' array with polymorphism
        JsonArray resultsArray = jsonObject.getAsJsonArray("results");
        ArrayList<Media> results = new ArrayList<>();

        for (JsonElement element : resultsArray) {
            JsonObject mediaObject = element.getAsJsonObject();

            // Determine type by checking fields
            Media mediaItem;
            if (mediaObject.has("title")) {
                mediaItem = context.deserialize(mediaObject, Movie.class);
            } else if (mediaObject.has("name")) {
                mediaItem = context.deserialize(mediaObject, Series.class);
            } else {
                throw new JsonParseException("Unknown media type in results");
            }

            results.add(mediaItem);
        }

        // Create and return the SearchResult object
        SearchResult searchResult = new SearchResult();
        searchResult.setPage(page);
        searchResult.setTotalPages(total_pages);
        searchResult.total_results = total_results;
        searchResult.results = results;

        return searchResult;
    }
}
