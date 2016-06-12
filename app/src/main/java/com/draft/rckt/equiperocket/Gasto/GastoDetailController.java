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

    private int gasto_id;
    private String gasto_title;
    private String gasto_descr;
    private float gasto_value;
    private String gasto_type;
    private Date gasto_date;

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
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detail_gasto_controller_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GastoDetailController.this, GastoController.class);
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
                // tratamento botao modificacao
                Intent intent = new Intent(GastoDetailController.this, GastoModifyController.class);
                intent.putExtra("gasto_id", get_gasto_id());
                intent.putExtra("gasto_title", get_gasto_title());
                intent.putExtra("gasto_descr", get_gasto_descr());
                intent.putExtra("gasto_value", get_gasto_value());
                intent.putExtra("gasto_type", get_gasto_type());
                intent.putExtra("gasto_date", get_gasto_date());
                startActivity(intent);
                return true;
        }
        return false;
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
        boolean isDeleteSucessful =  removeGasto(get_gasto_id());
        dialog.dismiss();
        if(isDeleteSucessful)
            Toast.makeText(getApplicationContext(),"Gasto deletado com sucesso.",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Falha ao deletar gasto." , Toast.LENGTH_SHORT).show();
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
