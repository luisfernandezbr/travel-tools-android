package br.com.mobiplus.ferramentadeviajem.mvp.event;

import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class OnLoadExchangeRatesSuccessEvent
{
    private CurrencyExchange currencyExchange;

    public OnLoadExchangeRatesSuccessEvent(CurrencyExchange currencyExchange)
    {
        this.currencyExchange = currencyExchange;
    }

    public CurrencyExchange getCurrencyExchange()
    {
        return currencyExchange;
    }
}
