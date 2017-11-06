package br.com.mobiplus.ferramentadeviajem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.concretesolutions.canarinho.watcher.ValorMonetarioWatcher;
import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;
import br.com.mobiplus.ferramentadeviajem.models.CurrencyExchange;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.presenter.ProductCalcPresenter;
import br.com.mobiplus.ferramentadeviajem.mvp.presenter.ProductCalcPresenterImpl;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.ExchangeRatesRepository;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.PaymentType;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.SituationType;
import br.com.mobiplus.ferramentadeviajem.mvp.view.ProductCalcView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

@EActivity(R.layout.activity_main)
public class ProductCalcActivity extends AppCompatActivity implements ProductCalcView
{
    public static final String TAG = "ProductCalcActivity";

    private String array_moeda[];
    private ImageButton changeMoeda;
    private ImageView imageSwap;
    private CurrencyExchange moedaAPI;
    private int idLocal = 0;
    private int idConvert = 1;
    private Spinner spinnerMoedaConvert;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    private Typeface face;

    private ExchangeRatesRepository exchangeRatesRepository;

    @ViewById(R.id.editAmout)
    ExtendedEditText editAmount;

    @ViewById(R.id.editCurrencyExchange)
    ExtendedEditText editCurrencyExchange;

    @ViewById
    TextView textCurrencySymbolFrom;

    @ViewById
    TextView textCurrencySymbolTo;

    @ViewById
    TextView textAmountFromLabel;

    @ViewById
    TextView textAmountFromValue;

    @ViewById
    TextView textAmountToValue;

    @ViewById
    Button buttonGoToDetails;

    @ViewById
    Button buttonCleanForm;

    @ViewById
    Button buttonInfoPayment;

    @ViewById
    RadioGroup radioPaymentType;

    @ViewById
    RadioGroup radioSituationType;

    private ProductCalcPresenter presenter;

    @AfterViews
    public void afterViews() {
        this.configTypefaces();

//        exchangeRatesRepository = new ExchangeRatesRepositoryImpl(this);
//        exchangeRatesRepository.loadExchangeRates("USD", new String[]{"BRL","EUR"}, this);

        array_moeda = new String[2];
        array_moeda[0] = "USD $";
        array_moeda[1] = "EUR €";

        AlertDialog.Builder dialogBuilderMoeda = new AlertDialog.Builder(ProductCalcActivity.this);
        dialogBuilderMoeda.setTitle("Moeda Local")
                .setItems(array_moeda, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        idLocal = i;
                        if (i == 0)
                        {
                            textCurrencySymbolFrom.setText("U$");
                            editAmount.setPrefix("U$ ");
                            textAmountFromLabel.setText("Total em U$");

                            ProductCalcActivity.this.fireLoadExchangeRatesEvent();
                        } else if (i == 1)
                        {
                            textCurrencySymbolFrom.setText("€");
                            editAmount.setPrefix("€ ");
                            textAmountFromLabel.setText("Total em €");

                            ProductCalcActivity.this.fireLoadExchangeRatesEvent();
                        }
                    }
                });

        textAmountFromValue.setText("0,00");
        textAmountToValue.setText("0,00");

        final CustoViagem custoViagem = new CustoViagem();

        textAmountToValue.addTextChangedListener(new ValorMonetarioWatcher());
        textAmountFromValue.addTextChangedListener(new ValorMonetarioWatcher());

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
                textAmountToValue.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
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
                textAmountToValue.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
                textAmountFromValue.setText(String.format("%.2f", custoViagem.getTotalLocal()));
                fireCurrencyDetailsUpdateEvent();
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
                    textAmountFromValue.setText(String.format("%.2f", custoViagem.getTotalLocal()));
                    textAmountToValue.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
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

                    textAmountFromValue.setText(String.format("%.2f", custoViagem.getTotalLocal()));
                    textAmountToValue.setText(String.format("%.2f", custoViagem.getTotalConvertido()));
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
                String moedaValorString = textCurrencySymbolFrom.getText().toString();
                String moedaConvertidaString = textCurrencySymbolTo.getText().toString();

                DetalhesActivity.start(ProductCalcActivity.this, custoViagem, moedaValorString, moedaConvertidaString);
            }
        });

        buttonCleanForm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                custoViagem.limpaValores();

                editAmount.setText("0,00");
                textAmountToValue.setText("0,00");
                textAmountFromValue.setText("0,00");

                radioPaymentType.clearCheck();
                radioSituationType.setOnCheckedChangeListener(null);
                radioSituationType.clearCheck();
                radioSituationType.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });

        AlertDialog alertDialog = dialogBuilderMoeda.create();
        View.OnClickListener onClickListener = this.getOnClickListener(alertDialog);
        textCurrencySymbolFrom.setOnClickListener(onClickListener);

        this.presenter = new ProductCalcPresenterImpl(this);
        this.fireLoadExchangeRatesEvent();
    }

    private void fireLoadExchangeRatesEvent()
    {
        this.presenter.onFireLoadExchangeRatesEvent(new OnFireLoadExchangeRatesEvent("USD", new String[]{"BRL","EUR"}));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.presenter.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.presenter.onStop();
    }

    @Override
    public void updateAmounts(CalculatedCurrency calculatedCurrency)
    {
        Log.d(TAG, String.format("USD: %f, BRL: %f", calculatedCurrency.getAmountFrom(), calculatedCurrency.getAmountTo()));
    }

    @Override
    public void onCurrencyExchangeLoaded(CurrencyExchange currencyExchange)
    {
        this.moedaAPI = currencyExchange;

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

    private void fireCurrencyDetailsUpdateEvent() {
        ExchangeInfos exchangeInfos = new ExchangeInfos();

        exchangeInfos.setExchangeRate(this.moedaAPI.getRates().getBRL());
        exchangeInfos.setAmountFrom(this.getDoubleValueFrom(editAmount));
        exchangeInfos.setCurrencyFrom("USD");
        exchangeInfos.setCurrencyTo("BRL");
        exchangeInfos.setPaymentType(PaymentType.NONE);
        exchangeInfos.setSituationType(SituationType.NONE);

        OnFireCurrencyDetailsUpdateEvent event = new OnFireCurrencyDetailsUpdateEvent(exchangeInfos);
        this.presenter.onFireCurrencyDetailsUpdate(event);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductCalcActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);

        return builder.create();
    }

    private void showDialog(String dialogTitle, String message)
    {
        createDialog(dialogTitle, message).show();
    }
}
