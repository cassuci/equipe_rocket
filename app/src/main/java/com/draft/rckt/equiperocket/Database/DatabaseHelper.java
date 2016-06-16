package com.draft.rckt.equiperocket.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.draft.rckt.equiperocket.Database.DatabaseContract.UserEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.GastoEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;
import com.draft.rckt.equiperocket.Gasto.Gasto;


/**
 * Created by Gabriel on 6/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String SQL_CREATE_USERS =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " ( " +
                    UserEntry.COLUMN_NAME_USER_ID + " VARCHAR(20), " +
                    UserEntry.COLUMN_NAME_PASSWORD + " VARCHAR(20) NOT NULL, " +
                    UserEntry.COLUMN_NAME_NAME + " VARCHAR(40) , " +
                    " PRIMARY KEY (" + UserEntry.COLUMN_NAME_USER_ID + ")" +
                " );" ;

    private static final String SQL_CREATE_RECEITAS =
            " CREATE TABLE " + ReceitaEntry.TABLE_NAME + " (" +
                    ReceitaEntry.COLUMN_NAME_ENTRY_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    ReceitaEntry.COLUMN_NAME_USER_ID  + " VARCHAR(20) NOT NULL, " +
                    ReceitaEntry.COLUMN_NAME_TITLE + " VARCHAR(20), " +
                    ReceitaEntry.COLUMN_NAME_CONTENT  + " VARCHAR(200) , " +
                    ReceitaEntry.COLUMN_NAME_VALUE + " DOUBLE NOT NULL, " +
                    ReceitaEntry.COLUMN_NAME_TYPE + " VARCHAR(20) , " +
                    ReceitaEntry.COLUMN_NAME_DATE + " BIGINT NOT NULL, " +
                    " FOREIGN KEY (" + ReceitaEntry.COLUMN_NAME_USER_ID + " ) " +
                        "REFERENCES " + UserEntry.TABLE_NAME + " ( " + UserEntry.COLUMN_NAME_USER_ID + " ) " +
                        " ON DELETE NO ACTION " +
                        " ON UPDATE NO ACTION " +
            " )";

    private static final String SQL_CREATE_GASTOS =
            " CREATE TABLE " + GastoEntry.TABLE_NAME + " (" +
                    GastoEntry.COLUMN_NAME_ENTRY_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    GastoEntry.COLUMN_NAME_USER_ID  + " VARCHAR(20) NOT NULL, " +
                    GastoEntry.COLUMN_NAME_TITLE + " VARCHAR(20), " +
                    GastoEntry.COLUMN_NAME_CONTENT  + " VARCHAR(200) , " +
                    GastoEntry.COLUMN_NAME_VALUE + " DOUBLE NOT NULL, " +
                    GastoEntry.COLUMN_NAME_TYPE + " VARCHAR(20) , " +
                    GastoEntry.COLUMN_NAME_DATE + " BIGINT NOT NULL, " +
                    " FOREIGN KEY (" + GastoEntry.COLUMN_NAME_USER_ID + " ) " +
                    "REFERENCES " + UserEntry.TABLE_NAME + " ( " + UserEntry.COLUMN_NAME_USER_ID + " ) " +
                    " ON DELETE NO ACTION " +
                    " ON UPDATE NO ACTION " +
            " )";


    private static final String SQL_DELETE_USERS = " DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME + ";";
    private static final String SQL_DELETE_RECEITA = "DROP TABLE IF EXISTS " + ReceitaEntry.TABLE_NAME + ";";
    private static final String SQL_DELETE_GASTO = "DROP TABLE IF EXISTS " + GastoEntry.TABLE_NAME + ";";


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_RECEITAS);
        db.execSQL(SQL_CREATE_GASTOS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Conversao de versoes da database nao foi implementada
        // Para nao causar inconsistencia, a atualizacao apaga a versao anterior
        db.execSQL(SQL_DELETE_RECEITA);
        db.execSQL(SQL_DELETE_GASTO);
        db.execSQL(SQL_DELETE_USERS);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
