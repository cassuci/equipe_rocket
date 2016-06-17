package com.draft.rckt.equiperocket.Receita;

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

public class CustomListAdapterReceita extends RecyclerView.Adapter<CustomListAdapterReceita.MyViewHolder> {

        private ArrayList<Receita> arrayReceita;
        private LayoutInflater layoutInflater;
        private List<Receita> receitaList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, value, date;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.titulo_id);
                value = (TextView) view.findViewById(R.id.valor_id);
                date = (TextView) view.findViewById(R.id.data_id);
            }
        }


        public CustomListAdapterReceita(List<Receita> receitaList) {
            this.receitaList = receitaList;
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
            Receita rec = receitaList.get(position);
            holder.title.setText(rec.getTitulo());
            holder.value.setText(String.valueOf(rec.getValor()));
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
            String data = formatoData.format(rec.getData());
            holder.date.setText(data);
        }

        @Override
        public int getItemCount() {
        return receitaList.size();
    }
}

