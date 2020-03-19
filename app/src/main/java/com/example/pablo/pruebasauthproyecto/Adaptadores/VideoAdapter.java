package com.example.pablo.pruebasauthproyecto.Adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.pruebasauthproyecto.Modelo.CancionArtistaHearTthis;
import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 23/03/2018.
 */

public class VideoAdapter extends ArrayAdapter<Recursos> {

    private Context context;
    private List<Recursos> videosList = new ArrayList<>();
    Recursos recursos;

    public VideoAdapter(Context context, ArrayList<Recursos> list) {
        super(context, 0 , list);
        this.context = context;
        videosList = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView2, @NonNull ViewGroup parent) {
        View listaItems2 = convertView2;
        if(listaItems2 == null) {
            listaItems2 = LayoutInflater.from(context).inflate(R.layout.layout_video_usuario,parent,false);
        }

        recursos = videosList.get(position);

        TextView tituloVideo = (TextView) listaItems2.findViewById(R.id.tv_titulo_videousuario);
        tituloVideo.setText(recursos.getNombre());

        return listaItems2;
    }
}
