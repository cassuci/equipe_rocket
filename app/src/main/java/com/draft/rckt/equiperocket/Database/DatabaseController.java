package com.draft.rckt.equiperocket.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.draft.rckt.equiperocket.Receita.Receita;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ivanlucas on 12/06/16.
 */
public class DatabaseController {
    private static DatabaseHelper dbHelper = null;

    public DatabaseController(Context context) {
        if (dbHelper == null)
            dbHelper = new DatabaseHelper(context);
    }

    public void addItem(String tabela, String user_id, String receita_id, String titulo, String desc, float valor, String tipo, Date data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("receita_id", receita_id);
        contentValues.put("user_id", user_id);
        contentValues.put("titulo", titulo);
        contentValues.put("descricao", desc);
        contentValues.put("valor", valor);
        contentValues.put("tipo", tipo);
        //TODO: descobrir como inclue data do BD
        //contentValues.put("data", data);

        db.insert(tabela, null, contentValues);

    }

    public ArrayList<Receita> getAllItens(String tabela){

        String query_get = "select * FROM " + tabela;
        //   +" where receita_id = 1 OR receita_id = 5";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_get, null);
        ArrayList<Receita> array = null;

        int i = 0;

        if (cursor != null && cursor.moveToFirst())
        {
            array = new ArrayList<Receita>();
            do {
                Receita receita = new Receita();
                receita.receita_id = cursor.getString(0);
                receita.user_id = cursor.getString(1);
                receita.titulo = cursor.getString(2);
                receita.desc = cursor.getString(3);
                receita.valor = cursor.getFloat(4);
                receita.tipo = cursor.getString(5);
                //TODO: descobrir como pega data do BD
                array.add(receita);

            } while (cursor.moveToNext());
        }

        return array;
    }


}
