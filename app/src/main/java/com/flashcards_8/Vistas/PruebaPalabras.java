package com.flashcards_8.Vistas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.Entidades.Alumno;
import com.flashcards_8.Entidades.Docente;
import com.flashcards_8.Entidades.Palabra;
import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PruebaPalabras extends AppCompatActivity {

    // Declaración de variables
    DbHelper conn;
    int idMaestro, idAlumno, aciertos = 0, fallos = 0, idSeleccionado;
    String nivel, nombreA, nombreM, FechaI, FechaF;
    int palabra1 = 1, palabra2 = 1, palabra3 = 1, palabra4 = 1, c = 1, c2 = 1;
    ArrayList<Palabra> listaPalabras;
    ImageView imagenes;
    String musica, tipoPrueba;
    TextView txtNivel;
    private MediaPlayer player = null;
    boolean seleccionado = false;
    Button B1, B2, B3, B4;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_palabras);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        B1 = findViewById(R.id.Opcion1);
        B2 = findViewById(R.id.Opcion2);
        B3 = findViewById(R.id.Opcion3);
        B4 = findViewById(R.id.Opcion4);

        txtNivel = findViewById(R.id.txtNivelP);
        FechaI = obtenerFecha();
        imagenes = findViewById(R.id.imageViewPalabras);
        imagenes.bringToFront();
        conn = new DbHelper(this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);

        // Obtener datos del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idMaestro = extras.getInt("idMaestro");
            idAlumno = extras.getInt("idAlumno");
            nivel = extras.getString("nivel");
            tipoPrueba = extras.getString("prueba");
        } else {
            Log.d("Debug", "Intent was null");
        }
        txtNivel.setText(nivel);

        try {
            obtenerPalabras(nivel);
            ciclo1();
        } catch (Exception e) {
            Toast.makeText(this, "error al iniciar: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    // Obtener todas las palabras del nivel y seleccionar 10 aleatoriamente
    private void obtenerPalabras(String nivel) {
        SQLiteDatabase db = conn.getReadableDatabase();
        listaPalabras = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID + ", " + Utilidades.CAMPO_PALABRA + ", " + Utilidades.CAMPO_AUDIO + ", " + Utilidades.CAMPO_IMAGEN + " FROM " + nivel, null);

        while (cursor.moveToNext()) {
            Palabra palabra = new Palabra();
            palabra.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_ID)));
            palabra.setPalabra(cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_PALABRA)));
            palabra.setImagen(cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_IMAGEN)));
            byte[] audioBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_AUDIO));
            palabra.setAudio(audioBlob != null ? new String(audioBlob) : null);
            listaPalabras.add(palabra);
        }
        cursor.close();
        Collections.shuffle(listaPalabras);
        listaPalabras = new ArrayList<>(listaPalabras.subList(0, Math.min(10, listaPalabras.size()))); // Limitar a 10 palabras
    }

    // Iniciar ciclo de mostrar palabras
    public void ciclo1() {
        if (c < listaPalabras.size()) {
            asignarBoton();
        } else {
            ResultadoJuegoActivity.aciertos=aciertos;
            ResultadoJuegoActivity.fallos=fallos;
            ResultadoJuegoActivity.calificacion=obtenerPuntaje(aciertos,fallos);
            Intent miIntent= new Intent(PruebaPalabras.this,ResultadoJuegoActivity.class);
            startActivity(miIntent);
            finish();
        }
    }

    // Asignar palabra y opciones a los botones
    public void asignarBoton() {
        if (c < listaPalabras.size()) {
            imagenes.setVisibility(View.INVISIBLE);
            Palabra palabraCorrecta = listaPalabras.get(c);
            c++;
            musica = palabraCorrecta.getAudio() != null ? palabraCorrecta.getAudio().trim() : null;
            if (musica != null) {
                reproducirAudio(musica); // Reproducir el audio de la palabra automáticamente
            }

            ArrayList<Palabra> opciones = new ArrayList<>(listaPalabras);
            opciones.remove(palabraCorrecta);
            Collections.shuffle(opciones);
            opciones = new ArrayList<>(opciones.subList(0, 3));
            opciones.add(palabraCorrecta);
            Collections.shuffle(opciones);

            B1.setText(opciones.get(0).getPalabra());
            palabra1 = opciones.get(0).getId();

            B2.setText(opciones.get(1).getPalabra());
            palabra2 = opciones.get(1).getId();

            B3.setText(opciones.get(2).getPalabra());
            palabra3 = opciones.get(2).getId();

            B4.setText(opciones.get(3).getPalabra());
            palabra4 = opciones.get(3).getId();
        } else {
            cerrarSesion();
        }
    }

    // Verificar opción seleccionada
    public void verificarOpcion(int idSelec) {
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (idSelec == listaPalabras.get(c - 1).getId()) {
            if (idSelec == palabra1) {
                B1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43FA3E")));

            } else if (idSelec == palabra2) {
                B2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43FA3E")));
            } else if (idSelec == palabra3) {
                B3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43FA3E")));
            } else if (idSelec == palabra4) {
                B4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43FA3E")));
            }

            String imagen = listaPalabras.get(c - 1).getImagen().trim();
            imagenes.setImageURI(Uri.parse(imagen));
            imagenes.setVisibility(View.VISIBLE);
            guardarRespuesta(db, idSelec, true, obtenerFecha());
            aciertos += 1;


        } else {
            guardarRespuesta(db, idSelec, false, obtenerFecha());
            fallos += 1;
            if (idSelec == palabra1) {
                B1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F83A3A")));
            } else if (idSelec == palabra2) {
                B2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F83A3A")));
            } else if (idSelec == palabra3) {
                B3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F83A3A")));
            } else if (idSelec == palabra4) {
                B4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F83A3A")));
            }
        }
    }

    // Guardar respuesta en la base de datos
    private void guardarRespuesta(SQLiteDatabase db, int idPalabra, boolean resultado, String fecha) {
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID_NINO, idAlumno);
        values.put(Utilidades.CAMPO_ID_PALABRA, idPalabra);
        values.put(Utilidades.CAMPO_RESULTADO, resultado ? 1 : 0);
        values.put(Utilidades.CAMPO_DIFICULTAD, nivel);
        values.put(Utilidades.CAMPO_FECHA, fecha);

        long registro = db.insert(Utilidades.TABLE_REGISTRO, null, values);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            cerrarSesion();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // Manejar clics en botones
    public void OnClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.Bpracticaregresar) {
            cerrarSesion();
            finish();
        } else if (viewId == R.id.Opcion1) {
            seleccionarOpcion(B1, palabra1);
        } else if (viewId == R.id.Opcion2) {
            seleccionarOpcion(B2, palabra2);
        } else if (viewId == R.id.Opcion3) {
            seleccionarOpcion(B3, palabra3);
        } else if (viewId == R.id.Opcion4) {
            seleccionarOpcion(B4, palabra4);
        } else if (viewId == R.id.btnReproducir) {
            reproducirAudio(musica);
        } else if (viewId == R.id.btnconfirmar) {
            confirmarSeleccion();
        }
    }

    // Seleccionar una opción
    private void seleccionarOpcion(Button boton, int idPalabra) {
        B1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));
        B2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));
        B3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));
        B4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));

        boton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f48b43")));
        idSeleccionado = idPalabra;
        seleccionado = true;
    }

    // Reproducir el audio de la palabra
    private void reproducirAudio(String audioUri) {
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

    // Confirmar selección de opción
    private void confirmarSeleccion() {
        if (seleccionado) {
            verificarOpcion(idSeleccionado);
            seleccionado = false;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    B1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));
                    B2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));
                    B3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));
                    B4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6200EE")));

                    ciclo1();
                }
            }, 1000);
            if (c2 == 4) {
                c2 = 1;
            } else {
                c2 += 1;
            }
        } else {
            Toast.makeText(this, "Seleccione una opción", Toast.LENGTH_SHORT).show();
        }
    }

    // Cerrar la sesión de prueba
    private void cerrarSesion() {
        try {
            FechaF = obtenerFecha();
            SQLiteDatabase dbR = conn.getReadableDatabase();
            SQLiteDatabase dbW = conn.getWritableDatabase();
            SQLiteDatabase db = conn.getWritableDatabase();
            Alumno alumno = null;
            Docente docente = null;
            Cursor cursorA = dbR.rawQuery("SELECT * FROM " + Utilidades.TABLE_ALUMNOS + " WHERE " + Utilidades.CAMPO_ID + " = " + idAlumno, null);
            while (cursorA.moveToNext()) {
                alumno = new Alumno();
                alumno.setId(cursorA.getInt(0));
                alumno.setNombre(cursorA.getString(1));
                nombreA = alumno.getNombre();
            }
            Cursor cursorM = dbR.rawQuery("SELECT * FROM " + Utilidades.TABLE_DOCENTE + " WHERE " + Utilidades.CAMPO_ID + " = " + idMaestro, null);
            while (cursorM.moveToNext()) {
                docente = new Docente();
                docente.setId(cursorM.getInt(0));
                docente.setNombre(cursorM.getString(1));
                nombreM = docente.getNombre();
            }
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_ID_NINO, idAlumno);
            values.put(Utilidades.CAMPO_NOMBREM, nombreM);
            values.put(Utilidades.CAMPO_NOMBRE, nombreA);
            values.put(Utilidades.CAMPO_DIFICULTAD, nivel);
            values.put(Utilidades.CAMPO_TIPO_PRUEBA, tipoPrueba);
            values.put(Utilidades.CAMPO_FECHAI, FechaI);
            values.put(Utilidades.CAMPO_FECHAF, FechaF);
            values.put(Utilidades.CAMPO_ACIERTOS, aciertos);
            values.put(Utilidades.CAMPO_FALLOS, fallos);
            values.put(Utilidades.CAMPO_CALIFICACION, obtenerPuntaje(aciertos, fallos) + "%");
            long idResultado = dbW.insert(Utilidades.TABLE_SESION_NINO, Utilidades.CAMPO_ID, values);
        } catch (Exception e) {
            Toast.makeText(PruebaPalabras.this, "error en cerrar sesion por: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    // Obtener fecha y hora actual
    private String obtenerFecha() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    // Calcular puntaje
    private float obtenerPuntaje(int aciertos, int fallos) {
        float promedio = 0;
        try {
            promedio = (100 / (aciertos + fallos)) * aciertos;
        } catch (Exception e) {
            Toast.makeText(this, "error en score: " + e, Toast.LENGTH_SHORT).show();
        }
        return promedio;
    }
}
