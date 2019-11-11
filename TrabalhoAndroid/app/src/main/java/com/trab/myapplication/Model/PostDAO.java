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
        values.put("imagesource",post.imagesource);
        values.put("datapublicacao",post.datapublicacao.toString());
        write.insert(ConnectionFactory.TABELA_POST,null,values);
        return true;
    }
    public Post getPostFromDB(int postid){
        Post post = new Post();
        Cursor cursor = read.rawQuery("SELECT * FROM "+ConnectionFactory.TABELA_POST+" WHERE id == "+postid+";",null);
        post.descricao = cursor.getString(cursor.getColumnIndex("descricao"));
        post.imagesource = cursor.getBlob(cursor.getColumnIndex("imagesource"));
        post.datapublicacao = convertDate(cursor.getString(cursor.getColumnIndex("datapublicacao")));
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
            post.datapublicacao = convertDate(cursor.getString(cursor.getColumnIndex("datapublicacao")));
            post.id = cursor.getInt(cursor.getColumnIndex("id"));
            post.userid = cursor.getInt(cursor.getColumnIndex("userid"));
            list.add(post);
        }
        return list;
    }
    public boolean isLiked(int userid, int postid){
        Cursor cursor = read.rawQuery("SELECT userid FROM "+ConnectionFactory.TABELA_LIKES+" WHERE userid=="+userid+"AND postid=="+postid+";",null);
        if (cursor==null) return false;
        else return true;
    }
    public boolean Like(int userid,int postid){
        if(isLiked(userid,postid)){
            write.execSQL("DELETE FROM "+ ConnectionFactory.TABELA_LIKES +" WHERE userid=="+userid+"AND postid == "+postid+";");
            return false;
        }else {
        ContentValues values = new ContentValues();
        values.put("userid",userid);
        values.put("postid",postid);
        write.insert(ConnectionFactory.TABELA_LIKES,null,values);
        return true;
        }
    }
    public static Date convertDate(String inputDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateInString = inputDate;
        Date date = null;
        try {
            date =sdf.parse(dateInString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
