package br.com.mobiplus.ferramentadeviajem.mvp.model;

import org.greenrobot.eventbus.EventBus;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CurrencyDetails;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.PaymentType;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.SituationType;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class ProductCalcModelImpl implements ProductCalcModel
{

    public static final double AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES = 500.00D;
    public static final double TAX_IOF_MONEY = 1.32;
    public static final double TAX_IOF_DEBIT_CREDIT_CARD = 6.34;

    @Override
    public void doCalculateCurrencyDetails(CurrencyDetails currencyDetails)
    {
        double exchangeRate = currencyDetails.getExchangeRate();
        double amountFrom = currencyDetails.getAmountFrom();
        PaymentType paymentType = currencyDetails.getPaymentType();
        SituationType situationType = currencyDetails.getSituationType();

        amountFrom = this.handleSituationType(amountFrom, situationType);
        amountFrom = this.handlePaymentType(amountFrom, paymentType);

        double amountTo = amountFrom * exchangeRate;

        CalculatedCurrency calculatedCurrency = new CalculatedCurrency();
        calculatedCurrency.setAmountTo(amountTo);
        calculatedCurrency.setAmountFrom(amountFrom);

        this.sendOnCurrencyCalculatedEvent(calculatedCurrency);
    }

    private double handleSituationType(double amountFrom, SituationType situationType)
    {
        if (amountFrom > AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES) {
            switch (situationType) {
                case DECLARED: {
                    amountFrom += this.calculatePlusOnDeclaredSituation(amountFrom);
                    break;
                }
                case FINED: {
                    amountFrom += this.calculatePlusOnFinedSituation(amountFrom);
                    break;
                }
                case NOT_DECLARED: {}
                case NONE: {}
                default: {}
            }
        }
        return amountFrom;
    }

    private double handlePaymentType(double amountFrom, PaymentType paymentType)
    {
        switch (paymentType) {
            case MONEY: {
                amountFrom += this.calculatePlusMoneyIof(amountFrom);
                break;
            }
            case DEBIT_CREDIT_CARD: {
                amountFrom += this.calculatePlusDebitCreditCardIof(amountFrom);
                break;
            }
            case NONE: {
                break;
            }
        }
        return amountFrom;
    }

    private double calculatePlusDebitCreditCardIof(double amountFrom)
    {
        return (amountFrom * TAX_IOF_DEBIT_CREDIT_CARD) / 100;
    }

    private double calculatePlusMoneyIof(double amountFrom)
    {
        return (amountFrom * TAX_IOF_MONEY) / 100;
    }

    private double calculatePlusOnFinedSituation(double amountFrom)
    {
        return ((amountFrom - AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES) * 100) / 100;
    }

    private double calculatePlusOnDeclaredSituation(double amountFrom)
    {
        return (amountFrom - AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES) * 50 / 100;
    }

    private void sendOnCurrencyCalculatedEvent(CalculatedCurrency calculatedCurrency)
    {
        EventBus.getDefault().post(new OnCurrencyCalculatedEvent(calculatedCurrency));
    }
}
