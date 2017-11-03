package br.com.mobiplus.ferramentadeviajem.mvp.model;

import org.greenrobot.eventbus.EventBus;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CurrencyDetails;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class ProductCalcModelImpl implements ProductCalcModel
{
    @Override
    public void doCalculateCurrencyDetails(CurrencyDetails currencyDetails)
    {

        CalculatedCurrency calculatedCurrency = new CalculatedCurrency();
        this.sendOnCurrencyCalculatedEvent(calculatedCurrency);
    }

    private void sendOnCurrencyCalculatedEvent(CalculatedCurrency calculatedCurrency)
    {
        EventBus.getDefault().post(new OnCurrencyCalculatedEvent(calculatedCurrency));
    }
}
