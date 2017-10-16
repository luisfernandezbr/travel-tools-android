package br.com.mobiplus.ferramentadeviajem.service;

import br.com.mobiplus.ferramentadeviajem.models.MoedaAPI;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Develop on 09/09/2017.
 */

public interface FreeCurrencyService {

    @GET("/latest")
    Call<MoedaAPI> getCurrency(@Query("base") String valor, @Query("symbols") String symbols);

}
