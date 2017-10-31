package br.com.mobiplus.ferramentadeviajem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.NumberFormat;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IdRes;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.concretesolutions.canarinho.watcher.ValorMonetarioWatcher;
import br.com.mobiplus.ferramentadeviajem.models.MoedaAPI;
import br.com.mobiplus.ferramentadeviajem.service.RetrofitService;
import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class MainCalculatorActivity extends AppCompatActivity implements Callback<MoedaAPI> {
    private String array_moeda[];
    private ImageButton changeMoeda;
    private ImageView imageSwap;
    private MoedaAPI moedaAPI;
    private ExtendedEditText campoTaxa;
    private int idLocal = 0;
    private int idConvert = 1;
    //    private NumberFormat formato;
    private Spinner spinnerMoedaConvert;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

//    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SharedPreferences sharedPreferences = this.getSharedPreferences("theme", Context.MODE_PRIVATE);
        setTheme(R.style.AppThemeDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTypeface(R.id.dinheiro);
        setTypeface(R.id.cartao);
        setTypeface(R.id.declarado);
        setTypeface(R.id.nDeclarado);
        setTypeface(R.id.multado);
        setTypeface(R.id.taxa);
        setTypeface(R.id.valor);

        array_moeda = new String[3];
        array_moeda[0] = "USD $";
        array_moeda[1] = "BRL R$";
        array_moeda[2] = "EUR €";

        final RetrofitService retrofitService = new RetrofitService("http://api.fixer.io/");
        retrofitService.getCurrency("USD", "BRL,EUR", getApplicationContext(), this);


        final ExtendedEditText campoValor = (ExtendedEditText) findViewById(R.id.valor);
//        final TextView campoConvert = (TextView) findViewById(R.id.convert);
        final TextView campoTotalBR = (TextView) findViewById(R.id.totalBR);
        final TextView campoTotalUS = (TextView) findViewById(R.id.totalUS);
//        final TextView campoSimboloValor = (TextView) findViewById(R.id.simboloMoedaValor);
//        final TextView campoSimboloConvertida = (TextView) findViewById(R.id.simboloMoedaConvertida);
        final TextView moedaValor = findViewById(R.id.moedaValor);
        final TextView moedaConvert = findViewById(R.id.moedaConvert);
        final Button detalhes = (Button) findViewById(R.id.btnDetalhes);
        final Button limpar = (Button) findViewById(R.id.btnLimpar);
        final RadioGroup campoPagamento = (RadioGroup) findViewById(R.id.rgPagamento);
        final RadioGroup campoSituacao = (RadioGroup) findViewById(R.id.rgSituacao);
        final LinearLayout moedaEuro = (LinearLayout) findViewById(R.id.moedaEuro);
        imageSwap = findViewById(R.id.imageSwap);
//        changeMoeda = (ImageButton) findViewById(R.id.imagem_change);
        campoTaxa = (ExtendedEditText) findViewById(R.id.taxa);

        array_moeda = new String[3];
        array_moeda[0] = "USD $";
        array_moeda[1] = "BRL R$";
        array_moeda[2] = "EUR €";




/*
        final Spinner spinnerMoedaValor = (Spinner) findViewById(R.id.moedaValor);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_moeda);
        spinnerMoedaValor.setAdapter(adapter);

        spinnerMoedaConvert = (Spinner) findViewById(R.id.moedaConvert);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_moeda);
        spinnerMoedaConvert.setAdapter(adapter);
        spinnerMoedaConvert.setSelection(1);
        */

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainCalculatorActivity.this);
        builder.setMessage("Só há acréscimo se exceder em 500 o valor dos produtos.");
        builder.setTitle("Alerta");

        AlertDialog.Builder builderMoeda = new AlertDialog.Builder(MainCalculatorActivity.this);
        builderMoeda.setTitle("Moeda Local")
                .setItems(array_moeda, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idLocal = i;
                        if (i == 0) {
                            moedaValor.setText("U$");
                            campoTaxa.setPrefix("U$ ");
                            retrofitService.getCurrency("USD", "USD,BRL,EUR",  getApplicationContext(), MainCalculatorActivity.this);
                        } else if (i == 1) {
                            moedaValor.setText("R$");
                            campoTaxa.setPrefix("R$ ");
                            retrofitService.getCurrency("BRL", "BRL,USD,EUR",  getApplicationContext(), MainCalculatorActivity.this);

                        } else if (i == 2) {
                            moedaValor.setText("€");
                            campoTaxa.setPrefix("€ ");
                            retrofitService.getCurrency("EUR", "USD,BRL,EUR", getApplicationContext(), MainCalculatorActivity.this);
                        }
                    }
                });

        final Dialog dialogMoedalocal = builderMoeda.create();

        builderMoeda = new AlertDialog.Builder(MainCalculatorActivity.this);
        builderMoeda.setTitle("Moeda para conversão")
                .setItems(array_moeda, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            moedaConvert.setText("U$");
                            campoValor.setPrefix("U$ ");
                            if (moedaAPI != null) {
                                campoTaxa.setText(String.format("%.2f", moedaAPI.getRates().getUSD()));
                            }
                        } else if (i == 1) {
                            moedaConvert.setText("R$");
                            campoValor.setPrefix("R$ ");
                            if (moedaAPI != null) {
                                campoTaxa.setText(String.format("%.2f", moedaAPI.getRates().getBRL()));
                            }
                        } else if (i == 2) {
                            moedaConvert.setText("€");
                            campoValor.setPrefix("€ ");
                            if (moedaAPI != null) {
                                campoTaxa.setText(String.format("%.2f", moedaAPI.getRates().getEur()));
                            }
                        }
                    }
                });

        final Dialog dialogMoedaConvert = builderMoeda.create();


