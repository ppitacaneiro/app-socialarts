package com.example.pablo.pruebasauthproyecto.Modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pablo on 26/12/2017.
 */

public class Proyecto implements Serializable {
    private String idAutor;
    private String nombre;
    private String descripcion;
    private String fecha;
    private String genero;
    private String autor;
    private String votos;

    private ArrayList<Recursos> recursos = new ArrayList<>();
    private String keyProyecto;

    public Proyecto() {
    }

    public Proyecto(String autor,String descripcion,String fecha,String genero,String idAutor,String nombre,String votos) {
        this.autor = autor;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.genero = genero;
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.votos = votos;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(String idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public ArrayList<Recursos> getRecursos() {
        return recursos;
    }

    public void addRecurso(Recursos r) {
        recursos.add(r);
    }

    public String getVotos() {
        return votos;
    }

    public void setVotos(String votos) {
        this.votos = votos;
    }

    public String getKeyProyecto() {
        return keyProyecto;
    }

    public void setKeyProyecto(String keyProyecto) {
        this.keyProyecto = keyProyecto;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "idAutor='" + idAutor + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", genero='" + genero + '\'' +
                ", autor='" + autor + '\'' +
                ", votos='" + votos + '\'' +
                ", recursos=" + recursos +
                ", keyProyecto='" + keyProyecto + '\'' +
                '}';
    }
}
