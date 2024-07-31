package com.flashcards_8.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;


import androidx.annotation.Nullable;

import com.flashcards_8.Utilidades.Utilidades;


public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_DOCENTE);
        db.execSQL(Utilidades.CREAR_TABLA_ALUMNOS);
        db.execSQL(Utilidades.CREAR_TABLA_PALABRAS);
        db.execSQL(Utilidades.CREAR_TABLA_PALABRAS2);
        db.execSQL(Utilidades.CREAR_TABLA_PALABRAS3);
        db.execSQL(Utilidades.CREAR_TABLA_PALABRASESP);
        db.execSQL(Utilidades.CREAR_TABLA_REGISTRO);
        db.execSQL(Utilidades.CREAR_TABLA_SESION_NINO);
        db.execSQL(Utilidades.CREAR_TABLA_CONTADOR_PALABRAS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLE_DOCENTE);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLE_ALUMNOS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLE_PALABRAS_NIVEL1);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLE_PALABRAS_NIVEL2);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLE_PALABRAS_NIVEL3);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLE_PALABRAS_NIVELESP);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLE_REGISTRO);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLE_SESION_NINO);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLE_CONTADOR_PALABRAS);
        onCreate(db);

    }

    // En tu método para insertar datos en la tabla nivel1 dentro de DbHelper
    public void insertarDatosNivel1(String palabra, byte[] imagen, byte[] audio) {
        // Obtiene una instancia de SQLiteDatabase
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un objeto ContentValues
        ContentValues values = new ContentValues();

        // Agrega los valores que deseas insertar
        values.put(Utilidades.CAMPO_PALABRA, palabra);
        values.put(Utilidades.CAMPO_IMAGEN, imagen);
        values.put(Utilidades.CAMPO_AUDIO, audio);

        // Inserta los datos en la tabla nivel1
        db.insert(Utilidades.TABLE_PALABRAS_NIVEL1, null, values);

        // Cierra la conexión con la base de datos
        db.close();
    }

    public void insertarDatosNivel2(String palabra, byte[] imagen, byte[] audio) {
        // Obtiene una instancia de SQLiteDatabase
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un objeto ContentValues
        ContentValues values = new ContentValues();

        // Agrega los valores que deseas insertar
        values.put(Utilidades.CAMPO_PALABRA, palabra);
        values.put(Utilidades.CAMPO_IMAGEN, imagen);
        values.put(Utilidades.CAMPO_AUDIO, audio);

        // Inserta los datos en la tabla nivel2
        db.insert(Utilidades.TABLE_PALABRAS_NIVEL2, null, values); // Corregido a NIVEL2
        db.close();
    }

    public void insertarDatosNivel3(String palabra, byte[] imagen, byte[] audio) {
        // Obtiene una instancia de SQLiteDatabase
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un objeto ContentValues
        ContentValues values = new ContentValues();

        // Agrega los valores que deseas insertar
        values.put(Utilidades.CAMPO_PALABRA, palabra);
        values.put(Utilidades.CAMPO_IMAGEN, imagen);
        values.put(Utilidades.CAMPO_AUDIO, audio);

        // Inserta los datos en la tabla nivel3
        db.insert(Utilidades.TABLE_PALABRAS_NIVEL3, null, values); // Corregido a NIVEL3
        db.close();
    }

}