//        formato = NumberFormat.getInstance();
//        formato.setMinimumFractionDigits(2);
//        formato.setMaximumFractionDigits(2);

//        campoConvert.setText("0");
        campoValor.setText("0");
        campoTotalUS.setText("0");
        campoTotalBR.setText("0");

        final CustoViagem viagem = new CustoViagem();

        campoTotalBR.addTextChangedListener(new ValorMonetarioWatcher());
        campoTotalUS.addTextChangedListener(new ValorMonetarioWatcher());

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
//                campoConvert.setText(String.format("%.2f", viagem.getValorConvertido()));
                campoTotalBR.setText(String.format("%.2f", viagem.getTotalBR()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }));

        campoValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setFocusable(true);
            }
        });


        campoValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (campoValor.getText().length() > 0 && campoTaxa.getText().length() > 0) {
                    viagem.atualizaValorConvertido(Double.parseDouble(charSequence.toString()), Double.parseDouble(campoTaxa.getText().toString()));
                }
                else {
                    viagem.atualizaValorConvertido(0, 0);
                }
//                campoConvert.setText(String.format("%.2f", viagem.getValorConvertido()));
                campoTotalBR.setText(String.format("%.2f", viagem.getTotalBR()));
                campoTotalUS.setText(String.format("%.2f", viagem.getTotalUS()));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

