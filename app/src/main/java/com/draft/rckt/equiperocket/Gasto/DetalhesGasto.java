package com.draft.rckt.equiperocket.Gasto;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.draft.rckt.equiperocket.R;

public class DetalhesGasto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_gasto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detalhes_gasto_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:

                DeleteGasto();
                return true;
            case R.id.edit:
                Intent intent_edit = new Intent();
                /**Editar funcao intent.setClass(DetalhesGasto.this, NOMECLASSEEDITARGASTO)
                 intent_edit.setClass(DetalhesGasto.this,EditarGasto.class);**/
                startActivity(intent_edit);
                finish();
                return true;

        }
        return true;
    }
    public boolean DeleteGasto(){
        String string = "delete";
        DialogFragment newFragment = new DeleteFragment();
        newFragment.show(getFragmentManager(),string);
        return true;
    }

}
