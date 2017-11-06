package br.com.mobiplus.ferramentadeviajem.service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.mobiplus.ferramentadeviajem.R;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeResultInfos;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcDetailsView;

public class ProductCalcDetailsActivity extends AppCompatActivity implements ProductCalcDetailsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_calc_details);

    }

    @Override
    public void onExchangeResultLoaded(ExchangeResultInfos exchangeResultInfos) {

    }
}
