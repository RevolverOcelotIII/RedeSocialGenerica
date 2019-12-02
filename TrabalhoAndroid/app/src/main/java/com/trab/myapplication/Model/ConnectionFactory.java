package com.trab.myapplication.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class ConnectionFactory extends SQLiteOpenHelper {

    public static int VERSION = 3;
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
                    "    profilesource blob);\n");
            db.execSQL(
                    "create table if not exists "+TABELA_POST+"(\n" +
                            "\tid integer primary key autoincrement,\n" +
                            "    descricao varchar (500),\n" +
                            "    imagesource blob,\n" +
                            "    userid int,\n" +
                            "    datapublicacao varchar(10),\n" +
                            "    foreign key (userid) references "+TABELA_USER+"(id));");
            Log.w("Teste","Todas as tabelas foram criadas com sucesso");
        }catch(Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{

            db.execSQL("create table if not exists "+TABELA_USER+"(\n" +
                    "\tid integer primary key autoincrement,\n" +
                    "    nome varchar(100),\n" +
                    "    email varchar(100),\n" +
                    "    senha varchar(100),\n" +
                    "    numero varchar(12),\n" +
                    "    profilesource blob);\n");
            db.execSQL(
                    "create table if not exists "+TABELA_POST+"(\n" +
                            "\tid integer primary key autoincrement,\n" +
                            "    descricao varchar (500),\n" +
                            "    imagesource blob,\n" +
                            "    userid int,\n" +
                            "    datapublicacao varchar(10),\n" +
                            "    foreign key (userid) references "+TABELA_USER+"(id));");
            Log.w("Teste","Todas as tabelas foram criadas com sucesso");
        }catch(Exception e){

        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //db.execSQL("delete from " + TABELA_POST + ";");
        //db.execSQL("delete from " + TABELA_USER + ";");
        //db.execSQL("drop table if exists "+TABELA_USER+";");
        //db.execSQL("drop table if exists "+TABELA_POST+";");
    }
}
