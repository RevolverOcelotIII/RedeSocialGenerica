package com.trab.myapplication.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post implements Serializable {
    public int id;
    public String descricao;
    public Date datapublicacao;
    public int userid;
    public byte[] imagesource;
}
