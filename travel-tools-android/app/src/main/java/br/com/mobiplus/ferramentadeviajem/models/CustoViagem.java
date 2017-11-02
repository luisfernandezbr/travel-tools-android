package br.com.mobiplus.ferramentadeviajem.models;

import java.io.Serializable;

/**
 * Created by Develop on 02/09/2017.
 */

public class CustoViagem implements Serializable
{

    private double valor = 0;
    private double valorConvertido = 0;
    private double totalConvertido = 0;
    private double totalLocal = 0;
    private int situacao;
    private int pagamento;
    private double valorSituacao = 0;
    private double valorPagamentoConvertido = 0;
    private double valorPagamentoLocal = 0;
    private double taxa;

    public double getValorPagamentoLocal()
    {
        return valorPagamentoLocal;
    }

    public double getValor()
    {
        return valor;
    }

    public void setValor(double valor)
    {
        this.valor = valor;
    }

    public double getValorConvertido()
    {
        return valorConvertido;
    }

    public void setValorConvertido(double valorConvertido)
    {
        this.valorConvertido = valorConvertido;
    }

    public double getTotalConvertido()
    {
        return totalConvertido;
    }

    public int getSituacao()
    {
        return situacao;
    }

    public int getPagamento()
    {
        return pagamento;
    }

    public double getValorSituacao()
    {
        return valorSituacao;
    }

    public double getValorPagamentoConvertido()
    {
        return valorPagamentoConvertido;
    }

    public double getTotalLocal()
    {
        return totalLocal;
    }

    public double getTaxa()
    {
        return taxa;
    }

    public boolean atualizaValorConvertido(double valor, double taxa)
    {
        this.valor = valor;
        this.valorConvertido = valor * taxa;
        this.taxa = taxa;
        this.atualizaPagamento(this.pagamento);
        return this.atualizaSituacao(this.situacao);
    }

    public boolean atualizaSituacao(int position)
    {
        this.situacao = position;
        if (position == 0)
        {
            this.valorSituacao = 0;
        } else if (position == 1 && this.valor > 500)
        {
            this.valorSituacao = ((this.valor - 500) * 50) / 100;
        } else if ((position == 2 || position == 0) && this.valor > 500)
        {
            this.valorSituacao = 0;
        } else if (position == 3 && this.valor > 500)
        {
            this.valorSituacao = ((this.valor - 500) * 100) / 100;
        } else
        {
            this.valorSituacao = 0;
            this.atualizaValorUS();
            this.atualizaValorTotal();
            return false;
        }
        this.atualizaValorUS();
        this.valorSituacao *= this.taxa;
        this.atualizaValorTotal();
        return true;
    }

    public void atualizaValorTotal()
    {
        this.totalConvertido = this.valorConvertido + this.valorSituacao + this.valorPagamentoConvertido;
    }

    public void atualizaValorUS()
    {
        this.totalLocal = this.valor + this.valorSituacao + this.valorPagamentoLocal;
    }

    public void atualizaPagamento(int position)
    {
        this.pagamento = position;
        if (position == 1)
        {
            this.valorPagamentoLocal = (this.valor * 1.32) / 100;
            this.valorPagamentoConvertido = (this.valorConvertido * 1.32) / 100;
        } else if (position == 2)
        {
            this.valorPagamentoLocal = (this.valor * 6.34) / 100;
            this.valorPagamentoConvertido = (this.valorConvertido * 6.34) / 100;
        }
        this.atualizaValorTotal();
        this.atualizaValorUS();
    }

    public void limpaValores()
    {
        this.valor = 0;
        this.valorConvertido = 0;
        this.totalConvertido = 0;
        this.totalLocal = 0;
        this.situacao = 0;
        this.pagamento = 0;
        this.valorSituacao = 0;
        this.valorPagamentoConvertido = 0;
        this.valorPagamentoLocal = 0;
    }


}
