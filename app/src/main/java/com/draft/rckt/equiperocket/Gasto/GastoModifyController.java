package com.draft.rckt.equiperocket.Gasto;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class GastoModifyController extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private int gasto_id;
    private String gasto_title;
    private String gasto_descr;
    private float gasto_value;
    private String gasto_type;
    private Date gasto_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null){
            set_gasto_id((int) b.get("gasto_id"));
            set_gasto_title((String) b.get("gasto_title"));
            set_gasto_descr((String) b.get("gasto_descr"));
            set_gasto_value((float) b.get("gasto_value"));
            set_gasto_type((String) b.get("gasto_type"));
            set_gasto_date((Long) b.get("gasto_date"));
        }else{
            //TODO: mostrar pop-up de erro
        }

        db.close();

        setContentView(R.layout.activity_gasto_modify_controller);
    }

    public void set_gasto_id(int gasto_id) {
        this.gasto_id = gasto_id;
    }

    public void set_gasto_title(String gasto_title) {
        this.gasto_title = gasto_title;
    }

    public void set_gasto_descr(String gasto_descr) {
        this.gasto_descr = gasto_descr;
    }

    public void set_gasto_value(float gasto_value) {
        this.gasto_value = gasto_value;
    }

    public void set_gasto_type(String gasto_type) {
        this.gasto_type = gasto_type;
    }

    public void set_gasto_date(Long gasto_date) {
        this.gasto_date.setTime(gasto_date);
    }

    public int get_gasto_id() {
        return gasto_id;
    }

    public String get_gasto_title() {
        return gasto_title;
    }

    public String get_gasto_descr() {
        return gasto_descr;
    }

    public float get_gasto_value() {
        return gasto_value;
    }

    public String get_gasto_type() {
        return gasto_type;
    }

    public Date get_gasto_date() {
        return gasto_date;
    }

}
