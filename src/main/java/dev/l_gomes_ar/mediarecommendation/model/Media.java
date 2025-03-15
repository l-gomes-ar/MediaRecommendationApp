package dev.l_gomes_ar.mediarecommendation.model;

public abstract class Media {
    protected String[] genre_ids;
    protected int id;
    public String original_language;
    protected String overview;
    protected String vote_average;

    // Provide list of genres by id
    protected String getGenreList(GenreList genreList) {
        if (genre_ids == null) return "Not available";

        StringBuilder list = new StringBuilder();
        for (String id : genre_ids) {
            list.append(genreList.genres.get(id)).append(", ");
        }

        list.append("\b\b.");
        return list.toString();
    }

    public String[] getGenreId() {
        return this.genre_ids;
    }

    // Shortens overview string (55 characters)
    protected String shortenOverview(String text) {
        if (!text.isEmpty()) {
            if (text.length() > 55) {
                return text.substring(0, 55) + "...";
            } else {
                return text;
            }
        }
        return "";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return null;
    }

    public String getName() {
        return null;
    }

    public abstract void displayShortSummary();

    public abstract String toString(GenreList genreList);

    // Overrides the equals method to add Media object comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id == ((Media) obj).id;
    }
}
