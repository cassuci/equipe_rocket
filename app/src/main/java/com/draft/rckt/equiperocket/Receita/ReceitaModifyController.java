package com.draft.rckt.equiperocket.Receita;

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

public class ReceitaModifyController extends AppCompatActivity implements OnItemSelectedListener {

    private Receita receita;
    private Receita oldReceita;
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
        setContentView(R.layout.activity_receita_modify_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_receita_mod);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbControl = new DatabaseController(getApplication());

        setReceita();

        initView();

        fillViewWithData();

    }

    private void fillViewWithData()
    {
//        DecimalFormat formatoValor = new DecimalFormat("###,###,##0.00"); // aqui criamos um
//        inputValor.setText(String.valueOf(formatoValor.format(gasto.getValor())));
        inputValor.setText(String.valueOf(receita.getValor()));
        inputName.setText(receita.getTitulo());
        inputDesc.setText(receita.getDesc());
        System.out.println(inputValor);
        int i, index = -1;
        // Inicialmente o i era testado até i < 5. Como estava dando erro, passou a ser i < 4.
        for (i = 0; index == -1 && i < 4; i++ ) {
            if (spinner.getItemAtPosition(i).toString().equals(receita.getTipo().toString())) {
                index = i;
                tipo = receita.getTipo().toString();
            }
        }
        spinner.setSelection(index);
        btnModify.setEnabled(false);
    }

    private void initView() {
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name_rec);
        inputLayoutValor = (TextInputLayout) findViewById(R.id.input_layout_valor_rec);
        inputName = (EditText) findViewById(R.id.input_name_rec);
        inputValor = (EditText) findViewById(R.id.input_valor_rec);
        inputDesc = (EditText) findViewById(R.id.input_desc_rec);
        btnModify = (Button) findViewById(R.id.btn_rec_mod);
        spinner = (Spinner) findViewById(R.id.spinner_tipo_rec);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_receita_array, android.R.layout.simple_spinner_item);
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

        receita.setTitulo(inputName.getText().toString());
        receita.setDesc(inputDesc.getText().toString());
        receita.setTipo(tipo);
        receita.setValor(Double.parseDouble(inputValor.getText().toString()));
        receita.setData(new Date());

        if (dbControl.updateReceita(receita)) {
            Toast.makeText(getApplicationContext(), "Receita modificada com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(getApplicationContext(), "Falha na modificação do receita", Toast.LENGTH_LONG).show();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name_rec));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateValor() {
        if (inputValor.getText().toString().trim().isEmpty()) {
            inputLayoutValor.setError(getString(R.string.err_msg_valor_rec));
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
        if (inputName.getText().toString().equals(receita.getTitulo().toString()) &&
                inputDesc.getText().toString().equals(receita.getDesc().toString()) &&
                tipo.equals(receita.getTipo().toString()) &&
                Double.parseDouble(inputValor.getText().toString()) ==  receita.getValor())
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

    private void setReceita(){
        receita = (Receita) getIntent().getExtras().getSerializable("receita");
        oldReceita = receita;
    }

}
