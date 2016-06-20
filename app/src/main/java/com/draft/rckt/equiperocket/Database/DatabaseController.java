package com.draft.rckt.equiperocket.Database;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.GastoEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.UserEntry;


import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Gasto.Gasto;
import com.draft.rckt.equiperocket.Receita.Receita;
import com.draft.rckt.equiperocket.Usuario.Usuario;

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
    private SQLiteDatabase db;
    private static Usuario user;

    public DatabaseController(Context context) {
        if (dbHelper == null) {
            this.context = context;
            dbHelper = new DatabaseHelper(this.context);
            user = Usuario.getInstance();

            //TODO temporario
            if  (loginUser("marininha", "10anos") == false)
                addUsers("Marina Bolada", "marininha", "10anos");

            if (getAllReceitaOrderByDate() == null)
            {
                int i;
                Receita a = new Receita();
                double v = 402;
                for (i = 1; i < 20; i++) {
                    Receita rec = new Receita();
                    rec.setTitulo("Receit " + i);
                    rec.setDesc("aaaaaaa bbbbbb    ccccccc " + i);
                    rec.setTipo("qualquer uma");
                    v += i;
                    rec.setValor(v);

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -i);
                    rec.setData(calendar.getTime());
                    addItemReceita(rec);
                    a = rec;
                }

                deleteReceita(a);

                Receita rec = new Receita();
                rec.setReceita_id(1);
                rec.setTitulo("Receita alterada ");
                rec.setDesc("o importante é que alterou");
                rec.setTipo("aaaaaa ");
                rec.setValor(67.90);

                rec.setData(new Date());
                updateReceita(rec);

                Gasto b = new Gasto();
                v = 402;
                for (i = 1; i < 21; i++) {
                    Gasto gasto = new Gasto();
                    gasto.setTitulo("Gasto " + i);
                    gasto.setDescr("aaaaaaa bbbbbb    ccccccc " + i);
                    gasto.setTipo("qualquer uma");
                    v += i;
                    gasto.setValor(v);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -i);
                    gasto.setData(calendar.getTime());
                    addItemGasto(gasto);
                    b = gasto;
                }

                deleteGasto(b);

                Gasto g = new Gasto();
                g.setGasto_id(1);
                g.setTitulo("Gasto alterado  ");
                g.setDescr("o importante é que alterou!!! uhuuuul");
                g.setTipo("aaaaaa");
                g.setData(new Date());
                g.setValor(67.90);
                updateGasto(g);
            }
        }
    }


    public boolean loginUser(String username, String senha)
    {
        String query_get = "select * FROM " + UserEntry.TABLE_NAME +
                " WHERE " + UserEntry.COLUMN_NAME_USER_ID + " = '" + username + "' " +
                " AND " + UserEntry.COLUMN_NAME_PASSWORD + " = '" + senha + "' ";

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);

        if (cursor != null && cursor.moveToFirst()) {
            user.setNome(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_NAME)));
            user.setUser_id(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_ID)));
            updateSaldo();
            return true;
        }

        return false;
    }

    public boolean addUsers(String nome, String username, String senha) {
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_NAME_NAME, nome);
        contentValues.put(UserEntry.COLUMN_NAME_USER_ID, username);
        contentValues.put(UserEntry.COLUMN_NAME_PASSWORD, senha);
        long flag = db.insert(UserEntry.TABLE_NAME, null, contentValues);
        if (flag == -1)
            return false;

        user.setUser_id(username);
        user.setNome(nome);
        user.setSaldo((double) 0);

        return true;
    }

    public boolean addItemReceita(Receita rec) {
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.putNull(ReceitaEntry.COLUMN_NAME_ENTRY_ID);
        contentValues.put(ReceitaEntry.COLUMN_NAME_USER_ID, user.getUser_id());
        contentValues.put(ReceitaEntry.COLUMN_NAME_TITLE, rec.getTitulo());
        contentValues.put(ReceitaEntry.COLUMN_NAME_CONTENT, rec.getDesc());
        contentValues.put(ReceitaEntry.COLUMN_NAME_VALUE, rec.getValor());
        contentValues.put(ReceitaEntry.COLUMN_NAME_TYPE, rec.getTipo());
        contentValues.put(ReceitaEntry.COLUMN_NAME_DATE, rec.getData().getTime());
        long flag = db.insert(ReceitaEntry.TABLE_NAME, null, contentValues);
        if (flag == -1)
            return false;

        updateSaldo();
        return true;
    }

    public boolean addItemGasto(Gasto gasto) {
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.putNull(GastoEntry.COLUMN_NAME_ENTRY_ID);
        contentValues.put(GastoEntry.COLUMN_NAME_USER_ID, user.getUser_id());
        contentValues.put(GastoEntry.COLUMN_NAME_TITLE, gasto.getTitulo());
        contentValues.put(GastoEntry.COLUMN_NAME_CONTENT, gasto.getDescr());
        contentValues.put(GastoEntry.COLUMN_NAME_VALUE, gasto.getValor());
        contentValues.put(GastoEntry.COLUMN_NAME_TYPE, gasto.getTipo());
        contentValues.put(GastoEntry.COLUMN_NAME_DATE, gasto.getData().getTime());
        long flag = db.insert(GastoEntry.TABLE_NAME, null, contentValues);
        if (flag == -1)
            return false;

        updateSaldo();
        return true;
    }

    public Receita getReceita(int id){

        String query_get = "select * FROM " + ReceitaEntry.TABLE_NAME +
                " WHERE " + ReceitaEntry.COLUMN_NAME_ENTRY_ID + " = " + id;

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);

        if (cursor != null && cursor.moveToFirst())
        {
            Receita receita = new Receita();
            receita.setReceita_id(cursor.getInt(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_ENTRY_ID)));
            receita.setUser_id(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_USER_ID)));
            receita.setTitulo(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_TITLE)));
            receita.setDesc(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_CONTENT)));
            receita.setValor(cursor.getDouble(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_VALUE)));
            receita.setTipo(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_TYPE)));
            receita.setData(new Date(cursor.getLong(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_DATE))));
            return receita;
        }
        return null;
    }

    public Gasto getGasto(int id){

        String query_get = "select * FROM " + GastoEntry.TABLE_NAME +
                " WHERE " + GastoEntry.COLUMN_NAME_ENTRY_ID + " = " + id;

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);

        if (cursor != null && cursor.moveToFirst())
        {
            Gasto gasto = new Gasto();
            gasto.setGasto_id(cursor.getInt(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_ENTRY_ID)));
            gasto.setUser_id(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_USER_ID)));
            gasto.setTitulo(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_TITLE)));
            gasto.setDescr(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_CONTENT)));
            gasto.setValor(cursor.getDouble(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_VALUE)));
            gasto.setTipo(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_TYPE)));
            gasto.setData(new Date(cursor.getLong(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_DATE))));
            return gasto;
        }
        return null;
    }

    public ArrayList<Receita> getAllReceitaOrderByDate(){

        String query_get = "select * FROM " + ReceitaEntry.TABLE_NAME +
                " WHERE " + ReceitaEntry.COLUMN_NAME_USER_ID + " = '" + user.getUser_id() + "' " +
                " ORDER BY " + ReceitaEntry.COLUMN_NAME_DATE + " DESC;";

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);
        ArrayList<Receita> array = null;

        if (cursor != null && cursor.moveToFirst())
        {
            array = new ArrayList<Receita>();
            do {
                Receita receita = new Receita();
                receita.setReceita_id(cursor.getInt(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_ENTRY_ID)));
                receita.setUser_id(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_USER_ID)));
                receita.setTitulo(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_TITLE)));
                receita.setDesc(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_CONTENT)));
                receita.setValor(cursor.getDouble(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_VALUE)));
                receita.setTipo(cursor.getString(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_TYPE)));
                receita.setData(new Date(cursor.getLong(cursor.getColumnIndex(ReceitaEntry.COLUMN_NAME_DATE))));
                array.add(receita);

            } while (cursor.moveToNext());
        }
        return array;
    }

    public ArrayList<Gasto> getAllGastoOrderByDate(){

        String query_get = "select * FROM " + GastoEntry.TABLE_NAME +
                " WHERE " + GastoEntry.COLUMN_NAME_USER_ID + " = '"  + user.getUser_id() + "' " +
                " ORDER BY " + GastoEntry.COLUMN_NAME_DATE + " DESC;";

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_get, null);
        ArrayList<Gasto> array = null;

        if (cursor != null && cursor.moveToFirst())
        {
            array = new ArrayList<Gasto>();
            do {
                Gasto gasto = new Gasto();
                gasto.setGasto_id(cursor.getInt(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_ENTRY_ID)));
                gasto.setUser_id(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_USER_ID)));
                gasto.setTitulo(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_TITLE)));
                gasto.setDescr(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_CONTENT)));
                gasto.setValor(cursor.getDouble(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_VALUE)));
                gasto.setTipo(cursor.getString(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_TYPE)));
                gasto.setData(new Date(cursor.getLong(cursor.getColumnIndex(GastoEntry.COLUMN_NAME_DATE))));
                array.add(gasto);

            } while (cursor.moveToNext());
        }
        return array;
    }

    public boolean deleteReceita(Receita receita)
    {
        db = dbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = ReceitaEntry.COLUMN_NAME_ENTRY_ID + " = " + receita.getReceita_id();
        int n_rows = db.delete(DatabaseContract.ReceitaEntry.TABLE_NAME, where_clause, null);
        if (n_rows > 0) {
            updateSaldo();
            return true;
        }
        return  false;
    }

    public boolean deleteGasto(Gasto gasto)
    {
        db = dbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = GastoEntry.COLUMN_NAME_ENTRY_ID + " = " + gasto.getGasto_id();

        int n_rows = db.delete(GastoEntry.TABLE_NAME, where_clause, null);
        if (n_rows > 0) {
            updateSaldo();
            return true;
        }
        return  false;
    }

    public ArrayList<Receita> getReceitaByPeriod(ArrayList<Receita> arrayReceita, Calendar startDate, Calendar finishDate)
    {
        if (arrayReceita == null || arrayReceita.isEmpty())
            return null;
        
        ArrayList<Receita> array = new ArrayList<Receita>();
        int i = 0;
        long inicio = 0;
        if (startDate != null) {
            startDate.add(Calendar.HOUR, 0);
            startDate.add(Calendar.MINUTE, 0);
            startDate.add(Calendar.SECOND, 0);
            inicio = (startDate.getTime()).getTime();
        }
        else
        {
            Receita rec = arrayReceita.get(arrayReceita.size() - 1);
            inicio = rec.getData().getTime();
        }

        long fim = 0;
        if (finishDate != null) {
            finishDate.add(Calendar.HOUR, 23);
            finishDate.add(Calendar.MINUTE, 59);
            finishDate.add(Calendar.SECOND, 59);
            fim = (finishDate.getTime()).getTime();
        }
        else
        {
            Receita rec = arrayReceita.get(0);
            fim = rec.getData().getTime();
        }

        long atual;
        int ver = 0;
        for (i = 0; i < arrayReceita.size(); i++)
        {
            atual = arrayReceita.get(i).getData().getTime();
            if (atual >= inicio && atual <= fim) {
                Receita rec = new Receita();
                rec = arrayReceita.get(i);
                array.add(rec);
                ver = 1;
            }
        }
        if (ver == 0)
            array =  null;

        return array;
    }


    public ArrayList<Gasto> getGastoByPeriod(ArrayList<Gasto> arrayGasto, Calendar startDate, Calendar finishDate)
    {
        if (arrayGasto == null || arrayGasto.isEmpty())
            return null;

        ArrayList<Gasto> array = new ArrayList<Gasto>();
        int i = 0;
        long inicio = 0;
        if (startDate != null) {
            startDate.add(Calendar.HOUR, 0);
            startDate.add(Calendar.MINUTE, 0);
            startDate.add(Calendar.SECOND, 0);
            inicio = (startDate.getTime()).getTime();
        }
        else
        {
            Gasto gasto = arrayGasto.get(arrayGasto.size() - 1);
            inicio = gasto.getData().getTime();
        }

        long fim = 0;
        if (finishDate != null) {
            finishDate.add(Calendar.HOUR, 23);
            finishDate.add(Calendar.MINUTE, 59);
            finishDate.add(Calendar.SECOND, 59);
            fim = (finishDate.getTime()).getTime();
        }
        else
        {
            Gasto gasto = arrayGasto.get(0);
            fim = gasto.getData().getTime();
        }

        long atual;
        int ver = 0;
        for (i = 0; i < arrayGasto.size(); i++)
        {
            atual = arrayGasto.get(i).getData().getTime();
            if (atual >= inicio && atual <= fim) {
                Gasto aux = new Gasto();
                aux = arrayGasto.get(i);
                array.add(aux);
                ver = 1;
            }
        }
        if (ver == 0)
            array =  null;

        return array;
    }

    public boolean updateReceita(Receita rec)
    {
        db = dbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = ReceitaEntry.COLUMN_NAME_ENTRY_ID + " = " + rec.getReceita_id();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReceitaEntry.COLUMN_NAME_TITLE, rec.getTitulo());
        contentValues.put(ReceitaEntry.COLUMN_NAME_CONTENT, rec.getDesc());
        contentValues.put(ReceitaEntry.COLUMN_NAME_VALUE, rec.getValor());
        contentValues.put(ReceitaEntry.COLUMN_NAME_TYPE, rec.getTipo());
        contentValues.put(ReceitaEntry.COLUMN_NAME_DATE, rec.getData().getTime());

        int n_rows = db.update(ReceitaEntry.TABLE_NAME, contentValues, where_clause, null);
        if (n_rows > 0) {
            updateSaldo();
            return true;
        }
        return false;
    }

    public boolean updateGasto(Gasto gasto)
    {
        db = dbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = GastoEntry.COLUMN_NAME_ENTRY_ID + " = " + gasto.getGasto_id();

        ContentValues contentValues = new ContentValues();
        contentValues.put(GastoEntry.COLUMN_NAME_TITLE, gasto.getTitulo());
        contentValues.put(GastoEntry.COLUMN_NAME_CONTENT, gasto.getDescr());
        contentValues.put(GastoEntry.COLUMN_NAME_VALUE, gasto.getValor());
        contentValues.put(GastoEntry.COLUMN_NAME_TYPE, gasto.getTipo());
        contentValues.put(GastoEntry.COLUMN_NAME_DATE, gasto.getData().getTime());

        int n_rows = db.update(GastoEntry.TABLE_NAME, contentValues, where_clause, null);
        if (n_rows > 0) {
            updateSaldo();
            return true;
        }
        return false;
    }


    public void updateSaldo()
    {
        String query_rec = "select sum(" + ReceitaEntry.COLUMN_NAME_VALUE + ") " +
                "from " + ReceitaEntry.TABLE_NAME +
                " WHERE " + ReceitaEntry.COLUMN_NAME_USER_ID + " = '" + user.getUser_id() + "' ";

        // ganha acesso a database
        db = dbHelper.getReadableDatabase();

        double valorReceita = 0;
        try {
            Cursor cursor = db.rawQuery(query_rec, null);
            if (cursor != null && cursor.moveToFirst()) {
                valorReceita = cursor.getDouble(0);
            }

        } catch (Throwable e) { }

        String query_gasto = "select sum(" + GastoEntry.COLUMN_NAME_VALUE + ") " +
                "from " + GastoEntry.TABLE_NAME +
                " WHERE " + GastoEntry.COLUMN_NAME_USER_ID + " = '" + user.getUser_id() + "' ";

        double valorGasto = 0;
        try {
            Cursor cursor = db.rawQuery(query_gasto, null);
            if (cursor != null && cursor.moveToFirst()) {
                valorGasto = cursor.getDouble(0);
            }

        } catch (Throwable e) { }

        user.setSaldo(valorReceita - valorGasto);
    }
}
