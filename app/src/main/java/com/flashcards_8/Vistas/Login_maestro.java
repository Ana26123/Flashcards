package com.flashcards_8.Vistas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

public class Login_maestro extends AppCompatActivity {

    private EditText txtUsuario, txtContrasena;
    private Button btnIniciarSesion, btnAgregar;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_maestro);

        DbHelper conn = new DbHelper(this, Utilidades.DATABASE_NAME,null,Utilidades.DATABASE_VERSION);

        txtUsuario = findViewById(R.id.txt_usuario);
        txtContrasena = findViewById(R.id.txt_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_siguiente);
        btnAgregar = findViewById(R.id.btn_agregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login_maestro.this, Agregar_maestro.class);
                startActivity(i);

            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtUsuario.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    // Se verifica si el campo de usuario o contraseña está vacío
                    Toast.makeText(Login_maestro.this, "Por favor, ingresa tu nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    // Si no están vacíos, se procede a validar las credenciales
                    if (validarCredenciales(usuario, contrasena)) {
                        // Credenciales válidas, iniciar la actividad principal
                        Intent intent = new Intent(Login_maestro.this, Login_alumno.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Credenciales inválidas, se muestra un mensaje de error
                        Toast.makeText(Login_maestro.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    /**
     * Método para validar las credenciales del usuario en la base de datos.
     * @param usuario El nombre de usuario ingresado.
     * @param contrasena La contraseña ingresada.
     * @return true si las credenciales son válidas, false de lo contrario.
     */
    private boolean validarCredenciales(String usuario, String contrasena) {
        DbHelper dbHelper = new DbHelper(Login_maestro.this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consultar la base de datos para verificar si el usuario y la contraseña coinciden
        String[] projection = {
                Utilidades.CAMPO_ID
        };
        String selection = Utilidades.CAMPO_NOMBRE + " = ? AND " + Utilidades.CAMPO_CONTRASEÑA + " = ?";
        String[] selectionArgs = {usuario, contrasena};

        Cursor cursor = db.query(
                Utilidades.TABLE_DOCENTE, // Nombre de la tabla en la que se realizará la consulta
                projection,              // Columnas que se desean recuperar de la tabla
                selection,               // Cláusula WHERE para filtrar los resultados
                selectionArgs,           // Valores de los parámetros de la cláusula WHERE
                null,                    // GROUP BY: Agrupamiento de filas (no se utiliza en este caso)
                null,                    // HAVING: Restricciones para grupos (no se utiliza en este caso)
                null                     // ORDER BY: Orden de las filas (no se utiliza en este caso)
        );


        // Si el cursor tiene al menos un resultado, significa que las credenciales son válidas
        boolean credencialesValidas = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return credencialesValidas;
    }


}

