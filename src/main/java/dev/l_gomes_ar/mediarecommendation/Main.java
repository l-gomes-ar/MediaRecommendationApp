package dev.l_gomes_ar.mediarecommendation;


import dev.l_gomes_ar.mediarecommendation.ui.UI;

public class Main {
    public static void main(String[] args) {
        try {
            UI ui = new UI();
            ui.startMenu();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}