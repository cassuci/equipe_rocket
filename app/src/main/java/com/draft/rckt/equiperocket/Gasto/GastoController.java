package com.draft.rckt.equiperocket.Gasto;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

public class GastoController extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DatabaseHelper(this.getApplicationContext());
        db = mDbHelper.getReadableDatabase(); // ganha acesso a database

        //TODO:  implementar leitura dos gastos

        db.close(); // libera database

        setContentView(R.layout.activity_gasto_controller);
    }
}
