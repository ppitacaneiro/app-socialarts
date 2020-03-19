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

import com.example.pablo.pruebasauthproyecto.Modelo.CancionArtistaHearTthis;
import com.example.pablo.pruebasauthproyecto.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 23/03/2018.
 */

public class AudioAdapter extends ArrayAdapter<CancionArtistaHearTthis> {

    private Context context;
    private List<CancionArtistaHearTthis> cancionesList = new ArrayList<>();
    CancionArtistaHearTthis cancionArtistaHearTthis;

    public AudioAdapter(Context context, ArrayList<CancionArtistaHearTthis> list) {
        super(context, 0 , list);
        this.context = context;
        cancionesList = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.layout_cancion,parent,false);

        cancionArtistaHearTthis = cancionesList.get(position);

        TextView titulo = (TextView) listItem.findViewById(R.id.textView_titulo);
        titulo.setText(cancionArtistaHearTthis.getTitulo());

        TextView genero = (TextView) listItem.findViewById(R.id.textView_genero);
        genero.setText(cancionArtistaHearTthis.getGenero());

        ImageView thumb = (ImageView) listItem.findViewById(R.id.imageView_cancion);

        Context context = thumb.getContext();
        Uri uri = Uri.parse(cancionArtistaHearTthis.getThumgImg());
        Picasso.with(context)
                .load(uri)
                .resize(355,225)
                .centerCrop()
                .into(thumb);

        return listItem;
    }
}
