package com.trab.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    SharedPreferences preferences;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        //DBset's
        preferences = getSharedPreferences(LoginScreen.SAVED_USER,0);
        postDAO = new PostDAO(getApplicationContext());
        userDAO = new UserDAO(getApplicationContext());
//        Toast.makeText(getApplicationContext(),"Login efetuado como usuário: "+/*+preferences.getInt("LoggedUserId",-1)*/new UserDAO(getApplicationContext()).getUserFromDB(preferences.getInt("LoggedUserId",-1)).id,Toast.LENGTH_LONG).show();
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.addfab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityByButton(1);
            }
        });

        Toolbar toolbar = findViewById(R.id.timelinetoolbar);
        toolbar.setTitle("POSTAGENS RECENTES");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        boolean useronly = intent.getBooleanExtra("useronly",false);

        if(useronly){

            postagens = postDAO.getUserPosts(preferences.getInt("LoggedUserId",-1));

        }else postagens = postDAO.getAllPosts();

        if(postagens.isEmpty()){
            TextView nullview = (TextView) findViewById(R.id.nullmessage);
            nullview.setVisibility(View.VISIBLE);
        }

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
        }
        PostAdapter postAdapter = new PostAdapter(postagens,users,isliked,loggeduser);
        postRecyclerView.setAdapter(postAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_itens, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.userpage:
                startActivityByButton(0);
                break;
            case R.id.timeLine:
                startActivityByButton(2);
        }
        return super.onOptionsItemSelected(item);
    }
    private void startActivityByButton(int contexto){
        switch (contexto){
            case 0:
                Intent intent = new Intent(this,UserDetails.class);
                startActivity(intent);
                finish();
                break;
            case 1:
                Intent intentnew = new Intent(this,CreatePost.class);
                startActivity(intentnew);
                finish();
                break;
            case 2:
                Intent intentnew2 = new Intent(this,TimeLine.class);
                startActivity(intentnew2);
                finish();
                break;
        }
    }
}
