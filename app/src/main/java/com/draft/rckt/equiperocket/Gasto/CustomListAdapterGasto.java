package com.draft.rckt.equiperocket.Gasto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.draft.rckt.equiperocket.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanlucas on 12/06/16.
 */

public class CustomListAdapterGasto extends RecyclerView.Adapter<CustomListAdapterGasto.MyViewHolder> {

    private ArrayList<Gasto> arrayGasto;
    private LayoutInflater layoutInflater;
    private List<Gasto> gastoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, value, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titulo_id);
            value = (TextView) view.findViewById(R.id.valor_id);
            date = (TextView) view.findViewById(R.id.data_id);
        }
    }


    public CustomListAdapterGasto(List<Gasto> gastoList) {
        this.gastoList = gastoList;
    }

    /**TODO
     * Escolher um nome melhor para o layout
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    /**TODO
     * Arrumar holder.date.setText para mostrar a data. Antes utilizar o formatter comentado
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        //Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Gasto gasto = gastoList.get(position);
        holder.title.setText(gasto.getTitulo());
        holder.value.setText(String.valueOf(gasto.getValor()));
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formatoData.format(gasto.getData());
        holder.date.setText(data);
    }

    @Override
    public int getItemCount() {
        return gastoList.size();
    }
}