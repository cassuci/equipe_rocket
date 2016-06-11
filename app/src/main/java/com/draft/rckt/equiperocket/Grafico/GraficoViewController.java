package com.draft.rckt.equiperocket.Grafico;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;
import com.jjoe64.graphview.GraphView;

import java.sql.Date;

public class GraficoViewController extends AppCompatActivity {

    private static final int RECEITA = 1;
    private static final int GASTO = 1;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private boolean showReceitas;
    private boolean showGastos;
    private String receita_type;
    private String gasto_type;
    private Date data_inicio;
    private Date data_fim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        //TODO: Separar informacoes do bundle

        setContentView(R.layout.activity_grafico_view_controller);
    }


    protected void show_graph(){

        String where_clause_receita = null;
        String where_clause_gasto = null;

        // se algum campo de restricao foi especificado, construir statement de WHERE
        if (receita_type != null || data_inicio != null || data_fim != null){
            where_clause_receita = create_where_clause(RECEITA);
        }

        //TODO: consulta das receitas


        // se algum campo de restricao foi especificado, construir statement de WHERE
        if (gasto_type != null || data_inicio != null || data_fim != null){
            where_clause_receita = create_where_clause(GASTO);
        }

        //TODO: consulta dos gasto


        mDbHelper = new DatabaseHelper(this.getApplicationContext());
        db = mDbHelper.getReadableDatabase(); // ganha acesso a database



    }

    private String create_where_clause(int registro_type) {

        String where = "WHERE ";

        String receita_type = get_receita_type();
        String gasto_type = get_gasto_type();
        Date data_inicio = get_data_inicio();
        Date data_fim = get_data_fim();


        if (registro_type == RECEITA) {
            if (receita_type != null)
                where.concat(DatabaseContract.ReceitaEntry.COLUMN_NAME_TYPE + " = " + receita_type);


            if (data_inicio != null)
                if (where.compareTo("WHERE ") != 0)
                    where.concat(" AND" + DatabaseContract.ReceitaEntry.COLUMN_NAME_DATE + " >= " + data_inicio);
                else
                    where.concat(DatabaseContract.ReceitaEntry.COLUMN_NAME_DATE + " >= " + data_inicio);

            if (data_fim != null)
                if (where.compareTo("WHERE ") != 0)
                    where.concat(" AND" + DatabaseContract.ReceitaEntry.COLUMN_NAME_DATE + " <= " + data_fim);
                else
                    where.concat(DatabaseContract.ReceitaEntry.COLUMN_NAME_DATE + " <= " + data_fim);

        }else if(registro_type == GASTO){
            if (gasto_type != null) {
                where.concat(DatabaseContract.GastoEntry.COLUMN_NAME_TYPE + " = " + receita_type);
            }


            if (data_inicio != null)
                if (where.compareTo("WHERE ") != 0)
                    where.concat(" AND" + DatabaseContract.GastoEntry.COLUMN_NAME_DATE + " >= " + data_inicio);
                else
                    where.concat(DatabaseContract.GastoEntry.COLUMN_NAME_DATE + " >= " + data_inicio);

            if (data_fim != null)
                if (where.compareTo("WHERE ") != 0)
                    where.concat(" AND" + DatabaseContract.GastoEntry.COLUMN_NAME_DATE + " <= " + data_fim);
                else
                    where.concat(DatabaseContract.GastoEntry.COLUMN_NAME_DATE + " <= " + data_fim);
        }

        where.concat(";");
        return where;
    }

    public String get_receita_type() {
        return receita_type;
    }

    public String get_gasto_type() {
        return gasto_type;
    }

    public Date get_data_inicio() {
        return data_inicio;
    }

    public Date get_data_fim() {
        return data_fim;
    }
}
