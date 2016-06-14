package com.draft.rckt.equiperocket.Login;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.draft.rckt.equiperocket.Database.DatabaseContract;
import com.draft.rckt.equiperocket.Database.DatabaseContract.UserEntry;
import com.draft.rckt.equiperocket.Database.DatabaseHelper;
import com.draft.rckt.equiperocket.R;

public class Login extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mDbHelper = new DatabaseHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

        // TODO: Abaixo é um exemplo de insercao e busca na tabela de usuario
        // TODO: remover o exemplo e implementar de fato a atividade de login
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME_USER_ID, "cassuci");
        values.put(UserEntry.COLUMN_NAME_PASSWORD, "cassuci");
        values.put(UserEntry.COLUMN_NAME_NAME, "Gabriel");



        long row_id = db.insert(UserEntry.TABLE_NAME, null, values);

        Toast toast = Toast.makeText(getApplicationContext(), Long.toString(row_id), Toast.LENGTH_SHORT);
        toast.show();



        String [] columns = {UserEntry.COLUMN_NAME_NAME, UserEntry.COLUMN_NAME_PASSWORD, UserEntry.COLUMN_NAME_USER_ID};
        Cursor cursor = db.query(UserEntry.TABLE_NAME, columns, UserEntry.COLUMN_NAME_USER_ID + " = \"cassuci\"", null, null, null, null);

        String username = null;
        String password = null;
        String name = null;
        if (cursor != null){
            while(cursor.moveToNext()){
                username = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_ID));
                password = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_PASSWORD));;
                name = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_NAME));;
            }
        }

        AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("USERNAME " + username + '\n'
        + "NAME " + name + '\n'
        + "PASSWORD " + password + '\n');
        alertDialog.show();
    }
}
