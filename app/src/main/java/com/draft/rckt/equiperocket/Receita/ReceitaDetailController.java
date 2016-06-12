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

public class ReceitaDetailController extends AppCompatActivity implements View.OnClickListener, CreateReceitaDialog.NoticeDialogListener {

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

        mDbHelper = new DatabaseHelper(getApplicationContext());

        // TODO: extrair detalhes da receita para mostrar na tela

        setContentView(R.layout.activity_gasto_detail_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detail_gasto_controller_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReceitaDetailController.this, GastoController.class);
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
                intent.putExtra("receita_id", get_receita_id());
                intent.putExtra("receita_title", get_receita_title());
                intent.putExtra("receita_descr", get_receita_descr());
                intent.putExtra("receita_value", get_receita_value());
                intent.putExtra("receita_type", get_receita_type());
                intent.putExtra("receita_date", get_receita_date());
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
        boolean isDeleteSucessful =  removeReceita(get_receita_id());
        dialog.dismiss();
        if(isDeleteSucessful)
            Toast.makeText(getApplicationContext(),"Gasto deletado com sucesso.",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Falha ao deletar gasto." , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        // Template botao if (v.getId() == R.id.#IDBOTAO){

        // tratamento botao deletar
        //removeReceita(get_receita_id());

        // tratamento botao modificacao
        /*Intent intent = new Intent(ReceitaDetailController.this, ReceitaModifyController.class);
        intent.putExtra("receita_id", get_receita_id());
        intent.putExtra("receita_title", get_receita_title());
        intent.putExtra("receita_descr", get_receita_descr());
        intent.putExtra("receita_value", get_receita_value());
        intent.putExtra("receita_type", get_receita_type());
        intent.putExtra("receita_date", get_receita_date());
        startActivity(intent);*/
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
