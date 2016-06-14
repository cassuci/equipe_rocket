package com.draft.rckt.equiperocket.Gasto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.draft.rckt.equiperocket.R;

import java.util.ArrayList;

/**
 * Created by ivanlucas on 12/06/16.
 */

public class CustomListAdapter1 extends BaseAdapter {

    private ArrayList<Gasto> arrayGasto;
    private LayoutInflater layoutInflater;

    public CustomListAdapter1(Context aContext, ArrayList<Gasto> arrayGasto) {
        this.arrayGasto = arrayGasto;
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

        Gasto rec = arrayGasto.get(position);
        view.titulo.setText(rec.titulo);
        view.data.setText("aaaaaa");
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
        return arrayGasto.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayGasto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
