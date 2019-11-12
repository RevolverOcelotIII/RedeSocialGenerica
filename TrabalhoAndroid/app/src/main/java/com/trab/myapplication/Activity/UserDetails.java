package com.trab.myapplication.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trab.myapplication.Model.User;
import com.trab.myapplication.Model.UserDAO;
import com.trab.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserDetails extends AppCompatActivity {
    private UserDAO userDAO = new UserDAO(getApplicationContext());
    final EditText nometext = (EditText) findViewById(R.id.editnometext);
    final EditText emailtext = (EditText) findViewById(R.id.editemailtext);
    final EditText senhatext = (EditText) findViewById(R.id.editsenhatext);
    EditText confirmatext = (EditText) findViewById(R.id.editconfirmatext);
    final EditText telefonetext = (EditText) findViewById(R.id.edittelefonetext);
    final TableLayout cadastrotable = (TableLayout) findViewById(R.id.edittable);
    final CheckBox editcheck = (CheckBox) findViewById(R.id.editcheck);
    final Button logbtn = (Button) findViewById(R.id.editbtn);
    final Button postbtn = (Button) findViewById(R.id.postbtn);
    final Button exitbtn = (Button) findViewById(R.id.exitbtn);
    final ImageView userpicture = (ImageView) findViewById(R.id.usereditimage);
    final SharedPreferences preferences = getSharedPreferences(LoginScreen.SAVED_USER, 0);
    private boolean imagechosed = true;
    private User loggeduser;
    private User tempuser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        tempuser.imagesource = loggeduser.imagesource;
        loggeduser = userDAO.getUserFromDB(preferences.getInt("LoggedUserId",-1));
        userpicture.setImageBitmap(BitmapFactory.decodeByteArray(loggeduser.imagesource,0,loggeduser.imagesource.length));
        nometext.setText(loggeduser.nome);
        emailtext.setText(loggeduser.email);
        senhatext.setText(loggeduser.senha);
        telefonetext.setText(loggeduser.telefone);
        editcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editcheck.isChecked()){
                    nometext.setEnabled(true);
                    emailtext.setEnabled(true);
                    senhatext.setEnabled(true);
                    confirmatext.setVisibility(View.VISIBLE);
                    telefonetext.setEnabled(true);
                    logbtn.setVisibility(View.VISIBLE);
                }else {
                    nometext.setEnabled(false);
                    emailtext.setEnabled(false);
                    senhatext.setEnabled(false);
                    confirmatext.setVisibility(View.INVISIBLE);
                    telefonetext.setEnabled(false);
                    logbtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("LoggedUserId", -1);
                editor.commit();
                Intent intent = new Intent(UserDetails.this,LoginScreen.class);
                startActivity(intent);
            }
        });
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nometext.getText().equals("")||telefonetext.getText().equals("")||emailtext.getText().equals("")||senhatext.getText().equals("")||!imagechosed){
                    Toast.makeText(getApplicationContext(),"Não foi possivel efetuar o login :0 , veja o que deu errado aí",Toast.LENGTH_LONG).show();
                }else{
                if(senhatext.getText().equals(confirmatext.getText())){
                    tempuser.senha = senhatext.getText().toString();
                    tempuser.id = loggeduser.id;
                    tempuser.email = emailtext.getText().toString();
                    tempuser.telefone = telefonetext.getText().toString();
                    tempuser.nome = nometext.getText().toString();
                    userDAO.editUser(tempuser);
                }else {
                    Toast.makeText(getApplicationContext(), "Os campos de senha e confirmação de senha não combinam >:(", Toast.LENGTH_LONG).show();
                }
                }
            }
        });
        userpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Get image"),1);
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
                tempuser.imagesource = imageInByte;
                //Bitmap btm = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
