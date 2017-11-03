package br.com.mobiplus.ferramentadeviajem.mvp.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnLoadExchangeRatesSuccessEvent;
import br.com.mobiplus.ferramentadeviajem.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luisfernandez on 02/11/17.
 */

public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository
{

    private RetrofitService retrofitService;

    public ExchangeRatesRepositoryImpl()
    {
        this.retrofitService = new RetrofitService("http://api.fixer.io/");
    }

    @Override
    public void loadExchangeRates(String currencyFrom, String[] currencyTo)
    {
        String currenciesString = this.getCurrenciesString(currencyTo);
        Callback<CurrencyExchange> callback = this.getRetrofitCallback();
        this.retrofitService.getCurrency(currencyFrom, currenciesString, callback);
    }


    @NonNull
    private Callback<CurrencyExchange> getRetrofitCallback()
    {
        return new Callback<CurrencyExchange>()
        {
            @Override
            public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response)
            {
                this.handleResponse(response);
            }

            @Override
            public void onFailure(Call<CurrencyExchange> call, Throwable t)
            {
                Log.e("TAG", "Error loading exchange rates.", t);
            }

            private void handleResponse(Response<CurrencyExchange> response)
            {
                if (response.isSuccessful())
                {
                    this.handleSuccessResponse(response);
                } else
                {
                    Log.e("TAG", "Error loading exchange rates. " + response.message());
                }
            }

            private void handleSuccessResponse(Response<CurrencyExchange> response)
            {
                CurrencyExchange currencyExchange = response.body();
                sendOnLoadExchangeRatesSuccessEvent(currencyExchange);
            }
        };
    }

    private void sendOnLoadExchangeRatesSuccessEvent(CurrencyExchange currencyExchange)
    {
        EventBus.getDefault().post(new OnLoadExchangeRatesSuccessEvent(currencyExchange));
    }

    private String getCurrenciesString(String[] currencyTo)
    {
        StringBuilder result = new StringBuilder();

        for (int index = 0; index < currencyTo.length; index++)
        {
            String currency = currencyTo[index];
            result.append(currency);

            if (this.isNotTheLastIndex(currencyTo, index))
            {
                result.append(",");
            }
        }

        return result.toString();
    }

    private boolean isNotTheLastIndex(String[] currencyTo, int index)
    {
        return index < currencyTo.length - 1;
    }
}
