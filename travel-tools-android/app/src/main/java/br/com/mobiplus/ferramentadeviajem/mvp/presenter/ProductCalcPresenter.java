package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnLoadExchangeRatesSuccessEvent;

/**
 * Created by luisfernandez on 03/11/17.
 */

public interface ProductCalcPresenter extends BasePresenter
{
    void onFireLoadExchangeRatesEvent(OnFireLoadExchangeRatesEvent event);
    void onLoadExchangeRatesSuccessEvent(OnLoadExchangeRatesSuccessEvent event);
    void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event);
    void onCurrencyCalculated(OnCurrencyCalculatedEvent event);
}
