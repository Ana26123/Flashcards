package com.flashcards_8.Vistas;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Creditos;
import com.flashcards_8.Utilidades.GeneradorDePalabras;
import com.flashcards_8.Utilidades.infoTec;
import com.flashcards_8.db.DbHelper;
import com.flashcards_8.Entidades.Palabra;
import com.flashcards_8.Utilidades.Utilidades;


import com.flashcards_8.db.DbManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    //Variables
    public int idMaestro;
    public int idAlumno;
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;
    private DbManager dbManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);

        dbManager = new DbManager(this);
        dbManager.open();

        // Insertar palabras en nivel 1
        List<Palabra> palabras = GeneradorDePalabras.generarListaDePalabras();
        dbManager.insertarMultiplesPalabrasNivel1(palabras);

        // Insertar palabras en nivel 2
        List<Palabra> palabras2 = GeneradorDePalabras.generarListaDePalabrasNivel2();
        dbManager.insertarMultiplesPalabrasNivel2(palabras2);

        // Insertar palabras en nivel 3
        List<Palabra> palabras3 = GeneradorDePalabras.generarListaDePalabrasNivel3();
        dbManager.insertarMultiplesPalabrasNivel3(palabras3);

        dbManager.close();

        // Código existente para configurar la interfaz de usuario
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idMaestro = extras.getInt("idMaestro");
            idAlumno = extras.getInt("idAlumno");
        } else {
            Log.d("Debug", "Intent was null");
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {

                int itemId = Item.getItemId();

                if (itemId == R.id.navAlumnos){
                    Toast.makeText(Menu.this, "Lista de alumnos", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Menu.this, Alumnos.class));
                }

                if (itemId == R.id.navMaestros){
                    Toast.makeText(Menu.this, "Lista de docentes", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Menu.this, Docentes.class));
                }

                if (itemId == R.id.navPalabras){
                    Toast.makeText(Menu.this, "Lista de palabras", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Menu.this, Palabras.class));
                }

                if (itemId == R.id.navSesion){
                    Toast.makeText(Menu.this, "Sesiones de los alumnos", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Menu.this, Sesiones.class));
                }

                if (itemId == R.id.InfoTEC){
                    Toast.makeText(Menu.this, "InfoTEC", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Menu.this, infoTec.class));
                }

                if (itemId == R.id.Creditos){
                    Toast.makeText(Menu.this, "Otros creditos", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Menu.this, Creditos.class));
                }

                if (itemId == R.id.navSalir){
                    Toast.makeText(Menu.this, "Salir de la aplicacion", Toast.LENGTH_SHORT).show();
                    opcionSalir();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });

    }

    public void OnClick2(View view) {
        if (view.getId() == R.id.IMGnvl1) {
            if (obtenerCantidad(Utilidades.TABLE_PALABRAS_NIVEL1) != 0) {
                menuNivel(Utilidades.TABLE_PALABRAS_NIVEL1);
            } else {
                Toast.makeText(this, "Este nivel no tiene palabras", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.IMGnvl2) {
            if (obtenerCantidad(Utilidades.TABLE_PALABRAS_NIVEL2) != 0) {
                menuNivel(Utilidades.TABLE_PALABRAS_NIVEL2);
            } else {
                Toast.makeText(this, "Este nivel no tiene palabras", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.IMGnvl3) {
            if (obtenerCantidad(Utilidades.TABLE_PALABRAS_NIVEL3) != 0) {
                menuNivel(Utilidades.TABLE_PALABRAS_NIVEL3);
            } else {
                Toast.makeText(this, "Este nivel no tiene palabras", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.IMGnvlEsp) {
            if (obtenerCantidad(Utilidades.TABLE_PALABRAS_NIVELESP) != 0) {
                menuNivel(Utilidades.TABLE_PALABRAS_NIVELESP);
            } else {
                Toast.makeText(this, "Este nivel no tiene palabras", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private int obtenerCantidad(String Nivel) {
        DbHelper conn = new DbHelper(this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
        SQLiteDatabase dbCan = conn.getReadableDatabase();
        ArrayList<Palabra> listaPalabras = new ArrayList<>();

        Cursor cursor = dbCan.rawQuery("SELECT " + Utilidades.CAMPO_ID + " , " + Utilidades.CAMPO_PALABRA + " FROM " + Nivel, null);
        while (cursor.moveToNext()) {
            // Utiliza el constructor con argumentos
            Palabra palabra = new Palabra(cursor.getInt(0), cursor.getString(1), null, null);
            listaPalabras.add(palabra);
        }
        cursor.close();
        dbCan.close();

        return listaPalabras.size();
    }


    private void menuNivel(String nivel) {
        String[] options = {"Flashcards","Prueba de Palabras"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar un modo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int opcion) {
                // manejar clicks
                Intent intent;
                if (opcion == 1) {
                    try {
                        intent = new Intent(Menu.this, PruebaPalabras.class);
                        intent.putExtra("nivel", nivel);
                        intent.putExtra("idMaestro",idMaestro);
                        intent.putExtra("idAlumno",idAlumno);
                        intent.putExtra("prueba","Palabra");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (opcion == 0) {
                    menuTiempo(nivel);
                }
            }
        });
        builder.create().show();
    }

    private void menuTiempo(String nivel) {
        String[] options = {"2", "4","6", "8", "10"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Intervalos de tiempo (seg)");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int opcion) {
                // manejar clicks
                Intent intent;intent = new Intent(Menu.this, ModoPractica.class);
                intent.putExtra("idMaestro",idMaestro);
                intent.putExtra("idAlumno",idAlumno);
                intent.putExtra("nivel", nivel);
                if (opcion == 0) {
                    intent.putExtra("tiempo", 2000);
                    startActivity(intent);
                } else if (opcion == 1) {
                    intent.putExtra("tiempo", 4000);
                    startActivity(intent);
                }else if (opcion == 2) {
                    intent.putExtra("tiempo", 6000);
                    startActivity(intent);
                }else if (opcion == 3) {
                    intent.putExtra("tiempo", 8000);
                    startActivity(intent);
                }else if (opcion == 4) {
                    intent.putExtra("tiempo", 10000);
                    startActivity(intent);
                }
            }
        });
        builder.create().show();
    }

    private void opcionSalir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea salir de la aplicacion?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
