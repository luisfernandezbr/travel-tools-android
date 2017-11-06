package br.com.mobiplus.ferramentadeviajem.mvp.event;

import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class OnFireCurrencyDetailsUpdateEvent
{
    private ExchangeInfos exchangeInfos;

    public OnFireCurrencyDetailsUpdateEvent(ExchangeInfos exchangeInfos)
    {
        this.exchangeInfos = exchangeInfos;
    }

    public ExchangeInfos getExchangeInfos()
    {
        return exchangeInfos;
    }
}
