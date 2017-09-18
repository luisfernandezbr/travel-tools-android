package br.com.mobiplus.ferramentadeviajem.service;

import android.content.Context;

import br.com.mobiplus.ferramentadeviajem.models.MoedaAPI;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
//    private double[] valor = new double[3];
    private Retrofit retrofit;
//    private boolean conexão=false;

/*    public boolean isConexão() {
        return conexão;
    }

    public void setConexão(boolean conexão) {
        this.conexão = conexão;
    }
*/

 /*   public double getValor(int position) {
        if (position == 0) {
            return valor[0];
        } else if (position == 1) {
            return valor[1];
        } else if (position == 2) {
            return valor[2];
        }else
            return 0;
    }

    public void setValor(double[] valor){
        this.valor = valor;
    }
*/

    public RetrofitService(String baseURL) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor( getLoggingInterceptor()).build();

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);

        this.retrofit = retroBuilder.build();
    }

    public void getCurrency(String moedaLocal, String moedasEstrangeiras, final int position, final Context contexto, Callback<MoedaAPI> callback){

        FreeCurrencyService currency = retrofit.create(FreeCurrencyService.class);
        Call<MoedaAPI> call = currency.getCurrency(moedaLocal, moedasEstrangeiras);

        call.enqueue(callback);
    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;

    }

}
