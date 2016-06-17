package com.draft.rckt.equiperocket.Gasto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gabriel on 6/13/2016.
 */
public class Gasto implements Serializable{

    private String user_id;
    private int gasto_id;
    private String titulo;
    private String tipo;
    private String descr;
    private double valor;
    private Date data;

    public Gasto(){

    }

    public Gasto(String user_id, int gasto_id, String titulo, String tipo, String descr, float valor, Date data) {
        this.user_id = user_id;
        this.gasto_id = gasto_id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.descr = descr;
        this.valor = valor;
        this.data = data;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getGasto_id() {
        return gasto_id;
    }

    public void setGasto_id(int gasto_id) {
        this.gasto_id = gasto_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String desc) {
        this.descr = desc;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
