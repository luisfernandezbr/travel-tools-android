package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import android.app.usage.UsageEvents;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeResultInfos;

/**
 * Created by Develop on 03/11/2017.
 */

public interface ProductCalcDetailsPresenter extends BasePresenter{

    void onLoadProductCalcValue(ExchangeInfos exchangeInfos);
    void onExchangeResultInfoCalculatedEvent(OnExchangeResultInfoCalculatedEvent event);

}
