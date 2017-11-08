package br.com.mobiplus.ferramentadeviajem.mvp.view;

import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;

/**
 * Created by luisfernandez on 03/11/17.
 */

public interface ProductCalcView
{
    void updateAmounts(CalculatedCurrency calculatedCurrency);

    void onCurrencyExchangeLoaded(CurrencyExchange currencyExchange);
}
