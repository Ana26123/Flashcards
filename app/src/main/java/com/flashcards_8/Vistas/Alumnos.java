package com.flashcards_8.Vistas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.Entidades.Alumno;
import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.util.ArrayList;

public class Alumnos extends AppCompatActivity {

    DbHelper conn;
    ListView LValumnos;
    ArrayList<Alumno> listaAlumnos;
    ArrayList<String> listaInformacion;
    boolean seleccion = false;
    int idAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inicializar la conexión con la base de datos y la ListView
        conn = new DbHelper(Alumnos.this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
        LValumnos = findViewById(R.id.LvAlumnos);
        mostrarLista();
    }

    // Consultar la lista de alumnos desde la base de datos
    private void consultarListaAlumnos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Alumno alumno = null;
        listaAlumnos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLE_ALUMNOS, null);

        while (cursor.moveToNext()) {
            alumno = new Alumno();
            alumno.setId(cursor.getInt(0));
            alumno.setNombre(cursor.getString(1));
            alumno.setApellido(cursor.getString(2));
            alumno.setEdad(cursor.getString(3));
            alumno.setEdadmental(cursor.getString(4));
            alumno.setSexo(cursor.getString(5));
            alumno.setComentarios(cursor.getString(6));
            listaAlumnos.add(alumno);
        }
        cursor.close();
        obtenerLista();
    }

    // Obtener la lista de nombres y apellidos de los alumnos para mostrar en la ListView
    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (Alumno alumno : listaAlumnos) {
            listaInformacion.add(alumno.getNombre() + " " + alumno.getApellido());
        }
    }

    // Lanzador para editar un alumno
    ActivityResultLauncher<Intent> editarAlumnolauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        mostrarLista();
                        seleccion = false;
                    } else {
                        mostrarLista();
                        seleccion = false;
                    }
                }
            }
    );

    // Lanzador para agregar un alumno
    ActivityResultLauncher<Intent> agregarAlumnolauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        mostrarLista();
                        seleccion = false;
                    } else {
                        mostrarLista();
                        seleccion = false;
                    }
                }
            }
    );

    // Mostrar la lista de alumnos en la ListView
    private void mostrarLista() {
        consultarListaAlumnos();

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.custom_list_item, R.id.custom_text_view, listaInformacion);
        LValumnos.setAdapter(adaptador);

        LValumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long idl) {
                idAlumno = listaAlumnos.get(pos).getId();
                seleccion = true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // Manejar los clics en los botones
    public void OnClick(View view) {
        Intent intent = null;
        int viewId = view.getId();
        if (viewId == R.id.BTNAgregarAlumnos) {
            intent = new Intent(this, Agregar_alumno.class);
            agregarAlumnolauncher.launch(intent);
            seleccion = false;
            mostrarLista();
        } else if (viewId == R.id.BTNModificarAlumnos) {
            if (seleccion) {
                intent = new Intent(this, Agregar_alumno.class);
                intent.putExtra("idAlumnoM", idAlumno); // Pasar el ID del alumno
                editarAlumnolauncher.launch(intent);
                seleccion = false;
                mostrarLista();
            } else {
                Toast.makeText(this, "Debe Seleccionar un Alumno", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.BTNEliminarAlumnos) {
            if (seleccion) {
                seleccion = false;
                eliminarAlumno();
                mostrarLista();
            } else {
                Toast.makeText(this, "Debe Seleccionar un Alumno", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.btnVolverAlumnos) {
            finish();
        }
    }

    // Eliminar un alumno de la base de datos
    private void eliminarAlumno() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.delete(Utilidades.TABLE_ALUMNOS, Utilidades.CAMPO_ID + " = " + idAlumno, null);
        db.delete(Utilidades.TABLE_SESION_NINO, Utilidades.CAMPO_ID_NINO + " = " + idAlumno, null);
        Toast.makeText(this, "Se eliminaron todos los datos del alumno", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
    }
}
