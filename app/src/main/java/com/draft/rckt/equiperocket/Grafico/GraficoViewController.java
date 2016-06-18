package com.draft.rckt.equiperocket.Grafico;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Database.DatabaseContract.GastoEntry;
import com.draft.rckt.equiperocket.Database.DatabaseContract.ReceitaEntry;
import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.Gasto.Gasto;
import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.Receita;
import com.draft.rckt.equiperocket.Receita.ReceitaController;
import com.draft.rckt.equiperocket.Relatorio.RelatorioController;
import com.draft.rckt.equiperocket.Usuario.Usuario;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GraficoViewController extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RECEITA = 0;
    private static final int GASTO = 1;
    private static final int GRAFICO_BARRAS = 0;
    private static final int GRAFICO_LINHAS = 1;
    private static final int GRAFICO_PONTOS = 2;


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private int graph_type;
    private boolean showReceitas;
    private boolean showGastos;
    private HashMap<String, Boolean> filtro_receitas;
    private HashMap<String, Boolean> filtro_gastos;

    private long data_inicio;
    private long data_fim;

    private boolean include_receitas;
    private boolean include_gastos;

    private Calendar startCal;
    private Calendar endCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        include_receitas = (boolean) b.get("receitas_include");
        include_gastos = (boolean) b.get("gastos_include");

        filtro_receitas = (HashMap<String, Boolean>) b.get("receitas_filtros");
        filtro_gastos = (HashMap<String, Boolean>) b.get("gastos_filtros");

        boolean startDateSet = (boolean) b.get("startDateSet");
        if (startDateSet)
            startCal = (Calendar) b.get("startDate");
        else{
            startCal = Calendar.getInstance();
            startCal.set(1970,0,1);
        }

        boolean endDateSet = (boolean) b.get("endDateSet");
        if (endDateSet)
            endCal = (Calendar) b.get("endDate");
        else{
            endCal = Calendar.getInstance();
            endCal.set(1970, 0 , 2);
        }

        setContentView(R.layout.activity_grafico_view_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_grafico_view);
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
        Usuario user = Usuario.getInstance();
        nav_user.setText(user.getNome());

        show_graph();
    }


    protected void show_graph() {

        DatabaseController mDbController = new DatabaseController(getApplicationContext());

        GraphView grafico = (GraphView) findViewById(R.id.graphView_grafico);

        ArrayList<Receita> receitas;
        ArrayList<Gasto> gastos;

        if (include_receitas) {
            receitas = mDbController.getAllReceitaOrderByDate();

            Date maxTime = receitas.get(0).getData();
            Date minTime = receitas.get(0).getData();

            ArrayList<DataPoint> pontos_receitas = new ArrayList<DataPoint>();
            for (int i = 0; i < receitas.size(); i++){
                double value = (double)receitas.get(i).getValor();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receitas.get(i).getData());

                // mantem registro das datas maxima e minima
                // para ajustar o eixo temporal do grafico
                if (maxTime.getTime() < calendar.getTime().getTime()){
                    maxTime = calendar.getTime();
                }
                if (calendar.getTime().getTime() < minTime.getTime()){
                    minTime = calendar.getTime();
                }

                pontos_receitas.add(
                        new DataPoint(calendar.getTime(),value
                        ));
            }

            DataPoint[] pontos_receitas_array = pontos_receitas.toArray(new DataPoint[pontos_receitas.size()]);
            LineGraphSeries<DataPoint> series_receita = new LineGraphSeries<>(pontos_receitas_array);
            series_receita.setColor(Color.BLUE);
            /*BarGraphSeries<DataPoint> series_receita = new BarGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(receitas.get(0).getData().getTime(), receitas.get(0).getValor()),
                    new DataPoint(receitas.get(1).getData().getTime(), receitas.get(1).getValor())
            });*/

            // set manual x bounds to have nice steps
            grafico.getViewport().setMinX(minTime.getTime());
            grafico.getViewport().setMaxX(maxTime.getTime());
            grafico.getViewport().setXAxisBoundsManual(true);

            grafico.addSeries(series_receita);

            grafico.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            grafico.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of
        }

        if (include_gastos) {
            gastos = mDbController.getAllGastoOrderByDate();

            Date maxTime = gastos.get(0).getData();
            Date minTime = gastos.get(0).getData();

            ArrayList<DataPoint> pontos_gastos = new ArrayList<DataPoint>();
            for (int i = 0; i < gastos.size(); i++) {
                double value = (double) gastos.get(i).getValor();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(gastos.get(i).getData());

                // mantem registro das datas maxima e minima
                // para ajustar o eixo temporal do grafico
                if (maxTime.getTime() < calendar.getTime().getTime()) {
                    maxTime = calendar.getTime();
                }
                if (calendar.getTime().getTime() < minTime.getTime()) {
                    minTime = calendar.getTime();
                }

                pontos_gastos.add(
                        new DataPoint(calendar.getTime(), value
                        ));
            }

            DataPoint[] pontos_gastos_array = pontos_gastos.toArray(new DataPoint[pontos_gastos.size()]);
            PointsGraphSeries<DataPoint> series_gasto = new PointsGraphSeries<>(pontos_gastos_array);
            series_gasto.setColor(Color.RED);
                /*BarGraphSeries<DataPoint> series_receita = new BarGraphSeries<DataPoint>(new DataPoint[]{
                        new DataPoint(receitas.get(0).getData().getTime(), receitas.get(0).getValor()),
                        new DataPoint(receitas.get(1).getData().getTime(), receitas.get(1).getValor())
                });*/

            // set manual x bounds to have nice steps
            grafico.getViewport().setMinX(minTime.getTime());
            grafico.getViewport().setMaxX(maxTime.getTime());
            grafico.getViewport().setXAxisBoundsManual(true);

            grafico.addSeries(series_gasto);

            grafico.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            grafico.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of
        }

    }

    private void plot_lines(List<Graph_Item> receita_items, List<Graph_Item> gasto_items) {
        //TODO: implementar
    }

    private void plot_bars(List<Graph_Item> receita_items, List<Graph_Item> gasto_items) {
        //TODO: implementar
    }

    private void plot_points(List<Graph_Item> receita_items, List<Graph_Item> gasto_items) {
        //TODO: implementar
    }

    public int get_graph_type() {
        return graph_type;
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
