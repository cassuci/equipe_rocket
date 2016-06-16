package com.draft.rckt.equiperocket.Database;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Receita.Receita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ivanlucas on 12/06/16.
 */
public class DatabaseController {
    private static DatabaseHelper dbHelper = null;
    private static Context context;
    private SQLiteDatabase db ;

    public DatabaseController(Context context) {
        if (dbHelper == null) {
            this.context = context;

            dbHelper = new DatabaseHelper(this.context);

            //TODO temporario
            if  (getAllReceitaOrderByDate() == null) {

                ContentValues contentValues = new ContentValues();
                contentValues.put("username", "1");
                contentValues.put("senha", "senhaTeste");
                contentValues.put("nome", "NomeTeste");
                db.insert("Users", null, contentValues);
                int i;

                double v = 402;
                for (i = 1; i < 15; i++) {
                    Receita rec = new Receita();
                    rec.user_id = "1";
                    rec.receita_id = i;
                    rec.titulo = "Receit " + i;
                    rec.desc = "aaaaaaa bbbbbb    ccccccc " + i;
                    rec.tipo = "qualquer uma";
                    v += i;
                    rec.valor = v;
                    rec.data = new Date();
                    addItemReceita(rec);
                }
            }
        }
    }


    public void addItemReceita(Receita rec) {
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReceitaEntry.COLUMN_NAME_ENTRY_ID, rec.receita_id);
        contentValues.put(ReceitaEntry.COLUMN_NAME_USER_ID, rec.user_id);
        contentValues.put(ReceitaEntry.COLUMN_NAME_TITLE, rec.titulo);
        contentValues.put(ReceitaEntry.COLUMN_NAME_CONTENT, rec.desc);
        contentValues.put(ReceitaEntry.COLUMN_NAME_VALUE, rec.valor);
        contentValues.put(ReceitaEntry.COLUMN_NAME_TYPE, rec.tipo);
        contentValues.put(ReceitaEntry.COLUMN_NAME_DATE, rec.data.getTime());
        db.insert(ReceitaEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Receita> getAllReceitaOrderByDate(){

        //TODO: depois arrumar o id para o id do usuário
        String query_get = "select * FROM " + ReceitaEntry.TABLE_NAME +
                " WHERE " + ReceitaEntry.COLUMN_NAME_USER_ID + " = '1'" +
                " ORDER BY " + ReceitaEntry.COLUMN_NAME_DATE + " DESC;";

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);
        ArrayList<Receita> array = null;

        int i = 0;

        if (cursor != null && cursor.moveToFirst())
        {
            Toast.makeText(context, "VAZIO", Toast.LENGTH_SHORT).show();

            array = new ArrayList<Receita>();
            do {
                Receita receita = new Receita();
                receita.receita_id = cursor.getInt(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_ENTRY_ID));
                receita.user_id = cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_USER_ID));
                receita.titulo = cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_TITLE));
                receita.desc = cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_CONTENT));
                receita.valor = cursor.getDouble(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_VALUE));
                receita.tipo = cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_TYPE));
                receita.data = new Date(cursor.getLong(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_DATE)));
                array.add(receita);

            } while (cursor.moveToNext());
        }
        return array;
    }
}
