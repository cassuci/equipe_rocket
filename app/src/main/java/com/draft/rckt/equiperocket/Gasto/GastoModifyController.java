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

    private Gasto gasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            setGasto();
        } else {
            //TODO: mostrar pop-up de erro
        }

        //TODO: inserir informacoes da receita na UI

        setContentView(R.layout.activity_gasto_modify_controller);
    }

    private void setGasto() {
        this.gasto = (Gasto) getIntent().getExtras().getSerializable("gasto");
    }

    @Override
    public void onClick(View v) {

        // if (v.getId() == R.id.#IDBOTAOCONFIRMA)
        // tratamento confirmacao de modificacao
        //TODO: extrair informações da UI e editar insercoes no Bundle
        Gasto gasto_modificado = new Gasto(); //TODO: usar construtor completo do gasto
        modify_confirmed(gasto_modificado);


        //TODO: finalizar este tratamento
        // if (v.getId() == R.id.#IDBOTAOCANCELA)
        // tratamento botao cancela
        // mata activity
        //finish();


    }

    /**
     * Executa acoes de modificacao do registro no banco de dados
     * @param gasto_modificado objeto contendo as informacoes atualizadas do gasto
     */
    private void modify_confirmed(Gasto gasto_modificado) {
        String gasto_title = null;
        String gasto_descr = null;
        double gasto_value;
        String gasto_type = null;

        // separa informacoes a serem atualizadas
        gasto_title = gasto_modificado.getTitulo();
        gasto_descr = gasto_modificado.getDescr();
        gasto_value = gasto_modificado.getValor();
        gasto_type = gasto_modificado.getTipo();

        // TODO: editar o acesso ao BD para utilizar DatabaseController
        // cria grupo de valores a serem atualizados
        ContentValues values = new ContentValues();
        values.put(GastoEntry.COLUMN_NAME_TITLE, gasto_title);
        values.put(GastoEntry.COLUMN_NAME_CONTENT, gasto_descr);
        values.put(GastoEntry.COLUMN_NAME_VALUE, gasto_value);
        values.put(GastoEntry.COLUMN_NAME_TYPE, gasto_type);

        // where statement para identificar registro a ser atualizado
        String where_clause = GastoEntry.COLUMN_NAME_ENTRY_ID
                + " = " + Integer.toString(gasto.getGasto_id());

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
}
