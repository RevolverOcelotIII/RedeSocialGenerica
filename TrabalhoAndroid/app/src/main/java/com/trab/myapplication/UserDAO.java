package com.trab.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public UserDAO(Context contexto) {
        ConnectionFactory connectionFactory = new ConnectionFactory(contexto);
        write = connectionFactory.getWritableDatabase();
        read = connectionFactory.getReadableDatabase();
    }

    public boolean saveUser(User usuario){
        ContentValues values = new ContentValues();
        values.put("nome",usuario.nome);
        values.put("email",usuario.email);
        values.put("senha",usuario.senha);
        values.put("nome",usuario.nome);
        values.put("numero",usuario.telefone);
        write.insert(ConnectionFactory.TABELA_USER,null,values);
        return true;
    }
    public boolean listUser(){
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_USER+";",null);
        return true;
    }
    public boolean editUser(User usuario){
        write.execSQL("INSERT INTO "+ConnectionFactory.TABELA_USER+" VALUES('"+usuario.nome+"','"+usuario.email+"','"+usuario.senha+"','"+usuario.telefone+"','"+usuario.imagesource+"') WHERE ID="+usuario.id+"");
        return true;
    }
    public boolean searchUserByEmail(String email){
        Cursor cursor = read.rawQuery("SELECT EMAIL FROM "+ConnectionFactory.TABELA_USER+" WHERE EMAIL='"+email+"';",null);
        if(cursor==null){
            return false;
        }else {
            return true;
        }
    }
}
