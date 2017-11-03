package br.com.mobiplus.ferramentadeviajem.mvp.repository;

/**
 * Created by luisfernandez on 02/11/17.
 */

public interface ExchangeRatesRepository
{
    void loadExchangeRates(String currencyFrom, String[] currencyTo);
}
