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

        //Ivan
        //TODO:  implementar leitura das receitas

        db.close(); // libera database

        setContentView(R.layout.activity_receita_controller);
    }

}
