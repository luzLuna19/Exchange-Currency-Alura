package com.exchange_currency.utils;

import java.util.Scanner;

/**
 * The InputValidator class provides methods for validating user input.
 * It includes methods for validating integer and double inputs.
 */
public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);

    public static int getValidIntegerInput(String message, int min, int max) {
        while (true) {
            System.out.print(message);
            if (!scanner.hasNextInt()) {
                displayErrorMessage("Please enter a valid integer.");
                scanner.next(); 
                continue;
            }
            int input = scanner.nextInt();
            if (input < min || input > max) {
                displayErrorMessage("Please enter a number between " + min + " and " + max + ".");
                continue;
            }
            return input;
        }
    }

    /**
     * Prompts the user to enter a positive double value and returns it.
     * 
     * @param message the message to display when prompting the user for input
     * @return the positive double value entered by the user
     */
    public static double getPositiveDoubleInput(String message) {
        while (true) {
            System.out.print(message);
            if (!scanner.hasNextDouble()) {
                displayErrorMessage("Please enter a valid number.");
                scanner.next(); 
                continue;
            }
            double input = scanner.nextDouble();
            if (input <= 0) {
                displayErrorMessage("The amount must be a positive number greater than zero.");
                continue;
            }
            return input;
        }
    }

    /**
     * Closes the scanner object if it is not null.
     */
    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * Displays an error message to the console.
     *
     * @param message the error message to be displayed
     */
    private static void displayErrorMessage(String message) {
        System.out.println("Error: " + message);
    }
}