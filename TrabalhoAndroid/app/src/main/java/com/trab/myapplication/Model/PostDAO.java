package com.trab.myapplication.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostDAO {
    private SQLiteDatabase write;
    private SQLiteDatabase read;
    public PostDAO(Context contexto) {
        ConnectionFactory connectionFactory = new ConnectionFactory(contexto);
        write = connectionFactory.getWritableDatabase();
        read = connectionFactory.getReadableDatabase();
    }
    public boolean savePost(Post post){
        ContentValues values = new ContentValues();
        values.put("descricao",post.descricao);
        values.put("userid",post.userid);
        if(post.imagesource!=null){
        values.put("imagesource",post.imagesource);
        }
        values.put("datapublicacao",post.datapublicacao.toString());
        write.insert(ConnectionFactory.TABELA_POST,null,values);
        return true;
    }

    public void deleteAllPosts ()
    {
        write.delete(ConnectionFactory.TABELA_POST, "1=1", null);
    }
    public boolean haveImage(int postid){
        Post post = new Post();
        Cursor cursor = read.rawQuery("SELECT imagesource FROM "+ConnectionFactory.TABELA_POST+" WHERE id = "+postid+";",null);
        cursor.moveToNext();
        if (cursor.isNull(cursor.getColumnIndex("imagesource"))){
            return false;
        }else return true;
    }
    public Post getPostFromDB(int postid){
        Post post = new Post();
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_POST+" WHERE id = "+postid+";",null);
        cursor.moveToNext();
        post.descricao = cursor.getString(cursor.getColumnIndex("descricao"));
        post.imagesource = cursor.getBlob(cursor.getColumnIndex("imagesource"));
        post.datapublicacao = cursor.getString(cursor.getColumnIndex("datapublicacao"));
        post.id = cursor.getInt(cursor.getColumnIndex("id"));
        post.userid = cursor.getInt(cursor.getColumnIndex("userid"));
        return post;
    }
    public ArrayList<Post> getAllPosts(){
        ArrayList<Post>list = new ArrayList<>();
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_POST+";",null);
        while (cursor.moveToNext()){
            Post post = new Post();
            post.descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            post.imagesource = cursor.getBlob(cursor.getColumnIndex("imagesource"));
            post.datapublicacao = cursor.getString(cursor.getColumnIndex("datapublicacao"));
            post.id = cursor.getInt(cursor.getColumnIndex("id"));
            post.userid = cursor.getInt(cursor.getColumnIndex("userid"));
            list.add(post);
            Log.w("Teste","subindo tela "+post.id);
        }
        Log.w("Teste","voltando tela tela ");
        return list;
    }
    public ArrayList<Post> getUserPosts(int userid){
        ArrayList<Post>list = new ArrayList<>();
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_POST+" WHERE USERID="+userid+";",null);
        while (cursor.moveToNext()){
            Post post = new Post();
            post.descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            post.imagesource = cursor.getBlob(cursor.getColumnIndex("imagesource"));
            post.datapublicacao = cursor.getString(cursor.getColumnIndex("datapublicacao"));
            post.id = cursor.getInt(cursor.getColumnIndex("id"));
            post.userid = cursor.getInt(cursor.getColumnIndex("userid"));
            list.add(post);
            Log.w("Teste","subindo tela "+post.id);
        }
        Log.w("Teste","voltando tela tela ");
        return list;
    }
}
