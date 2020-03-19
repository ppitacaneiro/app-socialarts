package com.example.pablo.pruebasauthproyecto.Reproductores;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.pablo.pruebasauthproyecto.R;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    private final static String VIDEO = "video";
    private String urlVideo;
    private VideoView videoView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        urlVideo = getIntent().getStringExtra("video");
        videoView = (VideoView) findViewById(R.id.myVideo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buffering Video...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            Uri videoUri = Uri.parse(urlVideo);
            videoView.setVideoURI(videoUri);

            MediaController vidControl = new MediaController(this);
            vidControl.setAnchorView(videoView);
            videoView.setMediaController(vidControl);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("DEPURACION", e.getMessage());
        }

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                videoView.start();
            }
        });
    }
}
