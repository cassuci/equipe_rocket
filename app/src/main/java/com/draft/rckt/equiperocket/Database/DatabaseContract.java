package com.draft.rckt.equiperocket.Database;

import android.provider.BaseColumns;

/**
 * Created by Gabriel on 6/5/2016.
 */
public final class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_USER_ID = "username";
        public static final String COLUMN_NAME_PASSWORD = "senha";
        public static final String COLUMN_NAME_NAME = "nome";
    }


    public static abstract class ReceitaEntry implements BaseColumns {
        public static final String TABLE_NAME = "Receita";
        public static final String COLUMN_NAME_ENTRY_ID = "receita_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_TITLE = "titulo";
        public static final String COLUMN_NAME_CONTENT = "descricao";
        public static final String COLUMN_NAME_VALUE = "valor";
        public static final String COLUMN_NAME_TYPE = "tipo";
        public static final String COLUMN_NAME_DATE = "data";
    }

    public static abstract class GastoEntry implements BaseColumns {
        public static final String TABLE_NAME = "Gastos";
        public static final String COLUMN_NAME_ENTRY_ID = "gasto_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_TITLE = "titulo";
        public static final String COLUMN_NAME_CONTENT = "descricao";
        public static final String COLUMN_NAME_VALUE = "valor";
        public static final String COLUMN_NAME_TYPE = "tipo";
        public static final String COLUMN_NAME_DATE = "data";
    }
}