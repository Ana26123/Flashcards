package com.flashcards_8.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards_8.R;
import com.flashcards_8.db.DbHelper;
import com.flashcards_8.Entidades.Sesion;
import com.flashcards_8.Utilidades.Utilidades;

import java.util.ArrayList;

public class Sesiones extends AppCompatActivity {

    /*
    public int idAlumno,aciertos,fallos;
    public String puntaje;
    TextView txtNombre;
    ListView lvSesiones;
    ArrayList<String> listaInformacion;
    ArrayList<Sesion> listaSesiones;
    DbHelper conn;

    int contadorTipoPrueba1 = 0;
    int contadorTipoPrueba2 = 0;
    */

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones);
        //full pantalla
        /*
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtNombre = findViewById(R.id.txtAlumnoSesion);
        conn = new DbHelper(this, Utilidades.DATABASE_NAME,null,Utilidades.DATABASE_VERSION);
        lvSesiones = (ListView) findViewById(R.id.LVSesiones);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAlumno = extras.getInt("idAlumno");

        } else {
            Log.d("Debug","Intent was null");
        }
        consultarListaSesiones();

        ArrayAdapter adaptador = new ArrayAdapter(this,R.layout.list_item_segunda_lista,listaInformacion);
        lvSesiones.setAdapter(adaptador);

        lvSesiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos!=0){
                    aciertos = listaSesiones.get(pos-1).getAciertos();
                    fallos = listaSesiones.get(pos-1).getFallos();
                    puntaje = listaSesiones.get(pos-1).getCalificacion();
                    try {
                        Intent intent = new Intent(Sesiones.this,GraficaAciertosFallos.class);
                        intent.putExtra("aciertos",aciertos);
                        intent.putExtra("fallos",fallos);
                        intent.putExtra("puntaje",puntaje);
                        startActivity(intent);

                    }catch (Exception e){
                        Toast.makeText(Sesiones.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        */
    }

    /*
    private void consultarListaSesiones() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Sesion sesionA = null;
        listaSesiones = new ArrayList<Sesion>();
        //Select * from usuarios
        Cursor cursor = db.rawQuery("SELECT * FROM " +  Utilidades.TABLE_SESION_NINO + " WHERE " + Utilidades.CAMPO_ID_NINO + " = " + idAlumno,null);

        while(cursor.moveToNext()){
            sesionA = new Sesion();
            sesionA.setId(cursor.getInt(0));
            sesionA.setIdA(cursor.getInt(1));
            sesionA.setNombreM(cursor.getString(2));
            sesionA.setNombreA(cursor.getString(3));
            sesionA.setDificultad(cursor.getString(4));
            sesionA.setTipoPrueba(cursor.getString(5));
            sesionA.setFechaI(cursor.getString(6));
            sesionA.setFechaF(cursor.getString(7));
            sesionA.setAciertos(cursor.getInt(8));
            sesionA.setFallos(cursor.getInt(9));
            sesionA.setCalificacion(cursor.getString(10));
            listaSesiones.add(sesionA);

            aciertos = sesionA.getAciertos();
            fallos = sesionA.getFallos();
            puntaje = sesionA.getCalificacion();

            txtNombre.setText(sesionA.getNombreA());

            String tipoPruebaActual = sesionA.getTipoPrueba();
            if (tipoPruebaActual.equals("Audio")) {
                contadorTipoPrueba1++;
            } else if (tipoPruebaActual.equals("Palabra")) {
                contadorTipoPrueba2++;
            }

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        listaInformacion.add("Maestro"
                + "     Nivel"
                + "    Prueba"
                + "              Inicio"
                + "                      Final");
        for(int i=0;i<listaSesiones.size();i++){
            listaInformacion.add(listaSesiones.get(i).getNombreM()
                    + "   "+listaSesiones.get(i).getDificultad()
                    + "   "+listaSesiones.get(i).getTipoPrueba()
                    + "   "+listaSesiones.get(i).getFechaI()
                    + "   "+listaSesiones.get(i).getFechaF());
        }
    }
    public void onclick(View view) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

     */

}
