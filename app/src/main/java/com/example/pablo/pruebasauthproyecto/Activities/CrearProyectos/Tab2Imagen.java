package com.example.pablo.pruebasauthproyecto.Activities.CrearProyectos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pablo.pruebasauthproyecto.Modelo.Recursos;
import com.example.pablo.pruebasauthproyecto.R;
import com.example.pablo.pruebasauthproyecto.Utiles.Utilidades;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Pablo on 27/12/2017.
 */

public class Tab2Imagen extends Fragment {

    private static final int SELECCIONAR_IMAGEN = 1;
    private ImageView visorImg;
    private Button selecImg;
    private Button subirImg;
    private EditText textoImg;
    private Uri rutaArchivo;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageReference;

    private String claveProyecto;
    private int mYear, mMonth, mDay, horas, minutos, segs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.tab2imagen_fragment, container, false);
        claveProyecto = getArguments().getString("clave_proyecto");
        Log.d("DEPURACIÓN", "Fragment claveProyecto -> " + claveProyecto);
        return myview;
    }

    private void addRecurso(String res) {

        String txtImg = textoImg.getText().toString().trim();

        Recursos recursos = new Recursos(claveProyecto, txtImg, res, 1);
        DatabaseReference postsRef = myRef.child("recursos");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(recursos);
    }


    private void subirFoto() {

        String foto;

        if (rutaArchivo != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.subiendo_img));

            foto = "imagenes/" + Utilidades.generarNombreRecurso(claveProyecto) + ".jpg";

            StorageReference fotosRef = storageReference.child(foto);

            fotosRef.putFile(rutaArchivo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String urlFoto = downloadUrl.toString();
                            Log.d("DEPURACIÓN", "urlFoto -> " + urlFoto);
                            Toast.makeText(getContext(), R.string.imagen_subida_ok, Toast.LENGTH_SHORT).show();
                            addRecurso(urlFoto);
                            progressDialog.dismiss();
                            visorImg.setImageResource(R.drawable.camera);
                            textoImg.setText("");
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
                    progressDialog.setMessage(getString(R.string.subiendo_imagen));
                    progressDialog.show();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.error_upload_file, Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirExploradorArchivos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selecciona_imagen)), SELECCIONAR_IMAGEN);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference();

        // referencia a la bbdd
        this.database = FirebaseDatabase.getInstance();
        this.myRef = this.database.getReference();

        // iniciamos views
        visorImg = (ImageView) getView().findViewById(R.id.visorImg);
        selecImg = (Button) getView().findViewById(R.id.selecImg);
        subirImg = (Button) getView().findViewById(R.id.subirImg);
        textoImg = (EditText) getView().findViewById(R.id.textFoto);

        selecImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirExploradorArchivos();
            }
        });

        subirImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirFoto();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == SELECCIONAR_IMAGEN && resultCode == RESULT_OK && data != null && data.getData() != null) {
                rutaArchivo = data.getData();
                Log.d("DEPURACION", "URI -> " + rutaArchivo.toString());
                visorImg.setImageURI(rutaArchivo);
            }
    }
}
