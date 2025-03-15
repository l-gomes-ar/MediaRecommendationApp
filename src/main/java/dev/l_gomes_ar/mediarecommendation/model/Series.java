package dev.l_gomes_ar.mediarecommendation.model;

public class Series extends Media {
    protected String original_name;
    protected String name;

    public String getName() {
        return name;
    }

    public void displayShortSummary() {
        String string = "=====================================================================\n" +
                "ID: " + id + "\n" +
                "Title: " + name;
        System.out.println(string);
    }

    public String toString(GenreList genreList) {
        String formattedTitle = (name.equals(original_name)) ? "Title: " + name + "\n" : "Title: " + name + "\n" +
                "Original Title: " + original_name + "\n";

        return "===================================================================== \n" +
                "ID: " + id + "\n" +
                formattedTitle +
                "Overview: " + shortenOverview(overview) + "\n" +
                "Genres: " + getGenreList(genreList) + "\n" +
                "Average Rating: " + vote_average + "\n" +
                "Original Language: " + original_language;
    }
}
