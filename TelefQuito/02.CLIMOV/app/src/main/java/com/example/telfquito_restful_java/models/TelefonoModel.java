package com.example.telfquito_restful_java.models;

import java.io.Serializable;

public class TelefonoModel implements Serializable {
    private int codTelefono;
    private int disponible;
    private String marca;
    private String nombre;
    private String precio;
    private String imgUrl;

    // Getters and Setters
    public int getCodTelefono() {
        return codTelefono;
    }

    public void setCodTelefono(int codTelefono) {
        this.codTelefono = codTelefono;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "TelefonoModel{" +
                "codTelefono=" + codTelefono +
                ", disponible=" + disponible +
                ", marca='" + marca + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio='" + precio + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
