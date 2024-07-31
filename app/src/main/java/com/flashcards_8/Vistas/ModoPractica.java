package com.flashcards_8.Vistas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.Entidades.Palabra;
import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;

public class ModoPractica extends AppCompatActivity {

    // Declaración de variables
    TextView txtpalabra;
    DbHelper conn;

    ArrayList<Palabra> listaPalabras;
    HashSet<Integer> palabrasMostradas;
    public boolean continuar = true, pregunta = false;
    String musica, tipoPrueba;
    public MediaPlayer player;
    public String nivel;
    public int idMaestro, idAlumno, tiempo;
    Handler handler;
    HashMap<String, Integer> contadores = new HashMap<>(); //

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_practica);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inicialización de vistas y base de datos
        txtpalabra = findViewById(R.id.txtPalabraLibre);
        conn = new DbHelper(getApplicationContext(), Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);

        handler = new Handler();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idMaestro = extras.getInt("idMaestro");
            idAlumno = extras.getInt("idAlumno");
            nivel = extras.getString("nivel");
            tipoPrueba = extras.getString("prueba");
            tiempo = extras.getInt("tiempo");
        } else {
            Log.d("Debug", "Intent was null");
        }

        palabrasMostradas = new HashSet<>();
        obtenerPalabras(nivel.trim()); // Obtener todas las palabras del nivel actual
        Collections.shuffle(listaPalabras); // Aleatorizar la lista de palabras
        listaPalabras = new ArrayList<>(listaPalabras.subList(0, Math.min(10, listaPalabras.size()))); // Limitar a 10 palabras
        mostrarPalabra(); // Mostrar la primera palabra
    }

    // Método para obtener todas las palabras de la base de datos para un nivel específico
    private void obtenerPalabras(String nivel) {
        SQLiteDatabase db = conn.getReadableDatabase();
        Palabra palabra;
        listaPalabras = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID + ", " + Utilidades.CAMPO_PALABRA + ", " + Utilidades.CAMPO_AUDIO + " FROM " + nivel, null);

        while (cursor.moveToNext()) {
            palabra = new Palabra();
            palabra.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_ID)));
            palabra.setPalabra(cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_PALABRA)));
            byte[] audioBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_AUDIO));
            palabra.setAudio(audioBlob != null ? new String(audioBlob) : null);
            listaPalabras.add(palabra);
        }
        cursor.close();
    }

    // Método para mostrar la palabra actual
    private void mostrarPalabra() {
        if (continuar && !listaPalabras.isEmpty()) {
            Palabra palabra = listaPalabras.remove(0); // Obtener y eliminar la primera palabra de la lista
            // Asegurarse de que la palabra no se haya mostrado antes
            while (palabrasMostradas.contains(palabra.getId())) {
                if (listaPalabras.isEmpty()) {
                    finalizarNivel();
                    return;
                }
                palabra = listaPalabras.remove(0);
            }
            palabrasMostradas.add(palabra.getId()); // Añadir la palabra al conjunto de palabras mostradas
            txtpalabra.setText(palabra.getPalabra().trim());
            musica = palabra.getAudio() != null ? palabra.getAudio().trim() : null;
            if (musica != null) {
                reproducirAudio(musica);
            }
            actualizarContador(palabra);

            handler.postDelayed(this::mostrarPalabra, tiempo); // Programar la siguiente palabra
        } else {
            finalizarNivel();
        }
    }

    // Método para finalizar el nivel
    private void finalizarNivel() {
        Toast.makeText(this, "Nivel completado", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ModoPractica.this, Menu.class);
        startActivity(intent);
        finish();
    }

    // Método para actualizar el contador de respuestas
    private void actualizarContador(Palabra palabra) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String query = "SELECT * FROM " + Utilidades.TABLE_CONTADOR_PALABRAS +
                " WHERE " + Utilidades.CAMPO_ID_ALUMNO + " = " + idAlumno +
                " AND " + Utilidades.CAMPO_ID_PALABRA + " = " + palabra.getId() +
                " AND " + Utilidades.CAMPO_DIFICULTAD + " = '" + nivel + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int contadorIndex = cursor.getColumnIndex(Utilidades.CAMPO_CONTADOR);
            if (contadorIndex != -1) {
                int contadorActual = cursor.getInt(contadorIndex);
                ContentValues values = new ContentValues();
                values.put(Utilidades.CAMPO_CONTADOR, contadorActual + 1);
                db.update(Utilidades.TABLE_CONTADOR_PALABRAS, values, Utilidades.CAMPO_ID_ALUMNO + " = ? AND " + Utilidades.CAMPO_ID_PALABRA + " = ? AND " + Utilidades.CAMPO_DIFICULTAD + " = ?", new String[]{String.valueOf(idAlumno), String.valueOf(palabra.getId()), nivel});
            } else {
                Log.e("actualizarContador", "La columna " + Utilidades.CAMPO_CONTADOR + " no existe.");
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_ID_ALUMNO, idAlumno);
            values.put(Utilidades.CAMPO_ID_PALABRA, palabra.getId());
            values.put(Utilidades.CAMPO_CONTADOR, 1);
            values.put(Utilidades.CAMPO_DIFICULTAD, nivel);
            db.insert(Utilidades.TABLE_CONTADOR_PALABRAS, null, values);
        }
        cursor.close();
    }

    // Método para detener la reproducción de audio
    private void detenerReproduccion() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    // Método que maneja los clics en la interfaz de usuario
    public void onclick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btnVolverModoLibre) {
            detenerReproduccion();
            continuar = false;
            pregunta = true;
            Intent intent = new Intent(ModoPractica.this, Menu.class);
            startActivity(intent);
            finish();
        } else if (viewId == R.id.imgPreguntas) {
            reproducirAudio(musica);
        }
    }

    // Manejar el evento de presionar la tecla "back"
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            detenerReproduccion();
            continuar = false;
            pregunta = true;
            Intent intent = new Intent(ModoPractica.this, Menu.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // Método para reproducir el audio de la palabra actual
    private void reproducirAudio(String audioUri) {
        if (audioUri == null || audioUri.isEmpty()) {
            Toast.makeText(this, "URI del audio es nulo o vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        detenerReproduccion(); // Detener cualquier reproducción anterior

        player = new MediaPlayer();
        try {
            Uri uri = Uri.parse(audioUri);
            player.setDataSource(this, uri);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Toast.makeText(this, "Audio fallido", Toast.LENGTH_SHORT).show();
        }
    }
}
