package com.draft.rckt.equiperocket.Grafico;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.draft.rckt.equiperocket.Database.DatabaseContract.GastoEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;
import com.jjoe64.graphview.GraphView;

import java.sql.Date;
import java.util.List;

public class GraficoViewController extends AppCompatActivity {

    private static final int RECEITA = 0;
    private static final int GASTO = 1;
    private static final int GRAFICO_BARRAS = 0;
    private static final int GRAFICO_LINHAS = 1;
    private static final int GRAFICO_PONTOS = 2;


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private int graph_type;
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
        List<Graph_Item> receita_items = null;
        List<Graph_Item> gasto_items = null;



        if (showReceitas) {
            // se algum campo de restricao foi especificado, construir statement de WHERE
            if (receita_type != null || data_inicio != null || data_fim != null) {
                where_clause_receita = create_where_clause(RECEITA);
            }

            //TODO: verificar consulta das receitas
            mDbHelper = new DatabaseHelper(this.getApplicationContext());
            db = mDbHelper.getReadableDatabase(); // ganha acesso a database

            String[] columns= {ReceitaEntry.COLUMN_NAME_VALUE,
                    ReceitaEntry.COLUMN_NAME_DATE};
            Cursor cursor = db.query(ReceitaEntry.TABLE_NAME, columns, where_clause_receita, null,
                    null, null, ReceitaEntry.COLUMN_NAME_DATE);

            if (cursor != null){
                while(cursor.moveToNext()){
                    Graph_Item item = new Graph_Item(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_VALUE)),
                            cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_DATE)));
                    receita_items.add(item);
                }
                cursor.close();
            }

            db.close();
        }

        if (showGastos) {
            // se algum campo de restricao foi especificado, construir statement de WHERE
            if (gasto_type != null || data_inicio != null || data_fim != null) {
                where_clause_receita = create_where_clause(GASTO);
            }

            //TODO: testar consulta dos gasto
            mDbHelper = new DatabaseHelper(this.getApplicationContext());
            db = mDbHelper.getReadableDatabase(); // ganha acesso a database

            String[] columns ={GastoEntry.COLUMN_NAME_VALUE,
                    GastoEntry.COLUMN_NAME_DATE};
            Cursor cursor = db.query(GastoEntry.TABLE_NAME, columns, where_clause_receita, null,
                    null, null, GastoEntry.COLUMN_NAME_DATE);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Graph_Item item = new Graph_Item(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_VALUE)),
                            cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_DATE)));
                    gasto_items.add(item);
                }
                cursor.close();
            }
            db.close();
        }

        if(get_graph_type() == GRAFICO_BARRAS){
            plot_bars(receita_items, gasto_items);
        }else if(get_graph_type() == GRAFICO_LINHAS){
            plot_lines(receita_items, gasto_items);
        }else if (get_graph_type() == GRAFICO_PONTOS){
            plot_points(receita_items, gasto_items);
        }else{
            //TODO: tratamento de erro
        }


    }

    private void plot_lines(List<Graph_Item> receita_items, List<Graph_Item> gasto_items) {
        //TODO: implementar
    }

    private void plot_bars(List<Graph_Item> receita_items, List<Graph_Item> gasto_items) {
        //TODO: implementar
    }

    private void plot_points(List<Graph_Item> receita_items, List<Graph_Item> gasto_items) {
        //TODO: implementar
    }

    private String create_where_clause(int registro_type) {

        String where = null;

        String receita_type = get_receita_type();
        String gasto_type = get_gasto_type();
        Date data_inicio = get_data_inicio();
        Date data_fim = get_data_fim();


        if (registro_type == RECEITA) {
            if (receita_type != null)
                where.concat(ReceitaEntry.COLUMN_NAME_TYPE + " = " + receita_type);

            if (data_inicio != null)
                if (where != null)
                    where.concat(" AND" + ReceitaEntry.COLUMN_NAME_DATE + " >= " + data_inicio);
                else
                    where.concat(ReceitaEntry.COLUMN_NAME_DATE + " >= " + data_inicio);

            if (data_fim != null)
                if (where != null)
                    where.concat(" AND" + ReceitaEntry.COLUMN_NAME_DATE + " <= " + data_fim);
                else
                    where.concat(ReceitaEntry.COLUMN_NAME_DATE + " <= " + data_fim);

        }else if(registro_type == GASTO){
            if (gasto_type != null)
                where.concat(GastoEntry.COLUMN_NAME_TYPE + " = " + receita_type);

            if (data_inicio != null)
                if (where != null)
                    where.concat(" AND" + GastoEntry.COLUMN_NAME_DATE + " >= " + data_inicio);
                else
                    where.concat(GastoEntry.COLUMN_NAME_DATE + " >= " + data_inicio);

            if (data_fim != null)
                if (where != null)
                    where.concat(" AND" + GastoEntry.COLUMN_NAME_DATE + " <= " + data_fim);
                else
                    where.concat(GastoEntry.COLUMN_NAME_DATE + " <= " + data_fim);
        }

        return where;
    }

    public int get_graph_type() {
        return graph_type;
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
