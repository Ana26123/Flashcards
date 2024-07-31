package com.flashcards_8.Vistas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

public class Agregar_alumno extends AppCompatActivity {

    EditText nombrealumno, apellidosalumno, edad, edadmental, txtcomentarios;
    Spinner spinnerSexoAlumno;
    Button btnGuardar, btnvolver;
    int idAlumno = -1; // Variable para almacenar el ID del alumno a modificar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alumno);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nombrealumno = findViewById(R.id.boxNombreAlumno);
        apellidosalumno = findViewById(R.id.boxApellidoAlumno);
        edad = findViewById(R.id.boxEdadAlumno);
        edadmental = findViewById(R.id.boxEdadMentalAlumno);
        spinnerSexoAlumno = findViewById(R.id.select_sexo);
        txtcomentarios = findViewById(R.id.boxComentariosAlumnos);
        btnGuardar = findViewById(R.id.btnGuardarAgregarAlumno);
        btnvolver = findViewById(R.id.btnVolverAgregarAlumno);

        // Configurar el Spinner con las opciones de sexo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_sexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexoAlumno.setAdapter(adapter);

        // Revisar si hay un ID de alumno para modificar
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAlumno = extras.getInt("idAlumnoM", -1);
            if (idAlumno != -1) {
                cargarDatosAlumno(idAlumno);
            }
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombrealumno.getText().toString().trim();
                String apellidos = apellidosalumno.getText().toString().trim();
                String edadStr = edad.getText().toString().trim();
                String edadMentalStr = edadmental.getText().toString().trim();
                String sexo = spinnerSexoAlumno.getSelectedItem().toString();
                String comentarios = txtcomentarios.getText().toString().trim();

                // Expresión regular para validar letras y espacios
                String regexLetrasEspacios = "^[a-zA-Z\\s]+$";
                // Expresión regular para validar solo números
                String regexNumeros = "\\d+";

                if (nombre.isEmpty() || apellidos.isEmpty() || edadStr.isEmpty() || edadMentalStr.isEmpty()) {
                    Toast.makeText(Agregar_alumno.this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!nombre.matches(regexLetrasEspacios) || !apellidos.matches(regexLetrasEspacios)) {
                    Toast.makeText(Agregar_alumno.this, "El nombre y apellidos solo pueden contener letras y espacios", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!edadStr.matches(regexNumeros) || !edadMentalStr.matches(regexNumeros)) {
                    Toast.makeText(Agregar_alumno.this, "La edad y la edad mental deben ser números", Toast.LENGTH_SHORT).show();
                    return;
                }

                int edad = Integer.parseInt(edadStr);
                int edadMental = Integer.parseInt(edadMentalStr);

                // Validar rango de edad y edad mental
                if (edad < 0 || edad > 150 || edadMental < 0 || edadMental > 150) {
                    Toast.makeText(Agregar_alumno.this, "La edad y la edad mental deben estar en un rango válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realizar la inserción o actualización en la base de datos
                DbHelper conn = new DbHelper(Agregar_alumno.this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
                SQLiteDatabase db = conn.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Utilidades.CAMPO_NOMBRE, nombre);
                values.put(Utilidades.CAMPO_APELLIDOS, apellidos);
                values.put(Utilidades.CAMPO_EDAD, edad);
                values.put(Utilidades.CAMPO_EDAD_MENTAL, edadMental);
                values.put(Utilidades.CAMPO_SEXO, sexo);
                values.put(Utilidades.CAMPO_COMENTARIOS, comentarios);

                long idResultado;
                if (idAlumno == -1) {
                    // Insertar nuevo alumno
                    idResultado = db.insert(Utilidades.TABLE_ALUMNOS, null, values);
                } else {
                    // Actualizar alumno existente
                    idResultado = db.update(Utilidades.TABLE_ALUMNOS, values, Utilidades.CAMPO_ID + " = ?", new String[]{String.valueOf(idAlumno)});
                }
                db.close();

                if (idResultado == -1) {
                    Toast.makeText(Agregar_alumno.this, "Error al guardar el alumno", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Agregar_alumno.this, "Alumno guardado correctamente", Toast.LENGTH_SHORT).show();
                    limpiar();
                }
            }
        });

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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

    private void limpiar() {
        nombrealumno.setText("");
        apellidosalumno.setText("");
        edad.setText("");
        edadmental.setText("");
        txtcomentarios.setText("");
    }

    private void cargarDatosAlumno(int idAlumno) {
        DbHelper conn = new DbHelper(this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLE_ALUMNOS + " WHERE " + Utilidades.CAMPO_ID + " = " + idAlumno, null);

        if (cursor.moveToFirst()) {
            // Verificar y obtener el índice de cada columna
            int idxNombre = cursor.getColumnIndex(Utilidades.CAMPO_NOMBRE);
            int idxApellidos = cursor.getColumnIndex(Utilidades.CAMPO_APELLIDOS);
            int idxEdad = cursor.getColumnIndex(Utilidades.CAMPO_EDAD);
            int idxEdadMental = cursor.getColumnIndex(Utilidades.CAMPO_EDAD_MENTAL);
            int idxSexo = cursor.getColumnIndex(Utilidades.CAMPO_SEXO);
            int idxComentarios = cursor.getColumnIndex(Utilidades.CAMPO_COMENTARIOS);

            // Si el índice es válido (≥ 0), obtener el valor de la columna
            if (idxNombre >= 0) {
                nombrealumno.setText(cursor.getString(idxNombre));
            }
            if (idxApellidos >= 0) {
                apellidosalumno.setText(cursor.getString(idxApellidos));
            }
            if (idxEdad >= 0) {
                edad.setText(cursor.getString(idxEdad));
            }
            if (idxEdadMental >= 0) {
                edadmental.setText(cursor.getString(idxEdadMental));
            }
            if (idxSexo >= 0) {
                String sexo = cursor.getString(idxSexo);
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerSexoAlumno.getAdapter();
                int spinnerPosition = adapter.getPosition(sexo);
                spinnerSexoAlumno.setSelection(spinnerPosition);
            }
            if (idxComentarios >= 0) {
                txtcomentarios.setText(cursor.getString(idxComentarios));
            }
        }

        cursor.close();
        db.close();
    }

}
