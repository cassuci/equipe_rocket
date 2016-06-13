package com.draft.rckt.equiperocket.Receita;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivanlucas on 12/06/16.
 */
public class Receita implements Serializable {

    public Receita() {}

    public String user_id;
    public String receita_id;
    public String titulo;
    public String tipo;
    public String desc;
    public float valor;
    public Date data;

}
