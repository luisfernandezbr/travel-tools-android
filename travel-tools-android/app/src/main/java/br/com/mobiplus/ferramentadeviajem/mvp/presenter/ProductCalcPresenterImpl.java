package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModel;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModelImpl;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcView;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class ProductCalcPresenterImpl implements ProductCalcPresenter
{

    private ProductCalcModel model;
    private ProductCalcView view;

    public ProductCalcPresenterImpl(ProductCalcView productCalcView)
    {
        this.model = new ProductCalcModelImpl();
        this.view = productCalcView;
    }

    @Override
    @Subscribe
    public void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event)
    {
        this.model.doCalculateCurrencyDetails(event.getCurrencyDetails());
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
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
