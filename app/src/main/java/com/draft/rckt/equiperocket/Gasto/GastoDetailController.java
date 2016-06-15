package com.draft.rckt.equiperocket.Gasto;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Date;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

public class GastoDetailController extends AppCompatActivity implements CreateGastoDialog.NoticeDialogListener{
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private Gasto gasto;

    private TextView textView_titulo;
    private TextView textView_data;
    private TextView textView_valor;
    private TextView textView_descr;
    private TextView textView_tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DatabaseHelper(getApplicationContext());

        setContentView(R.layout.activity_gasto_detail_controller);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getTextView();
        setGasto();
        fillTextView();
    }

    private void fillTextView() {
        //TODO: Arrumar Data e descrição
        textView_titulo.setText(getGasto().getTitulo());
        textView_data.setText("A/A/A");
        textView_valor.setText(String.valueOf(getGasto().getValor()));
        textView_tipo.setText(getGasto().getTipo());
        textView_descr.setText("njlabjasbo abg ud uogau ga iudgudsgids iudsgbuisdbg sdbg bdsgdks bdkldgksbkdslb");

    }

    private void getTextView() {
        textView_titulo = (TextView) findViewById(R.id.textView_titulo_id);
        textView_data = (TextView) findViewById(R.id.textView_data_id);
        textView_valor = (TextView) findViewById(R.id.textView_valor_id);
        textView_tipo = (TextView) findViewById(R.id.textView_tipo_id);
        textView_descr = (TextView) findViewById(R.id.textView_desc_id);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detail_gasto_controller_menu, menu);
        return true;
    }

    /**
     * Caso o botao back tenha sido pressionado abre a view anterior
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GastoDetailController.this, GastoController.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        finish();
    }

    /**
     * Verifica qual item e determina a acao correspondente a ele
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                showNoticeDialog();
                return true;
            case R.id.edit:
                // tratamento botao modificacao
                Intent intent = new Intent(GastoDetailController.this, GastoModifyController.class);
                intent.putExtra("gasto", getGasto());
                startActivity(intent);
                return true;
            default:
                finish();
                return true;
        }
        //return false;
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateGastoDialog();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");

    }

    //Caso o usuario tenha clicado em "Cancelar", caixa de dialogo e' dispensada
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();

    }

    /*
        Caso o usuario tenha clicado em "Delete": deleta o gasto e mostra Toast indicando sucesso
        ou fracasso na exclusao do gasto do banco
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        boolean isDeleteSucessful = false;
                //removeGasto(get_gasto_id());
        dialog.dismiss();
        if(isDeleteSucessful) {
            Toast.makeText(getApplicationContext(), "Gasto deletado com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Falha ao deletar gasto.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Remove uma entrada da tabela de gastos
     * @param gasto_id identificador unico da entrada a ser deletada
     * @return true se houve sucesso na remocao, false caso contrario
     */
    public boolean removeGasto(int gasto_id){
        int n_rows; // numero de registros alterados na database

        db = mDbHelper.getWritableDatabase(); // ganha acesso a database

        // condicao de where da query
        String where_clause = DatabaseContract.GastoEntry.COLUMN_NAME_ENTRY_ID +
                " = " + Integer.toString(gasto_id);
        n_rows = db.delete(DatabaseContract.GastoEntry.TABLE_NAME, where_clause, null);
        db.close(); // libera database

        // se alterou alguma linha retorna true
        if (n_rows > 0)
            return true;
        else
            return false;
    }

    public Gasto getGasto(){
        return this.gasto;
    }

    private void setGasto(){
        this.gasto = (Gasto) getIntent().getExtras().getSerializable("gasto");
    }


}
