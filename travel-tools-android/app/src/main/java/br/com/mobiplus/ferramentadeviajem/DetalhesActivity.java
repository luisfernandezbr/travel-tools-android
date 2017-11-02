package br.com.mobiplus.ferramentadeviajem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;

public class DetalhesActivity extends AppCompatActivity
{


    public static void start(Context context, CustoViagem custoViagem, String moedaValor, String moedaConvertida) {
        Intent intent = new Intent(context, DetalhesActivity.class);
        intent.putExtra("custoViagem", custoViagem);
        intent.putExtra("moedaValor", moedaValor);
        intent.putExtra("moedaConvert", moedaConvertida);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        CustoViagem viagem = (CustoViagem) getIntent().getSerializableExtra("viagem");
        String moedaValor = getIntent().getStringExtra("moedaValor");
        String moedaConvertida = getIntent().getStringExtra("moedaConvert");
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


        campoTaxa.setText(moedaValor + " " + format(viagem.getTaxa()));
        campoValor.setText(moedaValor + " " + format(viagem.getValor()));
        campoValorConvertido.setText(moedaConvertida + " " + format(viagem.getValorConvertido()));
        campoTotalBR.setText(moedaConvertida + " " + format(viagem.getTotalConvertido()));
        campoTotalUS.setText(moedaValor + " " + format(viagem.getTotalLocal()));
        campoSituacaoConvertido.setText(moedaConvertida + " " + format(viagem.getValorSituacao()));
        campoPagamentoConvertido.setText(moedaConvertida + " " + format(viagem.getValorPagamentoConvertido()));
        campoPagamentoLocal.setText(moedaValor + " " + format(viagem.getValorPagamentoLocal()));

        if (viagem.getSituacao() == 1)
        {
            txtSituacaoValor.setText("Situação de Declaração (Declarado) - 50%");
            txtSituacaoConvertido.setText("Situação de Declaração (Declarado) - 50%");
            if (viagem.getValor() > 500)
            {
                campoSituacaoValor.setText(moedaValor + " " + format(((viagem.getValor() - 500) * 50) / 100));
            }
        } else if (viagem.getSituacao() == 2)
        {
            txtSituacaoValor.setText("Situação de Declaração (Não declarado) - 0%");
            txtSituacaoConvertido.setText("Situação de Declaração (Não declarado) - 0%");
        } else if (viagem.getSituacao() == 3)
        {
            txtSituacaoValor.setText("Situação de Declaração (Multado) - 100%");
            txtSituacaoConvertido.setText("Situação de Declaração (Multado) - 100%");
            if (viagem.getValor() > 500)
            {
                campoSituacaoValor.setText(moedaValor + " " + format(((viagem.getValor() - 500) * 100) / 100));
            }
        }

        if (viagem.getPagamento() == 1)
        {
            txtPagamento.setText("Tipo de Pagamento (Dinheiro)");
            txtPagamentoLocal.setText("Tipo de Pagamento (Dinheiro)");
        } else if (viagem.getPagamento() == 2)
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
