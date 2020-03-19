package com.example.pablo.pruebasauthproyecto.Modelo;

import java.io.Serializable;

/**
 * Created by Pablo on 27/12/2017.
 */

public class Recursos implements Serializable {

    private String idProyecto;
    private String nombre;
    private String ruta;
    private int tipo;

    public Recursos() {
    }

    public Recursos(String idProyecto, String nombre, String ruta, int tipo) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.ruta = ruta;
        this.tipo = tipo;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return "Recursos{" +
                "idProyecto='" + idProyecto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ruta='" + ruta + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
