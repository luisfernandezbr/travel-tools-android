package br.com.mobiplus.ferramentadeviajem.mvp.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;
import br.com.mobiplus.ferramentadeviajem.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luisfernandez on 02/11/17.
 */

public class CurrencyRepositoryImpl implements CurrencyRepository
{

    private RetrofitService retrofitService;
    Context context;

    public CurrencyRepositoryImpl(Context context)
    {
        this.context = context;
        this.retrofitService = new RetrofitService("http://api.fixer.io/");
    }

    @Override
    public void loadCurrencyExchange(String currencyFrom, String[] currencyTo, final DataCallback<CurrencyExchange, String> dataCallback)
    {
        String currenciesString = this.getCurrenciesString(currencyTo);
        Callback<CurrencyExchange> callback = this.getRetrofitCallback(dataCallback);
        this.retrofitService.getCurrency(currencyFrom, currenciesString, context, callback);
    }

    @NonNull
    private Callback<CurrencyExchange> getRetrofitCallback(final DataCallback<CurrencyExchange, String> dataCallback)
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
                dataCallback.onError(t.getMessage());
            }

            private void handleResponse(Response<CurrencyExchange> response)
            {
                if (response.isSuccessful())
                {
                    this.handleSuccessResponse(response);
                } else
                {
                    dataCallback.onError(response.message());
                }
            }

            private void handleSuccessResponse(Response<CurrencyExchange> response)
            {
                CurrencyExchange currencyExchange = response.body();
                dataCallback.onSuccess(currencyExchange);
            }
        };
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
