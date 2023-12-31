package com.example.conectamovil_diegopizarro.Modelo;

public class Usuario {
    private String name;
    private String email;
    private String urlFoto;

    public Usuario(){
    }

    public Usuario(String name, String email, String urlFoto) {
        this.name = name;
        this.urlFoto = urlFoto;
        this.email = email;
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

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
