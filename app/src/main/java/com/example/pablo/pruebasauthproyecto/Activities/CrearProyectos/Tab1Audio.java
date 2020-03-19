package com.example.pablo.pruebasauthproyecto.Activities.CrearProyectos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Pablo on 27/12/2017.
 */

public class Tab1Audio extends Fragment {

    private static final int SELECCIONAR_AUDIO = 1;
    private Button selecAudio;
    private Button subirAudioBot;
    private EditText textoAudio;
    private TextView nombreFileAudio;
    private Uri rutaArchivo;

    private String claveProyecto;
    private String strUsuario;
    private String cuentaUsuarioHearthis;
    private ProgressDialog progressDialog;
    private EditText urlHearthis;
    private Button addUrlHearthis;
    private StorageReference storageReference;
    private ProgressBar progressBarAudio;
    private static final String URL_HEART_THIS = "https://hearthis.at/";
    private static final String URL_HEART_THIS_API = "https://api-v2.hearthis.at/";

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.tab1audio_fragment, container, false);
        claveProyecto = getArguments().getString("clave_proyecto");
        Log.d("DEPURACIÓN", "Fragment claveProyecto -> " + claveProyecto);
        return myview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        urlHearthis = (EditText) getView().findViewById(R.id.url_hearthis);
        addUrlHearthis = (Button) getView().findViewById(R.id.button_hearthis);

        // referencia al firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // iniciamos views
        selecAudio = (Button) getView().findViewById(R.id.selecSong);
        subirAudioBot = (Button) getView().findViewById(R.id.subirAudio);
        textoAudio = (EditText) getView().findViewById(R.id.textAudio);
        nombreFileAudio = (TextView) getView().findViewById(R.id.nombreArchivoAudio);

        this.database = FirebaseDatabase.getInstance();
        this.myRef = this.database.getReference();

        subirAudioBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirAudio();
            }
        });

        selecAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirExploradorArchivos();
            }
        });

        addUrlHearthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarUrlHeartThis()) {
                    Log.d("DEPURACION", "CUENTA USUARIO OK!");
                    Log.d("DEPURACION", strUsuario);
                    cuentaUsuarioHearthis = URL_HEART_THIS_API + strUsuario;
                    addRecurso(cuentaUsuarioHearthis, 5);
                    Toast.makeText(getContext(), R.string.resurso_añadido_ok, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addRecurso(String res, int tipo) {
        Recursos recursos = null;

        switch (tipo) {
            // 4 para audio seleccionado por el usuario
            case 4 :
                String txtVideo = textoAudio.getText().toString().trim();
                recursos = new Recursos(claveProyecto, txtVideo, res, tipo);
                break;
            // 5 para URL API heartthis.at
            case 5 :
                recursos = new Recursos(claveProyecto, "", res, tipo);
                break;
        }
        DatabaseReference postsRef = myRef.child("recursos");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(recursos);
    }

    private void abrirExploradorArchivos() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_chooser)), SELECCIONAR_AUDIO);
    }

    private boolean validarUrlHeartThis() {

        if (urlHearthis.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.url_vacia, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!verificarUrl(urlHearthis.getText().toString().trim())) {
            Toast.makeText(getContext(), R.string.url_no_valida, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!verificarUsuario(urlHearthis.getText().toString().trim())) {
            Toast.makeText(getContext(), R.string.usuario_no_valido, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificarUrl (String urlHeartUser) {
        boolean urlValida = false;

        if (urlHeartUser.length() <  URL_HEART_THIS.length()) {
            urlValida = false;
        } else {
            String subUrl = urlHeartUser.substring(0,URL_HEART_THIS.length());
            if (subUrl.equalsIgnoreCase(URL_HEART_THIS)) {
                urlValida = true;
            }
        }
        return urlValida;
    }

    private boolean verificarUsuario (String urlHeartUser) {
        boolean usuarioOk = false;

        if (urlHeartUser.length() > URL_HEART_THIS.length()) {

            String [] usuario = urlHeartUser.split(URL_HEART_THIS);
            strUsuario = usuario[1];
            Log.d("DEPURACION", "USUARIO - >" + strUsuario);

            GetArtista getArtista = new GetArtista(strUsuario);
            AsyncTask<String, Void, Boolean> asyncTask = getArtista.execute();

            boolean userExiste = false;
            try {
                userExiste = asyncTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (userExiste) {
                Log.d("DEPURACION", "USUARIO EXISTE!");
                usuarioOk = true;
            } else {
                Toast.makeText(getContext(), getString(R.string.no_encuentro_usuario) + strUsuario, Toast.LENGTH_SHORT).show();
                return false;
            }

        } else {
            usuarioOk = false;
        }

        return usuarioOk;
    }

    private class GetArtista extends AsyncTask<String, Void, Boolean> {

        private String user;

        public GetArtista(String usuario) {
            this.user = usuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.validando_usuario));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.conexionHttp(URL_HEART_THIS_API + user);

            Log.d("DEPURACION", "JSON -> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject artista = new JSONObject(jsonStr);
                    String permanetlink = artista.getString("permalink");
                    Log.d("DEPURACION", permanetlink);

                    if (permanetlink.equalsIgnoreCase(user)) {
                        return true;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECCIONAR_AUDIO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String nombreArchivo = data.getData().getPath();
            rutaArchivo = data.getData();
            nombreFileAudio.setText(getFileName(rutaArchivo));
            Log.d("DEPURACION", "URI -> " + rutaArchivo.toString());
            Log.d("DEPURACION", "nombreArchivo -> " + getFileName(rutaArchivo));
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void subirAudio() {

        String audio;

        if (rutaArchivo != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.subiendo_audio));

            audio = "audio/" + Utilidades.generarNombreRecurso(claveProyecto) + ".wav";

            StorageReference audioRef = storageReference.child(audio);

            audioRef.putFile(rutaArchivo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String urlAudio = downloadUrl.toString();
                            Log.d("DEPURACIÓN", "urlAudio -> " + urlAudio);
                            Toast.makeText(getContext(), R.string.audio_upload_ok, Toast.LENGTH_SHORT).show();
                            addRecurso(urlAudio, 4);
                            progressDialog.dismiss();
                            nombreFileAudio.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMessage(getString(R.string.subiendo_audio));
                    progressDialog.show();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.error_upload_file, Toast.LENGTH_SHORT).show();
        }
    }
}

