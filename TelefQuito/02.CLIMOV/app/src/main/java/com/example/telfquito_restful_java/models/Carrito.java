package com.example.telfquito_restful_java.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrito implements Serializable {
    private List<TelefonoCarrito> telefonos;

    public Carrito() {
        this.telefonos = new ArrayList<>();
    }

    public List<TelefonoCarrito> getTelefonos() {
        if (telefonos == null) {
            telefonos = new ArrayList<>();
        }
        return telefonos;
    }

    public void setTelefonos(List<TelefonoCarrito> telefonos) {
        this.telefonos = telefonos;
    }

    public void vaciarCarrito() {
        this.telefonos.clear();
    }

    public void agregarTelefono(TelefonoCarrito telefono) {
        this.telefonos.add(telefono);
    }
}
