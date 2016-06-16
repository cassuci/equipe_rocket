package com.draft.rckt.equiperocket.Receita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.draft.rckt.equiperocket.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by ivanlucas on 12/06/16.
 */

public class CustomListAdapterReceita extends BaseAdapter {

    private ArrayList<Receita> arrayReceita;
    private LayoutInflater layoutInflater;

    public CustomListAdapterReceita(Context aContext, ArrayList<Receita> arrayReceita) {
        this.arrayReceita = arrayReceita;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyView view;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_row_layout, null);
            view = new MyView();
            view.titulo = (TextView) convertView.findViewById(R.id.titulo_id);
            view.valor = (TextView) convertView.findViewById(R.id.valor_id);
            view.data = (TextView) convertView.findViewById(R.id.data_id);
            convertView.setTag(view);
        } else {
            view = (MyView) convertView.getTag();
        }

        Receita rec = arrayReceita.get(position);
        view.titulo.setText(rec.titulo);
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formatoData.format(rec.data);
        view.data.setText(data);
        view.valor.setText(String.valueOf(rec.valor));
        return convertView;
    }

    static class MyView {
        TextView titulo;
        TextView valor;
        TextView data;
    }


    @Override
    public int getCount() {
        return arrayReceita.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayReceita.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
