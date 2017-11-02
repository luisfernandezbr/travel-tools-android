package br.com.mobiplus.ferramentadeviajem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.concretesolutions.canarinho.watcher.ValorMonetarioWatcher;
import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;
import br.com.mobiplus.ferramentadeviajem.models.MoedaAPI;
import br.com.mobiplus.ferramentadeviajem.service.RetrofitService;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class MainCalculatorActivity extends AppCompatActivity implements Callback<MoedaAPI>
{
    private String array_moeda[];
    private ImageButton changeMoeda;
    private ImageView imageSwap;
    private MoedaAPI moedaAPI;
    private ExtendedEditText campoTaxa;
    private int idLocal = 0;
    private int idConvert = 1;
    private Spinner spinnerMoedaConvert;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

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
        final TextView campoTotalConvertido = (TextView) findViewById(R.id.totalConvertido);
        final TextView campoTotalLocal = (TextView) findViewById(R.id.totalLocal);
        final TextView moedaValor = findViewById(R.id.moedaValor);
        final TextView moedaConvert = findViewById(R.id.moedaConvert);
        final TextView txtTotal = findViewById(R.id.txtTotal);
        final Button detalhes = (Button) findViewById(R.id.btnDetalhes);
        final Button limpar = (Button) findViewById(R.id.btnLimpar);
        final Button infoPagamento = (Button) findViewById(R.id.infoPagamento);
        final RadioGroup campoPagamento = (RadioGroup) findViewById(R.id.rgPagamento);
        final RadioGroup campoSituacao = (RadioGroup) findViewById(R.id.rgSituacao);
        campoTaxa = (ExtendedEditText) findViewById(R.id.taxa);

        array_moeda = new String[2];
        array_moeda[0] = "USD $";
        array_moeda[1] = "EUR €";

        AlertDialog.Builder builderMoeda = new AlertDialog.Builder(MainCalculatorActivity.this);
        builderMoeda.setTitle("Moeda Local")
                .setItems(array_moeda, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        idLocal = i;
                        if (i == 0)
                        {
                            moedaValor.setText("U$");
                            campoValor.setPrefix("U$ ");
                            txtTotal.setText("Total em U$");
                            retrofitService.getCurrency("USD", "USD,BRL,EUR", getApplicationContext(), MainCalculatorActivity.this);
                        } else if (i == 1)
                        {
                            moedaValor.setText("€");
                            campoValor.setPrefix("€ ");
                            txtTotal.setText("Total em €");
                            retrofitService.getCurrency("EUR", "USD,BRL,EUR", getApplicationContext(), MainCalculatorActivity.this);
                        }
                    }
                });

        campoTotalLocal.setText("0,00");
        campoTotalConvertido.setText("0,00");

        final CustoViagem viagem = new CustoViagem();

        campoTotalConvertido.addTextChangedListener(new ValorMonetarioWatcher());
        campoTotalLocal.addTextChangedListener(new ValorMonetarioWatcher());

        campoTaxa.addTextChangedListener((new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (campoTaxa.getText().length() > 0 && campoValor.getText().length() > 0)
                    viagem.atualizaValorConvertido(getDoubleValueFrom(campoValor), getDoubleValueFrom(s.toString()));
                else
                {
                    viagem.atualizaValorConvertido(0, 0);
                }
                campoTotalConvertido.setText(String.format("%.2f", viagem.getTotalConvertido()));
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        }));

        campoValor.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (campoValor.getText().length() > 0 && campoTaxa.getText().length() > 0)
                {
                    viagem.atualizaValorConvertido(getDoubleValueFrom(charSequence.toString()), getDoubleValueFrom(campoTaxa));
                } else
                {
                    viagem.atualizaValorConvertido(0, 0);
                }
                campoTotalConvertido.setText(String.format("%.2f", viagem.getTotalConvertido()));
                campoTotalLocal.setText(String.format("%.2f", viagem.getTotalLocal()));

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        campoPagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button != null)
                {
                    String nomeBtn = button.getText().toString();

                    if (nomeBtn.equals("Dinheiro"))
                    {
                        viagem.atualizaPagamento(1);
                    } else
                    {
                        viagem.atualizaPagamento(2);
                    }
                    campoTotalLocal.setText(String.format("%.2f", viagem.getTotalLocal()));
                    campoTotalConvertido.setText(String.format("%.2f", viagem.getTotalConvertido()));
                }
            }
        });

        onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton button = (RadioButton) group.findViewById(checkedId);

                if (button != null)
                {
                    int nomeBtn = button.getId();
                    boolean situacaoChecked = false;

                    if (nomeBtn == R.id.declarado)
                    {
                        situacaoChecked = viagem.atualizaSituacao(1);
                    } else if (nomeBtn == R.id.nDeclarado)
                    {
                        situacaoChecked = viagem.atualizaSituacao(2);
                    } else if (nomeBtn == R.id.multado)
                    {
                        situacaoChecked = viagem.atualizaSituacao(3);
                    }

                    if (!situacaoChecked && button != null)
                    {
                        createDialog("Alerta", "Só há acréscimo se exceder em 500 o valor dos produtos.").show();
                    }

                    campoTotalLocal.setText(String.format("%.2f", viagem.getTotalLocal()));
                    campoTotalConvertido.setText(String.format("%.2f", viagem.getTotalConvertido()));
                }
            }
        };


        campoSituacao.setOnCheckedChangeListener(onCheckedChangeListener);


        infoPagamento.setOnClickListener(getOnClickListener(createDialog("Informação", "Opções relacionadas ao pagamento realizado na casa de câmbio.\n Dinheiro : %\n Débito/Crédito: %")));

        detalhes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainCalculatorActivity.this, DetalhesActivity.class);
                intent.putExtra("viagem", viagem);
                intent.putExtra("moedaValor", moedaValor.getText());
                intent.putExtra("moedaConvert", moedaConvert.getText());
                startActivity(intent);

            }
        });

        limpar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                viagem.limpaValores();

                campoValor.setText("0,00");
                campoTotalConvertido.setText("0,00");
                campoTotalLocal.setText("0,00");

                campoPagamento.clearCheck();
                campoSituacao.setOnCheckedChangeListener(null);
                campoSituacao.clearCheck();
                campoSituacao.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });

        moedaValor.setOnClickListener(getOnClickListener(builderMoeda.create()));

    }

    private void onGetCurrencySuccess(MoedaAPI moedaAPI)
    {
        this.moedaAPI = moedaAPI;

        switch (idConvert)
        {
            case 0:
                campoTaxa.setText(String.format("%.2f", moedaAPI.getRates().getUSD()));
                break;
            case 1:
                campoTaxa.setText(String.format("%.2f", moedaAPI.getRates().getBRL()));
                break;
            case 2:
                campoTaxa.setText(String.format("%.2f", moedaAPI.getRates().getEur()));
                break;
        }
    }

    public View.OnClickListener getOnClickListener(final Dialog dialog)
    {
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.show();
            }
        };
        return onClickListener;
    }

    private Interceptor getLoggingInterceptor()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;

    }

    @Override
    public void onResponse(Call<MoedaAPI> call, Response<MoedaAPI> response)
    {
        MoedaAPI moedaAPI = response.body();
        onGetCurrencySuccess(moedaAPI);
    }

    @Override
    public void onFailure(Call<MoedaAPI> call, Throwable t)
    {
    }

    Typeface face;

    private void setTypeface(@IdRes int resid)
    {
        TextView textView = findViewById(resid);

        if (face == null)
        {
            face = Typeface.createFromAsset(getAssets(), "fonts/roboto_thin.ttf");
        }
        textView.setTypeface(face);
    }

    private double getDoubleValueFrom(@NonNull final EditText editText)
    {
        String editTextValue = editText.getText().toString();
        return this.getDoubleValueFrom(editTextValue);
    }

    private double getDoubleValueFrom(@NonNull final String textValue)
    {
        double result = 0.0d;

        if (!TextUtils.isEmpty(textValue.trim()))
        {
            result = Double.parseDouble(textValue.replace(",", "."));

        }

        return result;
    }

    private Dialog createDialog(String title, String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainCalculatorActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);

        return builder.create();
    }
}
