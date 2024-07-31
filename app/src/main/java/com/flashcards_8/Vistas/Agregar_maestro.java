package com.flashcards_8.Vistas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

public class Agregar_maestro extends AppCompatActivity {

    private EditText txtNombre, txtContra;
    private Button btnGuardar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_maestro);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtNombre = findViewById(R.id.boxNombre_Maestro);
        txtContra = findViewById(R.id.boxContrasena_Maestro);
        btnGuardar = findViewById(R.id.btnGuardar_Maestro);
        btnVolver = findViewById(R.id.btnVolver_Maestros);

        // Verificar si hay un id de docente para editar
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int idDocente = extras.getInt("idDocenteM", -1);
            if (idDocente != -1) {
                cargarDatosDocente(idDocente);
            }
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDocente();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
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
        if (keyCode == event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void cargarDatosDocente(int idDocente) {
        DbHelper conn = new DbHelper(this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLE_DOCENTE + " WHERE " + Utilidades.CAMPO_ID + " = " + idDocente, null);
        if (cursor.moveToFirst()) {
            txtNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_NOMBRE)));
            txtContra.setText(cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_CONTRASEÑA)));
        }
        cursor.close();
        db.close();
    }

    private void guardarDocente() {
        String vacio = "";
        String nombre = txtNombre.getText().toString().trim();
        String apellidos = txtContra.getText().toString().trim();

        // Expresión regular para validar letras solamente
        String regexLetrasEspacios = "^[a-zA-Z\\s]+$";

        // Expresión regular para validar letras y números
        String regexLetrasNumeros = "^[a-zA-Z0-9]+$";

        if (!nombre.equals(vacio) && !apellidos.equals(vacio)) {
            // Validar nombre
            if (nombre.matches(regexLetrasEspacios)) {
                // Validar apellidos
                if (apellidos.matches(regexLetrasNumeros)) {
                    DbHelper conn = new DbHelper(Agregar_maestro.this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(Utilidades.CAMPO_NOMBRE, nombre);
                    values.put(Utilidades.CAMPO_CONTRASEÑA, apellidos);

                    Bundle extras = getIntent().getExtras();
                    if (extras != null && extras.getInt("idDocenteM", -1) != -1) {
                        // Actualizar el registro existente
                        int idDocente = extras.getInt("idDocenteM");
                        db.update(Utilidades.TABLE_DOCENTE, values, Utilidades.CAMPO_ID + "=?", new String[]{String.valueOf(idDocente)});
                        Toast.makeText(Agregar_maestro.this, "Docente actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insertar un nuevo registro
                        Long idResultado = db.insert(Utilidades.TABLE_DOCENTE, Utilidades.CAMPO_ID, values);
                        if (idResultado == -1) {
                            Toast.makeText(Agregar_maestro.this, "Error al guardar el docente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Agregar_maestro.this, "Docente agregado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                    db.close();
                    limpiar();
                } else {
                    Toast.makeText(Agregar_maestro.this, "Los apellidos solo pueden contener letras y números", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Agregar_maestro.this, "El nombre solo puede contener letras", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Agregar_maestro.this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtContra.setText("");
    }
}
