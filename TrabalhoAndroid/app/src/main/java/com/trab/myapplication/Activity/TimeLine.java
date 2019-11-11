package com.trab.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.trab.myapplication.Adapter.PostAdapter;
import com.trab.myapplication.Model.Post;
import com.trab.myapplication.Model.PostDAO;
import com.trab.myapplication.Model.User;
import com.trab.myapplication.Model.UserDAO;
import com.trab.myapplication.R;

import java.util.ArrayList;

public class TimeLine extends AppCompatActivity {
    private RecyclerView postRecyclerView;
    private ArrayList<Post> postagens;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Boolean> isliked = new ArrayList<>();
    private PostDAO postDAO;
    private UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        //DBset's
        postDAO = new PostDAO(getApplicationContext());
        userDAO = new UserDAO(getApplicationContext());
        postagens = postDAO.getAllPosts();
        postRecyclerView = (RecyclerView) findViewById(R.id.postsRecyclerView);
        //Layout
        RecyclerView.LayoutManager postlayout = new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(postlayout);
        //Adapter
        SharedPreferences preferences = getSharedPreferences(LoginScreen.SAVED_USER,0);
        int loggeduser = preferences.getInt("LoggedUserId",0);
        for(Post p : postagens){
            User user = userDAO.getUserFromDB(p.userid);
            //Bitmap userpic = BitmapFactory.decodeByteArray(user.imagesource,0,user.imagesource.length);
            users.add(user);
            isliked.add(postDAO.isLiked(loggeduser,p.id));
        }
        PostAdapter postAdapter = new PostAdapter(postagens,users,isliked,loggeduser);
        postRecyclerView.setAdapter(postAdapter);
    }
}
