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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
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
    private Button srchbtn;
    private Toolbar customtoolbar;
    private EditText telefonetext;
    private SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
    private MaskTextWatcher mtw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        userDAO = new UserDAO(getApplicationContext());

        srchbtn = (Button) findViewById(R.id.catchimage);
        //customtoolbar = (Toolbar) findViewById(R.id.customtoolbar);
        //customtoolbar.setTitle(R.string.cadastrar);
        //setSupportActionBar(customtoolbar);
        final TextView title = (TextView) findViewById(R.id.texttitle);
        final EditText nometext = (EditText) findViewById(R.id.nometext);
        final TextView nomeview = (TextView) findViewById(R.id.nome);
        final EditText emailtext = (EditText) findViewById(R.id.emailtext);
        final EditText senhatext = (EditText) findViewById(R.id.senhatext);
        final EditText confirmatext = (EditText) findViewById(R.id.confirmatext);
        telefonetext = (EditText) findViewById(R.id.telefonetext);
        final TableLayout cadastrotable = (TableLayout) findViewById(R.id.cadastrotable);
        final CheckBox logcheck = (CheckBox) findViewById(R.id.logcheck);
        final Button logbtn = (Button) findViewById(R.id.logbtn);
        mtw = new MaskTextWatcher(telefonetext,smf);
        telefonetext.addTextChangedListener(mtw);

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
                    nometext.setVisibility(View.GONE);
                    nomeview.setVisibility(View.GONE);
                    cadastrotable.setVisibility(View.GONE);
                    srchbtn.setVisibility(View.INVISIBLE);
                    title.setText(R.string.login);
                    logbtn.setText(R.string.logar);
                }else{
                    nometext.setVisibility(View.VISIBLE);
                    nomeview.setVisibility(View.VISIBLE);
                    cadastrotable.setVisibility(View.VISIBLE);
                    srchbtn.setVisibility(View.VISIBLE);
                    title.setText(R.string.cadastro);
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
                    String confirmasenha = confirmatext.getText().toString();
                    if(user.nome==""||user.telefone==""||user.email==""||user.senha==""||!imagechosed){
                        Toast.makeText(getApplicationContext(),"Campos foram deixados em branco",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(user.nome.length()>=5||user.email.length()>=12||user.telefone.length()>=14||user.senha.length()>=6){
                            if(user.email.contains("@")&&(user.email.contains(".com")||user.email.contains(".br"))){
                                if(!userDAO.searchUserByEmail(user.email)){
                                    if(user.senha.equals(confirmasenha)){
                                        try {
                                            userDAO.saveUser(user);
                                            SharedPreferences preferences = getSharedPreferences(SAVED_USER, 0);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putInt("LoggedUserId", userDAO.getUserIDFromDBbyEmail(user.email));
                                            Toast.makeText(getApplicationContext(),"HMMMMM2",Toast.LENGTH_LONG).show();
                                            editor.commit();
                                            Log.w("Teste","User salvo");
                                            Intent intent = new Intent(LoginScreen.this,TimeLine.class);
                                            startActivity(intent);
                                            finish();
                                        }catch (Exception e){
                                           Toast.makeText(getApplicationContext(),"Não foi possivel efetuar o cadastro, veja o que deu errado aí, mas também pode ter sido erro nosso, então desculpa se for :)",Toast.LENGTH_LONG).show();
                                        }
                                    } else Toast.makeText(getApplicationContext(),"Senhas não batem",Toast.LENGTH_LONG).show();
                                }else  Toast.makeText(getApplicationContext(),"Usuário já cadastrado",Toast.LENGTH_LONG).show();
                            }else Toast.makeText(getApplicationContext(),"Email inválido inserido",Toast.LENGTH_LONG).show();
                        }else Toast.makeText(getApplicationContext(),"Dados inválidos(Provavelmente por serem curtos demais)",Toast.LENGTH_LONG).show();
                    }
                }else{
                    user.email = emailtext.getText().toString();
                    user.senha = senhatext.getText().toString();
                    if(user.email==""||user.senha==""){
                        Toast.makeText(getApplicationContext(),"Campos foram deixados em branco",Toast.LENGTH_LONG).show();
                    }else{
                        if(userDAO.searchUserByEmailAndPassword(user.email,user.senha)){
                            //int userdaoid = userDAO.getUserIDFromDBbyEmail(user.email);
                            SharedPreferences preferences = getSharedPreferences(SAVED_USER, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("LoggedUserId", userDAO.getUserIDFromDBbyEmail(user.email));
                            editor.commit();
                            Intent intent = new Intent(LoginScreen.this,TimeLine.class);
                            startActivity(intent);
                            finish();
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
                srchbtn.setText("");
                //Bitmap btm = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
