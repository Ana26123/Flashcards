package com.flashcards_8.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.flashcards_8.Entidades.Palabra;
import com.flashcards_8.Utilidades.Utilidades;

import java.util.List;

public class DbManager {
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        dbHelper = new DbHelper(context, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
    }

    // Método para abrir la base de datos
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    // Método para cerrar la base de datos
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Método para insertar una palabra en la tabla nivel1
    public long insertarPalabraNivel1(String palabra, String urlImagen, String urlAudio) {
        if (existePalabra(Utilidades.TABLE_PALABRAS_NIVEL1, palabra)) {
            return -1; // Indica que la palabra ya existe
        }

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_PALABRA, palabra);
        values.put(Utilidades.CAMPO_IMAGEN, urlImagen);
        values.put(Utilidades.CAMPO_AUDIO, urlAudio);

        return db.insert(Utilidades.TABLE_PALABRAS_NIVEL1, null, values);
    }

    // Método para insertar una palabra en la tabla nivel2
    public long insertarPalabraNivel2(String palabra, String imagen, String audio) {
        if (existePalabra(Utilidades.TABLE_PALABRAS_NIVEL2, palabra)) {
            return -1; // Indica que la palabra ya existe
        }

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_PALABRA, palabra);
        values.put(Utilidades.CAMPO_IMAGEN, imagen);
        values.put(Utilidades.CAMPO_AUDIO, audio);

        return db.insert(Utilidades.TABLE_PALABRAS_NIVEL2, null, values);
    }

    // Método para insertar una palabra en la tabla nivel3
    public long insertarPalabraNivel3(String palabra, String imagen, String audio) {
        if (existePalabra(Utilidades.TABLE_PALABRAS_NIVEL3, palabra)) {
            return -1; // Indica que la palabra ya existe
        }

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_PALABRA, palabra);
        values.put(Utilidades.CAMPO_IMAGEN, imagen);
        values.put(Utilidades.CAMPO_AUDIO, audio);

        return db.insert(Utilidades.TABLE_PALABRAS_NIVEL3, null, values);
    }

    // Método para insertar múltiples palabras en la tabla nivel1
    public void insertarMultiplesPalabrasNivel1(List<Palabra> palabras) {
        for (Palabra palabra : palabras) {
            insertarPalabraNivel1(palabra.getPalabra(), palabra.getImagen(), palabra.getAudio());
        }
    }

    // Método para insertar múltiples palabras en la tabla nivel2
    public void insertarMultiplesPalabrasNivel2(List<Palabra> palabras) {
        for (Palabra palabra : palabras) {
            insertarPalabraNivel2(palabra.getPalabra(), palabra.getImagen(), palabra.getAudio());
        }
    }

    // Método para insertar múltiples palabras en la tabla nivel3
    public void insertarMultiplesPalabrasNivel3(List<Palabra> palabras) {
        for (Palabra palabra : palabras) {
            insertarPalabraNivel3(palabra.getPalabra(), palabra.getImagen(), palabra.getAudio());
        }
    }

    // Método para verificar si una palabra existe en una tabla
    public boolean existePalabra(String tabla, String palabra) {
        String[] columns = {Utilidades.CAMPO_ID};
        String selection = Utilidades.CAMPO_PALABRA + " = ?";
        String[] selectionArgs = {palabra};

        Cursor cursor = db.query(tabla, columns, selection, selectionArgs, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}




