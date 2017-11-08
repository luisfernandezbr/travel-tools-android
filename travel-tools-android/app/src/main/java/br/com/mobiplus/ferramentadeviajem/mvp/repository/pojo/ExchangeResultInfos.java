package br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo;

import java.io.Serializable;

/**
 * Created by Develop on 03/11/2017.
 */

public class ExchangeResultInfos extends ExchangeInfos implements Serializable
{

    private double amountTo;
    private double paymentTaxAmountFrom;
    private double paymentTaxAmountTo;
    private double situationTaxAmountFrom;
    private double situationTaxAmountTo;
    private CalculatedCurrency calculatedCurrency;

    public double getPaymentTaxAmountFrom()
    {
        return paymentTaxAmountFrom;
    }

    public void setPaymentTaxAmountFrom(double paymentTaxAmountFrom)
    {
        this.paymentTaxAmountFrom = paymentTaxAmountFrom;
    }

    public double getPaymentTaxAmountTo()
    {
        return paymentTaxAmountTo;
    }

    public void setPaymentTaxAmountTo(double paymentTaxAmountTo)
    {
        this.paymentTaxAmountTo = paymentTaxAmountTo;
    }

    public double getSituationTaxAmountFrom()
    {
        return situationTaxAmountFrom;
    }

    public void setSituationTaxAmountFrom(double situationTaxAmountFrom)
    {
        this.situationTaxAmountFrom = situationTaxAmountFrom;
    }

    public double getSituationTaxAmountTo()
    {
        return situationTaxAmountTo;
    }

    public void setSituationTaxAmountTo(double situationTaxAmountTo)
    {
        this.situationTaxAmountTo = situationTaxAmountTo;
    }

    public CalculatedCurrency getCalculatedCurrency()
    {
        return calculatedCurrency;
    }

    public void setCalculatedCurrency(CalculatedCurrency calculatedCurrency)
    {
        this.calculatedCurrency = calculatedCurrency;
    }

    public double getAmountTo()
    {
        return amountTo;
    }

    public void setAmountTo(double amountTo)
    {
        this.amountTo = amountTo;
    }
}
