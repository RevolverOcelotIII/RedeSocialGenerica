package com.trab.myapplication.Activity;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.trab.myapplication.Model.User;
import com.trab.myapplication.Model.UserDAO;
import com.trab.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserDetails extends AppCompatActivity {
    private UserDAO userDAO;
    private boolean imagechosed = true;
    private User loggeduser;
    private User tempuser = new User();
    private ImageView userpicture;
    private Toolbar toolbar;
    private SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
    private MaskTextWatcher mtw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        userDAO = new UserDAO(getApplicationContext());

        userpicture = (ImageView) findViewById(R.id.usereditimage);
        final TextView nometext = (TextView) findViewById(R.id.editnometext);
        final TextView emailtext = (TextView) findViewById(R.id.editemailtext);
        final TextView senhatext = (TextView) findViewById(R.id.editsenhatext);
        final TextView confirmatext = (TextView) findViewById(R.id.editconfirmatext);
        final TextView telefonetext = (TextView) findViewById(R.id.edittelefonetext);
        final TableLayout cadastrotable = (TableLayout) findViewById(R.id.edittable);
        final Button logbtn = (Button) findViewById(R.id.editbtn);
        final Button postbtn = (Button) findViewById(R.id.postbtn);
        final Button exitbtn = (Button) findViewById(R.id.exitbtn);
        mtw = new MaskTextWatcher(telefonetext,smf);
        telefonetext.addTextChangedListener(mtw);
        final SharedPreferences preferences = getSharedPreferences(LoginScreen.SAVED_USER, 0);
        toolbar = (Toolbar) findViewById(R.id.userdetailstoolbar);
        toolbar.setTitle("Usuário");
        setSupportActionBar(toolbar);

        int userid = preferences.getInt("LoggedUseId",-1);
        loggeduser = userDAO.getUserFromDB(preferences.getInt("LoggedUserId",-1));
        tempuser.imagesource = loggeduser.imagesource;
        userpicture.setImageBitmap(BitmapFactory.decodeByteArray(loggeduser.imagesource,0,loggeduser.imagesource.length));
        nometext.setText(loggeduser.nome);
        emailtext.setText(loggeduser.email);
        senhatext.setText(loggeduser.senha);
        telefonetext.setText(loggeduser.telefone);
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityUserPosts();
            }
        });

        /*editcheck.setOnClickListener(new View.OnClickListener() {
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
                    confirmatext.setVisibility(View.GONE);
                    telefonetext.setEnabled(false);
                    logbtn.setVisibility(View.GONE);
                }
            }
        });*/
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("LoggedUserId", -1);
                editor.commit();
                Intent intent = new Intent(UserDetails.this,SplashScreen.class);
                startActivity(intent);
                finish();
            }
        });
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nometext.getText().equals("")||telefonetext.getText().equals("")||emailtext.getText().equals("")||senhatext.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Não foi possivel editar o cadastro :0 , veja o que deu errado aí",Toast.LENGTH_LONG).show();
                }else{
                    if(senhatext.getText().equals(confirmatext.getText())){
                        tempuser.id = loggeduser.id;
                        tempuser.email = emailtext.getText().toString();
                        tempuser.telefone = telefonetext.getText().toString();
                        tempuser.nome = nometext.getText().toString();
                        userDAO.editUser(tempuser);
                        startActivityByButton(true);
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
                userpicture.setImageBitmap(bitmap);
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
            finish();
        }else{
            Intent intent = new Intent(this,TimeLine.class);
            startActivity(intent);
            finish();
        }
    }
    private void startActivityUserPosts(){
        Intent intent = new Intent(this,TimeLine.class);
        intent.putExtra("useronly",true);
        startActivity(intent);
    }
}
