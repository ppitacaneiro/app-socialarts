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

import com.example.pablo.pruebasauthproyecto.Adaptadores.AudioAdapter;
import com.example.pablo.pruebasauthproyecto.Adaptadores.AudioAdapterUsuario;
import com.example.pablo.pruebasauthproyecto.Adaptadores.VideoAdapter;
import com.example.pablo.pruebasauthproyecto.Modelo.CancionArtistaHearTthis;
import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
import com.example.pablo.pruebasauthproyecto.Reproductores.AudioPlayer;
import com.example.pablo.pruebasauthproyecto.Reproductores.AudioPlayerUsuario;
import com.example.pablo.pruebasauthproyecto.Utiles.HttpHandler;
import com.example.pablo.pruebasauthproyecto.Utiles.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AudioProyectos extends Fragment implements AdapterView.OnItemClickListener {

    private Proyecto p;
    private ArrayList<Recursos> recursos;
    private ArrayList<Recursos> audiosUsuario = new ArrayList<>();
    private static final String CANCION = "cancion";
    private static final String PARAMETROS_URL = "/?type=tracks&page=1&count=20";
    ProgressDialog progressDialog;
    ArrayList<CancionArtistaHearTthis> listaCanciones = new ArrayList();
    AudioAdapter audioAdapter;
    AudioAdapterUsuario audioAdapterUsuario;
    private ListView list;
    private ListView listaAudioUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        p = (Proyecto) getArguments().getSerializable("proyecto");
        Log.d("DEPURACIÓN", " PROYECTO AUDIO -> " + p.toString());
        Log.d("DEPURACIÓN", "AUDIO");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_proyectos, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (ListView)getView().findViewById(R.id.lv_audio);
        listaAudioUsuarios = (ListView)getView().findViewById(R.id.lv_audio_usuario);

        audiosUsuario.clear();
        listaCanciones.clear();

        recursos = p.getRecursos();

        for (Recursos r : recursos) {
            Log.d("DEPURACION", "TIPO RECURSO -> " + r.getTipo());
            if (r.getTipo() == 5) {
                Log.d("DEPURACIÓN", "URL HEARTHIS.AT -> " + r.getRuta());
                new GetInfoArtista(r.getRuta() + PARAMETROS_URL).execute();
            } else if (r.getTipo() == 4) {
                Log.d("DEPURACION", "RUTA AUDIO -> " + r.getRuta());
                audiosUsuario.add(r);
            }
        }

        audioAdapterUsuario = new AudioAdapterUsuario(getContext(), audiosUsuario);
        listaAudioUsuarios.setAdapter(audioAdapterUsuario);
        Utilidades.setDynamicHeight(listaAudioUsuarios);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CancionArtistaHearTthis cancionArtistaHearTthis = (CancionArtistaHearTthis) list.getAdapter().getItem(i);
                Log.d("DEPURACION", cancionArtistaHearTthis.toString());

                Intent intent = new Intent(getContext(), AudioPlayer.class);
                intent.putExtra(CANCION, cancionArtistaHearTthis);
                startActivity(intent);
            }
        });

        listaAudioUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("DEPURACION", audiosUsuario.get(i).getRuta());
                Intent intent = new Intent(getContext(), AudioPlayerUsuario.class);
                intent.putExtra(CANCION, audiosUsuario.get(i).getRuta());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("DEPURACION", "CLICK -> " + String.valueOf(i));
    }

    private class GetInfoArtista extends AsyncTask<String, Void, ArrayAdapter<CancionArtistaHearTthis>> {

        private String url;

        public GetInfoArtista(String urlArtista) {
            this.url = urlArtista;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Procesando Recursos...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected ArrayAdapter<CancionArtistaHearTthis> doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.conexionHttp(url);

            Log.d("DEPURACION", "JSON -> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray canciones = new JSONArray(jsonStr);

                    String genero = "";
                    String title = "";
                    String thumb = "";
                    String stream_url = "";
                    String fecha = "";

                    for (int i = 0; i < canciones.length(); i++) {
                        JSONObject object = canciones.getJSONObject(i);

                        genero = object.getString("genre");
                        title = object.getString("title");
                        thumb = object.getString("thumb");
                        stream_url = object.getString("stream_url");
                        fecha = object.getString("release_date");

                        CancionArtistaHearTthis cancionArtistaHearTthis = new CancionArtistaHearTthis();
                        cancionArtistaHearTthis.setStreamUrl(stream_url);
                        cancionArtistaHearTthis.setThumgImg(thumb);
                        cancionArtistaHearTthis.setTitulo(title);
                        cancionArtistaHearTthis.setGenero(genero);
                        cancionArtistaHearTthis.setFecha(fecha);

                        Log.d("DEPURACIÓN", cancionArtistaHearTthis.toString());

                        listaCanciones.add(cancionArtistaHearTthis);

                        audioAdapter = new AudioAdapter(getContext(), listaCanciones);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return audioAdapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<CancionArtistaHearTthis> result) {
            super.onPostExecute(result);
            list.setAdapter(result);
            progressDialog.dismiss();
        }
    }
}
