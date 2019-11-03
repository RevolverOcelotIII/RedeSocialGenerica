package com.trab.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
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
        Button srchbtn = (Button) findViewById(R.id.catchimage);
        final UserDAO userDAO = new UserDAO(getApplicationContext());

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
                    User user = new User(nometext.getText().toString(),emailtext.getText().toString(),senhatext.getText().toString(),telefonetext.getText().toString(),"");
                    if(userDAO.searchUserByEmail(user.email)){

                    }
                    else {
                        userDAO.saveUser(user);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==1){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Uri tempuri = get
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
