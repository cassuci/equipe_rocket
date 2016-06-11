package com.draft.rckt.equiperocket.Receita;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
            set_receita_id((int) b.get("receita_id"));
            set_receita_title((String) b.get("receita_title"));
            set_receita_descr((String) b.get("receita_descr"));
            set_receita_value((float) b.get("receita_value"));
            set_receita_type((String) b.get("receita_type"));
            set_receita_date((Long) b.get("receita_date"));
        }else{
            //TODO: mostrar pop-up de erro
        }

        //TODO: inserir informacoes da receita na UI

        setContentView(R.layout.activity_receita_modify_controller);
    }

    public void set_receita_id(int receita_id) {
        this.receita_id = receita_id;
    }

    public void set_receita_title(String receita_title) {
        this.receita_title = receita_title;
    }

    public void set_receita_descr(String receita_descr) {
        this.receita_descr = receita_descr;
    }

    public void set_receita_value(float receita_value) {
        this.receita_value = receita_value;
    }

    public void set_receita_type(String receita_type) {
        this.receita_type = receita_type;
    }

    public void set_receita_date(Long receita_date) {
        this.receita_date.setTime(receita_date);
    }

    public int get_receita_id() {
        return receita_id;
    }

    public String get_receita_title() {
        return receita_title;
    }

    public String get_receita_descr() {
        return receita_descr;
    }

    public float get_receita_value() {
        return receita_value;
    }

    public String get_receita_type() {
        return receita_type;
    }

    public Date get_receita_date() {
        return receita_date;
    }
}
