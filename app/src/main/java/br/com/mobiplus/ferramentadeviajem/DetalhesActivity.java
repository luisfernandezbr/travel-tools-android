package br.com.mobiplus.ferramentadeviajem;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.mobiplus.ferramentadeviajem.models.CustoViagem;

public class DetalhesActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        CustoViagem viagem = (CustoViagem) getIntent().getSerializableExtra("viagem");
        String moedaValor = getIntent().getStringExtra("moedaValor");
        String moedaConvertida = getIntent().getStringExtra("moedaConvert");

        NumberFormat formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);

        TextView campoValor = (TextView) findViewById(R.id.valorTabela);
        TextView campoValorConvertido = (TextView) findViewById(R.id.valorConvertidoTabela);
        TextView campoSituacaoConvertido = (TextView) findViewById(R.id.situacaoConvertidaTabela);
        TextView campoSituacaoValor = (TextView) findViewById(R.id.situacaoValorTabela);
        TextView campoPagamento = (TextView) findViewById(R.id.pagamentoTabela);
        TextView txtSituacaoConvertido = (TextView) findViewById(R.id.txtSituacaoConvertido);
        TextView txtSituacaoValor = (TextView) findViewById(R.id.txtSituacaoValor);
        TextView txtPagamento = (TextView) findViewById(R.id.txtPagamento);
        TextView txtDetalhamentoValor = (TextView) findViewById(R.id.detalhamentoValor);
        TextView txtDetalhamentoConvertido = (TextView) findViewById(R.id.detalhamentoConvertido);
        TextView campoTotalBR = (TextView) findViewById(R.id.totalConvertidoTabela);
        TextView campoTotalUS = (TextView) findViewById(R.id.totalValorTabela);
        TextView campoTaxa = (TextView) findViewById(R.id.taxaTabela);

        txtDetalhamentoConvertido.setText("Detalhamento do Custo ("+moedaConvertida+")");
        txtDetalhamentoValor.setText("Detalhamento do Custo ("+moedaValor+")");


        campoTaxa.setText(moedaValor+" "+formato.format(viagem.getTaxa()));
        campoValor.setText(moedaValor+" "+formato.format(viagem.getValor()));
        campoValorConvertido.setText(moedaConvertida+" "+formato.format(viagem.getValorConvertido()));
        campoTotalBR.setText(moedaConvertida+" "+formato.format(viagem.getTotalBR()));
        campoTotalUS.setText(moedaValor+" "+formato.format(viagem.getTotalUS()));
        campoSituacaoConvertido.setText(moedaConvertida+" "+formato.format(viagem.getValorSituacao()));
        campoPagamento.setText(moedaConvertida+" "+formato.format(viagem.getValorPagamento()));

        if (viagem.getSituacao() == 1) {
            txtSituacaoValor.setText("Situação - Declarado(50%):");
            txtSituacaoConvertido.setText("Situação - Declarado(50%):");
            if(viagem.getValor()>500) {
                campoSituacaoValor.setText(moedaValor + " " + formato.format(((viagem.getValor() - 500) * 50) / 100));
            }
        } else if (viagem.getSituacao() == 2) {
            txtSituacaoValor.setText("Situação - Não Declarado(0%):");
            txtSituacaoConvertido.setText("Situação - Não Declarado(0%):");
        } else if (viagem.getSituacao() == 3) {
            txtSituacaoValor.setText("Situação - Multado(100%):");
            txtSituacaoConvertido.setText("Situação - Multado(100%):");
            if(viagem.getValor()>500) {
                campoSituacaoValor.setText(moedaValor + " " + formato.format(((viagem.getValor() - 500) * 100) / 100));
            }
        }

        if (viagem.getPagamento() == 1) {
            txtPagamento.setText("Tipo de Pag. - Dinheiro(1.32%):");
        } else if (viagem.getPagamento() == 2) {
            txtPagamento.setText("Tipo de Pag. - Débito/Crédito(6.34%):");

        }
    }
}
