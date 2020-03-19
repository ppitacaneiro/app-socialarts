package com.example.pablo.pruebasauthproyecto.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Activities.Usuarios.RegistroUsuario;
import com.example.pablo.pruebasauthproyecto.Modelo.Usuario;
import com.example.pablo.pruebasauthproyecto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegistro;
    private ProgressDialog progressDialog;
    private ImageView logo;

    // firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // iniciando firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        // iniciando views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegistro = (Button) findViewById(R.id.buttonRegistro);
        logo = (ImageView) findViewById(R.id.logo);
        progressDialog = new ProgressDialog(this);

        // animacion Logo
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        logo.startAnimation(animation);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                loginUsuario();
            }
        });

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroUsuario.class));
                finish();
            }
        });
    }

    private void loginUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        // validaciones
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Por favor introduzca un email" ,Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Por favor introduca una contraseña" ,Toast.LENGTH_LONG).show();
            return;
        }
        // Si el campo Usuario y la contraseña no estan vacios
        // mostramos processDialog
        progressDialog.setMessage(getString(R.string.alert_comprobando_datos));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // login ok
                    // Toast.makeText(MainActivity.this,"Accediendo al sistema",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, Navegador.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this,"Error ! ",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });

    }
}
