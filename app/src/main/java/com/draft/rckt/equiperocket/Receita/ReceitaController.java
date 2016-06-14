package com.draft.rckt.equiperocket.Receita;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.Receita.Receita;

import com.draft.rckt.equiperocket.R;

import java.util.ArrayList;
import java.util.Date;

public class ReceitaController extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textToolbar;
    private ListView listView;
    private ArrayList<Receita> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita_controller);


        //TODO arrumar toolbar para padronizar
        toolbar = (Toolbar) findViewById(R.id.tb_gerenciador_id);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textToolbar = (TextView) findViewById(R.id.textToolbar_id);
        textToolbar.setText("Gerenciamento de receita");

        listView = (ListView) findViewById(R.id.listView_id);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showReceita(position);
            }
        });

        preencherListViewTeste();

        listView.setAdapter(new CustomListAdapter(this, array));

    }

    private void preencherListViewTeste() {
        array = new ArrayList<Receita>();

        DatabaseController db = new DatabaseController(this);

        // TESTEEEEEEEEE
        int i;
        for (i = 1; i < 15; i++)
        {
            Receita rec = new Receita();
            rec.user_id = "user_id " + i;
            rec.receita_id = 1;
            rec.titulo = "Receit " + i;
            rec.desc = "aaaaaaa bbbbbb    ccccccc " + i;
            rec.tipo = "qualquer uma";
            rec.valor = (float) 4000.90 + i;
            array.add(rec);
           // db.addItemReceita(rec);
        }

       // array = db.getAllReceitaOrderByDate();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    public void showReceita(int pos) {

        Intent it = new Intent(this, ReceitaDetailController.class);
        Receita receita = array.get(pos);
        it.putExtra("receita", receita);

        startActivity(it);
        finish();

    }
}