package dev.l_gomes_ar.mediarecommendation.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Movie extends Media {
    protected String original_title;
    protected String title;
    protected String release_date;

    public String getTitle() {
        return title;
    }

    // Displays shorten formatted string with movie info
    public void displayShortSummary() {
        String string = "=====================================================================\n" +
                "ID: " + id + "\n" +
                "Title: " + title;
        System.out.println(string);
    }

    // Returns formatted string with movie info
    public String toString(GenreList genreList) {
        String formattedReleaseDate = (release_date != null && !release_date.isEmpty()) ?
                LocalDate.parse(release_date).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) :
                "Not available.";

        String formattedTitle = (title.equals(original_title)) ? "Title: " + title + "\n" : "Title: " + title + "\n" +
                "Original Title: " + original_title + "\n";

        return "=====================================================================\n" +
                "ID: " + id + "\n" +
                formattedTitle +
                "Overview: " + shortenOverview(overview) + "\n" +
                "Release Date: " + formattedReleaseDate + "\n" +
                "Genres: " + getGenreList(genreList) + "\n" +
                "Average Rating: " + vote_average + "\n" +
                "Original Language: " + original_language;
    }
}
