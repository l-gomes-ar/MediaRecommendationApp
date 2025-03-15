package dev.l_gomes_ar.mediarecommendation.model;

public abstract class Media {
    protected String adult;
    protected String backdrop_path;
    protected String[] genre_ids;
    protected int id;
    public String original_language;
    protected String overview;
    protected String popularity;
    protected String poster_path;
    protected String video;
    protected String vote_average;
    protected String vote_count;

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
//        StringBuilder genreIdsString = new StringBuilder();
//        for (String id : genre_ids) {
//            genreIdsString.append(id).append(",");
//        }
//        return genreIdsString.toString();
    }

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
    };

    public String getName() {
        return null;
    };

    public abstract void displayShortSummary();

    public abstract String toString(GenreList genreList);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id == ((Media) obj).id;
    }
}
