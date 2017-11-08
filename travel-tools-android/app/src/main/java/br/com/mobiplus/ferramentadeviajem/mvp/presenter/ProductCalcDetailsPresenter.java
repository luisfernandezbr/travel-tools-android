package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;

/**
 * Created by Develop on 03/11/2017.
 */

public interface ProductCalcDetailsPresenter extends BasePresenter{

    void onLoadProductCalcValue(ExchangeInfos exchangeInfos);
    void onExchangeResultInfoCalculatedEvent(OnExchangeResultInfoCalculatedEvent event);

}
