package com.trab.myapplication.Net;

import com.trab.myapplication.Model.Post;
import com.trab.myapplication.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonCreator {
    public JSONObject convertUser (User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nome", user.nome);
            jsonObject.put("email", user.email);
            jsonObject.put("senha", user.senha);
            jsonObject.put("telefone", user.telefone);
            jsonObject.put("imagesource", user.imagesource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public JSONObject convertPost (Post post, String user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("descricao", post.descricao);
            jsonObject.put("datapublicacao", post.datapublicacao);
            jsonObject.put("imagesource", post.imagesource);
            jsonObject.put("user", user);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public User createUserFromJson (JSONObject object){
        User user = new User();

        try{
            user.nome = object.getString("nome");
            user.email = object.getString("email");
            user.senha = object.getString("senha");
            user.telefone = object.getString("telefone");
            user.imagesource = (byte[]) object.get("imagesource");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public Post createPostFromJson (JSONObject object) {
        Post post = new Post();
        try {
            post.imagesource = (byte[]) object.get("imagesource");
            post.descricao = object.getString("descricao");
            post.datapublicacao = object.getString("datapublicacao");
        } catch (Exception e){
            e.printStackTrace();
        }

        return post;
    }
}
