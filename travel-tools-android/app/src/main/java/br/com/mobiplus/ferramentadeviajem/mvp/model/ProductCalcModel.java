package br.com.mobiplus.ferramentadeviajem.mvp.model;

import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;

/**
 * Created by luisfernandez on 03/11/17.
 */

public interface ProductCalcModel
{
    void doCalculateExchangeInfos(ExchangeInfos exchangeInfos);
    void doCalculateCurrencyDetails(ExchangeInfos exchangeInfos);
}
