package com.example.pablo.pruebasauthproyecto.Activities.Usuarios;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Activities.MainActivity;
import com.example.pablo.pruebasauthproyecto.Activities.Navegador;
import com.example.pablo.pruebasauthproyecto.Modelo.Usuario;
import com.example.pablo.pruebasauthproyecto.R;
import com.example.pablo.pruebasauthproyecto.Utiles.Utilidades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RegistroUsuario extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    static final int EDAD_MINIMA = 14;

    private EditText editTextEmailReg;
    private EditText editTextPasswordReg;
    private EditText editTextNameReg;
    private Button buttonRegister;
    private ImageButton btnDatePicker;
    private TextView txtDate;
    private RadioButton sexoHombre;
    private RadioButton sexoMujer;
    private CheckBox condiciones;
    private ImageButton indicaIntereses;
    private TextView viewIntereses;
    private ProgressDialog progressDialog;

    private FirebaseUser fUsr;
    private FirebaseAuth firebaseAuth;
    private Usuario usr;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private int mYear, mMonth, mDay;
    private String sexo;
    private String [] listaItems;
    private boolean [] itemsCheckeados;
    private ArrayList<Integer> itemsUsuario = new ArrayList<>();
    private String [] listaItemsSel;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        setTitle(getString(R.string.resgistro_usuario));

        progressDialog = new ProgressDialog(this);

        // array de intereses
        listaItems = getResources().getStringArray(R.array.intereses);
        itemsCheckeados = new boolean[listaItems.length];
        listaItemsSel = new String[listaItems.length];

        // obtenemos la fecha actual
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Iniciando datepicker dialog
        datePickerDialog = new DatePickerDialog(
                this, RegistroUsuario.this, mYear, mMonth, mDay);

        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        // iniciando views
        editTextEmailReg = (EditText) findViewById(R.id.editTextEmailReg);
        editTextPasswordReg = (EditText) findViewById(R.id.editTextPasswordReg);
        buttonRegister = (Button) findViewById(R.id.buttonReg);
        editTextNameReg = (EditText) findViewById(R.id.editTextNameReg);
        btnDatePicker = (ImageButton) findViewById(R.id.btn_date);
        txtDate = (TextView) findViewById(R.id.in_date);
        sexoHombre = (RadioButton) findViewById(R.id.sexoHombre);
        sexoMujer = (RadioButton) findViewById(R.id.sexoMujer);
        condiciones = (CheckBox) findViewById(R.id.checkCondiciones);
        indicaIntereses = (ImageButton) findViewById(R.id.btn_intereses);
        viewIntereses = (TextView) findViewById(R.id.in_intereses);

        gestionEventos();
    }

    void gestionEventos() {

        indicaIntereses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegistroUsuario.this);
                mBuilder.setTitle(getResources().getString(R.string.indica_tus_intereses));
                mBuilder.setMultiChoiceItems(listaItems, itemsCheckeados, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // si esta checheado
                        if (b) {
                            // si el ArrayList no contiene la posicion Chekada la añadimos
                            if (!itemsUsuario.contains(i)) {
                                itemsUsuario.add(i);
                            } else {
                                itemsUsuario.remove(i);
                            }
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int cual) {
                        String item = "";
                        // generamos el string de los items checkados y los mostramos en el tv
                        for(int i = 0; i < itemsUsuario.size(); i++) {
                            item = item + listaItems[itemsUsuario.get(i)];
                            // guardamos en un array de Strings los items seleccionados
                            listaItemsSel[i] = listaItems[itemsUsuario.get(i)];
                            // Toast.makeText(RegistroUsuario.this, listaItemsSel[i], Toast.LENGTH_SHORT).show();
                            // concatenamos una coma al item añadido si no es el ultimo
                            if (i != itemsUsuario.size() - 1) {
                                item = item + " ,";
                            }
                        }
                        viewIntereses.setText(item);
                    }
                });
                mBuilder.setNegativeButton(R.string.salir_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton(R.string.limpiar_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int cual) {
                        for (int i = 0; i < itemsCheckeados.length; i++) {
                            itemsCheckeados[i] = false;
                            itemsUsuario.clear();
                            viewIntereses.setText("");
                        }
                    }
                });

                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void registrarUsuario() {
        final String email =  editTextEmailReg.getText().toString().trim();
        final String password =  editTextPasswordReg.getText().toString().trim();
        final String name =  editTextNameReg.getText().toString().trim();
        final String fecha = txtDate.getText().toString().trim();
        final String intereses = viewIntereses.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.indicar_nombre, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.indicar_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utilidades.validarEmail(email)) {
            Toast.makeText(this, R.string.indicar_email_valido, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.indicar_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, R.string.pass_6_chars, Toast.LENGTH_SHORT).show();
            return;
        }

        if (fecha.equalsIgnoreCase(getString(R.string.fecha_nacimiento))) {
            Toast.makeText(this, R.string.indicar_fecha, Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(intereses)) {
            Toast.makeText(this, R.string.indica_intereses, Toast.LENGTH_SHORT).show();
            return;
        }

        if(sexoHombre.isChecked()) {
            sexo = "Hombre";
        } else if (sexoMujer.isChecked()) {
            sexo = "Mujer";
        } else {
            Toast.makeText(this, R.string.indicar_sexo, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!condiciones.isChecked()) {
            Toast.makeText(this, R.string.condiciones, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage(getString(R.string.registrando_usuario));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    fUsr = FirebaseAuth.getInstance().getCurrentUser();
                    usr = new Usuario(fUsr.getEmail(), name, password, fecha, sexo);
                    Log.d("DEPURACIÓN", usr.toString());

                    // insercion en database real time
                    myRef.child("usuarios").child(fUsr.getUid()).setValue(usr);

                    // convertimos el array en una lista para poder insertarla en Firebase
                    List interesesList = new ArrayList<String>(Arrays.asList(listaItemsSel));
                    myRef.child("intereses").child(fUsr.getUid()).setValue(interesesList);

                    Toast.makeText(RegistroUsuario.this, R.string.registrado, Toast.LENGTH_SHORT).show();

                    // redireccinamos a la activity principal
                    startActivity(new Intent(RegistroUsuario.this, Navegador.class));
                    finish();

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegistroUsuario.this, R.string.usuario_existe, Toast.LENGTH_SHORT).show();
                    } else {
                        // capturo otras posibles excepciones
                        Toast.makeText(RegistroUsuario.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        if ( i >=  mYear ) {
            Toast.makeText(RegistroUsuario.this, R.string.ano_incorrecto, Toast.LENGTH_SHORT).show();
        } else if ( mYear - i < EDAD_MINIMA ) {
            Toast.makeText(RegistroUsuario.this, R.string.edad_minima, Toast.LENGTH_SHORT).show();
        } else {
            String añoNac = String.valueOf(i);
            String mesNac = String.valueOf(i1);
            String diaNac = String.valueOf(i2);

            String fechaNac = diaNac + "/" + mesNac + "/" + añoNac;
            txtDate.setText(fechaNac);
        }
    }
}
