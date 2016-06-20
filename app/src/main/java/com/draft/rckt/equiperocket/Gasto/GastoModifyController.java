package com.draft.rckt.equiperocket.Gasto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Database.DatabaseController;
import com.draft.rckt.equiperocket.R;

import java.text.DecimalFormat;
import java.util.Date;

public class GastoModifyController extends AppCompatActivity implements OnItemSelectedListener {

    private Gasto gasto;
    private Gasto oldGasto;
    private Toolbar toolbar;
    private EditText inputName, inputValor, inputDesc;
    private TextInputLayout inputLayoutName, inputLayoutValor;
    private Button btnModify;
    private String tipo = " ";
    private DatabaseController dbControl;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_modify_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gasto_mod);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbControl = new DatabaseController(getApplication());

        setGasto();

        initView();

        fillViewWithData();

    }

    private void fillViewWithData()
    {
//        DecimalFormat formatoValor = new DecimalFormat("###,###,##0.00"); // aqui criamos um
//        inputValor.setText(String.valueOf(formatoValor.format(gasto.getValor())));
        inputValor.setText(String.valueOf(gasto.getValor()));
        inputName.setText(gasto.getTitulo());
        inputDesc.setText(gasto.getDescr());
        int i, index = -1;
        for (i = 0; index == -1; i++ ) {
            if (spinner.getItemAtPosition(i).toString().equals(gasto.getTipo().toString())) {
                index = i;
                tipo = gasto.getTipo().toString();
            }
        }
        spinner.setSelection(index);
        btnModify.setEnabled(false);
    }

    private void initView() {
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name_gasto);
        inputLayoutValor = (TextInputLayout) findViewById(R.id.input_layout_valor_gasto);
        inputName = (EditText) findViewById(R.id.input_name_gasto);
        inputValor = (EditText) findViewById(R.id.input_valor_gasto);
        inputDesc = (EditText) findViewById(R.id.input_desc_gasto);
        btnModify = (Button) findViewById(R.id.btn_gasto_mod);
        spinner = (Spinner) findViewById(R.id.spinner_tipo_gasto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_gasto_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputValor.addTextChangedListener(new MyTextWatcher(inputValor));
        inputDesc.addTextChangedListener(new MyTextWatcher(inputDesc));

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }


    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateValor()) {
            return;
        }

        gasto.setTitulo(inputName.getText().toString());
        gasto.setDescr(inputDesc.getText().toString());
        gasto.setTipo(tipo);
        gasto.setValor(Double.parseDouble(inputValor.getText().toString()));
        gasto.setData(new Date());

        if (dbControl.updateGasto(gasto)) {
            Toast.makeText(getApplicationContext(), "Gasto modificado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(getApplicationContext(), "Falha na modificação do gasto", Toast.LENGTH_LONG).show();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name_gasto));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateValor() {
        if (inputValor.getText().toString().trim().isEmpty()) {
            inputLayoutValor.setError(getString(R.string.err_msg_valor_gasto));
            requestFocus(inputValor);
            return false;
        } else {
            inputLayoutValor.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tipo = parent.getItemAtPosition(position).toString();
        enableButtonModify();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name_rec:
                    validateName();
                    break;
                case R.id.input_valor_rec:
                    validateValor();
                    break;
            }

            enableButtonModify();
        }
    }

    private void enableButtonModify()
    {
        if (inputName.getText().toString().equals(gasto.getTitulo().toString()) &&
                inputDesc.getText().toString().equals(gasto.getDescr().toString()) &&
                tipo.equals(gasto.getTipo().toString()) &&
                Double.parseDouble(inputValor.getText().toString()) ==  gasto.getValor())
            btnModify.setEnabled(false);
        else
            btnModify.setEnabled(true);
    }

    /**
     * Caso o botao back tenha sido pressionado abre a view anterior
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    private void setGasto(){
        gasto = (Gasto) getIntent().getExtras().getSerializable("gasto");
        oldGasto = gasto;
    }

}
