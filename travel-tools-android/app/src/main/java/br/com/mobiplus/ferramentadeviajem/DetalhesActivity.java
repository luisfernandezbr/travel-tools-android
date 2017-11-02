package br.com.mobiplus.ferramentadeviajem;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;

@EActivity(R.layout.activity_detalhes)
public class DetalhesActivity extends AppCompatActivity
{
    @Extra
    CustoViagem custoViagem;

    @Extra
    String moedaValor;

    @Extra
    String moedaConvertida;

    public static void start(Context context, CustoViagem custoViagem, String moedaValor, String moedaConvertida)
    {
        DetalhesActivity_.intent(context)
                .custoViagem(custoViagem)
                .moedaConvertida(moedaConvertida)
                .moedaValor(moedaValor)
                .start();
    }

    @AfterViews
    public void afterViews() {
        TextView campoValor = (TextView) findViewById(R.id.valorTabela);
        TextView campoValorConvertido = (TextView) findViewById(R.id.valorConvertidoTabela);
        TextView campoSituacaoConvertido = (TextView) findViewById(R.id.situacaoConvertidaTabela);
        TextView campoSituacaoValor = (TextView) findViewById(R.id.situacaoValorTabela);
        TextView campoPagamentoConvertido = (TextView) findViewById(R.id.pagamentoTabela);
        TextView campoPagamentoLocal = (TextView) findViewById(R.id.pagamentoTabelaLocal);
        TextView campoTotalBR = (TextView) findViewById(R.id.totalConvertidoTabela);
        TextView campoTotalUS = (TextView) findViewById(R.id.totalValorTabela);
        TextView campoTaxa = (TextView) findViewById(R.id.taxaTabela);
        TextView txtTaxa = (TextView) findViewById(R.id.txtTaxa);
        TextView txtSituacaoConvertido = (TextView) findViewById(R.id.txtSituacaoConvertido);
        TextView txtSituacaoValor = (TextView) findViewById(R.id.txtSituacaoValor);
        TextView txtPagamento = (TextView) findViewById(R.id.txtPagamento);
        TextView txtPagamentoLocal = (TextView) findViewById(R.id.txtPagamentoLocal);
        TextView txtDetalhamentoValor = (TextView) findViewById(R.id.detalhamentoValor);
        TextView txtDetalhamentoConvertido = (TextView) findViewById(R.id.detalhamentoConvertido);


        txtDetalhamentoConvertido.setText("Resumo do Custo em (" + moedaConvertida + ")");
        txtDetalhamentoValor.setText("Resumo do Custo em (" + moedaValor + ")");
        txtTaxa.setText("De (" + moedaValor + ") Para (" + moedaConvertida + ")");


        campoTaxa.setText(moedaValor + " " + format(custoViagem.getTaxa()));
        campoValor.setText(moedaValor + " " + format(custoViagem.getValor()));
        campoValorConvertido.setText(moedaConvertida + " " + format(custoViagem.getValorConvertido()));
        campoTotalBR.setText(moedaConvertida + " " + format(custoViagem.getTotalConvertido()));
        campoTotalUS.setText(moedaValor + " " + format(custoViagem.getTotalLocal()));
        campoSituacaoConvertido.setText(moedaConvertida + " " + format(custoViagem.getValorSituacao()));
        campoPagamentoConvertido.setText(moedaConvertida + " " + format(custoViagem.getValorPagamentoConvertido()));
        campoPagamentoLocal.setText(moedaValor + " " + format(custoViagem.getValorPagamentoLocal()));

        if (custoViagem.getSituacao() == 1)
        {
            txtSituacaoValor.setText("Situação de Declaração (Declarado) - 50%");
            txtSituacaoConvertido.setText("Situação de Declaração (Declarado) - 50%");
            if (custoViagem.getValor() > 500)
            {
                campoSituacaoValor.setText(moedaValor + " " + format(((custoViagem.getValor() - 500) * 50) / 100));
            }
        } else if (custoViagem.getSituacao() == 2)
        {
            txtSituacaoValor.setText("Situação de Declaração (Não declarado) - 0%");
            txtSituacaoConvertido.setText("Situação de Declaração (Não declarado) - 0%");
        } else if (custoViagem.getSituacao() == 3)
        {
            txtSituacaoValor.setText("Situação de Declaração (Multado) - 100%");
            txtSituacaoConvertido.setText("Situação de Declaração (Multado) - 100%");
            if (custoViagem.getValor() > 500)
            {
                campoSituacaoValor.setText(moedaValor + " " + format(((custoViagem.getValor() - 500) * 100) / 100));
            }
        }

        if (custoViagem.getPagamento() == 1)
        {
            txtPagamento.setText("Tipo de Pagamento (Dinheiro)");
            txtPagamentoLocal.setText("Tipo de Pagamento (Dinheiro)");
        } else if (custoViagem.getPagamento() == 2)
        {
            txtPagamento.setText("Tipo de Pagamento (Cartão)");
            txtPagamentoLocal.setText("Tipo de Pagamento (Cartão)");
        }
    }

    private String format(Double text)
    {
        String aux = String.format("%.2f", text);

        aux = aux.replace(".", ",");
        StringBuilder stringBuilder = new StringBuilder(aux);
        if (aux.length() <= 9 && aux.length() > 6)
            stringBuilder.insert(aux.length() - 6, ".");
        else if (aux.length() <= 12 && aux.length() > 9)
        {
            stringBuilder.insert(aux.length() - 6, ".");
            stringBuilder.insert(aux.length() - 9, ".");
        } else if (aux.length() <= 15 && aux.length() > 12)
        {
            stringBuilder.insert(aux.length() - 6, ".");
            stringBuilder.insert(aux.length() - 9, ".");
            stringBuilder.insert(aux.length() - 12, ".");

        }

        return stringBuilder.toString();
    }
}
