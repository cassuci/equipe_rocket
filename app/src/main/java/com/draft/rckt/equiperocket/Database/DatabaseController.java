package com.draft.rckt.equiperocket.Database;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.GastoEntry;


import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Gasto.Gasto;
import com.draft.rckt.equiperocket.Receita.Receita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

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

                Receita a = new Receita();
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
                    a = rec;
                }

                deleteReceita(a);

                Gasto b = new Gasto();
                v = 402;
                for (i = 1; i < 15; i++) {
                    Gasto gasto = new Gasto();
                    gasto.user_id = "1";
                    gasto.gasto_id = i;
                    gasto.titulo = "Receit " + i;
                    gasto.descr = "aaaaaaa bbbbbb    ccccccc " + i;
                    gasto.tipo = "qualquer uma";
                    v += i;
                    gasto.valor = v;
                    gasto.data = new Date();
                    addItemGasto(gasto);
                    b = gasto;
                }

                deleteGasto(b);

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

    public void addItemGasto(Gasto gasto) {
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(GastoEntry.COLUMN_NAME_ENTRY_ID, gasto.gasto_id);
        contentValues.put(GastoEntry.COLUMN_NAME_USER_ID, gasto.user_id);
        contentValues.put(GastoEntry.COLUMN_NAME_TITLE, gasto.titulo);
        contentValues.put(GastoEntry.COLUMN_NAME_CONTENT, gasto.descr);
        contentValues.put(GastoEntry.COLUMN_NAME_VALUE, gasto.valor);
        contentValues.put(GastoEntry.COLUMN_NAME_TYPE, gasto.tipo);
        contentValues.put(GastoEntry.COLUMN_NAME_DATE, gasto.data.getTime());
        db.insert(GastoEntry.TABLE_NAME, null, contentValues);
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

    public ArrayList<Gasto> getAllGastoOrderByDate(){

        //TODO: depois arrumar o id para o id do usuário
        String query_get = "select * FROM " + GastoEntry.TABLE_NAME +
                " WHERE " + GastoEntry.COLUMN_NAME_USER_ID + " = '1'" +
                " ORDER BY " + GastoEntry.COLUMN_NAME_DATE + " DESC;";

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);
        ArrayList<Gasto> array = null;

        int i = 0;

        if (cursor != null && cursor.moveToFirst())
        {
            array = new ArrayList<Gasto>();
            do {
                Gasto gasto = new Gasto();
                gasto.gasto_id = cursor.getInt(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_ENTRY_ID));
                gasto.user_id = cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_USER_ID));
                gasto.titulo = cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_TITLE));
                gasto.descr = cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_CONTENT));
                gasto.valor = cursor.getDouble(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_VALUE));
                gasto.tipo = cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_TYPE));
                gasto.data = new Date(cursor.getLong(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_DATE)));
                array.add(gasto);

            } while (cursor.moveToNext());
        }
        return array;
    }

    public boolean deleteReceita(Receita receita)
    {
        db = dbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = ReceitaEntry.COLUMN_NAME_ENTRY_ID +
                " = " + receita.getReceita_id();
        int n_rows = db.delete(DatabaseContract.ReceitaEntry.TABLE_NAME, where_clause, null);
        if (n_rows > 0)
            return true;
        return  false;
    }

    public boolean deleteGasto(Gasto gasto)
    {
        db = dbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = GastoEntry.COLUMN_NAME_ENTRY_ID +
                " = " + gasto.getGasto_id();
        int n_rows = db.delete(GastoEntry.TABLE_NAME, where_clause, null);
        if (n_rows > 0)
            return true;
        return  false;
    }

    public ArrayList<Receita> getReceitaByPeriod(ArrayList<Receita> arrayReceita, Calendar startDate, Calendar finishDate)
    {
        ArrayList<Receita>  array = new ArrayList<Receita>();
      //  startDate.set(Calendar.YEAR);
      //  startDate.s

        return array;
    }
}
