package br.com.mobiplus.ferramentadeviajem;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.mobiplus.ferramentadeviajem.models.MoedaAPI;
import br.com.mobiplus.ferramentadeviajem.service.RetrofitService;
import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainCalculatorActivity extends AppCompatActivity implements Callback<MoedaAPI> {
    private String array_moeda[];
    ImageButton changeMoeda;
    private MoedaAPI moedaAPI;
    private TextView campoTaxa;
    private NumberFormat formato;
    private Spinner spinnerMoedaConvert;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Handler handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calculator);

        final RetrofitService retrofitService = new RetrofitService("http://api.fixer.io/");
        retrofitService.getCurrency("USD", "BRL,EUR", 1, getApplicationContext(), this);


        final EditText campoValor = (EditText) findViewById(R.id.valor);
        final TextView campoConvert = (TextView) findViewById(R.id.convert);
        final TextView campoTotalBR = (TextView) findViewById(R.id.totalBR);
        final TextView campoTotalUS = (TextView) findViewById(R.id.totalUS);
        final TextView campoSimboloValor = (TextView) findViewById(R.id.simboloMoedaValor);
        final TextView campoSimboloConvertida = (TextView) findViewById(R.id.simboloMoedaConvertida);
        final Button detalhes = (Button) findViewById(R.id.btnDetalhes);
        changeMoeda = (ImageButton) findViewById(R.id.imagem_change);
        campoTaxa = (TextView) findViewById(R.id.taxa);
        final Button limpar = (Button) findViewById(R.id.btnLimpar);
        final RadioGroup campoPagamento = (RadioGroup) findViewById(R.id.rgPagamento);
        final RadioGroup campoSituacao = (RadioGroup) findViewById(R.id.rgSituacao);

        array_moeda = new String[3];
        array_moeda[0] = "USD $";
        array_moeda[1] = "BRL R$";
        array_moeda[2] = "EUR €";

        final Spinner spinnerMoedaValor = (Spinner) findViewById(R.id.moedaValor);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_moeda);
        spinnerMoedaValor.setAdapter(adapter);

        spinnerMoedaConvert = (Spinner) findViewById(R.id.moedaConvert);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_moeda);
        spinnerMoedaConvert.setAdapter(adapter);
        spinnerMoedaConvert.setSelection(1);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainCalculatorActivity.this);
        builder.setMessage("Só há acréscimo se exceder em 500 o valor dos produtos.");
        builder.setTitle("Alerta");

        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);

//        campoValor.setText("0");
        campoConvert.setText("0");
