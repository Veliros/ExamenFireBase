package com.example.marta.fbandroid;

/**
 * Objeto: Producto
 */
public class Producto {
    private String nombre;
    private String descripcion;
    private String categoria;
    private String precio;
    private String usuario;

    /**
     * CONSTRUCTOR
     *
     * @param nombre
     * @param descripcion
     * @param categoria
     * @param precio
     * @param usuario
     */
    public Producto(String nombre, String descripcion, String categoria, String precio, String usuario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.usuario = usuario;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return  "Nombre: " + nombre +
                ", descripcion: " + descripcion  +
                ", precio: " + precio ;
    }

}
