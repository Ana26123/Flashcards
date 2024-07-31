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

import com.flashcards_8.Entidades.Docente;
import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.util.ArrayList;

public class Docentes extends AppCompatActivity {

    DbHelper conn;
    ListView LVdocentes;
    ArrayList<Docente> listaDocentes;
    ArrayList<String> listaInformacion;
    boolean seleccion = false;
    int idDocente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docentes);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inicializar la conexión con la base de datos y la ListView
        conn = new DbHelper(Docentes.this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
        LVdocentes = findViewById(R.id.LvDocentes);
        mostrarLista();
    }

    // Consultar la lista de docentes desde la base de datos
    private void consultarListaDocentes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Docente docente = null;
        listaDocentes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLE_DOCENTE, null);

        while (cursor.moveToNext()) {
            docente = new Docente();
            docente.setId(cursor.getInt(0));
            docente.setNombre(cursor.getString(1));
            listaDocentes.add(docente);
        }
        cursor.close();
        obtenerLista();
    }

    // Obtener la lista de nombres de los docentes para mostrar en la ListView
    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (Docente docente : listaDocentes) {
            listaInformacion.add(docente.getNombre());
        }
    }

    // Lanzador para editar un docente
    ActivityResultLauncher<Intent> editarDocentelauncher = registerForActivityResult(
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

    // Lanzador para agregar un docente
    ActivityResultLauncher<Intent> agregarDocentelauncher = registerForActivityResult(
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

    // Mostrar la lista de docentes en la ListView
    private void mostrarLista() {
        consultarListaDocentes();

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.custom_list_item, R.id.custom_text_view, listaInformacion);
        LVdocentes.setAdapter(adaptador);

        LVdocentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long idl) {
                idDocente = listaDocentes.get(pos).getId();
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
        if (viewId == R.id.BTNAgregarDocentes) {
            intent = new Intent(this, Agregar_maestro.class);
            agregarDocentelauncher.launch(intent);
            seleccion = false;
            mostrarLista();
        } else if (viewId == R.id.BTNModificarDocentes) {
            if (seleccion) {
                intent = new Intent(this, Agregar_maestro.class);
                intent.putExtra("idDocenteM", idDocente);
                editarDocentelauncher.launch(intent);
                seleccion = false;
                mostrarLista();
            } else {
                Toast.makeText(this, "Debe Seleccionar un Docente", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.BTNEliminarDocentes) {
            if (seleccion) {
                seleccion = false;
                eliminarDocente();
                mostrarLista();
            } else {
                Toast.makeText(this, "Debe Seleccionar un Docente", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.btnVolverDocentes) {
            finish();
        }
    }

    // Eliminar un docente de la base de datos
    private void eliminarDocente() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.delete(Utilidades.TABLE_DOCENTE, Utilidades.CAMPO_ID + " = " + idDocente, null);
        Toast.makeText(this, "Se eliminaron todos los datos del docente", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
    }
}
