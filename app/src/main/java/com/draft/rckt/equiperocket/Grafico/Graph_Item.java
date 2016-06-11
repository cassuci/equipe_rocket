package com.draft.rckt.equiperocket.Grafico;

import java.sql.Date;

/**
 * Created by Gabriel on 6/11/2016.
 */
public class Graph_Item {

    private float value;
    private Date date;

    public Graph_Item(String value, String date) {
        setValue(value);
        setDate(date);
    }

    public void setValue(String value) {
        this.value = Float.parseFloat(value);
    }

    public void setDate(String date) {
        this.date = new Date(Long.parseLong(date));
    }
}
