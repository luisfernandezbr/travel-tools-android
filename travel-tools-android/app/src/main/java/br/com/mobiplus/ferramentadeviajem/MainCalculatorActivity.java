package br.com.mobiplus.ferramentadeviajem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;
import br.com.mobiplus.ferramentadeviajem.repository.CurrencyRepository;
import br.com.mobiplus.ferramentadeviajem.repository.CurrencyRepositoryImpl;
import br.com.mobiplus.ferramentadeviajem.repository.DataCallback;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class MainCalculatorActivity extends AppCompatActivity implements DataCallback<CurrencyExchange, String>
{
    public static final String TAG = "MainCalculatorActivity";
    private String array_moeda[];
    private ImageButton changeMoeda;
    private ImageView imageSwap;
    private CurrencyExchange moedaAPI;
    private ExtendedEditText editCurrencyExchange;
    private int idLocal = 0;
    private int idConvert = 1;
    private Spinner spinnerMoedaConvert;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    private Typeface face;

    private CurrencyRepository currencyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configTypefaces();

        currencyRepository = new CurrencyRepositoryImpl(this);
        currencyRepository.loadCurrencyExchange("USD", new String[]{"BRL","EUR"}, this);

        final ExtendedEditText editAmount = (ExtendedEditText) findViewById(R.id.editAmout);
        final TextView textAmountTo = (TextView) findViewById(R.id.textAmountTo);
        final TextView textAmountFrom = (TextView) findViewById(R.id.textAmountFrom);
        final TextView moedaValor = findViewById(R.id.textCurrencySimbolFrom);
        final TextView moedaConvert = findViewById(R.id.textCurrencySimbolTo);
        final TextView txtTotal = findViewById(R.id.txtTotal);
        final Button buttonGoToDetails = (Button) findViewById(R.id.buttonGoToDetails);
        final Button buttonCleanForm = (Button) findViewById(R.id.buttonCleanForm);
        final Button buttonInfoPayment = (Button) findViewById(R.id.buttonInfoPayment);
        final RadioGroup radioPaymentType = (RadioGroup) findViewById(R.id.radioPaymentType);
        final RadioGroup radioSituationType = (RadioGroup) findViewById(R.id.radioSituationType);
        editCurrencyExchange = (ExtendedEditText) findViewById(R.id.editCurrencyExchange);

        array_moeda = new String[2];
        array_moeda[0] = "USD $";
        array_moeda[1] = "EUR €";

        AlertDialog.Builder dialogBuilderMoeda = new AlertDialog.Builder(MainCalculatorActivity.this);
        dialogBuilderMoeda.setTitle("Moeda Local")
                .setItems(array_moeda, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        idLocal = i;
                        if (i == 0)
                        {
                            moedaValor.setText("U$");
                            editAmount.setPrefix("U$ ");
                            txtTotal.setText("Total em U$");

                            //retrofitService.getCurrency("USD", "USD,BRL,EUR", getApplicationContext(), MainCalculatorActivity.this);
                            currencyRepository.loadCurrencyExchange("USD", new String[]{"EUR", "BRL"}, MainCalculatorActivity.this);
                        } else if (i == 1)
                        {
                            moedaValor.setText("€");
                            editAmount.setPrefix("€ ");
                            txtTotal.setText("Total em €");
                            //retrofitService.getCurrency("EUR", "USD,BRL,EUR", getApplicationContext(), MainCalculatorActivity.this);
                            currencyRepository.loadCurrencyExchange("EUR", new String[]{"USD", "BRL"}, MainCalculatorActivity.this);
                        }
                    }
                });

        textAmountFrom.setText("0,00");
        textAmountTo.setText("0,00");

        final CustoViagem custoViagem = new CustoViagem();

        textAmountTo.addTextChangedListener(new ValorMonetarioWatcher());
        textAmountFrom.addTextChangedListener(new ValorMonetarioWatcher());

        editCurrencyExchange.addTextChangedListener((new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (editCurrencyExchange.getText().length() > 0 && editAmount.getText().length() > 0)
                    custoViagem.atualizaValorConvertido(getDoubleValueFrom(editAmount), getDoubleValueFrom(s.toString()));
                else
                {
                    custoViagem.atualizaValorConvertido(0, 0);
                }
                textAmountTo.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        }));

        editAmount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (editAmount.getText().length() > 0 && editCurrencyExchange.getText().length() > 0)
                {
                    custoViagem.atualizaValorConvertido(getDoubleValueFrom(charSequence.toString()), getDoubleValueFrom(editCurrencyExchange));
                } else
                {
                    custoViagem.atualizaValorConvertido(0, 0);
                }
                textAmountTo.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
                textAmountFrom.setText(String.format("%.2f", custoViagem.getTotalLocal()));

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        radioPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
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
                        custoViagem.atualizaPagamento(1);
                    } else
                    {
                        custoViagem.atualizaPagamento(2);
                    }
                    textAmountFrom.setText(String.format("%.2f", custoViagem.getTotalLocal()));
                    textAmountTo.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
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
                        situacaoChecked = custoViagem.atualizaSituacao(1);
                    } else if (nomeBtn == R.id.nDeclarado)
                    {
                        situacaoChecked = custoViagem.atualizaSituacao(2);
                    } else if (nomeBtn == R.id.multado)
                    {
                        situacaoChecked = custoViagem.atualizaSituacao(3);
                    }

                    if (!situacaoChecked && button != null)
                    {
                        String dialogTitle = "Alerta";
                        String message = "Só há acréscimo se exceder em 500 o valor dos produtos.";
                        showDialog(dialogTitle, message);
                    }

                    textAmountFrom.setText(String.format("%.2f", custoViagem.getTotalLocal()));
                    textAmountTo.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
                }
            }
        };


        radioSituationType.setOnCheckedChangeListener(onCheckedChangeListener);


        buttonInfoPayment.setOnClickListener(getOnClickListener(createDialog("Informação", "Opções relacionadas ao pagamento realizado na casa de câmbio.\n Dinheiro : %\n Débito/Crédito: %")));

        buttonGoToDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainCalculatorActivity.this, DetalhesActivity.class);
                intent.putExtra("custoViagem", custoViagem);
                intent.putExtra("moedaValor", moedaValor.getText());
                intent.putExtra("moedaConvert", moedaConvert.getText());
                startActivity(intent);

            }
        });

        buttonCleanForm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                custoViagem.limpaValores();

                editAmount.setText("0,00");
                textAmountTo.setText("0,00");
                textAmountFrom.setText("0,00");

                radioPaymentType.clearCheck();
                radioSituationType.setOnCheckedChangeListener(null);
                radioSituationType.clearCheck();
                radioSituationType.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });

        AlertDialog alertDialog = dialogBuilderMoeda.create();
        View.OnClickListener onClickListener = this.getOnClickListener(alertDialog);
        moedaValor.setOnClickListener(onClickListener);
    }

    private void configTypefaces()
    {
        setTypeface(R.id.dinheiro);
        setTypeface(R.id.cartao);
        setTypeface(R.id.declarado);
        setTypeface(R.id.nDeclarado);
        setTypeface(R.id.multado);
        setTypeface(R.id.editCurrencyExchange);
        setTypeface(R.id.editAmout);
    }

    private void onGetCurrencySuccess(CurrencyExchange moedaAPI)
    {
        this.moedaAPI = moedaAPI;

        switch (idConvert)
        {
            case 0:
                editCurrencyExchange.setText(String.format("%.2f", moedaAPI.getRates().getUSD()));
                break;
            case 1:
                editCurrencyExchange.setText(String.format("%.2f", moedaAPI.getRates().getBRL()));
                break;
            case 2:
                editCurrencyExchange.setText(String.format("%.2f", moedaAPI.getRates().getEur()));
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

    @Override
    public void onSuccess(CurrencyExchange currencyExchange)
    {
        onGetCurrencySuccess(currencyExchange);
    }

    @Override
    public void onError(String s)
    {
        Log.e(TAG, "Error when loading currency exchanges");
    }

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

    private void showDialog(String dialogTitle, String message)
    {
        createDialog(dialogTitle, message).show();
    }
}
