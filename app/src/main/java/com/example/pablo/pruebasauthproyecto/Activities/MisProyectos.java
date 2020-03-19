package com.example.pablo.pruebasauthproyecto.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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

public class MisProyectos extends AppCompatActivity {

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
        setContentView(R.layout.activity_mis_proyectos);

        fUsr = FirebaseAuth.getInstance().getCurrentUser();

        obtenerUser();

        recuperarProyectos ();

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
        Query consProy = proyectosRef.orderByChild("idAutor").equalTo(fUsr.getUid());

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

}
