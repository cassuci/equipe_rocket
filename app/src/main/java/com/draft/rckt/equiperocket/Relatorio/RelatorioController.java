package com.draft.rckt.equiperocket.Relatorio;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.Grafico.GraficoController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.ReceitaController;
import com.draft.rckt.equiperocket.Usuario.Usuario;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.util.Calendar;

public class RelatorioController extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    private Button buttonStartDate, buttonEndDate;
    private TextView textStart, textEnd;
    private static int dateSelected = -1, gastosSelected = 0, receitasSelected = 0;
    private final int START_DATE = 0;
    private final int END_DATE = 1;
    private Switch swReceitas, swGastos;
    private static Calendar startCal = Calendar.getInstance();
    private static Calendar endCal = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_relatorio);
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

        TextView nav_saldo = (TextView)hView.findViewById(R.id.navHeaderSaldo);
        DecimalFormat formatoValor = new DecimalFormat("###,###,##0.00");
        nav_saldo.setText("R$ " + formatoValor.format(user.getSaldo()));

        buttonStartDate = (Button) findViewById(R.id.button_start_date);
        buttonEndDate = (Button) findViewById(R.id.button_end_date);
        textStart = (TextView) findViewById(R.id.startDateDisplay);
        textEnd = (TextView) findViewById(R.id.endDateDisplay);

        buttonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate(v);
            }
        });

        buttonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate(v);
            }
        });

        swGastos = (Switch) findViewById(R.id.switch_relatorio_gasto_enabled);
        swReceitas = (Switch) findViewById(R.id.switch_relatorio_receita_enabled);

        swGastos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    gastosSelected = 1;
                else
                    gastosSelected = 0;
            }
        });

        swReceitas.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    receitasSelected = 1;
                else
                    receitasSelected = 0;
            }
        });

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

    @SuppressWarnings("StatementWithEmptyBody")
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
            // do nothing
        } else if (id == R.id.grafico) {
            intent.setClass(this, GraficoController.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static int startYear, startMonth, startDay, endYear, endMonth, endDay;

    public void startDate(View view)
    {
        dateSelected = START_DATE;
        initDateData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(startYear, startMonth, startDay);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),   // 0 a 11  !!!!
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    public void endDate(View view)
    {
        dateSelected = END_DATE;
        initDateData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(endYear, endMonth, endDay);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),   // 0 a 11  !!!!
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");    }

    private void initDateData()
    {
            Calendar c = Calendar.getInstance();
            startYear = endYear = c.get(Calendar.YEAR);
            startMonth = endMonth = c.get(Calendar.MONTH);
            startDay = endDay = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(dateSelected == START_DATE)
        {
            startCal.set(year, monthOfYear, dayOfMonth);
            startYear = year;
            startMonth = monthOfYear + 1;
            startDay = dayOfMonth;
            String d = formatDate(startCal);
            textStart.setText(d);
        }
        if(dateSelected == END_DATE)
        {
            endCal.set(year, monthOfYear, dayOfMonth);
            endYear = year;
            endMonth = monthOfYear + 1;
            endDay = dayOfMonth;
            String d = formatDate(endCal);
            textEnd.setText(d);
        }

        if(startCal.compareTo(endCal) > 0) {
            Calendar cAux = startCal;
            startCal = endCal;
            endCal = cAux;

            textStart.setText(formatDate(startCal));
            textEnd.setText(formatDate(endCal));
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        startYear = startMonth = startDay = endYear = endMonth = endDay = 0;
        textStart.setText("Não definida");
        textEnd.setText("Não definida");
        dateSelected = -1;
    }

    public void gerarRelatorio(View view)
    {
        if (gastosSelected == 0 && receitasSelected == 0) {
            Context context = getApplicationContext();
            Toast.makeText(context, "Escolha o tipo de relatório.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, RelatorioDetailController.class);
        startActivity(intent);
    }

    public String formatDate(Calendar c)
    {
        return String.format("%02d", c.get(c.DAY_OF_MONTH)) + "/" + String.format("%02d", c.get(c.MONTH)+1) +
                "/" + String.format("%02d", c.get(c.YEAR));
    }

    public static int getStartYear() { return startYear; }
    public static int getEndYear() { return endYear; }
    public static Calendar getStartCal() { return startCal; }
    public static Calendar getEndCal() { return endCal; }
    public static int getGastosSelected() {return gastosSelected; }
    public static int getReceitasSelected() {return receitasSelected; }

}
