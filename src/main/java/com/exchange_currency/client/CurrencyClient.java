package com.exchange_currency.client;

import java.io.IOException;

/**
 * The CurrencyClient interface represents a client for fetching exchange rates and converting currencies.
 */
public interface CurrencyClient {
    /**
     * Fetches the latest exchange rates from a data source.
     *
     * @throws IOException          if an I/O error occurs while fetching the exchange rates.
     * @throws InterruptedException if the thread is interrupted while fetching the exchange rates.
     */
    void fetchExchangeRates() throws IOException, InterruptedException;

    /**
     * Converts the specified amount from one currency to another.
     *
     * @param amount      the amount to be converted.
     * @param fromCurrency the currency to convert from.
     * @param toCurrency   the currency to convert to.
     * @return the converted amount.
     */
    double convertCurrency(double amount, String fromCurrency, String toCurrency);
}