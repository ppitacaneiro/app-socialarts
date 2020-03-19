package com.example.pablo.pruebasauthproyecto.Reproductores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Modelo.VideoYouTube;
import com.example.pablo.pruebasauthproyecto.R;
import com.example.pablo.pruebasauthproyecto.Utiles.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class VideoTubePlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private VideoYouTube videoYouTube;
    private TextView titVideo;
    private TextView descVideo;
    private TextView fechaVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tube);

        videoYouTube = (VideoYouTube) getIntent().getExtras().getSerializable("video");
        Log.d("DEPURACION", videoYouTube.toString());

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        titVideo = (TextView) findViewById(R.id.tit_video_youtube);
        titVideo.setText(videoYouTube.getTitulo());

        descVideo = (TextView) findViewById(R.id.desc_video_youtube);
        descVideo.setText(videoYouTube.getDescripcion());

        fechaVideo = (TextView) findViewById(R.id.fecha_video_youtube);
        fechaVideo.setText(videoYouTube.getFecha());

        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(videoYouTube.getId()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error),youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}
