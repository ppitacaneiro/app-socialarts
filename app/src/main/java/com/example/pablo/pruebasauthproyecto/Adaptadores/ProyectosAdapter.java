package com.example.pablo.pruebasauthproyecto.Adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.pablo.pruebasauthproyecto.Utiles.Config.URL_IMG_DEFECTO_NAVEGADOR;

/**
 * Created by Pablo on 15/03/2018.
 */

public class ProyectosAdapter extends RecyclerView.Adapter<ProyectosAdapter.ProyectosViewHolder> implements View.OnClickListener {

    private List<Proyecto> items;
    private ArrayList<Recursos> tempRecursos;
    private View.OnClickListener listener;

    public class ProyectosViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagen;
        public TextView nombre;
        public TextView autor;
        public TextView genero;

        public ProyectosViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            autor = (TextView) v.findViewById(R.id.autor);
            genero = (TextView) v.findViewById(R.id.genero);
        }
    }

    public ProyectosAdapter(List<Proyecto> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ProyectosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.proyectos_card, viewGroup, false);

        v.setOnClickListener(this);

        return new ProyectosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProyectosViewHolder viewHolder, int i) {
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.autor.setText(items.get(i).getAutor());
        viewHolder.genero.setText(items.get(i).getGenero());

        Context context = viewHolder.imagen.getContext();

        // obtener la foto de los recursos
        Log.d("DEPURACION", "TIPO RECURSO : " + items.get(i).getRecursos());
        tempRecursos = items.get(i).getRecursos();

        String fotoProyectoCard = null;
        for (Recursos rtemp : tempRecursos) {
            if (rtemp.getTipo() == 1) {
                fotoProyectoCard = rtemp.getRuta();
                break;
            } else {
                fotoProyectoCard = URL_IMG_DEFECTO_NAVEGADOR;
            }
        }

        Log.d("DEPURACION", "fotoProyectoCard : " + fotoProyectoCard);
        Uri uri = Uri.parse(fotoProyectoCard);
        Picasso.with(context)
                .load(uri)
                .resize(355,225)
                .centerCrop()
                .into(viewHolder.imagen);
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
