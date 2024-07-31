package com.flashcards_8.Entidades;

public class Alumno {

    private Integer id;
    private String nombre;
    private String apellido;
    private String sexo;
    private String edad;
    private String edadmental;
    private String comentarios;

    public Alumno(Integer id, String nombre, String apellido, String sexo, String edad, String edadmental, String comentarios) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.edad = edad;
        this.edadmental = edadmental;
        this.comentarios = comentarios;
    }

    public Alumno() {
        // Puedes inicializar las propiedades con valores predeterminados aquí si lo deseas
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getEdadmental() {
        return edadmental;
    }

    public void setEdadmental(String edadmental) {
        this.edadmental = edadmental;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}

