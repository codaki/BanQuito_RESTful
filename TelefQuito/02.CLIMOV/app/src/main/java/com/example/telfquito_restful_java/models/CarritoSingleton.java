package com.example.telfquito_restful_java.models;

public class CarritoSingleton {
    private static Carrito carritoInstance;

    public static Carrito getInstance() {
        if (carritoInstance == null) {
            carritoInstance = new Carrito();
        }
        return carritoInstance;
    }

    public static void clearCarrito() {
        if (carritoInstance != null) {
            carritoInstance.vaciarCarrito();
        }
    }
}
