package com.example.pablo.pruebasauthproyecto.Activities.VerProyectos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pablo.pruebasauthproyecto.Adaptadores.FotosAdapter;
import com.example.pablo.pruebasauthproyecto.Adaptadores.ProyectosAdapter;
import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;

import java.util.ArrayList;


public class FotosProyecto extends Fragment {

    Proyecto p;
    private RecyclerView recyclerFotos;
    private ArrayList<Recursos> recursos;
    private ArrayList<Recursos> fotos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        p = (Proyecto) getArguments().getSerializable("proyecto");
        Log.d("DEPURACIÓN", p.toString());
        Log.d("DEPURACIÓN", "FOTOS");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fotos_proyecto, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fotos.clear();

        recursos = p.getRecursos();

        for (Recursos r : recursos) {
            Log.d("DEPURACION", "TIPO RECURSO -> " + r.getTipo());
            if (r.getTipo() == 1) {
                Log.d("DEPURACIÓN", r.getRuta());
                fotos.add(r);
            }
        }

        // Obtener el Recycler
        recyclerFotos = (RecyclerView) getView().findViewById(R.id.rv_images);
        recyclerFotos.setHasFixedSize(true);

        FotosAdapter adapter = new FotosAdapter(fotos);

        /*
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d("DEPURACIÓN" , "Item Pulsado : " + proyectos.get(recycler.getChildAdapterPosition(view)).getNombre());
                Proyecto prosel = proyectos.get(recycler.getChildAdapterPosition(view));
                verProyecto(prosel);
            }
        });
        */

        recyclerFotos.setAdapter(adapter);

        // Usar un administrador para LinearLayout
        LinearLayoutManager lManager = new LinearLayoutManager(getContext());
        recyclerFotos.setLayoutManager(lManager);
    }
}
