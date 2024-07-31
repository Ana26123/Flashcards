package com.flashcards_8.Entidades;

import java.io.Serializable;

public class Palabra implements Serializable {
    private Integer id;
    private String palabra;
    private String imagen;
    private String audio;

    public Palabra(Integer id, String palabra, String imagen, String audio) {
        this.id = id;
        this.palabra = palabra;
        this.imagen = imagen;
        this.audio = audio;
    }

    public Palabra() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
