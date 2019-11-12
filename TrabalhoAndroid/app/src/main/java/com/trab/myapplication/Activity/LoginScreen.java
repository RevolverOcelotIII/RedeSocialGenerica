package com.trab.myapplication.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trab.myapplication.R;
import com.trab.myapplication.Model.User;
import com.trab.myapplication.Model.UserDAO;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LoginScreen extends AppCompatActivity {
    public static final String SAVED_USER = "user_data";
    private UserDAO userDAO;
    final User user = new User();
    public boolean imagechosed=false;
    public final Button srchbtn = (Button) findViewById(R.id.catchimage);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        userDAO = new UserDAO(getApplicationContext());

        final Toolbar customtoolbar = findViewById(R.id.customtoolbar);
        customtoolbar.setTitle(R.string.cadastrar);
        setSupportActionBar(customtoolbar);
        final EditText nometext = (EditText) findViewById(R.id.nometext);
        final TextView nomeview = (TextView) findViewById(R.id.nome);
        final EditText emailtext = (EditText) findViewById(R.id.emailtext);
        final EditText senhatext = (EditText) findViewById(R.id.senhatext);
        EditText confirmatext = (EditText) findViewById(R.id.confirmatext);
        final EditText telefonetext = (EditText) findViewById(R.id.telefonetext);
        final TableLayout cadastrotable = (TableLayout) findViewById(R.id.cadastrotable);
        final CheckBox logcheck = (CheckBox) findViewById(R.id.logcheck);
        final Button logbtn = (Button) findViewById(R.id.logbtn);

        srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Get image"),1);
            }
        });

        logcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logcheck.isChecked()){
                    nometext.setVisibility(View.INVISIBLE);
                    nomeview.setVisibility(View.INVISIBLE);
                    cadastrotable.setVisibility(View.INVISIBLE);
                    customtoolbar.setTitle(R.string.login);
                    logbtn.setText(R.string.logar);
                }else{
                    nometext.setVisibility(View.VISIBLE);
                    nomeview.setVisibility(View.VISIBLE);
                    cadastrotable.setVisibility(View.VISIBLE);
                    customtoolbar.setTitle(R.string.cadastro);
                    logbtn.setText(R.string.cadastrar);
                }
            }
        });
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!logcheck.isChecked()){
                    user.nome = nometext.getText().toString();
                    user.email = emailtext.getText().toString();
                    user.senha = senhatext.getText().toString();
                    user.telefone = telefonetext.getText().toString();
                    if(userDAO.searchUserByEmail(user.email)||user.nome==""||user.telefone==""||user.email==""||user.senha==""||!imagechosed){
                        Toast.makeText(getApplicationContext(),"Não foi possivel efetuar o cadastro :0 , veja o que deu errado aí",Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            userDAO.saveUser(user);
                            SharedPreferences preferences = getSharedPreferences(SAVED_USER, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("LoggedUserId", user.id);
                            editor.commit();
                            finish();
                            Intent intent = new Intent(LoginScreen.this,TimeLine.class);
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Não foi possivel efetuar o cadastro, veja o que deu errado aí, mas também pode ter sido erro nosso, então desculpa se for :)",Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    if(user.nome==""||user.telefone==""||user.email==""||user.senha==""||!imagechosed){
                        Toast.makeText(getApplicationContext(),"Não foi possivel efetuar o login :0 , veja o que deu errado aí",Toast.LENGTH_LONG).show();
                    }else{
                        if(userDAO.searchUserByEmailAndPassword(user.email,user.senha)){
                            SharedPreferences preferences = getSharedPreferences(SAVED_USER, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("LoggedUserId", user.id);
                            editor.commit();
                            finish();
                            Intent intent = new Intent(LoginScreen.this,TimeLine.class);
                            startActivity(intent);
                        }else Toast.makeText(getApplicationContext(),"Usuário e/ou senha incorretos >:(",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==1){
            try {
                imagechosed = true;
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();
                user.imagesource = imageInByte;
                srchbtn.setBackground(new BitmapDrawable(getResources(),bitmap));
                //Bitmap btm = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
