package com.example.conectamovil_diegopizarro.Modelo;

public class Contacto {
    private String nombre;
    private String numero;
    private String email;

    public Contacto() {
    }

    public Contacto(String nombre, String numero, String email) {
        this.nombre = nombre;
        this.numero = numero;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "nombre='" + nombre + '\'' +
                ", numero='" + numero + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
