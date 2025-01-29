package com.example.telfquito_restful_java.models;

import java.io.Serializable;

public class TelefonoCarrito implements Serializable {
    private int telefonoId;
    private int cantidad;

    public TelefonoCarrito() {}

    public TelefonoCarrito(int telefonoId, int cantidad) {
        this.telefonoId = telefonoId;
        this.cantidad = cantidad;
    }

    public int getTelefonoId() {
        return telefonoId;
    }

    public void setTelefonoId(int telefonoId) {
        this.telefonoId = telefonoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
