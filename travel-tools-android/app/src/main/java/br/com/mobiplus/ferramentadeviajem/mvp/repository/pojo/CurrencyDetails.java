package br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class CurrencyDetails
{
    private String currencyFrom;
    private String currencyTo;
    private double exchangeRate;
    private double amountFrom;
    private PaymentType paymentType;
    private SituationType situationType;

    public String getCurrencyFrom()
    {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom)
    {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo()
    {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo)
    {
        this.currencyTo = currencyTo;
    }

    public double getExchangeRate()
    {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate)
    {
        this.exchangeRate = exchangeRate;
    }

    public double getAmountFrom()
    {
        return amountFrom;
    }

    public void setAmountFrom(double amountFrom)
    {
        this.amountFrom = amountFrom;
    }

    public PaymentType getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType)
    {
        this.paymentType = paymentType;
    }

    public SituationType getSituationType()
    {
        return situationType;
    }

    public void setSituationType(SituationType situationType)
    {
        this.situationType = situationType;
    }
}
