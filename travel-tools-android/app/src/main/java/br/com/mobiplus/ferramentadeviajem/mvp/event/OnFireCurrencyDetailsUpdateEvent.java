package br.com.mobiplus.ferramentadeviajem.mvp.event;

import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CurrencyDetails;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class OnFireCurrencyDetailsUpdateEvent
{
    private CurrencyDetails currencyDetails;

    public OnFireCurrencyDetailsUpdateEvent(CurrencyDetails currencyDetails)
    {
        this.currencyDetails = currencyDetails;
    }

    public CurrencyDetails getCurrencyDetails()
    {
        return currencyDetails;
    }
}
