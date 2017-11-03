package br.com.mobiplus.ferramentadeviajem.mvp.presenter;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;

/**
 * Created by luisfernandez on 03/11/17.
 */

public interface ProductCalcPresenter
{
    void onStart();
    void onStop();
    void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event);
    void onCurrencyCalculated(OnCurrencyCalculatedEvent event);
}
