package com.example.pablo.pruebasauthproyecto.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Activities.CrearProyectos.CrearProyecto;
import com.example.pablo.pruebasauthproyecto.Activities.VerProyectos.FichaProyecto;
import com.example.pablo.pruebasauthproyecto.Adaptadores.ProyectosAdapter;
import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.Modelo.Usuario;
import com.example.pablo.pruebasauthproyecto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class Navegador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String PROYECTO = "proyecto";
    public final static String USUARIO = "usuario";

    private RecyclerView recycler;

    private FirebaseUser fUsr;
    private String intereses;
    private String claveProy;

    private ArrayList<Proyecto> proyectos = new ArrayList<>();
    private ArrayList<String> listaIntereses = new ArrayList<>();

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Recursos r;
    Proyecto p;
    Usuario usr;
    ProyectosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fUsr = FirebaseAuth.getInstance().getCurrentUser();

        recuperarProyectos();

        obtenerUser();
    }

    void obtenerUser() {
        // obtenemos el objeto usuario
        ref.child("usuarios").child(fUsr.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usr = dataSnapshot.getValue(Usuario.class);
                Log.d("DEPURACIÓN" , usr.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEPURACIÓN", databaseError.getMessage());
            }
        });
    }

    private void recuperarIntereses() {

        DatabaseReference interesesRef = ref.child("intereses").child(fUsr.getUid());

        interesesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                intereses = dataSnapshot.getValue().toString();
                Log.d("DEPURACION", intereses);
                listaIntereses.add(intereses);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void verProyecto(Proyecto proyecto) {
        Log.d("DEPURACIÓN", usr.toString());
        Intent intent = new Intent(this, FichaProyecto.class);
        intent.putExtra(this.PROYECTO, proyecto);
        intent.putExtra(this.USUARIO, usr);
        startActivity(intent);
    }

    private void cargarRecycler() {
        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        adapter = new ProyectosAdapter(proyectos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d("DEPURACIÓN" , "Item Pulsado : " + proyectos.get(recycler.getChildAdapterPosition(view)).getNombre());
                Proyecto prosel = proyectos.get(recycler.getChildAdapterPosition(view));
                verProyecto(prosel);
            }
        });

        recycler.setAdapter(adapter);

        // Usar un administrador para LinearLayout
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
    }


    private void recuperarProyectos() {

        recuperarIntereses();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recuperando Proyectos...");
        progressDialog.show();

        final DatabaseReference proyectosRef = ref.child("proyectos");
        Query consProy = proyectosRef.orderByChild("genero");

        consProy.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                claveProy = dataSnapshot.getKey();
                Log.d("DEPURACION", "claveProy2 -> " + claveProy);
                final Proyecto p = dataSnapshot.getValue(Proyecto.class);
                p.setKeyProyecto(claveProy);
                DatabaseReference recursosRef = ref.child("recursos");
                Query consRecursos = recursosRef.orderByChild("idProyecto").equalTo(claveProy);
                consRecursos.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        r = dataSnapshot.getValue(Recursos.class);
                        // Log.d("DEPURACIÓN","PROYECTO -> " + p.toString());
                        p.addRecurso(r);
                        // Log.d("DEPURACIÓN", "RECURSOS PROYECTO -> " + p.getRecursos().toString());
                        for (String inter : listaIntereses) {
                            Log.d("DEPURACION", "p.getGenero() : " + p.getGenero());
                            Log.d("DEPURACION", "inter : " + inter);
                            if (p.getGenero().equalsIgnoreCase(inter)) {
                                Log.d("DEPURACION", p.toString());
                                proyectos.add(p);
                            }
                        }
                        // eliminamos elementos repetidos del ArrayList
                        HashSet<Proyecto> hs = new HashSet<>();
                        hs.addAll(proyectos);
                        proyectos.clear();
                        proyectos.addAll(hs);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cargarRecycler();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.crear_proyect) {
            startActivity(new Intent(Navegador.this, CrearProyecto.class));
        } else if (id == R.id.mis_proyectos) {
            startActivity(new Intent(Navegador.this, MisProyectos.class));
        } else if (id == R.id.cerrar_sesion) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
