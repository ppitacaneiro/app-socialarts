package com.example.pablo.pruebasauthproyecto.Activities.CrearProyectos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Usuario;
import com.example.pablo.pruebasauthproyecto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class CrearProyecto extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public final static String CLAVE_PROYECTO = "clave_proyecto";

    private String claveProyecto;

    private FirebaseUser fUsr;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private DatePickerDialog datePickerDialog;
    private int mYear, mMonth, mDay;

    private EditText etnombreProyecto;
    private EditText descProyecto;
    private Button buttonCrearProyecto;
    private TextView inDateProyect;
    private ImageButton btnDateProyect;
    private TextView autorProyecto;
    private TextView generoProyecto;
    private ImageButton indicageneroProyecto;

    private String [] listaItems;
    private String generoSelProyecto;

    private Usuario usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);
        setTitle("SocialArts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // array de intereses
        listaItems = getResources().getStringArray(R.array.intereses);
        generoSelProyecto = listaItems[0];

        // obtenemos el UID del FirebaseUser
        fUsr = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("DEPURACIÓN", fUsr.getUid());

        // referencia a la bbdd
        this.database = FirebaseDatabase.getInstance();
        this.myRef = this.database.getReference();

        // obtenemos la fecha actual
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Iniciando datepicker dialog
        datePickerDialog = new DatePickerDialog(
                this, CrearProyecto.this, mYear, mMonth, mDay);

        // iniciamos Views
        etnombreProyecto = (EditText) findViewById(R.id.editTextNombreProyecto);
        descProyecto = (EditText) findViewById(R.id.desc_proyecto);
        buttonCrearProyecto = (Button) findViewById(R.id.buttonCrearProyecto);
        inDateProyect = (TextView) findViewById(R.id.in_date_proyect);
        btnDateProyect = (ImageButton) findViewById(R.id.btn_date_proyect);
        autorProyecto = (TextView) findViewById(R.id.autorProyecto);
        generoProyecto = (TextView) findViewById(R.id.in_genero_proyect);
        indicageneroProyecto = (ImageButton) findViewById(R.id.btn_genero_proyect);

        cargarDatos();

        gestionEventos();
    }

    void cargarDatos() {
        // obtenemos el objeto usuario
        myRef.child("usuarios").child(fUsr.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usr = dataSnapshot.getValue(Usuario.class);
                Log.d("DEPURACIÓN" , usr.toString());
                autorProyecto.setText(usr.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEPURACIÓN", databaseError.getMessage());
            }
        });
    }

    private boolean crearProyecto() {
        String nombre = etnombreProyecto.getText().toString().trim();
        String autor = autorProyecto.getText().toString().trim();
        String descripcion = descProyecto.getText().toString().trim();
        String fecha = inDateProyect.getText().toString().trim();
        String genero = generoProyecto.getText().toString().trim();
        String votos = "0";

        if(TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, R.string.alert_proyecto_nombre, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(fecha)) {
            Toast.makeText(this, R.string.alert_fecha_creacion, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(genero)) {
            Toast.makeText(this, R.string.alert_clasificar_proyecto, Toast.LENGTH_SHORT).show();
            return false;
        } else if(descripcion.length() > 300) {
            Toast.makeText(this, R.string.max_chars_desc, Toast.LENGTH_SHORT).show();
            return false;
        } else {

            // (String autor,String descripcion,String fecha,String genero,String idAutor,String nombre,String votos)
            Proyecto pro = new Proyecto(autor,descripcion,fecha,genero,fUsr.getUid(),nombre,votos);
            Log.d("DEPURACIÓN", pro.toString());

            DatabaseReference postsRef = myRef.child("proyectos");

            DatabaseReference newPostRef = postsRef.push();
            claveProyecto = newPostRef.getKey();
            newPostRef.setValue(pro);
            Log.d("DEPURACIÓN", claveProyecto);

            return true;
        }
    }

    void gestionEventos () {

        buttonCrearProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(crearProyecto()) {
                    Toast.makeText(CrearProyecto.this, R.string.proyecto_creado_ok, Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(CrearProyecto.this, AddRecursos.class));
                    Intent intent = new Intent(CrearProyecto.this, AddRecursos.class);
                    intent.putExtra(AddRecursos.CLAVE_PROYECTO, claveProyecto);
                    startActivity(intent);
                }
            }
        });

        btnDateProyect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        indicageneroProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CrearProyecto.this);
                mBuilder.setTitle(getResources().getString(R.string.indica_tus_intereses));
                mBuilder.setSingleChoiceItems(listaItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        generoSelProyecto =  listaItems[i];
                        // Toast.makeText(CrearProyecto.this, generoSelProyecto, Toast.LENGTH_SHORT).show();
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        generoProyecto.setText(generoSelProyecto);
                    }
                });
                mBuilder.setNegativeButton(R.string.salir_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String añoNac = String.valueOf(i);
        String mesNac = String.valueOf(i1);
        String diaNac = String.valueOf(i2);

        String fechaNac = diaNac + "/" + mesNac + "/" + añoNac;
        inDateProyect.setText(fechaNac);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
