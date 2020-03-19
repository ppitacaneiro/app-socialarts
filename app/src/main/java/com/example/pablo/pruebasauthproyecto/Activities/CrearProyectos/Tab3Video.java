package com.example.pablo.pruebasauthproyecto.Activities.CrearProyectos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
import com.example.pablo.pruebasauthproyecto.Utiles.Config;
import com.example.pablo.pruebasauthproyecto.Utiles.HttpHandler;
import com.example.pablo.pruebasauthproyecto.Utiles.Utilidades;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Pablo on 27/12/2017.
 */

public class Tab3Video extends Fragment {

    private String claveProyecto;
    private static final int REQUEST_CODE = 101;
    private Button grabarVideo;
    private Button subirVideo;
    private Uri rutaArchivo;
    private VideoView videoView;
    private EditText textoVideo;
    private MediaController mediaController;

    private EditText urlYoutube;
    private Button buttonYouTube;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.tab3video_fragment, container, false);
        claveProyecto = getArguments().getString("clave_proyecto");
        Log.d("DEPURACIÓN", "Fragment claveProyecto -> " + claveProyecto);
        return myview;
    }

    private boolean validarUrlYouTube(String urlYouTube) {
        boolean urlYouTubeOk = false;

        if (urlYouTube.isEmpty()) {
            Toast.makeText(getContext(), R.string.campo_no_vacio, Toast.LENGTH_LONG).show();
            urlYouTubeOk = false;
        } else if (!URLUtil.isValidUrl(urlYouTube)) {
            Toast.makeText(getContext(), R.string.url_no_valida, Toast.LENGTH_LONG).show();
            urlYouTubeOk = false;
        } else if (!urlYouTube.contains(Config.URL_YOUTUBE)) {
            Toast.makeText(getContext(), R.string.url_no_valida, Toast.LENGTH_LONG).show();
            Log.d("DEPURACIÓN","URL_YOUTUBE : " + Config.URL_YOUTUBE);
            Log.d("DEPURACIÓN","URL : " + urlYouTube);
            urlYouTubeOk = false;
        } else {

            String idVideo = urlYouTube.substring(Config.URL_YOUTUBE.length(), urlYouTube.length());
            Log.d("DEPURACION", idVideo);

            boolean videoEncontrado = false;
            GetVideo getVideo = new GetVideo(idVideo);
            AsyncTask<String, Void, Boolean> asyncTask = getVideo.execute();

            try {
                videoEncontrado = asyncTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (videoEncontrado) {
                urlYouTubeOk = true;
            } else {
                Toast.makeText(getContext(), R.string.no_encontrado_videoyoutube, Toast.LENGTH_SHORT).show();
                urlYouTubeOk = false;
            }
        }

        return urlYouTubeOk;
    }

    private void grabar() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void addRecurso(String res, int tipo) {
        Recursos recursos = null;

        switch (tipo) {
            // video grabado por el usuario
            case 2 :
                String txtVideo = textoVideo.getText().toString().trim();
                recursos = new Recursos(claveProyecto, txtVideo, res, tipo);
                break;
            // video de youtube
            case 3 :
                recursos = new Recursos(claveProyecto, "", res, tipo);
                break;
        }

        DatabaseReference postsRef = myRef.child("recursos");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(recursos);
    }

    private void subirVideo() {

        String video;

        if (rutaArchivo != null) {

            Log.d("DEPURACION", rutaArchivo.toString());

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.uploading_video));

            video = "videos/" + Utilidades.generarNombreRecurso(claveProyecto) + ".3gp";
            Log.d("DEPURACION", "VIDEO - > " + video);

            StorageReference videosRef = storageReference.child(video);

            videosRef.putFile(rutaArchivo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String urlVideo = downloadUrl.toString();
                    Log.d("DEPURACIÓN", "urlVideo -> " + urlVideo);
                    Toast.makeText(getContext(), R.string.video_success, Toast.LENGTH_SHORT).show();
                    addRecurso(urlVideo, 2);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMessage(getString(R.string.uploading_video));
                    progressDialog.show();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.error_upload_file, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference();

        // referencia a la bbdd
        this.database = FirebaseDatabase.getInstance();
        this.myRef = this.database.getReference();

        grabarVideo = (Button) getView().findViewById(R.id.grabarVideo);
        videoView = (VideoView) getView().findViewById(R.id.videoView);
        subirVideo = (Button) getView().findViewById(R.id.subirVideo);
        textoVideo = (EditText) getView().findViewById(R.id.textVideo);

        urlYoutube = (EditText) getView().findViewById(R.id.url_youtube);
        buttonYouTube = (Button) getView().findViewById(R.id.okYoutube);

        buttonYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarUrlYouTube(urlYoutube.getText().toString().trim())) {
                    Log.d("DEPURACION", "GUARDO URL");
                    addRecurso(urlYoutube.getText().toString().trim(), 3);
                    Toast.makeText(getContext(), R.string.video_yotube_ok, Toast.LENGTH_SHORT).show();
                    urlYoutube.setText("");
                }
            }
        });

        subirVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirVideo();
            }
        });

        grabarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabar();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            rutaArchivo = data.getData();

            videoView.setVideoURI(data.getData());
            mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }

    private class GetVideo extends AsyncTask<String, Void, Boolean> {

        private String id;

        public GetVideo(String idVideo) {
            this.id = idVideo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.validando_video_YouTube));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.conexionHttp(Config.URL_API_YOUTUBE_1 + id + Config.URL_API_YOUTUBE_2);
            Log.d("DEPURACION", "URL API YOUTUBE - > " + Config.URL_API_YOUTUBE_1 + id + Config.URL_API_YOUTUBE_2);

            Log.d("DEPURACION", "JSON -> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject obj = new JSONObject(jsonStr);
                    JSONArray arr = obj.getJSONArray("items");
                    for (int i = 0; i < arr.length(); i++) {
                        String idBuscar = arr.getJSONObject(i).getString("id");
                        Log.d("DEPURACION", idBuscar);

                        if (id.equalsIgnoreCase(idBuscar)) {
                            return true;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }
}


