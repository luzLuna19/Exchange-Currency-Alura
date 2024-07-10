package com.exchange_currency;

import com.exchange_currency.client.CurrencyClient;
import com.exchange_currency.client.CurrencyExchangeClient;
import com.exchange_currency.utils.InputValidator;
import java.io.IOException;

/**
 * The Main class is responsible for converting currencies using exchange rates.
 * It provides a menu-driven interface for the user to select the source currency, target currency,
 * and amount to convert. The class uses a CurrencyClient to fetch exchange rates and perform the conversion.
 */
public class Main {

    private static final String[] CURRENCY_CODES = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};
    private static final String[] CURRENCY_NAMES = {"Argentine Peso", "Bolivian Boliviano", "Brazilian Real",
                                                    "Chilean Peso", "Colombian Peso", "US Dollar"};

    /**
     * The main method is the entry point of the Main program.
     * It initializes a CurrencyClient object, fetches exchange rates,
     * and allows the user to convert currencies based on their input.
     * The user can choose the source currency, amount to convert,
     * and the target currency. The converted amount is then displayed.
     * The program continues to run until the user chooses to exit.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        CurrencyClient client = new CurrencyExchangeClient();

        try {
            client.fetchExchangeRates();

            boolean exit = false;
            while (!exit) {
                displayMenu();
                int option = InputValidator.getValidIntegerInput("Enter your choice (" + 1 + "-" + (CURRENCY_CODES.length + 1) + "): ", 1, CURRENCY_CODES.length + 1);
                if (option == CURRENCY_CODES.length + 1) {
                    exit = true;
                    System.out.println("Goodbye!");
                    continue;
                }

                String sourceCurrency = CURRENCY_CODES[option - 1];
                double amountToConvert = InputValidator.getPositiveDoubleInput("Enter the amount in " + sourceCurrency + " to convert: ");

                System.out.println("Select the target currency:");
                displayCurrencyOptions();

                option = InputValidator.getValidIntegerInput("Enter your choice (" + 1 + "-" + (CURRENCY_CODES.length + 1) + "): ", 1, CURRENCY_CODES.length + 1);
                if (option == CURRENCY_CODES.length + 1) {
                    exit = true;
                    System.out.println("Goodbye!");
                    continue;
                }

                String targetCurrency = CURRENCY_CODES[option - 1];

                double convertedAmount = client.convertCurrency(amountToConvert, sourceCurrency, targetCurrency);
                System.out.println(amountToConvert + " " + sourceCurrency + " is equivalent to " + convertedAmount + " " + targetCurrency);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching exchange rates: " + e.getMessage());
            e.printStackTrace();
        } finally {
            InputValidator.closeScanner();
        }
    }

    /**
     * Displays the currency conversion menu.
     */
    private static void displayMenu() {
        System.out.println("\n--- Currency Conversion Menu ---");
        displayCurrencyOptions();
        System.out.println((CURRENCY_CODES.length + 1) + "- Exit - Terminate program");
    }

    /**
     * Displays the currency options for conversion.
     * The method prints a list of available currencies to the console.
     */
    private static void displayCurrencyOptions() {
        System.out.println("Select a currency to convert (or 'Exit' to end the program):");
        for (int i = 0; i < CURRENCY_CODES.length; i++) {
            System.out.println((i + 1) + "- " + CURRENCY_CODES[i] + " - " + CURRENCY_NAMES[i]);
        }
    }
}