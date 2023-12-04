package com.example.conectamovil_diegopizarro;

public class HelperClass {
    String name, email, numero, password;

    public HelperClass(String name, String email, String numero, String password) {
        this.name = name;
        this.email = email;
        this.numero = numero;
        this.password = password;
    }

    public HelperClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
