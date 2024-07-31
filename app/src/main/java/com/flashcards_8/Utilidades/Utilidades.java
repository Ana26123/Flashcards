package com.flashcards_8.Utilidades;

import android.graphics.Color;

public class Utilidades {

    // Definición de la versión de la base de datos y el nombre del archivo de la base de datos
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FlashCards.db";

    //Campos de los individuos
    public static final String TABLE_DOCENTE = "Tdocente";
    public static final String TABLE_ALUMNOS = "Talumnos";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_CONTRASEÑA = "contraseña";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_APELLIDOS = "apellidos";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_EDAD_MENTAL = "edad_mental";
    public static final String CAMPO_SEXO = "sexo";
    public static final String CAMPO_COMENTARIOS = "comentarios";

    //Campos de las tablas de las palabras
    public static final String TABLE_PALABRAS_NIVEL1 = "nivel1";
    public static final String TABLE_PALABRAS_NIVEL2 = "nivel2";
    public static final String TABLE_PALABRAS_NIVEL3 = "nivel3";
    public static final String TABLE_PALABRAS_NIVELESP = "nivelEsp";
    public static final String CAMPO_PALABRA = "palabra";
    public static final String CAMPO_IMAGEN = "imagen";
    public static final String CAMPO_AUDIO = "audio";

    // Definición de la tabla de registro de respuestas
    public static final String TABLE_REGISTRO = "Registro";
    public static final String CAMPO_ID_NINO = "id_nino";
    public static final String CAMPO_ID_PALABRA = "id_palabra";
    public static final String CAMPO_RESULTADO = "resultado";
    public static final String CAMPO_DIFICULTAD = "dificultad";
    public static final String CAMPO_FECHA = "fecha";


    // Definición de la tabla de sesiones de niños
    public static final String TABLE_SESION_NINO = "SesionNino";
    public static final String CAMPO_NOMBREM = "nombre_maestro";
    public static final String CAMPO_TIPO_PRUEBA = "tipo_prueba";
    public static final String CAMPO_FECHAI = "fecha_inicio";
    public static final String CAMPO_FECHAF = "fecha_fin";
    public static final String CAMPO_ACIERTOS = "aciertos";
    public static final String CAMPO_FALLOS = "fallos";
    public static final String CAMPO_CALIFICACION = "calificacion";

    // Campos para el contador de palabras
    public static final String TABLE_CONTADOR_PALABRAS = "contador_palabras";
    public static final String CAMPO_ID_ALUMNO = "id_alumno";
    public static final String CAMPO_CONTADOR = "contador";


    // Sentencias SQL para crear tablas
    public static final String CREAR_TABLA_DOCENTE = "CREATE TABLE IF NOT EXISTS "+TABLE_DOCENTE
            +" ( "+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE+" TEXT, "
            +CAMPO_CONTRASEÑA+" TEXT) ";

    public static final String CREAR_TABLA_ALUMNOS = "CREATE TABLE IF NOT EXISTS "+TABLE_ALUMNOS
            +" ( "+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE+" TEXT, "
            +CAMPO_APELLIDOS+" TEXT, "
            +CAMPO_EDAD+" INTEGER, "
            +CAMPO_EDAD_MENTAL+" INTEGER, "
            +CAMPO_SEXO+" TEXT, "
            +CAMPO_COMENTARIOS+" TEXT) ";

    public static final String CREAR_TABLA_PALABRAS = "CREATE TABLE IF NOT EXISTS "+TABLE_PALABRAS_NIVEL1
            +" ( "+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_PALABRA+" TEXT, "
            +CAMPO_IMAGEN+" BLOB, "
            +CAMPO_AUDIO+" BLOB) ";

    public static final String CREAR_TABLA_PALABRAS2 = "CREATE TABLE IF NOT EXISTS "+TABLE_PALABRAS_NIVEL2
            +" ( "+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_PALABRA+" TEXT, "
            +CAMPO_IMAGEN+" BLOB, "
            +CAMPO_AUDIO+" BLOB) ";

    public static final String CREAR_TABLA_PALABRAS3 = "CREATE TABLE IF NOT EXISTS "+TABLE_PALABRAS_NIVEL3
            +" ( "+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_PALABRA+" TEXT, "
            +CAMPO_IMAGEN+" BLOB, "
            +CAMPO_AUDIO+" BLOB) ";

    public static final String CREAR_TABLA_PALABRASESP = "CREATE TABLE IF NOT EXISTS "+TABLE_PALABRAS_NIVELESP
            +" ( "+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_PALABRA+" VARCHAR, "
            +CAMPO_IMAGEN+" BLOB, "
            +CAMPO_AUDIO+" BLOB) ";

    public static final String CREAR_TABLA_REGISTRO = "CREATE TABLE IF NOT EXISTS " + TABLE_REGISTRO
            + " ( " + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_ID_NINO + " INTEGER, "
            + CAMPO_ID_PALABRA + " INTEGER, "
            + CAMPO_RESULTADO + " INTEGER, "
            + CAMPO_DIFICULTAD + " TEXT, "
            + CAMPO_FECHA + " TEXT) ";

    public static final String CREAR_TABLA_SESION_NINO = "CREATE TABLE IF NOT EXISTS " + Utilidades.TABLE_SESION_NINO + " ("
            + Utilidades.CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Utilidades.CAMPO_ID_NINO + " INTEGER, "
            + Utilidades.CAMPO_NOMBREM + " TEXT, "
            + Utilidades.CAMPO_NOMBRE + " TEXT, "
            + Utilidades.CAMPO_DIFICULTAD + " TEXT, "
            + Utilidades.CAMPO_TIPO_PRUEBA + " TEXT, "
            + Utilidades.CAMPO_FECHAI + " TEXT, "
            + Utilidades.CAMPO_FECHAF + " TEXT, "
            + Utilidades.CAMPO_ACIERTOS + " INTEGER, "
            + Utilidades.CAMPO_FALLOS + " INTEGER, "
            + Utilidades.CAMPO_CALIFICACION + " TEXT)";

    public static final String CREAR_TABLA_CONTADOR_PALABRAS = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTADOR_PALABRAS
            + " (" + CAMPO_ID_ALUMNO + " INTEGER, "
            + CAMPO_ID_PALABRA + " INTEGER, "
            + CAMPO_CONTADOR + " INTEGER, "
            + CAMPO_DIFICULTAD + " TEXT, "
            + "PRIMARY KEY(" + CAMPO_ID_ALUMNO + ", " + CAMPO_ID_PALABRA + ", " + CAMPO_DIFICULTAD + ")) ";


    // Colores personalizados
    public static final int[] PERSONALIZADO = {
            Color.rgb(62, 78, 252),
            Color.rgb(218, 92, 250),
            Color.rgb(112, 124, 252),
            Color.rgb(225, 135, 248),
            Color.rgb(140, 249, 255)
    };
}
