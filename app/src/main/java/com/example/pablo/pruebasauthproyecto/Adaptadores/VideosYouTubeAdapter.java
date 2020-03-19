package com.example.pablo.pruebasauthproyecto.Adaptadores;

import android.content.Context;
import android.net.Uri;
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
import com.example.pablo.pruebasauthproyecto.Modelo.VideoYouTube;
import com.example.pablo.pruebasauthproyecto.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 23/03/2018.
 */

public class VideosYouTubeAdapter extends ArrayAdapter<VideoYouTube> {

    private Context contexto;
    private List<VideoYouTube> videosYouTubeList = new ArrayList<>();
    VideoYouTube videoYouTube;

    public VideosYouTubeAdapter(Context context, ArrayList<VideoYouTube> list) {
        super(context, 0 , list);
        this.contexto = context;
        videosYouTubeList = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listaItems = convertView;
        if(listaItems == null) {
            listaItems = LayoutInflater.from(contexto).inflate(R.layout.layout_video_youtube, parent, false);
        }

        videoYouTube = videosYouTubeList.get(position);

        TextView tituloVideo = (TextView) listaItems.findViewById(R.id.tv_titulo_ytube);
        tituloVideo.setText(videoYouTube.getTitulo());

        ImageView thumb = (ImageView) listaItems.findViewById(R.id.imageView_videoyoutube);

        Context context = thumb.getContext();
        Uri uri = Uri.parse(videoYouTube.getThumb());
        Picasso.with(context)
                .load(uri)
                .resize(355,225)
                .centerCrop()
                .into(thumb);

        return listaItems;
    }

    @Override
    public int getCount() {
        return videosYouTubeList.size();
    }
}
