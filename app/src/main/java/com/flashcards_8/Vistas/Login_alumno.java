package com.flashcards_8.Vistas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.Entidades.Alumno;
import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.util.ArrayList;

public class Login_alumno extends AppCompatActivity {

    ListView lvAlumnos;
    ArrayList<String> listaInformacion;
    ArrayList<Alumno> listaAlumnos;
    DbHelper conn;
    public int idMaestro;
    public int idAlumno;
    public boolean Seleccion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_alumno);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        conn = new DbHelper(getApplicationContext(),Utilidades.DATABASE_NAME,null,Utilidades.DATABASE_VERSION);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idMaestro = extras.getInt("idMaestro");
            //recibe el id del maestro de la pantalla anterior (int)
        } else {
            Log.d("Debug","Intent was null");
        }
        mostrarLista();


    }

    private void consultarListaAlumnos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Alumno alumno = null;
        listaAlumnos = new ArrayList<Alumno>();
        //select * from alumnos
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLE_ALUMNOS,null);

        while(cursor.moveToNext()){
            alumno = new Alumno();
            alumno.setId(cursor.getInt(0));
            alumno.setNombre(cursor.getString(1));
            alumno.setApellido(cursor.getString(2));
            listaAlumnos.add(alumno);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for(int i=0;i<listaAlumnos.size();i++){
            listaInformacion.add(listaAlumnos.get(i).getNombre()
                    + " "+listaAlumnos.get(i).getApellido());
        }
    }

    ActivityResultLauncher<Intent> agregarInicioAlumno = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK ){
                        mostrarLista();
                        Seleccion = false;

                    }else{
                        mostrarLista();
                        Seleccion = false;
                    }
                }
            }
    );

    private void mostrarLista() {
        lvAlumnos = (ListView) findViewById(R.id.lvAlumnos);
        consultarListaAlumnos();

        // Crear un adaptador personalizado
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaInformacion) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) convertView;
                if (textView == null) {
                    // Si convertView es nulo, inflamos una nueva vista
                    textView = new TextView(getContext());
                    // Personalizamos la vista según tus preferencias
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(18);
                    // Puedes agregar más personalizaciones según tus necesidades
                }

                // Establecemos el texto del TextView con los datos del alumno
                textView.setText(listaInformacion.get(position));

                return textView;
            }
        };

        // Establecer el adaptador personalizado en el ListView
        lvAlumnos.setAdapter(adaptador);

        lvAlumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                idAlumno = listaAlumnos.get(pos).getId();
                Seleccion = true;
            }
        });
    }

    public void agregarAlumnoOnClick(View view) {
        Intent intent = new Intent(Login_alumno.this, Agregar_alumno.class);
        agregarInicioAlumno.launch(intent);
        Seleccion = false;
    }

    public void iniciarAlumnoOnClick(View view) {
        if (Seleccion) {
            Intent intent = new Intent(Login_alumno.this, Menu.class);
            intent.putExtra("idMaestro", idMaestro);
            intent.putExtra("idAlumno", idAlumno);
            startActivity(intent);
            mostrarLista();
            Seleccion = false;
        } else {
            Toast.makeText(this, "Debe Seleccionar un Alumno", Toast.LENGTH_SHORT).show();
        }
    }

    public void volverInicioOnClick(View view) {
        finish();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
