package dev.l_gomes_ar.mediarecommendation.model;

import java.util.ArrayList;

public class SearchResult {
    public ArrayList<Media> results;
    public int total_results;
    private int page;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotalPages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotalPages() {
        return total_pages;
    }
}
