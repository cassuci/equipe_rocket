package com.draft.rckt.equiperocket.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gabriel on 6/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String REAL_TYPE = " REAL";
    private static final String VARCHAR20_TYPE = " VARCHAR(20)";
    private static final String VARCHAR40_TYPE = " VARCHAR(40)";
    private static final String VARCHAR100_TYPE = " VARCHAR(100)";
    private static final String VARCHAR255_TYPE = " VARCHAR(255)";
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_NAME + " (" +
                    DatabaseContract.UserEntry.COLUMN_NAME_USER_ID + " VARCHAR(40) PRIMARY KEY," +
                    DatabaseContract.UserEntry.COLUMN_NAME_PASSWORD + VARCHAR20_TYPE + " NOT NULL" + COMMA_SEP +
                    DatabaseContract.UserEntry.COLUMN_NAME_NAME + VARCHAR100_TYPE +
                    " );"
                    + "CREATE TABLE " + DatabaseContract.ReceitaEntry.TABLE_NAME + " (" +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_USER_ID + " VARCHAR(40) FOREIGN KEY REFERENCES "+
                    DatabaseContract.UserEntry.TABLE_NAME+ "("+ DatabaseContract.UserEntry.COLUMN_NAME_USER_ID +")"+ " ON DELETE CASCADE"+COMMA_SEP +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_TITLE + VARCHAR40_TYPE + COMMA_SEP +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_CONTENT + VARCHAR255_TYPE+ COMMA_SEP +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_VALUE + REAL_TYPE+ " NOT NULL" +COMMA_SEP +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_TYPE + VARCHAR40_TYPE+ COMMA_SEP +
                    DatabaseContract.ReceitaEntry.COLUMN_NAME_DATE + DATE_TYPE+ " NOT NULL DEFAULT NOW()"+
                    " );"
                    + "CREATE TABLE " + DatabaseContract.GastoEntry.TABLE_NAME + " (" +
                    DatabaseContract.GastoEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.GastoEntry.COLUMN_NAME_USER_ID + " VARCHAR(40) FOREIGN KEY REFERENCES "+
                    DatabaseContract.UserEntry.TABLE_NAME+ "("+ DatabaseContract.UserEntry.COLUMN_NAME_USER_ID +")"+ " ON DELETE CASCADE"+COMMA_SEP +
                    DatabaseContract.GastoEntry.COLUMN_NAME_TITLE + VARCHAR40_TYPE + COMMA_SEP +
                    DatabaseContract.GastoEntry.COLUMN_NAME_CONTENT + VARCHAR255_TYPE+ COMMA_SEP +
                    DatabaseContract.GastoEntry.COLUMN_NAME_VALUE + REAL_TYPE+ " NOT NULL" + COMMA_SEP +
                    DatabaseContract.GastoEntry.COLUMN_NAME_TYPE + VARCHAR40_TYPE+ COMMA_SEP +
                    DatabaseContract.GastoEntry.COLUMN_NAME_DATE + DATE_TYPE+ " NOT NULL DEFAULT NOW()"+
                    " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.ReceitaEntry.TABLE_NAME + ";"
                    +"DROP TABLE IF EXISTS " + DatabaseContract.GastoEntry.TABLE_NAME + ";"
                    +"DROP TABLE IF EXISTS " + DatabaseContract.UserEntry.TABLE_NAME + ";";


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Conversao de versoes da database nao foi implementada
        // Para nao causar inconsistencia, a atualizacao apaga a versao anterior
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
