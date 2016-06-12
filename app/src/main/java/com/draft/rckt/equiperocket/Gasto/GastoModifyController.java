package com.draft.rckt.equiperocket.Gasto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseContract.GastoEntry;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class GastoModifyController extends AppCompatActivity implements View.OnClickListener {

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

        if (b != null) {
            set_gasto_id((int) b.get("gasto_id"));
            set_gasto_title((String) b.get("gasto_title"));
            set_gasto_descr((String) b.get("gasto_descr"));
            set_gasto_value((float) b.get("gasto_value"));
            set_gasto_type((String) b.get("gasto_type"));
            set_gasto_date((Long) b.get("gasto_date"));
        } else {
            //TODO: mostrar pop-up de erro
        }

        //TODO: inserir informacoes da receita na UI

        setContentView(R.layout.activity_gasto_modify_controller);
    }

    @Override
    public void onClick(View v) {

        // if (v.getId() == R.id.#IDBOTAOCONFIRMA)
        // tratamento confirmacao de modificacao
        //TODO: extrair informações da UI e editar insercoes no Bundle
        Bundle b = new Bundle();
        b.putString("gasto_title", null);
        b.putString("gasto_descr", null);
        b.putFloat("gasto_value", (float) 0.0);
        b.putString("gasto_type", null);
        modify_confirmed(b);


        //TODO: finalizar este tratamento
        // if (v.getId() == R.id.#IDBOTAOCANCELA)
        // tratamento botao cancela
        // mata activity
        //finish();


    }

    /**
     * Executa acoes de modificacao do registro no banco de dados
     * @param bundle conjunto de informacoes a serem atualizadas no banco de dados
     */
    private void modify_confirmed(Bundle bundle) {
        String gasto_title = null;
        String gasto_descr = null;
        float gasto_value;
        String gasto_type = null;

        // separa informacoes a serem atualizadas
        gasto_title = bundle.getString("gasto_title");
        gasto_descr = bundle.getString("gasto_descr");
        gasto_value = bundle.getFloat("gasto_value");
        gasto_type = bundle.getString("gasto_type");

        // cria grupo de valores a serem atualizados
        ContentValues values = null;
        values.put(GastoEntry.COLUMN_NAME_TITLE, gasto_title);
        values.put(GastoEntry.COLUMN_NAME_CONTENT, gasto_descr);
        values.put(GastoEntry.COLUMN_NAME_VALUE, gasto_value);
        values.put(GastoEntry.COLUMN_NAME_TYPE, gasto_type);

        // where statement para identificar registro a ser atualizado
        String where_clause = GastoEntry.COLUMN_NAME_ENTRY_ID
                + " = " + Integer.toString(get_gasto_id());

        mDbHelper = new DatabaseHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        int result = db.update(GastoEntry.TABLE_NAME, values, where_clause, null);
        db.close();

        if (result > 0){
            // informar modificacao realizada com sucesso
        }else{
            //TODO: informar falha na modificacao
        }

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
