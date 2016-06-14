package com.draft.rckt.equiperocket.Gasto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.CustomListAdapter;
import com.draft.rckt.equiperocket.Relatorio.RelatorioController;
import com.draft.rckt.equiperocket.Grafico.GraficoController;

import java.util.ArrayList;

public class GastoController extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ArrayList<Gasto> array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Gasto adicionado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                /**TODO
                 * Inserir classe de Inserção de Receita aqui
                 *
                 *  intent.setClass(GastoController.this,NOMECLASSEINSERCAORECEITA);
                 */
                intent.setClass(GastoController.this,GastoDetailController.class);
                startActivity(intent);
                Snackbar.make(view, "Gasto adicionado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listView_id);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showGasto(position);
            }
        });

        preencherListViewTeste();

        listView.setAdapter(new CustomListAdapter1(this, array));

    }
    private void preencherListViewTeste() {
        array = new ArrayList<Gasto>();

        DatabaseController db = new DatabaseController(this);

        // TESTEEEEEEEEE
        int i;
        for (i = 1; i < 15; i++)
        {
            Gasto rec = new Gasto();
            rec.user_id = "user_id " + i;
            rec.gasto_id = 1;
            rec.titulo = "Receit " + i;
            rec.descr = "aaaaaaa bbbbbb    ccccccc " + i;
            rec.tipo = "qualquer uma";
            rec.valor = (float) 4000.90 + i;
            array.add(rec);
            // db.addItemgasto(rec);
        }

        // array = db.getAllgastoOrderByDate();

    }
    public void showGasto(int pos) {

        Intent it = new Intent(this, GastoDetailController.class);
        Gasto gasto= array.get(pos);
        it.putExtra("gasto", gasto);

        startActivity(it);
        //finish();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*Atualizar a pagina atual depois de mudancas
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gasto_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();

        if (id == R.id.gerenciador_gastos) {
            //do nothing
        } else if (id == R.id.gerenciador_gastos) {
            intent.setClass(this, GastoController.class);
            startActivity(intent);
        } else if (id == R.id.relatorio) {
            intent.setClass(this, RelatorioController.class);
            startActivity(intent);
        } else if (id == R.id.grafico) {
            intent.setClass(this, GraficoController.class);
            startActivity(intent);
    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
