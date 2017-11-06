package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnLoadExchangeRatesSuccessEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModel;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModelImpl;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.ExchangeRatesRepository;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.ExchangeRatesRepositoryImpl;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcView;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class ProductCalcPresenterImpl implements ProductCalcPresenter
{
    private static final String TAG = "ProductCalcPresenterImpl";

    private ExchangeRatesRepository repository;
    private ProductCalcModel model;
    private ProductCalcView view;

    public ProductCalcPresenterImpl(ProductCalcView productCalcView)
    {
        this.model = new ProductCalcModelImpl();
        this.repository = new ExchangeRatesRepositoryImpl();
        this.view = productCalcView;
    }

    @Override
    public void onFireLoadExchangeRatesEvent(OnFireLoadExchangeRatesEvent event)
    {
        repository.loadExchangeRates(event.getCurrencyFrom(), event.getCurreciesTo());
    }

    @Override
    @Subscribe
    public void onLoadExchangeRatesSuccessEvent(OnLoadExchangeRatesSuccessEvent event)
    {
        this.view.onCurrencyExchangeLoaded(event.getCurrencyExchange());
    }

    @Override
    @Subscribe
    public void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event)
    {
        this.model.doCalculateCurrencyDetails(event.getExchangeInfos());
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
