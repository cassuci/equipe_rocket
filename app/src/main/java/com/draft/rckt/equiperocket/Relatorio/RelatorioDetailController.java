package com.draft.rckt.equiperocket.Relatorio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Gasto.Gasto;
import com.draft.rckt.equiperocket.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RelatorioDetailController extends AppCompatActivity {

    private TextView relatorioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_detail_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        TODO
        - salvar relatorio em txt   https://www.sitepoint.com/store-user-data-using-simple-text-files-and-apis-in-android/
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        relatorioText = (TextView) findViewById(R.id.relatorio_text);
        montaRelatorio();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private String relatorio = "";
    private int gastosSelected = RelatorioController.getGastosSelected(),
                receitasSelected = RelatorioController.getReceitasSelected();
    private float totalGastos = 0, totalReceitas = 0;
    private Calendar startCal = RelatorioController.getStartCal(),
                     endCal = RelatorioController.getEndCal();

    private void montaRelatorio()
    {
        cabecalho();
        if(gastosSelected == 1)
        {
            relatorio += "Gastos:\n";
            insereGastos();
        }
        if(receitasSelected == 1)
        {
            relatorio += "Receitas:\n";
            insereReceitas();
        }

        if(gastosSelected == receitasSelected)
            relatorio += "BALANÇO TOTAL = R$" + (totalReceitas - totalGastos) + "\n\n";

        insereData();

        relatorioText.setText(relatorio);
    }

    private void insereData() {
        Date d = new Date();
        relatorio += "Relatório gerado em " + DateFormat.getDateInstance(DateFormat.SHORT).format(d) + " às " +
                DateFormat.getTimeInstance(DateFormat.SHORT).format(d);
    }

    private void insereGastos() {
        // TODO
        // obter gastos
        //ArrayList<Gasto> list_gastos = DatabaseController.getGastoByPeriod(startCal, endCal);
        //se tipo contido list_gastos.
        relatorio += "\tNomeGasto1 = R$ 1.20\n\tNomeGasto2 = R$1000.00\n";
        relatorio += "TOTAL GASTOS = R$" + totalGastos + "\n\n";
    }

    private void insereReceitas() {
        // TODO
        // obter receitas
        relatorio += "\tNomeReceita1 = R$ 1.20\n\tNomeReceita2 = R$1000.00\n";
        relatorio += "TOTAL RECEITAS = R$" + totalReceitas + "\n\n";
    }

    private void cabecalho() {
        if (gastosSelected == 1) {
            if (receitasSelected == 1) {
                relatorio = "RELATÓRIO DE GASTOS E RECEITAS\n\n";
            } else {
                relatorio = "RELATÓRIO DE GASTOS\n\n";
            }

        } else {
            relatorio = "RELATÓRIO DE RECEITAS\n\n";
        }

        relatorio += "Período: " + String.format("%02d", startCal.get(startCal.DAY_OF_MONTH)) + "/" +
                    String.format("%02d", startCal.get(startCal.MONTH)+1) + "/" + String.format("%02d", startCal.get(startCal.YEAR)) +
                    " a " + String.format("%02d", endCal.get(endCal.DAY_OF_MONTH)) + "/" +
                    String.format("%02d", (endCal.get(endCal.MONTH)+1)) + "/" + String.format("%02d", endCal.get(endCal.YEAR)) + "\n\n";
    }
}
