package com.draft.rckt.equiperocket.Usuario;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gabriel on 6/13/2016.
 */
public class Usuario implements Serializable{

    private String user_id;
    private String nome;

    private static Usuario instance;

    public Usuario() {
    }

    public Usuario(String user_id, String nome) {
        this.user_id = user_id;
        this.nome = nome;
    }

    public static Usuario getInstance () {
        if (Usuario.instance == null) {
            Usuario.instance = new Usuario();
        }
        return Usuario.instance;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getNome() {
        return nome;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

}
