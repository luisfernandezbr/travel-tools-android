package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModel;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcView;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class ProductCalcPresenterImpl implements ProductCalcPresenter
{

    private ProductCalcModel model;
    private ProductCalcView view;

    public ProductCalcPresenterImpl(ProductCalcModel model)
    {
        this.model = model;
    }

    @Override
    @Subscribe
    public void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event)
    {
        this.model.doCalculateCurrencyDetails(event.getCurrencyDetails());
    }

    @Override
    @Subscribe
    public void onCurrencyCalculated(OnCurrencyCalculatedEvent event)
    {
        this.view.updateAmounts(event.getCalculatedCurrency());
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
