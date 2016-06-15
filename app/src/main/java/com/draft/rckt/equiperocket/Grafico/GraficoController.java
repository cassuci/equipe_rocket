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

    ExpandableListAdapter listAdapter_receitas;
    ExpandableListAdapter listAdapter_gastos;
    ExpandableListView expListView_receitas;
    ExpandableListView expListView_gastos;
    List<String> listDataHeader_receitas;
    HashMap<String, List<String>> listDataChild_receitas;
    List<String> listDataHeader_gastos;
    HashMap<String, List<String>> listDataChild_gastos;

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
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navHeaderTitle);
        nav_user.setText("bla");// TODO substituir por user_id

        // get the listview
        expListView_receitas = (ExpandableListView) findViewById(R.id.expandableList_grafico_receita_filtro);
        expListView_gastos = (ExpandableListView) findViewById(R.id.expandableList_grafico_gasto_filtro);

        // preparing list data
        prepareListData();

        listAdapter_receitas = new ExpandableListAdapter(this, listDataHeader_receitas, listDataChild_receitas);
        listAdapter_gastos = new ExpandableListAdapter(this, listDataHeader_gastos, listDataChild_gastos);

        // setting list adapter
        expListView_receitas.setAdapter(listAdapter_receitas);
        expListView_gastos.setAdapter(listAdapter_gastos);
    }

    private void prepareListData() {
        listDataHeader_receitas = new ArrayList<String>();
        listDataChild_receitas = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader_receitas.add("Filtro de busca (opcional)");

        // Adding child data
        List<String> filtros_receita = new ArrayList<String>();
        filtros_receita.add("Rendimentos");
        filtros_receita.add("Salário");
        filtros_receita.add("Bônus");
        filtros_receita.add("Outros");

        listDataHeader_gastos = new ArrayList<String>();
        listDataChild_gastos = new HashMap<String, List<String>>();

        listDataHeader_gastos.add("Filtro de busca (opcional)");

        List<String> filtros_gasto = new ArrayList<String>();
        filtros_gasto.add("Alimentação");
        filtros_gasto.add("Transporte");
        filtros_gasto.add("Contas");
        filtros_gasto.add("Lazer");

        listDataChild_receitas.put(listDataHeader_receitas.get(0), filtros_receita); // Header, Child data
        listDataChild_gastos.put(listDataHeader_gastos.get(0), filtros_gasto); // Header, Child data
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
