package br.com.mobiplus.ferramentadeviajem.mvp.event;

import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class OnCurrencyCalculatedEvent
{
    private CalculatedCurrency calculatedCurrency;

    public OnCurrencyCalculatedEvent(CalculatedCurrency calculatedCurrency)
    {
        this.calculatedCurrency = calculatedCurrency;
    }

    public CalculatedCurrency getCalculatedCurrency()
    {
        return calculatedCurrency;
    }
}
