package com.draft.rckt.equiperocket.Relatorio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Gasto.Gasto;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.Receita;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RelatorioDetailController extends AppCompatActivity {

    private TextView relatorioText;
    DatabaseController dbControl;
    RelatorioController rControl;
    private String relatorio = "";
    private int gastosSelected = RelatorioController.getGastosSelected(),
            receitasSelected = RelatorioController.getReceitasSelected();
    private float totalGastos = 0, totalReceitas = 0;
    private Calendar startCal = RelatorioController.getStartCal(),
            endCal = RelatorioController.getEndCal();
    private Calendar startCalGasto = Calendar.getInstance(), endCalGasto = Calendar.getInstance(),
            startCalRec = Calendar.getInstance(), endCalRec = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_detail_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbControl = new DatabaseController(getApplication());
        rControl = new RelatorioController();

        /*
        TODO
        - salvar relatorio em txt   https://www.sitepoint.com/store-user-data-using-simple-text-files-and-apis-in-android/
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        relatorioText = (TextView) findViewById(R.id.relatorio_text);

        // se user nao escolheu data inicial, ela eh a primeira do BD
        if (rControl.getStartYear() == 0)
        {
            Toast.makeText(getApplicationContext(), "start = 0", Toast.LENGTH_SHORT).show();
            startCal = null;
        }
        // se user nao escolheu data final, ela eh a ultima do BD
        if (rControl.getEndYear() == 0) {
            Toast.makeText(getApplicationContext(), "end = 0", Toast.LENGTH_SHORT).show();
            endCal = null;
        }

        montaRelatorio();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void montaRelatorio()
    {
        String dados = "";

        if(gastosSelected == 1)
        {
            dados += "Gastos:\n";
            dados += insereGastos();
        }
        if(receitasSelected == 1)
        {
            dados += "Receitas:\n";
            dados += insereReceitas();
        }

        if(gastosSelected == receitasSelected)
            dados += "BALANÇO TOTAL = R$" + (totalReceitas - totalGastos) + "\n\n";

        cabecalho();
        relatorio += dados;
        insereData();
        relatorioText.setText(relatorio);
    }

    private void insereData() {
        Date d = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        relatorio += "Relatório gerado em " + formatoData.format(d) + " às " +
                DateFormat.getTimeInstance(DateFormat.SHORT).format(d);
    }

    private String insereGastos() {
        ArrayList<Gasto> allGasto = dbControl.getAllGastoOrderByDate();
        ArrayList<Gasto> gasto = dbControl.getGastoByPeriod(allGasto, startCal, endCal);
        String relatorioRec = "";

        for(int i = 0; i < gasto.size(); i++)
        {
            relatorioRec += String.format("\t%-30s%s%.2f\n", gasto.get(i).getTitulo(), "R$ ", gasto.get(i).getValor());
            totalGastos += gasto.get(i).getValor();
        }

        relatorioRec += String.format("TOTAL GASTOS%22s%.2f\n\n", "R$ ", totalGastos);

        if(startCal == null)
            startCalGasto.setTime(gasto.get(gasto.size()-1).getData());
        if(endCal == null)
            endCalGasto.setTime(gasto.get(0).getData());

        return relatorioRec;
    }

    private String insereReceitas() {
        ArrayList<Receita> allReceita = dbControl.getAllReceitaOrderByDate();
        ArrayList<Receita> receita = dbControl.getReceitaByPeriod(allReceita, startCal, endCal);
        String relatorioGasto = "";

        for(int i = 0; i < receita.size(); i++)
        {
            relatorioGasto += String.format("\t%-30s%s%.2f\n", receita.get(i).getTitulo(), "R$ ", receita.get(i).getValor());
            totalReceitas += receita.get(i).getValor();
        }

        relatorioGasto += String.format("TOTAL RECEITAS%20s%.2f\n\n", "R$ ", totalReceitas);

        if(startCal == null)
            startCalRec.setTime(receita.get(receita.size()-1).getData());
        if(endCal == null)
            endCalRec.setTime(receita.get(0).getData());

        return relatorioGasto;
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

        if(startCal == null) {
            // gasto < rec
            if (startCalGasto.compareTo(startCalRec) < 0)
                startCal = startCalGasto;
            else
                startCal = startCalRec;
        }

        if(endCal == null) {
            if (endCalGasto.compareTo(endCalRec) < 0)
                endCal = endCalGasto;
            else
                endCal = endCalRec;
        }

        relatorio += "Período: " +  rControl.formatDate(startCal) + " a " + rControl.formatDate(endCal) + "\n\n";
    }
}
