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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 15/03/2018.
 */

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.FotosViewHolder> implements View.OnClickListener {

    private ArrayList<Recursos> items;
    private View.OnClickListener listener;

    public class FotosViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagen;
        public TextView texto;

        public FotosViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.iv_photo);
            texto = (TextView) v.findViewById(R.id.tv_text_foto);
        }
    }

    public FotosAdapter(ArrayList<Recursos> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FotosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.foto_galeria, viewGroup, false);

        v.setOnClickListener(this);

        return new FotosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FotosViewHolder viewHolder, int i) {

        viewHolder.texto.setText(items.get(i).getNombre());

        Context context = viewHolder.imagen.getContext();
        Uri uri = Uri.parse(items.get(i).getRuta());
        Picasso.with(context)
                .load(uri)
                .resize(200,200)
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
