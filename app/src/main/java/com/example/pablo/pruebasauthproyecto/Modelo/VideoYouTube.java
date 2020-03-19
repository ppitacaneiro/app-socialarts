package com.example.pablo.pruebasauthproyecto.Modelo;

import java.io.Serializable;

/**
 * Created by Pablo on 28/03/2018.
 */

public class VideoYouTube implements Serializable {

    private String titulo;
    private String descripcion;
    private String fecha;
    private String thumb;
    private String id;

    public VideoYouTube() {
    }

    public VideoYouTube(String titulo, String descripcion, String fecha, String thumb, String url) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.thumb = thumb;
        this.id = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VideoYouTube{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", thumb='" + thumb + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
