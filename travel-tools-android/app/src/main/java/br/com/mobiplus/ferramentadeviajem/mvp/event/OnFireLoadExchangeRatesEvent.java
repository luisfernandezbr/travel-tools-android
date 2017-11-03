package br.com.mobiplus.ferramentadeviajem.mvp.event;

/**
 * Created by luisfernandez on 03/11/17.
 */

public class OnFireLoadExchangeRatesEvent
{
    private final String currencyFrom;
    private final String[] curreciesTo;

    public OnFireLoadExchangeRatesEvent(String currencyFrom, String[] curreciesTo)
    {
        this.currencyFrom = currencyFrom;
        this.curreciesTo = curreciesTo;
    }

    public String getCurrencyFrom()
    {
        return currencyFrom;
    }

    public String[] getCurreciesTo()
    {
        return curreciesTo;
    }
}
