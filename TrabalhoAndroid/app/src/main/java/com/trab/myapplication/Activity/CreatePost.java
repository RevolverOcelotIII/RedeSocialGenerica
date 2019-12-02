package com.trab.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Build;
import android.os.Bundle;

import com.trab.myapplication.Model.Post;
import com.trab.myapplication.Model.PostDAO;
import com.trab.myapplication.Model.User;
import com.trab.myapplication.Model.UserDAO;
import com.trab.myapplication.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreatePost extends AppCompatActivity {

    private UserDAO userDAO;
    private PostDAO postDAO;
    private User currentuser;
    private Post post;
    private ImageView postpic;
    private Button openimagebtn;
    private Date today;
    private Button removebtn;

    SharedPreferences preferences;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        post = new Post();
        preferences = getSharedPreferences(LoginScreen.SAVED_USER,0);
        userDAO = new UserDAO(getApplicationContext());
        postDAO = new PostDAO(getApplicationContext());
        currentuser = userDAO.getUserFromDB(preferences.getInt("LoggedUserId",-1));

        TextView nomefield = (TextView) findViewById(R.id.usernamefieldpost);
        final TextView datefield = (TextView) findViewById(R.id.datetimefieldpost);
        ImageView userpic = (ImageView) findViewById(R.id.userimagepost);
        final EditText descpost = (EditText) findViewById(R.id.descricaosetfieldpost);
        postpic = (ImageView) findViewById(R.id.postimagepost);
        Button postbtn = (Button) findViewById(R.id.posbtn);
        removebtn = (Button) findViewById(R.id.removepicture);

        removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postpic.setVisibility(View.GONE);
            }
        });

        openimagebtn = (Button) findViewById(R.id.addpicture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.createposttoolbar);
        toolbar.setTitle("ADICIONAR POSTAGEM");
        today = new Date();
        String pattern = "dd/MM/yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        datefield.setText(df.format(today));
        nomefield.setText(currentuser.nome);
        userpic.setImageBitmap(BitmapFactory
                .decodeByteArray(currentuser.imagesource,0,currentuser.imagesource.length));

        openimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Get image"),1);
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.datapublicacao = datefield.getText().toString();
                post.descricao = descpost.getText().toString();
                post.userid = currentuser.id;
                if(postpic.getVisibility()==View.GONE){
                    post.imagesource=null;
                }
                if(descpost.getText().toString().isEmpty()&&postpic.getVisibility()==View.GONE){
                    Toast.makeText(getApplicationContext(),"Não deixe a postagem vazia >:(",Toast.LENGTH_LONG).show();
                }else{
                    postDAO.savePost(post);
                    outOfActivity();
                }
            }
        });
    }
    public void outOfActivity(){
        Intent intent = new Intent(this,TimeLine.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==1){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();
                post.imagesource = imageInByte;
                postpic.setImageBitmap(BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length));
                openimagebtn.setText("ALTERAR IMAGEM");
                postpic.setVisibility(View.VISIBLE);
                removebtn.setVisibility(View.VISIBLE);
                //Bitmap btm = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
                startActivityByButton(true);
                break;
            case R.id.timeLine:
                startActivityByButton(false);
        }
        return super.onOptionsItemSelected(item);
    }
    private void startActivityByButton(boolean contexto){
        if(contexto){
            Intent intent = new Intent(this,UserDetails.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,TimeLine.class);
            startActivity(intent);
        }
    }
}
