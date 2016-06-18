package com.draft.rckt.equiperocket.Grafico;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.R;
import com.draft.rckt.equiperocket.Receita.ReceitaController;
import com.draft.rckt.equiperocket.Relatorio.RelatorioController;
import com.draft.rckt.equiperocket.Usuario.Usuario;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GraficoController extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {


    boolean include_receitas;
    boolean include_gastos;

    ExpListViewAdapterWithCheckbox listAdapter_receitas;
    ExpListViewAdapterWithCheckbox listAdapter_gastos;
    ExpandableListView expListView_receitas;
    ExpandableListView expListView_gastos;
    ArrayList<String> listDataHeader_receitas;
    HashMap<String, List<String>> listDataChild_receitas;
    ArrayList<String> listDataHeader_gastos;
    HashMap<String, List<String>> listDataChild_gastos;


    private TextView startDateText;
    private TextView endDateText;

    private static Calendar startCal = Calendar.getInstance();
    private static Calendar endCal = Calendar.getInstance();

    private Button button_endDate;
    private Button button_startDate;
    private Button button_createGraph;

    private static int dateSelected = -1;
    private boolean startDateSet = false;
    private boolean endDateSet = false;

    private final int START_DATE = 0;
    private final int END_DATE = 1;

    private static int startYear;
    private static int startMonth;
    private static int startDay;
    private static int endYear;
    private static int endMonth;
    private static int endDay;


    String[] filtros_receitas = new String[]{"Rendimentos", "Salário", "Bônus", "Outros"};
    String[] filtros_gastos = new String[] {"Alimentação", "Transporte", "Contas", "Lazer", "Outros"};



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
        Usuario user = Usuario.getInstance();
        nav_user.setText(user.getNome());

        TextView nav_saldo = (TextView)hView.findViewById(R.id.navHeaderSaldo);
        DecimalFormat formatoValor = new DecimalFormat("###,###,##0.00");
        nav_saldo.setText("R$ " + formatoValor.format(user.getSaldo()));

        // get the listview
        expListView_receitas = (ExpandableListView) findViewById(R.id.expandableList_grafico_receita_filtro);
        expListView_gastos = (ExpandableListView) findViewById(R.id.expandableList_grafico_gasto_filtro);

        // preparing list data
        prepareListData();

        listAdapter_receitas = new ExpListViewAdapterWithCheckbox(this, listDataHeader_receitas, listDataChild_receitas);
        listAdapter_gastos = new ExpListViewAdapterWithCheckbox(this, listDataHeader_gastos, listDataChild_gastos);

        expListView_receitas.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        expListView_gastos.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        // setting list adapter
        expListView_receitas.setAdapter(listAdapter_receitas);
        setListViewHeight(expListView_receitas);
        expListView_gastos.setAdapter(listAdapter_gastos);
        setListViewHeight(expListView_gastos);


        // configurando tratadores dos switches
        Switch switch_receitas = (Switch) findViewById(R.id.switch_grafico_receita_enabled);
        assert switch_receitas != null;
        switch_receitas.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        include_receitas = isChecked;
                    }
                });
        Switch switch_gastos = (Switch) findViewById(R.id.switch_grafico_gasto_enabled);
        assert switch_gastos != null;
        switch_gastos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                include_gastos = isChecked;
            }
        });

        // configurando tratadores dos botoes
        button_createGraph = (Button) findViewById(R.id.button_grafico_createGraph);
        assert button_createGraph != null;
        button_createGraph.setOnClickListener(this);

        button_startDate = (Button) findViewById(R.id.button_grafico_startDate);
        button_endDate = (Button) findViewById(R.id.button_grafico_endDate);

        button_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate(v);
            }
        });
        button_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate(v);
            }
        });

        //instanciando textFields das datas
        startDateText = (TextView) findViewById(R.id.textView_grafico_startDate);
        endDateText = (TextView) findViewById(R.id.textView_grafico_endDate);

    }

    private void prepareListData() {
        listDataHeader_receitas = new ArrayList<String>();
        listDataChild_receitas = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader_receitas.add("Filtro de busca (opcional)");

        // Adding child data
        List<String> filtros_receita = new ArrayList<String>();

        for (int i = 0; i < this.filtros_receitas.length; i++){
            filtros_receita.add(filtros_receitas[i]);
        }

        listDataHeader_gastos = new ArrayList<String>();
        listDataChild_gastos = new HashMap<String, List<String>>();

        listDataHeader_gastos.add("Filtro de busca (opcional)");

        List<String> filtros_gasto = new ArrayList<String>();
        for (int i = 0; i < this.filtros_gastos.length; i++){
            filtros_gasto.add(filtros_gastos[i]);
        }

        listDataChild_receitas.put(listDataHeader_receitas.get(0), filtros_receita); // Header, Child data
        listDataChild_gastos.put(listDataHeader_gastos.get(0), filtros_gasto); // Header, Child data
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_grafico_createGraph){
            startGraphCreateActivity();
        }
    }

    private void startGraphCreateActivity() {


        HashMap<String, Boolean> filtro_receitas = new HashMap<String, Boolean>();
        HashMap<String, Boolean> filtro_gastos = new HashMap<String, Boolean>();

        boolean[] filtro_receitas_itens_checked = listAdapter_receitas.getChildCheckStates(0);
        boolean[] filtro_gastos_itens_checked = listAdapter_gastos.getChildCheckStates(0);

        if (filtro_receitas_itens_checked != null) {
            for (int i = 0; i < this.filtros_receitas.length; i++) {
                filtro_receitas.put(this.filtros_receitas[i], filtro_receitas_itens_checked[i]);
            }
        }else {
            for (int i = 0; i < this.filtros_receitas.length; i++) {
                filtro_receitas.put(this.filtros_receitas[i], false);
            }
        }
        if (filtro_gastos_itens_checked != null) {
            for (int i = 0; i < this.filtros_gastos.length; i++) {
                filtro_gastos.put(this.filtros_gastos[i], filtro_gastos_itens_checked[i]);
            }
        }else{
            for (int i = 0; i < this.filtros_gastos.length; i++) {
                filtro_gastos.put(this.filtros_gastos[i], false);
            }
        }

        Intent intent = new Intent(GraficoController.this, GraficoViewController.class);
        intent.putExtra("receitas_include", include_receitas);
        intent.putExtra("gastos_include", include_gastos);
        intent.putExtra("receitas_filtros", filtro_receitas);
        intent.putExtra("gastos_filtros", filtro_gastos);
        intent.putExtra("startDateSet", startDateSet);
        intent.putExtra("startDate", startCal);
        intent.putExtra("endDateSet", endDateSet);
        intent.putExtra("endDate", endCal);

        startActivity(intent);
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
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpListViewAdapterWithCheckbox listAdapter = (ExpListViewAdapterWithCheckbox) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void startDate(View view)
    {
        dateSelected = START_DATE;
        initDateData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(startYear, startMonth, startDay);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),   // 0 a 11  !!!!
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setOnCancelListener((DialogInterface.OnCancelListener) this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    public void endDate(View view)
    {
        dateSelected = END_DATE;
        initDateData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(endYear, endMonth, endDay);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),   // 0 a 11  !!!!
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setOnCancelListener((DialogInterface.OnCancelListener) this);
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
            String d = startCal.get(startCal.DAY_OF_MONTH)+"/"+(startCal.get(startCal.MONTH)+1)+"/"+startCal.get(startCal.YEAR);
            startDateText.setText(d);
            startDateSet = true;
        }
        if(dateSelected == END_DATE)
        {
            endCal.set(year, monthOfYear, dayOfMonth);
            endYear = year;
            endMonth = monthOfYear + 1;
            endDay = dayOfMonth;
            String d = endCal.get(endCal.DAY_OF_MONTH)+"/"+(endCal.get(endCal.MONTH)+1)+"/"+endCal.get(endCal.YEAR);
            endDateText.setText(d);
            endDateSet = true;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        /*startYear = startMonth = startDay = endYear = endMonth = endDay = 0;
        startDateText.setText("Não definida");
        endDateText.setText("Não definida");
        dateSelected = -1;*/
    }

    public static Calendar getStartCal() { return startCal; }
    public static Calendar getEndCal() { return endCal; }

}