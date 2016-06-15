package com.draft.rckt.equiperocket.Receita;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.Gasto.CreateGastoDialog;
import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.R;

import java.sql.Date;

public class ReceitaDetailController extends AppCompatActivity implements CreateReceitaDialog.NoticeDialogListener {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private Toolbar toolbar;
    private TextView textToolbar;

    private Receita receita;
    private TextView textView_titulo;
    private TextView textView_data;
    private TextView textView_valor;
    private TextView textView_desc;
    private TextView textView_tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DatabaseHelper(getApplicationContext());

        // TODO: verificar as funcionalidades dos botões

        setContentView(R.layout.activity_receita_detail_controller);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //TOOLBAR
//        toolbar = (Toolbar) findViewById(R.id.tb_detalhes_id);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        textToolbar = (TextView) findViewById(R.id.textToolbar_id);
//        textToolbar.setText("Detalhes da receita");

        //TEXTVIEW
        textView_titulo = (TextView) findViewById(R.id.textView_titulo_id);
        textView_data = (TextView) findViewById(R.id.textView_data_id);
        textView_valor = (TextView) findViewById(R.id.textView_valor_id);
        textView_tipo = (TextView) findViewById(R.id.textView_tipo_id);
        textView_desc = (TextView) findViewById(R.id.textView_desc_id);

        //Pegando dados da receita selecionada
        setReceita();

        //Preenchendo TextView
        fillTextView();

    }

    private void fillTextView()
    {
        //TODO: Arrumar Data e descrição
        textView_titulo.setText(getReceita().getTitulo());
        textView_data.setText("A/A/A");
        textView_valor.setText(String.valueOf(getReceita().getValor()));
        textView_tipo.setText(getReceita().getTipo());
        textView_desc.setText("njlabjasbo abg ud uogau ga iudgudsgids iudsgbuisdbg sdbg bdsgdks bdkldgksbkdslb");
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detail_gasto_controller_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReceitaDetailController.this, ReceitaController.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                showNoticeDialog();
                return true;
            case R.id.edit:
                Intent intent = new Intent(ReceitaDetailController.this, ReceitaModifyController.class);
                //TODO: Muito feio isso aqui, arrumar tbm. Dica: Passar um objeto Receita, só isso
                intent.putExtra("receita", getReceita());
                startActivity(intent);
                return true;

        }
        return true;
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateGastoDialog();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        boolean isDeleteSucessful =  removeReceita(getReceita().getReceita_id());
        dialog.dismiss();
        if(isDeleteSucessful)
            Toast.makeText(getApplicationContext(),"Receita deletada com sucesso.",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Falha ao deletar receita." , Toast.LENGTH_SHORT).show();
    }

    /**
     * Remove registro da tabela de receitas
     * @param receita_id identificador unico da receita
     * @return true se um registro foi removido, false caso contrario
     *
     */
    protected boolean removeReceita(int receita_id) {

        int n_rows; // numero de registros alterados na database

        db = mDbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = DatabaseContract.ReceitaEntry.COLUMN_NAME_ENTRY_ID +
                " = " + Integer.toString(receita_id);
        n_rows = db.delete(DatabaseContract.ReceitaEntry.TABLE_NAME, where_clause, null);
        db.close(); // libera database

        // se alterou alguma linha retorna true
        if (n_rows > 0)
            return true;
        else
            return false;
    }

    public Receita getReceita(){
        return this.receita;
    }

    private void setReceita(){
        this.receita = (Receita) getIntent().getExtras().getSerializable("receita");
    }
}
