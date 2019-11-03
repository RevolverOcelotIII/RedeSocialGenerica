package com.trab.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class ConnectionFactory extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NAME = "redesocialgenerica";
    public static String TABELA_USER = "usuario";
    public static String TABELA_POST = "post";
    public ConnectionFactory(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("create table if not exists "+TABELA_USER+"(\n" +
                    "\tid integer primary key autoincrement,\n" +
                    "    nome varchar(100),\n" +
                    "    email varchar(100),\n" +
                    "    senha varchar(100),\n" +
                    "    numero varchar(10),\n" +
                    "    profilesource varchar(100));\n");
            db.execSQL(
                    "create table if not exists "+TABELA_POST+"(\n" +
                            "\tid integer primary key autoincrement,\n" +
                            "    descricao varchar (500),\n" +
                            "    imagesource varchar (200),\n" +
                            "    userid int,\n" +
                            "    datapublicacao datetime,\n" +
                            "    foreign key (userid) references user(id));");
        }catch(Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
