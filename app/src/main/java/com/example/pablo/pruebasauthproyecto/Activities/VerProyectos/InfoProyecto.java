package com.example.pablo.pruebasauthproyecto.Activities.VerProyectos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class InfoProyecto extends Fragment {

    String votosActuales;
    String idUsuario;
    boolean pulsadoLike = false;
    boolean like = false;

    Proyecto p;
    Usuario usr;

    TextView autor;
    TextView descripcion;
    TextView fecha;
    TextView titulo;
    TextView genero;
    TextView votos;
    ImageButton botLikeButton;

    private FirebaseUser fUsr;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        p = (Proyecto) getArguments().getSerializable("proyecto");
        usr = (Usuario) getArguments().getSerializable("usuario");
        Log.d("DEPURACIÓN", p.toString());
        Log.d("DEPURACIÓN", "USUARIO -> " + usr.toString());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_proyecto, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final HashMap<String, Object> proyectosVotados = new HashMap<String, Object>();

        // obetnemos el id del usuario
        fUsr = FirebaseAuth.getInstance().getCurrentUser();
        idUsuario = fUsr.getUid();

        // referencia a la bbdd
        this.database = FirebaseDatabase.getInstance();
        this.myRef = this.database.getReference();
        final DatabaseReference postsRef = myRef.child("proyectos");
        final DatabaseReference usersRef = myRef.child("usuarios");

        autor = (TextView) getView().findViewById(R.id.tvautor);
        descripcion = (TextView) getView().findViewById(R.id.tvdesc_proyecto);
        fecha = (TextView) getView().findViewById(R.id.tvfecha);
        titulo = (TextView) getView().findViewById(R.id.tvtituloProyecto);
        genero = (TextView) getView().findViewById(R.id.tvgenero);
        votos = (TextView) getView().findViewById(R.id.tvVotos);
        botLikeButton = (ImageButton) getView().findViewById(R.id.likebutton);

        mostarDatos();

        // Obtener Votos
        postsRef.child(p.getKeyProyecto()).child("votos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                votosActuales = dataSnapshot.getValue().toString();
                votos.setText(votosActuales + " likes");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        final DatabaseReference votosRef = myRef.child("votos");
        // Obtener proyectos Votados por el Usuario
        Query queryProyVotados = myRef.child("votos").child(idUsuario);
        queryProyVotados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Boolean> map = (Map) dataSnapshot.getValue();
                    for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                        Log.d("DEPURACIÓN", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                        if (entry.getKey().equalsIgnoreCase(p.getKeyProyecto()) && entry.getValue() == true) {
                            like = true;
                            botLikeButton.setImageResource(R.drawable.buttonlike);
                        } else if (entry.getKey().equalsIgnoreCase(p.getKeyProyecto()) && entry.getValue() == false) {
                            like = false;
                        }
                    }

                    botLikeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int likes;
                            if (like) {
                                like = false;
                                botLikeButton.setImageResource(R.drawable.buttonlike2);
                                proyectosVotados.put(p.getKeyProyecto(), false);
                                if (Integer.parseInt(votosActuales) == 0) {
                                    likes = 0;
                                } else {
                                    likes = Integer.parseInt(votosActuales) - 1;
                                }
                            } else {
                                like = true;
                                botLikeButton.setImageResource(R.drawable.buttonlike);
                                likes = Integer.parseInt(votosActuales) + 1;
                                proyectosVotados.put(p.getKeyProyecto(), true);
                            }
                            postsRef.child(p.getKeyProyecto()).child("votos").setValue(String.valueOf(likes));
                            votosRef.child(idUsuario).updateChildren(proyectosVotados);
                        }
                    });
                } else {
                    HashMap<String, Boolean> inicioLikes = new HashMap<>();
                    inicioLikes.put(p.getKeyProyecto(), false);
                    votosRef.child(idUsuario).setValue(inicioLikes);
                    Log.d("DEPURACIÓN", "inicioLikes -> " + inicioLikes.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void mostarDatos() {
        autor.setText(p.getAutor());
        descripcion.setText(p.getDescripcion());
        fecha.setText(p.getFecha());
        titulo.setText(p.getNombre());
        genero.setText(p.getGenero());
        votos.setText(String .valueOf(p.getVotos()) + " likes");
    }

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

