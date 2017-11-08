package br.com.mobiplus.ferramentadeviajem.mvp.event;

import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeResultInfos;

/**
 * Created by Develop on 03/11/2017.
 */

public class OnExchangeResultInfoCalculatedEvent
{
    private ExchangeResultInfos exchangeResultInfos;

    public OnExchangeResultInfoCalculatedEvent(ExchangeResultInfos exchangeResultInfos)
    {
        this.exchangeResultInfos = exchangeResultInfos;
    }

    public ExchangeResultInfos getExchangeResultInfos()
    {
        return exchangeResultInfos;
    }
}
