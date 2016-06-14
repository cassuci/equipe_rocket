package com.draft.rckt.equiperocket.Grafico;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.Gasto.GastoDetailController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.ReceitaController;
import com.draft.rckt.equiperocket.Relatorio.RelatorioController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//TODO: TERMINAR INTERFACE
public class GraficoController extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_grafico);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableList_grafico_receita_filtro);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Filtro de busca");

        // Adding child data
        List<String> filtros = new ArrayList<String>();
        filtros.add("Rendimentos");
        filtros.add("Salário");
        filtros.add("Bônus");
        filtros.add("Outros");

        listDataChild.put(listDataHeader.get(0), filtros); // Header, Child data
    }


    @Override
    public void onClick(View v) {
        //TODO: coletar informacoes inseridas na UI e comecar a atividade de visualizacao
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();

        if (id == R.id.gerenciador_gastos) {
            intent.setClass(this, GastoController.class);
            startActivity(intent);
        } else if (id == R.id.gerenciador_receitas) {
            intent.setClass(this, ReceitaController.class);
            startActivity(intent);
        } else if (id == R.id.relatorio) {
            intent.setClass(this, RelatorioController.class);
            startActivity(intent);
        } else if (id == R.id.grafico) {
            // do nothings
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
