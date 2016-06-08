package com.draft.rckt.equiperocket.Receita;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

public class ReceitaController extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DatabaseHelper(this.getApplicationContext());
        db = mDbHelper.getReadableDatabase(); // ganha acesso a database
        db.close(); // libera database

        setContentView(R.layout.activity_receita_controller);
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
}
