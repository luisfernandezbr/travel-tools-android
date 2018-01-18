package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnLoadExchangeRatesSuccessEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.model.ProductCalcModel;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.ExchangeRatesRepository;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcView;

/**
 * Created by luisfernandez on 18/01/18.
 */
public class ProductCalcPresenterTest
{

    @Mock
    private ProductCalcView productCalcView;

    @Mock
    private ExchangeRatesRepository exchangeRatesRepository;

    @Mock
    private ProductCalcModel productCalcModel;

    @Mock
    private OnFireLoadExchangeRatesEvent onFireLoadExchangeRatesEvent;

    @Mock
    private OnLoadExchangeRatesSuccessEvent onLoadExchangeRatesSuccessEvent;

    @Mock
    private OnFireCurrencyDetailsUpdateEvent onFireCurrencyDetailsUpdateEvent;

    @Mock
    private OnCurrencyCalculatedEvent onCurrencyCalculatedEvent;

    private ProductCalcPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new ProductCalcPresenterImpl(productCalcView, exchangeRatesRepository, productCalcModel);
    }

    @Test
    public void onFireLoadExchangeRatesEvent() throws Exception
    {
        presenter.onFireLoadExchangeRatesEvent(onFireLoadExchangeRatesEvent);

        Mockito.verify(exchangeRatesRepository).loadExchangeRates(null, null);
    }

    @Test
    public void onLoadExchangeRatesSuccessEvent() throws Exception
    {
        presenter.onLoadExchangeRatesSuccessEvent(onLoadExchangeRatesSuccessEvent);

        Mockito.verify(productCalcView).onCurrencyExchangeLoaded(null);
    }

    @Test
    public void onFireCurrencyDetailsUpdate() throws Exception
    {
        presenter.onFireCurrencyDetailsUpdate(onFireCurrencyDetailsUpdateEvent);

        Mockito.verify(productCalcModel).doCalculateCurrencyDetails(null);
    }

    @Test
    public void onCurrencyCalculated() throws Exception
    {
        presenter.onCurrencyCalculated(onCurrencyCalculatedEvent);

        Mockito.verify(productCalcView).updateAmounts(null);
    }
}