package com.example.pablo.pruebasauthproyecto.Activities.VerProyectos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Adaptadores.VideoAdapter;
import com.example.pablo.pruebasauthproyecto.Adaptadores.VideosYouTubeAdapter;
import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.Modelo.VideoYouTube;
import com.example.pablo.pruebasauthproyecto.R;
import com.example.pablo.pruebasauthproyecto.Reproductores.VideoPlayer;
import com.example.pablo.pruebasauthproyecto.Reproductores.VideoTubePlayer;
import com.example.pablo.pruebasauthproyecto.Utiles.Config;
import com.example.pablo.pruebasauthproyecto.Utiles.HttpHandler;
import com.example.pablo.pruebasauthproyecto.Utiles.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class VideoProyectos extends Fragment {

    private final static String VIDEO = "video";

    private Proyecto p;
    private ArrayList<Recursos> recursos;
    private ArrayList<Recursos> videosUsuario = new ArrayList<>();
    private ArrayList<VideoYouTube> videosYouTube = new ArrayList<>();
    private ArrayList<String> idsYouTube = new ArrayList<>();

    ListView listViewVideosUsuario;
    ListView listViewYouTube;
    VideosYouTubeAdapter videosYouTubeAdapter;
    VideoAdapter videoAdapter;
    ProgressBar progressBarVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        p = (Proyecto) getArguments().getSerializable("proyecto");
        Log.d("DEPURACIÓN", p.toString());
        Log.d("DEPURACIÓN", "VIDEO");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_proyectos, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewYouTube = (ListView) getView().findViewById(R.id.lv_videos_youtube);
        listViewVideosUsuario = (ListView) getView().findViewById(R.id.lv_videos_usuario);
        progressBarVideo = (ProgressBar)getView().findViewById(R.id.progressBarVideo);

        idsYouTube.clear();
        videosUsuario.clear();

        recursos = p.getRecursos();

        for (Recursos r : recursos) {
            if (r.getTipo() == 2) {
                Log.d("DEPURACION", "RUTA VIDEO -> " + r.getRuta());
                videosUsuario.add(r);
            } else if (r.getTipo() == 3) {
                //Log.d("DEPURACION", "RUTA VIDEO -> " + r.getRuta());
                String idUrl = r.getRuta().substring(Config.URL_YOUTUBE.length(), r.getRuta().length());
                // Log.d("DEPURACION", "ID -> " + idUrl);
                idsYouTube.add(idUrl);
                Log.d("DEPURACION", "idsYouTube : " + String.valueOf(idsYouTube.size()));
            }
        }

        videoAdapter = new VideoAdapter(getContext(), videosUsuario);
        listViewVideosUsuario.setAdapter(videoAdapter);
        Utilidades.setDynamicHeight(listViewVideosUsuario);

        new GetYouTubeVideos().execute(idsYouTube);

        gestionarEventos ();
    }

    private void gestionarEventos () {
        listViewYouTube.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoYouTube videoYouTube = (VideoYouTube) listViewYouTube.getAdapter().getItem(i);
                // Log.d("DEPURACION", videoYouTube.toString());

                Intent intent = new Intent(getContext(), VideoTubePlayer.class);
                intent.putExtra(VIDEO, videoYouTube);
                startActivity(intent);
            }
        });

        listViewVideosUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), VideoPlayer.class);
                intent.putExtra(VIDEO, videosUsuario.get(i).getRuta());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();
        // Log.d("DEPURACION", "onResume");
    }

    private class GetYouTubeVideos extends AsyncTask<ArrayList<String>, Void, ArrayAdapter<VideoYouTube>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayAdapter<VideoYouTube> doInBackground(ArrayList<String>... arg0) {

            videosYouTube.clear();

            progressBarVideo.setVisibility(View.VISIBLE);

            HttpHandler sh = new HttpHandler();

            ArrayList<String> ids = arg0[0];

            for (String id : ids) {

                String jsonStr = sh.conexionHttp(Config.URL_API_YOUTUBE_1 + id + Config.URL_API_YOUTUBE_2);

                // Log.d("DEPURACION", "JSON -> " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                String title = jsonArray.getJSONObject(i).getJSONObject("snippet")
                                        .getString("title");
                                String descripcion = jsonArray.getJSONObject(i).getJSONObject("snippet")
                                        .getString("description");
                                String fecha = jsonArray.getJSONObject(i).getJSONObject("snippet")
                                        .getString("publishedAt");
                                String thumbnail = jsonArray.getJSONObject(i).getJSONObject("snippet")
                                        .getJSONObject("thumbnails")
                                        .getJSONObject("default")
                                        .getString("url");

                                VideoYouTube videoYouTube = new VideoYouTube();
                                videoYouTube.setTitulo(title);
                                videoYouTube.setDescripcion(descripcion);
                                videoYouTube.setFecha(fecha);
                                videoYouTube.setThumb(thumbnail);
                                videoYouTube.setId(id);

                                // Log.d("DEPURACION", videoYouTube.toString());

                                videosYouTube.add(videoYouTube);
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            videosYouTubeAdapter = new VideosYouTubeAdapter(getContext(), videosYouTube);
            return videosYouTubeAdapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<VideoYouTube> result) {
            super.onPostExecute(result);
            progressBarVideo.setVisibility(View.INVISIBLE);
            listViewYouTube.setAdapter(result);
            Utilidades.setDynamicHeight(listViewYouTube);
        }
    }
}
