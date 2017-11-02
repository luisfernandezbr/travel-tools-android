package br.com.mobiplus.ferramentadeviajem.service;

import android.content.Context;

import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService
{
    private Retrofit retrofit;

    public RetrofitService(String baseURL)
    {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor()).build();

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);

        this.retrofit = retroBuilder.build();
    }

    public void getCurrency(String moedaLocal, String moedasEstrangeiras, final Context contexto, Callback<CurrencyExchange> callback)
    {
        FreeCurrencyService currency = retrofit.create(FreeCurrencyService.class);
        Call<CurrencyExchange> call = currency.getCurrency(moedaLocal, moedasEstrangeiras);

        call.enqueue(callback);
    }

    private Interceptor getLoggingInterceptor()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
