package br.com.mobiplus.ferramentadeviajem.mvp.model;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.com.mobiplus.ferramentadeviajem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.CalculatedCurrency;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeInfos;
import br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo.ExchangeResultInfos;
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
    public void doCalculateExchangeInfos(ExchangeInfos exchangeInfos)
    {
        ExchangeResultInfos exchangeResultInfos = new ExchangeResultInfos();
        exchangeResultInfos.setCalculatedCurrency(handleCurrencyCalculated(exchangeInfos));

        double exchangeRate = exchangeInfos.getExchangeRate();
        double amountFrom = exchangeInfos.getAmountFrom();
        PaymentType paymentType = exchangeInfos.getPaymentType();
        SituationType situationType = exchangeInfos.getSituationType();

        double amountTo = amountFrom * exchangeRate;
        double situationTypeTaxFrom = this.calculateSituationType(amountFrom, situationType);
        double situationTypeTaxTo = situationTypeTaxFrom * exchangeRate;
        double paymentTypeTaxFrom = this.calculatePaymentType(amountFrom, paymentType);
        double paymentTypeTaxTo = paymentTypeTaxFrom * exchangeRate;

        exchangeResultInfos.setPaymentTaxAmountFrom(paymentTypeTaxFrom);
        exchangeResultInfos.setPaymentTaxAmountTo(paymentTypeTaxTo);

        exchangeResultInfos.setSituationTaxAmountFrom(situationTypeTaxFrom);
        exchangeResultInfos.setSituationTaxAmountTo(situationTypeTaxTo);

        exchangeResultInfos.setCurrencyFrom(exchangeInfos.getCurrencyFrom());
        exchangeResultInfos.setCurrencyTo(exchangeInfos.getCurrencyTo());
        exchangeResultInfos.setAmountFrom(amountFrom);
        exchangeResultInfos.setExchangeRate(exchangeRate);
        exchangeResultInfos.setPaymentType(paymentType);
        exchangeResultInfos.setSituationType(situationType);

        this.sendOnExchangeResultInfoCalculatedEvent(exchangeResultInfos);
    }

    @Override
    public void doCalculateCurrencyDetails(ExchangeInfos exchangeInfos)
    {
        CalculatedCurrency calculatedCurrency = handleCurrencyCalculated(exchangeInfos);

        this.sendOnCurrencyCalculatedEvent(calculatedCurrency);
    }

    private CalculatedCurrency handleCurrencyCalculated(ExchangeInfos exchangeInfos){

        double exchangeRate = exchangeInfos.getExchangeRate();
        double amountFrom = exchangeInfos.getAmountFrom();
        PaymentType paymentType = exchangeInfos.getPaymentType();
        SituationType situationType = exchangeInfos.getSituationType();

        amountFrom += (this.calculateSituationType(amountFrom, situationType) + this.calculatePaymentType(amountFrom, paymentType));

        double amountTo = amountFrom * exchangeRate;

        CalculatedCurrency calculatedCurrency = new CalculatedCurrency();
        calculatedCurrency.setAmountFrom(amountFrom);
        calculatedCurrency.setAmountTo(amountTo);

        return calculatedCurrency;

    }

    private double calculateSituationType(final double amount, SituationType situationType)
    {
        double situationTaxResult = 0.00d;
        if (amount > AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES) {
            switch (situationType) {
                case DECLARED: {
                    situationTaxResult = this.calculatePlusOnDeclaredSituation(amount);
                    break;
                }
                case FINED: {
                    situationTaxResult = this.calculatePlusOnFinedSituation(amount);
                    break;
                }
                case NOT_DECLARED: {}
                case NONE: {}
                default: {}
            }
        }
        return situationTaxResult;
    }

    private double calculatePaymentType(final double amount, PaymentType paymentType)
    {
        double paymentTaxResult = 0.00d;
        switch (paymentType) {
            case MONEY: {
                paymentTaxResult += this.calculatePlusMoneyIof(amount);
                break;
            }
            case DEBIT_CREDIT_CARD: {
                paymentTaxResult += this.calculatePlusDebitCreditCardIof(amount);
                break;
            }
            case NONE: {
                break;
            }
        }
        return paymentTaxResult;
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

    private void sendOnExchangeResultInfoCalculatedEvent(ExchangeResultInfos exchangeResultInfos)
    {
        EventBus.getDefault().post(new OnExchangeResultInfoCalculatedEvent(exchangeResultInfos));
    }
}
