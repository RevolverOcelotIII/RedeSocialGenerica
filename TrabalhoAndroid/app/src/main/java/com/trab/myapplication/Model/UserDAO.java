package com.trab.myapplication.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;

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
        values.put("profilesource",usuario.imagesource);
        write.insert(ConnectionFactory.TABELA_USER,null,values);
        return true;
    }
    public User getUserFromDB(int userid){
        User user = new User();
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_USER+" WHERE id == "+userid+";",null);
        user.nome = cursor.getString(cursor.getColumnIndex("nome"));
        user.email = cursor.getString(cursor.getColumnIndex("email"));
        user.senha = cursor.getString(cursor.getColumnIndex("senha"));
        user.telefone = cursor.getString(cursor.getColumnIndex("numero"));
        user.imagesource = cursor.getBlob(cursor.getColumnIndex("profilesource"));
        user.id = cursor.getInt(cursor.getColumnIndex("id"));
        return user;
    }
    public ArrayList<User> getAllUsersExceptLogged(int loggeduser){
        ArrayList<User>list = new ArrayList<User>();
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_USER+" WHERE id!="+loggeduser+";",null);
        while (cursor.moveToNext()){
            User user = new User();
            user.nome = cursor.getString(cursor.getColumnIndex("nome"));
            user.email = cursor.getString(cursor.getColumnIndex("email"));
            user.senha = cursor.getString(cursor.getColumnIndex("senha"));
            user.telefone = cursor.getString(cursor.getColumnIndex("numero"));
            user.imagesource = cursor.getBlob(cursor.getColumnIndex("profilesource"));
            user.id = cursor.getInt(cursor.getColumnIndex("id"));
            list.add(user);
        }
        return list;
    }
    public boolean editUser(User usuario){
        write.execSQL("INSERT INTO "+ConnectionFactory.TABELA_USER+" VALUES('"+usuario.nome+"','"+usuario.email+"','"+usuario.senha+"','"+usuario.telefone+"','"+usuario.imagesource+"') WHERE ID=="+usuario.id+"");
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
    public boolean searchUserByEmailAndPassword(String email,String password){
        Cursor cursor = read.rawQuery("SELECT EMAIL FROM "+ConnectionFactory.TABELA_USER+" WHERE EMAIL=='"+email+"' AND SENHA=='"+password+"';",null);
        if(cursor==null){
            return false;
        }else {
            return true;
        }
    }
}
