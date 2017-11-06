package br.com.mobiplus.ferramentadeviajem.mvp.repository.pojo;

/**
 * Created by Develop on 03/11/2017.
 */

public class ExchangeResultInfos extends ExchangeInfos {

    private double paymentTaxAmountFrom;
    private double paymentTaxAmountTo;
    private double situationTaxAmountFrom;
    private double situationTaxAmountTo;

    public double getPaymentTaxAmountFrom() {
        return paymentTaxAmountFrom;
    }

    public void setPaymentTaxAmountFrom(double paymentTaxAmountFrom) {
        this.paymentTaxAmountFrom = paymentTaxAmountFrom;
    }

    public double getPaymentTaxAmountTo() {
        return paymentTaxAmountTo;
    }

    public void setPaymentTaxAmountTo(double paymentTaxAmountTo) {
        this.paymentTaxAmountTo = paymentTaxAmountTo;
    }

    public double getSituationTaxAmountFrom() {
        return situationTaxAmountFrom;
    }

    public void setSituationTaxAmountFrom(double situationTaxAmountFrom) {
        this.situationTaxAmountFrom = situationTaxAmountFrom;
    }

    public double getSituationTaxAmountTo() {
        return situationTaxAmountTo;
    }

    public void setSituationTaxAmountTo(double situationTaxAmountTo) {
        this.situationTaxAmountTo = situationTaxAmountTo;
    }
}
