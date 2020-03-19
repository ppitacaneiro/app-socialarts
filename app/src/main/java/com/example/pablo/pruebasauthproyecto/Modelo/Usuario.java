package com.example.pablo.pruebasauthproyecto.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pablo on 22/12/2017.
 */

public class Usuario implements Serializable {

    private String email;
    private String nombre;
    private String password;
    private String fecha;
    private String sexo;

    public Usuario () {
    }

    public Usuario(String email, String nombre, String password, String fecha, String sexo) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.fecha = fecha;
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", fecha='" + fecha + '\'' +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}
