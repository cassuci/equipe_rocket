package com.draft.rckt.equiperocket.Receita;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivanlucas on 12/06/16.
 */
public class Receita implements Serializable {

    private String user_id;
    private int receita_id;
    private String titulo;
    private String tipo;
    private String desc;
    private double valor;
    private Date data;

    public Receita(){

    }

    public Receita(String user_id, int receita_id, String titulo, String tipo, String desc, float valor, Date data) {
        this.user_id = user_id;
        this.receita_id = receita_id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.desc = desc;
        this.valor = valor;
        this.data = data;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getReceita_id() {
        return receita_id;
    }

    public void setReceita_id(int receita_id) {
        this.receita_id = receita_id;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
