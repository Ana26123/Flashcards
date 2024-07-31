package com.flashcards_8.Vistas;

import static com.flashcards_8.R.layout.activity_resultado_juego;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;


public class ResultadoJuegoActivity extends AppCompatActivity {

    static int aciertos= 0;
    static int fallos= 0;
    static float calificacion=0;

    TextView resultadocorrectas, resultadocalif, resultadoincorrectas, txtresultados, txtpalabrascorrectas, txtpalabrasincorrectas, txtcalif;
    Button btnsalir;
    LottieAnimationView like, centellas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(activity_resultado_juego);

        txtresultados= findViewById(R.id.txtresultados);
        txtpalabrascorrectas= findViewById(R.id.txtpalabrascorrectas);
        txtpalabrasincorrectas= findViewById(R.id.txtpalabrasincorrectas);
        txtcalif= findViewById(R.id.txtcalif);
        resultadocorrectas= findViewById(R.id.resultadocorrectas);
        resultadoincorrectas= findViewById(R.id.resultadoincorrectas);
        resultadocalif= findViewById(R.id.resultadocalif);
        like= findViewById(R.id.like);
        centellas= findViewById(R.id.centellas);


                    if (calificacion >= 80){
                        like.setVisibility(View.INVISIBLE);
                    }




                    if (calificacion <= 80){
                        centellas.setVisibility(View.INVISIBLE);
                    }




        resultadocorrectas.setText(aciertos + "");
        resultadoincorrectas.setText(fallos + "");
        resultadocalif.setText(calificacion+ "");

        btnsalir= findViewById(R.id.btnsalirres);
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}