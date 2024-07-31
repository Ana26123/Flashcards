package com.flashcards_8.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Busca el botón por su ID
        Button btnIniciar = findViewById(R.id.btniniciar);

        // Agrega un OnClickListener al botón
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent para iniciar la actividad de login del maestro
                Intent intent = new Intent(MainActivity.this, Login_maestro.class);

                // Se inicia la actividad
                startActivity(intent);
            }
        });
    }
}
