package br.com.mobiplus.ferramentadeviajem.mvp.repository;

import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;

/**
 * Created by luisfernandez on 02/11/17.
 */

public interface CurrencyRepository
{
    void loadCurrencyExchange(String currencyFrom, String[] currencyTo, DataCallback<CurrencyExchange, String> dataCallback);
}
