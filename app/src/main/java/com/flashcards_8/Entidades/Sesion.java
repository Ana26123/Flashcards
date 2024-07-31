package com.flashcards_8.Entidades;

import java.io.Serializable;

public class Sesion implements Serializable {
    private Integer id;
    private Integer idA;
    private String nombreM;
    private String nombreA;
    private String dificultad;
    private String tipoPrueba;
    private String fechaI;
    private String fechaF;
    private Integer aciertos;
    private Integer fallos;
    private String calificacion;

    public Sesion(Integer id, Integer idA, String nombreM, String nombreA, String dificultad, String tipoPrueba, String fechaI, String fechaF, Integer aciertos, Integer fallos, String calificacion) {
        this.id = id;
        this.idA = idA;
        this.nombreM = nombreM;
        this.nombreA = nombreA;
        this.dificultad = dificultad;
        this.tipoPrueba = tipoPrueba;
        this.fechaI = fechaI;
        this.fechaF = fechaF;
        this.aciertos = aciertos;
        this.fallos = fallos;
        this.calificacion = calificacion;
    }

    public Sesion() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdA() {
        return idA;
    }

    public void setIdA(Integer idA) {
        this.idA = idA;
    }

    public String getNombreM() {
        return nombreM;
    }

    public void setNombreM(String nombreM) {
        this.nombreM = nombreM;
    }

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(String tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    public String getFechaI() {
        return fechaI;
    }

    public void setFechaI(String fechaI) {
        this.fechaI = fechaI;
    }

    public String getFechaF() {
        return fechaF;
    }

    public void setFechaF(String fechaF) {
        this.fechaF = fechaF;
    }

    public Integer getAciertos() {
        return aciertos;
    }

    public void setAciertos(Integer aciertos) {
        this.aciertos = aciertos;
    }

    public Integer getFallos() {
        return fallos;
    }

    public void setFallos(Integer fallos) {
        this.fallos = fallos;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }
}