/*        campoValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((campoValor.getText().length() > 0 && campoTaxa.getText().length() > 0) &&
                        (campoValor.getText() != null && campoTaxa.getText() != null))
                    viagem.atualizaValorConvertido(Double.parseDouble(s.toString()), Double.parseDouble(campoTaxa.getText().toString()));
                else {
                    viagem.atualizaValorConvertido(0, 0);
                }
//                campoConvert.setText(String.format("%.2f", viagem.getValorConvertido()));
                campoTotalBR.setText(String.format("%.2f", viagem.getTotalBR()));
                campoTotalUS.setText(String.format("%.2f", viagem.getTotalUS()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

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
                    campoTotalUS.setText(String.format("%.2f",viagem.getTotalUS()));
                    campoTotalBR.setText(String.format("%.2f", viagem.getTotalBR()));
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

                    campoTotalUS.setText(String.format("%.2f", viagem.getTotalUS()));
                    campoTotalBR.setText(String.format("%.2f", viagem.getTotalBR()));
                }
            }
        };

        campoSituacao.setOnCheckedChangeListener(onCheckedChangeListener);

        detalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCalculatorActivity.this, DetalhesActivity.class);
                intent.putExtra("viagem", viagem);
                intent.putExtra("moedaValor", moedaValor.getText());
              intent.putExtra("moedaConvert", moedaConvert.getText());
                startActivity(intent);

            }
        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viagem.limpaValores();

                campoValor.setText("0");
  //              campoConvert.setText("0");
                campoTotalBR.setText("0");
                campoTotalUS.setText("0");

                campoPagamento.clearCheck();
                campoSituacao.setOnCheckedChangeListener(null);
                campoSituacao.clearCheck();
                campoSituacao.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });


        moedaValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMoedalocal.show();
            }
        });

        moedaConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMoedaConvert.show();
            }
        });

        imageSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int idAux = idConvert;
                idConvert = idLocal;
                idLocal = idAux;


                String textAux = moedaValor.getText().toString();
                moedaValor.setText(moedaConvert.getText());
                moedaConvert.setText(textAux);

                campoTaxa.setPrefix(moedaValor.getText().toString()+" ");
                campoValor.setPrefix(moedaConvert.getText().toString()+" ");

                if (idAux == 0) {
                    retrofitService.getCurrency("USD", "USD,BRL,EUR",  getApplicationContext(), MainCalculatorActivity.this);
                } else if (idAux == 1) {
                    retrofitService.getCurrency("BRL", "BRL,USD,EUR",  getApplicationContext(), MainCalculatorActivity.this);
                } else if (idAux == 2) {
                    retrofitService.getCurrency("EUR", "USD,BRL,EUR", getApplicationContext(), MainCalculatorActivity.this);
                }

            }
        });

    }

    private void testando(View view){
        view.getId();
    }

        /*
        spinnerMoedaConvert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    campoSimboloConvertida.setText("$");
                    if(moedaAPI != null) {
                        campoTaxa.setText(String.format("%.2f",moedaAPI.getRates().getUSD()));
                    }
                } else if (position == 1) {
                    campoSimboloConvertida.setText("R$");
                    if(moedaAPI != null) {
                        campoTaxa.setText(String.format("%.2f",moedaAPI.getRates().getBRL()));
                    }
                } else if (position == 2) {
                    campoSimboloConvertida.setText("€");
                    if(moedaAPI != null) {
                        campoTaxa.setText(String.format("%.2f",moedaAPI.getRates().getEur()));
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
        });*/

/*
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
    */

    private void onGetCurrencySuccess(MoedaAPI moedaAPI){
        this.moedaAPI = moedaAPI;

        switch(idConvert){
            case 0:
                campoTaxa.setText(String.format("%.2f",moedaAPI.getRates().getUSD()));
                break;
            case 1:
                campoTaxa.setText(String.format("%.2f",moedaAPI.getRates().getBRL()));
                break;
            case 2:
                campoTaxa.setText(String.format("%.2f",moedaAPI.getRates().getEur()));
                break;
        }
    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;

    }

    @Override
    public void onResponse(Call<MoedaAPI> call, Response<MoedaAPI> response) {
        MoedaAPI moedaAPI = response.body();
        onGetCurrencySuccess(moedaAPI);
    }
    @Override
    public void onFailure(Call<MoedaAPI> call, Throwable t) {
    }

    Typeface face;

    private void setTypeface (@IdRes int resid) {
        TextView textView = findViewById(resid);

        if (face == null) {
            face=Typeface.createFromAsset(getAssets(),"fonts/roboto_thin.ttf");
        }
        textView.setTypeface(face);
    }

    private double getDoubleValueFrom(@NonNull final EditText editText) {
        String editTextValue = editText.getText().toString();
        return this.getDoubleValueFrom(editTextValue);
    }

    private double getDoubleValueFrom(@NonNull final String textValue) {
        double result = 0.0d;

        if (!TextUtils.isEmpty(textValue.trim())) {
            result = Double.parseDouble(textValue.replace(",","."));

        }

        return result;
    }
}
