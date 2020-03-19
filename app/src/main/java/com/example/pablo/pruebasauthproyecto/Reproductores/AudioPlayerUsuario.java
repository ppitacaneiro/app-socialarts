package com.example.pablo.pruebasauthproyecto.Reproductores;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.pruebasauthproyecto.Modelo.CancionArtistaHearTthis;
import com.example.pablo.pruebasauthproyecto.R;
import com.squareup.picasso.Picasso;

public class AudioPlayerUsuario extends AppCompatActivity {

    private String url;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialogAudio;
    private boolean inicio = true;
    private static final String CANCION = "cancion";

    private ImageButton btn;
    private TextView textViewTitulo;
    private TextView textViewGenero;
    private TextView textViewFecha;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();

        url = intent.getExtras().getString("cancion");

        Log.d("DEPURACIÓN", "cancion ->" + url);

        btn = (ImageButton) findViewById(R.id.play_song);
        textViewFecha = (TextView) findViewById(R.id.tv_text_fecha);
        textViewGenero = (TextView) findViewById(R.id.tv_text_genero);
        textViewTitulo = (TextView) findViewById(R.id.tv_text_titulo);
        imageView = (ImageView) findViewById(R.id.imageView_cancion_big);

        imageView.setImageResource(R.drawable.music);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialogAudio = new ProgressDialog(this);

        gestionEventos();
    }

    private void gestionEventos() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playPause) {
                    btn.setImageResource(R.drawable.playaudio);
                    if (inicio) {
                        new Player().execute(url);
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }
                    playPause = true;
                } else {
                    btn.setImageResource(R.drawable.pauseaudio);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause = false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean audioPreparado = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        inicio = true;
                        btn.setImageResource(R.drawable.playaudio);
                        playPause = false;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                audioPreparado = true;

            } catch (Exception e) {
                Log.d("DEPURACION", e.getMessage());
                audioPreparado = false;
            }

            return audioPreparado;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialogAudio.isShowing()) {
                progressDialogAudio.cancel();
            }

            mediaPlayer.start();
            inicio = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialogAudio.setMessage("Buffering...");
            progressDialogAudio.show();
        }
    }
}
