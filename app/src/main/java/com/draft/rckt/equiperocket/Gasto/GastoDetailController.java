package com.draft.rckt.equiperocket.Gasto;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

public class GastoDetailController extends AppCompatActivity implements CreateGastoDialog.NoticeDialogListener{

    private Gasto gasto;
    private static boolean isModified = false;
    private TextView textView_titulo;
    private TextView textView_data;
    private TextView textView_valor;
    private TextView textView_descr;
    private TextView textView_tipo;
    private DatabaseController dbControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbControl = new DatabaseController(getApplicationContext());

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
        textView_titulo.setText(gasto.getTitulo());
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formatoData.format(gasto.getData());
        textView_data.setText(data);
        DecimalFormat formatoValor = new DecimalFormat("###,###,##0.00"); // aqui criamos um
        textView_valor.setText("R$ " + formatoValor.format(gasto.getValor()));
        textView_tipo.setText(gasto.getTipo());
        textView_descr.setText(gasto.getDescr());

    }

    private void getTextView() {
        textView_titulo = (TextView) findViewById(R.id.textView_titulo_id);
        textView_data = (TextView) findViewById(R.id.textView_data_id);
        textView_valor = (TextView) findViewById(R.id.textView_valor_id);
        textView_tipo = (TextView) findViewById(R.id.textView_tipo_id);
        textView_descr = (TextView) findViewById(R.id.textView_desc_id);
        textView_descr.setMovementMethod(new ScrollingMovementMethod());
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
                isModified = true;
                Intent intent = new Intent(GastoDetailController.this, GastoModifyController.class);
                intent.putExtra("gasto", gasto);
                startActivity(intent);
                return true;
            default:
                finish();
                return true;
        }
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
        boolean isDeleteSucessful = dbControl.deleteGasto(gasto);;

        dialog.dismiss();
        if(isDeleteSucessful) {
            Toast.makeText(getApplicationContext(), "Gasto deletado com sucesso.", Toast.LENGTH_SHORT).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(), "Falha ao deletar gasto.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void setGasto(){
        gasto = (Gasto) getIntent().getExtras().getSerializable("gasto");
        if (isModified) //arrumando caso de modificação
        {
            gasto = dbControl.getGasto(gasto.getGasto_id());
            isModified = false;
        }
    }
}
