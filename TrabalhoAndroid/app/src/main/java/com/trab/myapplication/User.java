package com.trab.myapplication;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String nome;
    public String email;
    public String senha;
    public String telefone;
    public String imagesource;

    public User( String nome, String email, String senha, String telefone,String imagesource) { ;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.imagesource = imagesource;
    }

}
