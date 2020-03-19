package com.example.pablo.pruebasauthproyecto.Adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 23/03/2018.
 */

public class AudioAdapterUsuario extends ArrayAdapter<Recursos> {

    private Context context;
    private List<Recursos> cancionesListUsuario = new ArrayList<>();
    Recursos recursos;

    public AudioAdapterUsuario(Context context, ArrayList<Recursos> list) {
        super(context, 0 , list);
        this.context = context;
        cancionesListUsuario = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.layout_cancion,parent,false);

        recursos = cancionesListUsuario.get(position);

        TextView titulo = (TextView) listItem.findViewById(R.id.textView_titulo);
        titulo.setText(recursos.getNombre());

        TextView genero = (TextView) listItem.findViewById(R.id.textView_genero);
        genero.setText("");

        ImageView thumb = (ImageView) listItem.findViewById(R.id.imageView_cancion);

        thumb.setImageResource(R.drawable.audioicon);

        return listItem;
    }
}
