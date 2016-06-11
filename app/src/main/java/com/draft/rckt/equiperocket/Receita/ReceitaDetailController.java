package com.draft.rckt.equiperocket.Receita;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class ReceitaDetailController extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private int receita_id;
    private String receita_title;
    private String receita_descr;
    private float receita_value;
    private String receita_type;
    private Date receita_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita_detail_controller);
    }

    @Override
    public void onClick(View v) {

        // Template botao if (v.getId() == R.id.#IDBOTAO){

        // tratamento botao deletar
        removeReceita(get_receita_id());

        // tratamento botao modificacao
        Intent intent = new Intent(ReceitaDetailController.this, ReceitaModifyController.class);
        intent.putExtra("receita_id", get_receita_id());
        intent.putExtra("receita_title", get_receita_title());
        intent.putExtra("receita_descr", get_receita_descr());
        intent.putExtra("receita_value", get_receita_value());
        intent.putExtra("receita_type", get_receita_type());
        intent.putExtra("receita_date", get_receita_date());
        startActivity(intent);
    }

    /**
     * Remove registro da tabela de receitas
     * @param receita_id identificador unico da receita
     * @return true se um registro foi removido, false caso contrario
     *
     */
    protected boolean removeReceita(int receita_id){

        int n_rows; // numero de registros alterados na database

        db = mDbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = "WHERE " + DatabaseContract.ReceitaEntry.COLUMN_NAME_ENTRY_ID +
                " = " + Integer.toString(receita_id);
        n_rows = db.delete(DatabaseContract.ReceitaEntry.TABLE_NAME, where_clause,null);
        db.close(); // libera database

        // se alterou alguma linha retorna true
        if (n_rows > 0)
            return true;
        else
            return false;

    }


    public int get_receita_id() {
        return receita_id;
    }

    public String get_receita_title() {
        return receita_title;
    }

    public String get_receita_descr() {
        return receita_descr;
    }

    public float get_receita_value() {
        return receita_value;
    }

    public String get_receita_type() {
        return receita_type;
    }

    public Date get_receita_date() {
        return receita_date;
    }
}
