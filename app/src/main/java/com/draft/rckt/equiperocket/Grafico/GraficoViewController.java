package com.draft.rckt.equiperocket.Grafico;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.Gasto.Gasto;
import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.Receita;
import com.draft.rckt.equiperocket.Receita.ReceitaController;
import com.draft.rckt.equiperocket.Relatorio.RelatorioController;
import com.draft.rckt.equiperocket.Usuario.Usuario;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GraficoViewController extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean include_receitas;
    private boolean include_gastos;

    private HashMap<String, Boolean> filtro_receitas;
    private HashMap<String, Boolean> filtro_gastos;

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
        }

        setContentView(R.layout.activity_grafico_view_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_grafico_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
        receitas = mDbController.getAllReceitaOrderByDate();

        ArrayList<Gasto> gastos;
        gastos = mDbController.getAllGastoOrderByDate();

        Date maxTime_rec = null;
        Date minTime_rec = null;

        Date maxTime_gasto = null;
        Date minTime_gasto = null;

        double minValue_rec = -1.0;
        double maxValue_rec = -1.0;

        double minValue_gasto = -1.0;
        double maxValue_gasto = -1.0;

        if (include_receitas && receitas != null) {

            ArrayList<DataPoint> pontos_receitas = new ArrayList<>();
            for (int i = 0; i < receitas.size(); i++){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receitas.get(i).getData());
                boolean include_curr_rec = false;

                // testes dos filtros
                // data
                if (calendar.getTime().getTime() >= startCal.getTime().getTime()
                        && calendar.getTime().getTime() <= (endCal.getTime().getTime() +  8.64e+7)) {

                    // filtro do tipo
                    if (filtro_receitas.containsValue(true)) {
                        Boolean filter = filtro_receitas.get(receitas.get(i).getTipo());
                        if (filter != null) {
                            if (filter)
                                include_curr_rec = true;
                        }
                    } else {
                        include_curr_rec = true;
                    }
                }

                // inclui a receita
                if (include_curr_rec){
                    // inicializa as variaveis de maximo e minimo com a primeira receita valida
                    if (maxTime_rec == null){
                        maxTime_rec = calendar.getTime();
                    }
                    if (minTime_rec == null){
                        minTime_rec = calendar.getTime();
                    }

                    // mantem registro das datas maxima e minima
                    // para ajustar o eixo temporal do grafico
                    if (maxTime_rec.getTime() < calendar.getTime().getTime()) {
                        maxTime_rec = calendar.getTime();
                    }
                    if (minTime_rec.getTime() > calendar.getTime().getTime()) {
                        minTime_rec = calendar.getTime();
                    }

                    double value = receitas.get(i).getValor();
                    if (minValue_rec == -1.0){
                        minValue_rec = value;
                    }
                    if (maxValue_rec == -1.0){
                        maxValue_rec = value;
                    }


                    if (minValue_rec > value){
                        minValue_rec = value;
                    }
                    if (maxValue_rec < value){
                        maxValue_rec = value;
                    }


                    pontos_receitas.add(
                            new DataPoint(calendar.getTime(), value
                            ));
                }
            }

            DataPoint[] pontos_receitas_array = pontos_receitas.toArray(new DataPoint[pontos_receitas.size()]);
            LineGraphSeries<DataPoint> series_receita = new LineGraphSeries<>(pontos_receitas_array);
            PointsGraphSeries<DataPoint> series_receita_2 = new PointsGraphSeries<>(pontos_receitas_array);

            series_receita.setColor(Color.BLUE);
            series_receita.setTitle("Receitas");

            series_receita_2.setColor(Color.BLUE);
            series_receita_2.setTitle("Receitas");

            // set manual x bounds to have nice steps
            if (minTime_rec != null && maxTime_rec != null) {
                maxTime_rec.setTime((long) (maxTime_rec.getTime() + 8.64e+7));
                minTime_rec.setTime((long) (minTime_rec.getTime() - 8.64e+7));
                grafico.getViewport().setMinX(minTime_rec.getTime());
                grafico.getViewport().setMaxX(maxTime_rec.getTime());
                grafico.getViewport().setXAxisBoundsManual(true);
            }

            if (minValue_rec != -1.0 && maxValue_rec != -1.0){
                minValue_rec = 0.9 * minValue_rec;
                maxValue_rec = 1.1 * maxValue_rec;
                grafico.getViewport().setMinY(minValue_rec);
                grafico.getViewport().setMaxY(maxValue_rec);
                grafico.getViewport().setYAxisBoundsManual(true);
            }

            if (pontos_receitas.size() == 1)
                grafico.addSeries(series_receita_2);
            else if(pontos_receitas.size() > 1) {
                grafico.addSeries(series_receita);
            }

            grafico.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            grafico.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of
            grafico.getLegendRenderer().setVisible(true);
            grafico.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        }

        if (include_gastos && gastos != null) {

            ArrayList<DataPoint> pontos_gastos = new ArrayList<>();
            for (int i = 0; i < gastos.size(); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(gastos.get(i).getData());
                boolean include_curr_gasto = false;

                // testes dos filtros
                // data
                if (calendar.getTime().getTime() >= startCal.getTime().getTime()
                        && calendar.getTime().getTime() <= (endCal.getTime().getTime()+  8.64e+7)){

                    // filtro do tipo
                    if (filtro_gastos.containsValue(true)) {
                        Boolean filter = filtro_gastos.get(gastos.get(i).getTipo());
                        if (filter != null) {
                            if (filter)
                                include_curr_gasto = true;
                        }
                    } else {
                        include_curr_gasto = true;
                    }
                }

                // inclui a receita
                if (include_curr_gasto){
                    // inicializa as variaveis de maximo e minimo com a primeira receita valida
                    if (maxTime_gasto == null){
                        maxTime_gasto = calendar.getTime();
                    }
                    if (minTime_gasto == null){
                        minTime_gasto = calendar.getTime();
                    }

                    // mantem registro das datas maxima e minima
                    // para ajustar o eixo temporal do grafico
                    if (maxTime_gasto.getTime() < calendar.getTime().getTime()) {
                        maxTime_gasto = calendar.getTime();
                    }
                    if (minTime_gasto.getTime() > calendar.getTime().getTime()) {
                        minTime_gasto = calendar.getTime();
                    }

                    double value = gastos.get(i).getValor();

                    if (minValue_gasto == -1.0){
                        minValue_gasto = value;
                    }
                    if (maxValue_gasto == -1.0){
                        maxValue_gasto = value;
                    }


                    if (minValue_gasto > value){
                        minValue_gasto = value;
                    }
                    if (maxValue_gasto < value){
                        maxValue_gasto = value;
                    }
                    pontos_gastos.add(
                            new DataPoint(calendar.getTime(), value
                            ));
                }
            }

            DataPoint[] pontos_gastos_array = pontos_gastos.toArray(new DataPoint[pontos_gastos.size()]);
            LineGraphSeries<DataPoint> series_gasto = new LineGraphSeries<>(pontos_gastos_array);
            PointsGraphSeries<DataPoint> series_gasto_2 = new PointsGraphSeries<>(pontos_gastos_array);

            series_gasto.setColor(Color.RED);
            series_gasto.setTitle("Gastos");

            series_gasto_2.setColor(Color.RED);
            series_gasto_2.setTitle("Gastos");

            // set manual x bounds to have nice steps
            if (minTime_gasto != null && maxTime_gasto != null) {
                if (minTime_rec != null) {
                    if (minTime_gasto.getTime() <= (minTime_rec.getTime()  + 8.64e+7 )) {
                        minTime_gasto.setTime((long) (minTime_gasto.getTime() - 8.64e+7));
                        grafico.getViewport().setMinX(minTime_gasto.getTime());
                    }
                }else{
                    minTime_gasto.setTime((long) (minTime_gasto.getTime() - 8.64e+7));
                    grafico.getViewport().setMinX(minTime_gasto.getTime());
                }
                if (maxTime_rec != null) {
                    if (maxTime_gasto.getTime() >= (maxTime_rec.getTime() - 8.64e+7)){
                        maxTime_gasto.setTime((long) (maxTime_gasto.getTime() + 8.64e+7));
                        grafico.getViewport().setMaxX(maxTime_gasto.getTime());
                    }
                }else{
                    maxTime_gasto.setTime((long) (maxTime_gasto.getTime() + 8.64e+7));
                    grafico.getViewport().setMaxX(maxTime_gasto.getTime());
                }
                grafico.getViewport().setXAxisBoundsManual(true);
            }

            if (minValue_gasto != -1.0 && maxValue_gasto != -1.0){
                minValue_gasto = 0.9 * minValue_gasto;
                maxValue_gasto = 1.1 * maxValue_gasto;

                if (minValue_rec != -1.0 && minValue_gasto < minValue_rec) {
                    grafico.getViewport().setMinY(minValue_gasto);
                }else if (minValue_rec == -1.0){
                    grafico.getViewport().setMinY(minValue_gasto);
                }

                if (maxValue_rec != -1.0 && maxValue_gasto > maxValue_rec) {
                    grafico.getViewport().setMaxY(maxValue_gasto);
                }else if (maxValue_rec == -1.0){
                    grafico.getViewport().setMaxY(maxValue_gasto);
                }

                grafico.getViewport().setYAxisBoundsManual(true);

            }


            if (pontos_gastos.size()== 1)
                grafico.addSeries(series_gasto_2);
            else if (pontos_gastos.size() > 1)
                grafico.addSeries(series_gasto);

            grafico.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            grafico.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of
            grafico.getLegendRenderer().setVisible(true);
            grafico.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        }
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
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
