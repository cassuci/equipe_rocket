package com.draft.rckt.equiperocket.Gasto;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class GastoDetailController extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private int gasto_id;
    private String gasto_title;
    private String gasto_descr;
    private float gasto_value;
    private String gasto_type;
    private Date gasto_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_detail_controller);
    }

    @Override
    public void onClick(View v) {

        // Template botao if (v.getId() == R.id.#IDBOTAO){

        // tratamento botao deletar
        removeGasto(get_gasto_id());

        // tratamento botao modificacao
        Intent intent = new Intent(GastoDetailController.this, GastoModifyController.class);
        intent.putExtra("gasto_id", get_gasto_id());
        intent.putExtra("gasto_title", get_gasto_title());
        intent.putExtra("gasto_descr", get_gasto_descr());
        intent.putExtra("gasto_value", get_gasto_value());
        intent.putExtra("gasto_type", get_gasto_type());
        intent.putExtra("gasto_date", get_gasto_date());
        startActivity(intent);
    }

    /**
     * Remove registro da tabela de gastos
     * @param gasto_id identificador unico do gasto
     * @return true se um registro foi removido, false caso contrario
     *
     */
    protected boolean removeGasto(int gasto_id){

        int n_rows; // numero de registros alterados na database

        db = mDbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = "WHERE " + DatabaseContract.GastoEntry.COLUMN_NAME_ENTRY_ID +
                " = " + Integer.toString(gasto_id);
        n_rows = db.delete(DatabaseContract.GastoEntry.TABLE_NAME, where_clause,null);
        db.close(); // libera database

        // se alterou alguma linha retorna true
        if (n_rows > 0)
            return true;
        else
            return false;

    }


    public int get_gasto_id() {
        return gasto_id;
    }

    public String get_gasto_title() {
        return gasto_title;
    }

    public String get_gasto_descr() {
        return gasto_descr;
    }

    public float get_gasto_value() {
        return gasto_value;
    }

    public String get_gasto_type() {
        return gasto_type;
    }

    public Date get_gasto_date() {
        return gasto_date;
    }

}
