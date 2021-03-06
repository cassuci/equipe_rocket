package com.draft.rckt.equiperocket.Receita;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.Gasto.CreateGastoDialog;
import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;


import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ReceitaDetailController extends AppCompatActivity implements CreateReceitaDialog.NoticeDialogListener {

    private SQLiteDatabase db;

    private Receita receita;
    private TextView textView_titulo;
    private TextView textView_data;
    private TextView textView_valor;
    private TextView textView_desc;
    private TextView textView_tipo;
    private DatabaseController dbControl;
    private static boolean isModified = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbControl = new DatabaseController(getApplicationContext());

        setContentView(R.layout.activity_receita_detail_controller);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_receita_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title_receita_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setando TextViews
        getTextViews();

        //Pegando dados da receita selecionada
        setReceita();

        //Preenchendo TextView
        fillTextView();

    }

    private void getTextViews()
    {
        //TEXTVIEW
        textView_titulo = (TextView) findViewById(R.id.textView_titulo_receita_id);
        textView_data = (TextView) findViewById(R.id.textView_data_receita_id);
        textView_valor = (TextView) findViewById(R.id.textView_valor_receita_id);
        textView_tipo = (TextView) findViewById(R.id.textView_tipo_receita_id);
        textView_desc = (TextView) findViewById(R.id.textView_desc_receita_id);
        textView_desc.setMovementMethod(new ScrollingMovementMethod());

    }

    private void fillTextView()
    {
        textView_titulo.setText(receita.getTitulo());
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formatoData.format(receita.getData());
        textView_data.setText(data);
        DecimalFormat formatoValor = new DecimalFormat("###,###,##0.00"); // aqui criamos um
        textView_valor.setText("R$ " + formatoValor.format(receita.getValor()));
        textView_tipo.setText(receita.getTipo());
        textView_desc.setText(receita.getDesc());
    }

    private void setReceita(){
        this.receita = (Receita) getIntent().getExtras().getSerializable("receita");
        if (isModified) //arrumando caso de modificação
        {
            receita = dbControl.getReceita(receita.getReceita_id());
            isModified = false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detail_receita_controller_menu, menu);
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
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                showNoticeDialog();
                return true;
            case R.id.edit:
                isModified = true;
                Intent intent = new Intent(ReceitaDetailController.this, ReceitaModifyController.class);
                intent.putExtra("receita", receita);
                startActivity(intent);
                return true;
            default:
                finish();
                return true;
        }
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateReceitaDialog();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        boolean isDeleteSucessful = dbControl.deleteReceita(receita);

        dialog.dismiss();
        if(isDeleteSucessful) {
            Toast.makeText(getApplicationContext(), "Receita deletada com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        }else
           Toast.makeText(getApplicationContext(),"Falha ao deletar receita." , Toast.LENGTH_SHORT).show();
    }
}
