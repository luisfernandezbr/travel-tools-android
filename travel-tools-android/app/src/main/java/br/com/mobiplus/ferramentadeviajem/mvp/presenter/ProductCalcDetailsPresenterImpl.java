package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModel;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModelImpl;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcDetailsView;

/**
 * Created by Develop on 03/11/2017.
 */

public class ProductCalcDetailsPresenterImpl implements ProductCalcDetailsPresenter{

    ProductCalcDetailsView view;
    ProductCalcModel model;

    public ProductCalcDetailsPresenterImpl(ProductCalcDetailsView view) {
        this.view = view;
        model = new ProductCalcModelImpl();

    }

    @Override
    public void onLoadProductCalcValue(ExchangeInfos exchangeInfos) {
        model.doCalculateExchangeInfos(exchangeInfos);
    }

    @Override
    @Subscribe
    public void onExchangeResultInfoCalculatedEvent(OnExchangeResultInfoCalculatedEvent event) {
        view.onExchangeResultLoaded(event.getExchangeResultInfos());
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }
}