//        campoTaxa.setText("0");
        campoTotalUS.setText("0");
        campoTotalBR.setText(campoConvert.getText());

        final CustoViagem viagem = new CustoViagem();

        campoTaxa.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (campoTaxa.getText().length() > 0 && campoValor.getText().length() > 0)
                    viagem.atualizaValorConvertido(Double.parseDouble(campoValor.getText().toString()), Double.parseDouble(s.toString()));
                else {
                    viagem.atualizaValorConvertido(0, 0);
                }
                campoConvert.setText(formato.format(viagem.getValorConvertido()));
                campoTotalBR.setText(formato.format(viagem.getTotalBR()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }));

        campoValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (campoValor.getText().length() > 0 && campoTaxa.getText().length() > 0)
                    viagem.atualizaValorConvertido(Double.parseDouble(s.toString()), Double.parseDouble(campoTaxa.getText().toString()));
                else {
                    viagem.atualizaValorConvertido(0, 0);
                }
                campoConvert.setText(formato.format(viagem.getValorConvertido()));
                campoTotalBR.setText(formato.format(viagem.getTotalBR()));
                campoTotalUS.setText(formato.format(viagem.getTotalUS()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        campoPagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button != null) {
                    String nomeBtn = button.getText().toString();

                    if (nomeBtn.equals("Dinheiro")) {
                        viagem.atualizaPagamento(1);
                    } else {
                        viagem.atualizaPagamento(2);
                    }
                    campoTotalBR.setText(formato.format(viagem.getTotalBR()));
                }
            }
        });

        onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);

                if (button != null) {
                    int nomeBtn = button.getId();
                    boolean situacaoChecked = false;

                    if (nomeBtn == R.id.declarado) {
                        situacaoChecked = viagem.atualizaSituacao(1);
                    } else if (nomeBtn == R.id.nDeclarado) {
                        situacaoChecked = viagem.atualizaSituacao(2);
                    } else if (nomeBtn == R.id.multado) {
                        situacaoChecked = viagem.atualizaSituacao(3);
                    }

                    if (!situacaoChecked && button != null) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    campoTotalUS.setText(formato.format(viagem.getTotalUS()));
                    campoTotalBR.setText(formato.format(viagem.getTotalBR()));
                }
            }
        };

        campoSituacao.setOnCheckedChangeListener(onCheckedChangeListener);

        detalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCalculatorActivity.this, DetalhesActivity.class);
                intent.putExtra("viagem", viagem);
                intent.putExtra("moedaValor", campoSimboloValor.getText());
                intent.putExtra("moedaConvert", campoSimboloConvertida.getText());
                startActivity(intent);

            }
        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viagem.limpaValores();

                campoValor.setText("0");
                campoConvert.setText("0");
                campoTotalBR.setText("0");
                campoTotalUS.setText("0");
                campoTaxa.setText("0");

                campoPagamento.clearCheck();
                campoSituacao.setOnCheckedChangeListener(null);
                campoSituacao.clearCheck();
                campoSituacao.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });

        spinnerMoedaConvert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    campoSimboloConvertida.setText("$");
                    if(moedaAPI != null) {
                        campoTaxa.setText(formato.format(moedaAPI.getRates().getUSD()));
                    }
                } else if (position == 1) {
                    campoSimboloConvertida.setText("R$");
                    if(moedaAPI != null) {
                        campoTaxa.setText(formato.format(moedaAPI.getRates().getBRL()));
                    }
                } else if (position == 2) {
                    campoSimboloConvertida.setText("€");
                    if(moedaAPI != null) {
                        campoTaxa.setText(formato.format(moedaAPI.getRates().getEur()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMoedaValor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    campoSimboloValor.setText("$");
                    retrofitService.getCurrency("USD", "USD,BRL,EUR", spinnerMoedaConvert.getSelectedItemPosition(), getApplicationContext(), MainCalculatorActivity.this);
                } else if (position == 1) {
                    campoSimboloValor.setText("R$");
                    retrofitService.getCurrency("BRL", "BRL,USD,EUR", spinnerMoedaConvert.getSelectedItemPosition(), getApplicationContext(), MainCalculatorActivity.this);

                } else if (position == 2) {
                    campoSimboloValor.setText("€");
                    retrofitService.getCurrency("EUR", "USD,BRL,EUR", spinnerMoedaConvert.getSelectedItemPosition(), getApplicationContext(), MainCalculatorActivity.this);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        changeMoeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int moeda1 = spinnerMoedaConvert.getSelectedItemPosition();
                int moeda2 = spinnerMoedaValor.getSelectedItemPosition();
                spinnerMoedaConvert.setSelection(moeda2);
                spinnerMoedaValor.setSelection(moeda1);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onGetCurrencySuccess(MoedaAPI moedaAPI){
        this.moedaAPI = moedaAPI;

        switch((int) spinnerMoedaConvert.getSelectedItemId()){
            case 0:
                campoTaxa.setText(formato.format(moedaAPI.getRates().getUSD()));
                break;
            case 1:
                campoTaxa.setText(formato.format(moedaAPI.getRates().getBRL()));
                break;
            case 2:
                campoTaxa.setText(formato.format(moedaAPI.getRates().getEur()));
                break;
        }
    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call<MoedaAPI> call, Response<MoedaAPI> response) {
        MoedaAPI moedaAPI = response.body();
        onGetCurrencySuccess(moedaAPI);
    }
    @Override
    public void onFailure(Call<MoedaAPI> call, Throwable t) {
    }
}
