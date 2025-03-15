package dev.l_gomes_ar.mediarecommendation.deserializer;

import com.google.gson.*;
import dev.l_gomes_ar.mediarecommendation.model.GenreList;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GenreListDeserializer implements JsonDeserializer<GenreList> {
    @Override
    public GenreList deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Parse the list of genres array
        JsonArray jsonArray = jsonObject.get("genres").getAsJsonArray();

        // Initialise instance of hashmap
        HashMap<String, String> genres = new HashMap<>();

        // Add each elem to the hashmap
        for (JsonElement element : jsonArray) {
            JsonObject elemObj = element.getAsJsonObject();
            genres.put(elemObj.get("id").getAsString(), elemObj.get("name").getAsString());
        }

        // Add hashmap to GenreList class
        return new GenreList(genres);
    }
}
