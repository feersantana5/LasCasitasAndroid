package com.fernando.beaconPem;

public class Menu {

    String Nombre, PrimerPlato, SegundoPlato, Postre, Imagen;
    Double Precio;

    public Menu(String nombre, String primerPlato, String segundoPlato, String postre, String imagen, Double precio) {
        Nombre = nombre;
        PrimerPlato = primerPlato;
        SegundoPlato = segundoPlato;
        Postre = postre;
        Imagen = imagen;
        Precio = precio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPrimerPlato() {
        return PrimerPlato;
    }

    public void setPrimerPlato(String primerPlato) {
        PrimerPlato = primerPlato;
    }

    public String getSegundoPlato() {
        return SegundoPlato;
    }

    public void setSegundoPlato(String segundoPlato) {
        SegundoPlato = segundoPlato;
    }

    public String getPostre() {
        return Postre;
    }

    public void setPostre(String postre) {
        Postre = postre;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }

    public Menu(){}

}
