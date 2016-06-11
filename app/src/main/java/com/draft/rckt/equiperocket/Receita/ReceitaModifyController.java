package com.draft.rckt.equiperocket.Receita;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class ReceitaModifyController extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private int receita_id;
    private String receita_title;
    private String receita_descr;
    private float receita_value;
    private String receita_type;
    private Date receita_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null){
            receita_id = (int) b.get("receita_id");
            receita_title = (String) b.get("receita_title");
            receita_descr = (String) b.get("receita_descr");
            receita_value = (float) b.get("receita_value");
            receita_type = (String) b.get("receita_type");
            receita_date.setTime((Long) b.get("receita_date"));
        }else{
            //TODO: mostrar pop-up de erro
        }


        db.close();

        setContentView(R.layout.activity_receita_modify_controller);
    }

    public int get_receita_id() {
        return receita_id;
    }
}
