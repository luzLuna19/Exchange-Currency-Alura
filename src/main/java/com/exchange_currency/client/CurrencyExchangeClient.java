package com.exchange_currency.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.github.cdimascio.dotenv.Dotenv;


/**
 * The CurrencyExchangeClient class is responsible for fetching exchange rates from a currency API
 * and converting currency amounts based on the fetched rates.
 */
public class CurrencyExchangeClient implements CurrencyClient {
    private static final String BASE_API_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String ENDPOINT = "/latest/USD";
    private String apiUrl;
    private HttpClient httpClient;
    private Gson gson;
    private JsonObject exchangeRates;

    public CurrencyExchangeClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
        * Fetches the exchange rates from the server.
        *
        * @throws IOException if an I/O error occurs while sending the HTTP request or receiving the response.
        * @throws InterruptedException if the current thread is interrupted while waiting for the HTTP request to complete.
        */
    @Override
    public void fetchExchangeRates() throws IOException, InterruptedException {
        HttpRequest request = createHttpRequest();
        HttpResponse<String> response = sendHttpRequest(request);

        if (response.statusCode() == 200) {
            parseExchangeRates(response.body());
        } else {
            throw new IOException("Failed to fetch exchange rates. Status code: " + response.statusCode());
        }
    }

    /**
     * Converts the specified amount from one currency to another.
     *
     * @param amount      the amount to be converted
     * @param fromCurrency the currency code of the currency to convert from
     * @param toCurrency   the currency code of the currency to convert to
     * @return the converted amount
     * @throws IllegalArgumentException if the exchange rates or currency codes are invalid
     */
    @Override
    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        validateExchangeRates();
        validateCurrencyCode(fromCurrency);
        validateCurrencyCode(toCurrency);

        double fromRate = exchangeRates.get(fromCurrency).getAsDouble();
        double toRate = exchangeRates.get(toCurrency).getAsDouble();

        return (amount / fromRate) * toRate;
    }

    /**
     * Creates an HTTP request for the currency exchange API.
     *
     * @return the created HTTP request
     */
    private HttpRequest createHttpRequest() {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        this.apiUrl = BASE_API_URL + apiKey + ENDPOINT;
        return HttpRequest.newBuilder()
                .uri(URI.create(this.apiUrl))
                .GET()
                .build();
    }

    /**
     * Sends an HTTP request and returns the response.
     *
     * @param request the HTTP request to send
     * @return the HTTP response
     * @throws IOException if an error occurs while sending the request
     * @throws InterruptedException if the fetch operation is interrupted
     */
    private HttpResponse<String> sendHttpRequest(HttpRequest request) throws IOException, InterruptedException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new IOException("Error occurred while fetching exchange rates: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            throw new InterruptedException("Fetch operation was interrupted: " + e.getMessage());
        }
    }

    /**
     * Parses the exchange rates from the response body and sets them in the exchangeRates field.
     *
     * @param responseBody the response body containing the exchange rates
     */
    private void parseExchangeRates(String responseBody) {
        this.exchangeRates = gson.fromJson(responseBody, JsonObject.class)
                .getAsJsonObject("conversion_rates");
    }

    /**
     * Validates the exchange rates.
     * 
     * @throws IllegalStateException if exchange rates have not been fetched. 
     *         Please call fetchExchangeRates() first.
     */
    private void validateExchangeRates() {
        if (exchangeRates == null) {
            throw new IllegalStateException("Exchange rates have not been fetched. Please call fetchExchangeRates() first.");
        }
    }

    /**
     * Validates the given currency code.
     *
     * @param currencyCode the currency code to validate
     * @throws IllegalArgumentException if the currency code is invalid
     */
    private void validateCurrencyCode(String currencyCode) {
        if (!exchangeRates.has(currencyCode)) {
            throw new IllegalArgumentException("Invalid currency code provided: " + currencyCode);
        }
    }
}