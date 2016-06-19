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
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        relatorio += "Relatório gerado em " + formatoData.format(d) + " às " +
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

        ArrayList<Receita> allReceita = dbControl.getAllReceitaOrderByDate();

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

        ArrayList<Receita> receita = dbControl.getReceitaByPeriod(allReceita, startCal, endCal);
        for(int i = 0; i < receita.size(); i++)
        {
            relatorio += String.format("\t%-21s", receita.get(i).getTitulo()) + String.format("R$ %f", receita.get(i).getValor());
            totalReceitas += receita.get(i).getValor();
        }
        
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

        relatorio += "Período: " +  rControl.formatDate(startCal) + " a " + rControl.formatDate(endCal) + "\n\n";
    }
}
