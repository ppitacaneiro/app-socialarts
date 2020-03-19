package com.example.pablo.pruebasauthproyecto.Modelo;

import java.io.Serializable;

/**
 * Created by Pablo on 22/03/2018.
 */

public class CancionArtistaHearTthis implements Serializable {

    private String titulo;
    private String genero;
    private String thumgImg;
    private String streamUrl;
    private String fecha;

    public CancionArtistaHearTthis() {
    }

    public CancionArtistaHearTthis(String titulo, String genero, String thumgImg, String streamUrl, String fecha) {
        this.titulo = titulo;
        this.genero = genero;
        this.thumgImg = thumgImg;
        this.streamUrl = streamUrl;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getThumgImg() {
        return thumgImg;
    }

    public void setThumgImg(String thumgImg) {
        this.thumgImg = thumgImg;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "CancionArtistaHearTthis{" +
                "titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", thumgImg='" + thumgImg + '\'' +
                ", streamUrl='" + streamUrl + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
