package com.example.marta.fbandroid;

import java.util.ArrayList;

/**
 * Objeto examen
 */
public class Favs {
    private String producto, usuario;

    /**
     * Constructor
     * @param producto
     */
    public Favs(String producto, String usuario) {
        this.producto = producto;
        this.usuario = usuario;
    }

    public String getProductos() {
        return producto;
    }

    public void setProductos(String producto) {
        this.producto = producto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
