package com.draft.rckt.equiperocket.Receita;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class ReceitaModifyController extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private Receita receita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null){
            setReceita();
        }else{
            //TODO: mostrar pop-up de erro
        }

        //TODO: inserir informacoes da receita na UI

        setContentView(R.layout.activity_receita_modify_controller);
    }

    private void setReceita() {
        this.receita = (Receita) getIntent().getExtras().getSerializable("receita");
    }

    @Override
    public void onClick(View v) {
        //TODO: realizar tratamento dos botoes disponiveis (modificar, cancelar)
    }


}
