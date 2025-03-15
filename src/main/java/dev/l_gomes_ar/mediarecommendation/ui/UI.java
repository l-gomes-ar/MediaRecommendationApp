package dev.l_gomes_ar.mediarecommendation.ui;

import dev.l_gomes_ar.mediarecommendation.model.SearchResult;
import dev.l_gomes_ar.mediarecommendation.model.Media;
import dev.l_gomes_ar.mediarecommendation.service.MediaManager;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {
    private static final String RECOMMENDATION = "recommendation";
    private static final String SEARCH = "search";

    private final String[]  starterMenuOptions = {
            "1 - View Recommendations", // -> mediaOptions -> addMediaOptions - > Go Back
            "2 - Search Media", // -> mediaOptions -> addMediaOptions
            "3 - View Your Liked List", // -> Go Back
            "4 - Exit"
    };

    private final String[] mediaOptions = {
            "1 - Movie",
            "2 - TV Series",
            "3 - Go back"
    };

    private final String[] addMediaOptions = {
            "1 - Search a different page", // -> Page number:
            "2 - Add media to your list", // -> Media id:
            "3 - Go back"
    };

    private final Scanner scanner = new Scanner(System.in);
    private final MediaManager mediaManager = new MediaManager();

    // Start main menu
    public void startMenu() {
        System.out.println("=====================================================================");
        System.out.println("               WELCOME TO THE MEDIA RECOMMENDATION APP               ");
        System.out.println("=====================================================================");

        while (true) {
            for (String line : starterMenuOptions) {
                System.out.println(line);
            }
            System.out.println("=====================================================================");
            String option = getString("Select an option: ");

            switch (option) {
                case "1" -> viewRecommendationsMenu();
                case "2" -> searchMediaMenu();
                case "3" -> displayLikedMenu();
                case "4" -> {
                    scanner.close();
                    return;
                }
            }
        }
    }

    // Start menu for showing recommendations
    public void viewRecommendationsMenu() {
        String type = getMediaOption();
        if (type == null) return;

        if (type.equals("back")) return;

        mediaManager.getRecommendations(type);
        displayRecommendations();
        addMediaMenu(RECOMMENDATION, type, "");
    }

    // Start menu for querying the TMdb API
    public void searchMediaMenu() {
        String type = getMediaOption();
        if (type == null) return;
        if (type.equals("back")) return;
        String query = getString("Search: ");

        mediaManager.searchMedia(type, query);
        displaySearchList();
        addMediaMenu(SEARCH, type, query);
    }

    // Start menu for adding media to liked list
    public void addMediaMenu(String listType, String type, String query) {
        SearchResult list = listType.equals(RECOMMENDATION) ?
                mediaManager.recommendationList :
                mediaManager.searchList;

        String option;
        do {
            option = displayOptions(addMediaOptions);
            switch (option) {
                case "1" -> {
                    int page = getInt("Page number: ");

                    if (page < 1 || page > list.getTotalPages()) {
                        System.out.println("Page Not Found");
                        break;
                    }

                    if (listType.equals(RECOMMENDATION))
                        mediaManager.getRecommendations(type, page);
                    else
                        mediaManager.searchMedia(type, query, page);


                    // Retrieve updated list
                    list = listType.equals(RECOMMENDATION) ?
                            mediaManager.recommendationList :
                            mediaManager.searchList;

                    displayListFromSearchResult(list);
                }
                case "2" -> {
                    list = listType.equals(RECOMMENDATION) ?
                            mediaManager.recommendationList :
                            mediaManager.searchList;
                    int id = getInt("Media id: ");
                    System.out.println("=====================================================================");
                    mediaManager.addMediaToLikedList(id, list);
                    System.out.println("=====================================================================");
                }
            }
        } while (!option.equals("3"));
    }

    // Start menu for displaying the list of liked media
    public void displayLikedMenu() {
        String type = getMediaOption();
        if (type == null) return;
        displayLikedList(type);
    }

    // Retrieves valid media option from user
    public String getMediaOption() {
        String option = displayOptions(mediaOptions);
        return switch (option) {
            case "1" -> "movie";
            case "2" -> "series";
            case "3" -> "back";
            default -> {
                System.out.println("Not a valid option!");
                System.out.println("=====================================================================");
                yield  null;
            }
        };
    }

    // Displays options to user
    public String displayOptions(String[] options) {
        for (String line : options) {
            System.out.println(line);
        }
        System.out.println("=====================================================================");
        return getString("Select an option: ");
    }

    // Displays media recommendation list
    private void displayRecommendations() {
        System.out.println("=====================================================================");
        System.out.println("                      MEDIA RECOMMENDATION LIST                      ");
        displayListFromSearchResult(mediaManager.recommendationList);
    }

    // Displays search result list
    private void displaySearchList() {
        System.out.println("=====================================================================");
        System.out.println("                           SEARCH RESULTS                            ");
        displayListFromSearchResult(mediaManager.searchList);
    }

    // Displays SearchResult list object passing list as an argument
    private void displayListFromSearchResult(SearchResult list) {
        for (Media mediaItem : list.results) {
            System.out.println(mediaItem.toString(mediaManager.genreList));
        }
        System.out.println("=====================================================================");
        System.out.println("Page: " + list.getPage() + " of " + list.getTotalPages() +
                " || " + list.total_results + " Total Results");
        System.out.println("=====================================================================");
    }

    // Displays liked list passing type (movie or series) as argument
    private void displayLikedList(String type) {
        System.out.println("=====================================================================");
        System.out.println("                          YOUR LIKED LIST                            ");
        ArrayList<Media> likedList = type.equals("movie") ? mediaManager.movieLikedList : mediaManager.seriesLikedList;

        if (likedList.isEmpty()) {
            System.out.println("=====================================================================");
            System.out.println("No items in your liked list");
        } else {
            for (Media item : likedList) {
                item.displayShortSummary();
            }
        }
        System.out.println("=====================================================================");
    }

    // Retrieves input from user
    private String getString(String text) {
        System.out.print(text);
        return scanner.nextLine();
    }

    // Retrieves valid int input from user
    private int getInt(String text) {
        int integer;
        while (true) {
            System.out.print(text);
            try {
                integer = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                integer = -1;
            }
            if (integer == -1) System.out.println("Not a valid number!");
            else return integer;
        }
    }
}
