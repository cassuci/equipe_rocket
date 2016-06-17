package com.draft.rckt.equiperocket.Gasto;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.draft.rckt.equiperocket.R;

public class GastoInsertController extends AppCompatActivity implements OnItemSelectedListener  {

    private Toolbar toolbar;
    private EditText inputName, inputValor, inputDesc;
    private TextInputLayout inputLayoutName, inputLayoutValor;
    private Button btnInsert;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_insert_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gasto_insert);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name_gasto);
        inputLayoutValor = (TextInputLayout) findViewById(R.id.input_layout_valor_gasto);
        inputName = (EditText) findViewById(R.id.input_name_gasto);
        inputValor = (EditText) findViewById(R.id.input_valor_gasto);
        inputDesc = (EditText) findViewById(R.id.input_desc_gasto);
        btnInsert = (Button) findViewById(R.id.btn_gasto_insert);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_tipo_gasto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_gasto_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputValor.addTextChangedListener(new MyTextWatcher(inputValor));

        btnInsert.setOnClickListener(new View.OnClickListener() {
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

        double valor = Double.parseDouble(inputValor.getText().toString());
        String str = "nome = " + inputName.getText().toString() + "\nvalor = " + valor + "\ntipo = " + tipo + "\ndesc = " + inputDesc.getText().toString();
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();


        // TODO
        // inserir nome, valor, tipo e descrição,  VERIFICAR TAMANHOS
        // if (inserção ok) -> toast
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
        }
    }
}
