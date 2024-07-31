package com.flashcards_8.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.R;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Agregar_palabras extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;

    private ImageView imageView;
    private Uri imageUri;
    private Uri audioUri;
    private EditText editTextPalabra;
    private Spinner spinnerNiveles;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_palabra);

        imageView = findViewById(R.id.imgPalabra);
        editTextPalabra = findViewById(R.id.campoPalabra);
        spinnerNiveles = findViewById(R.id.spinnerNiveles);
        dbHelper = new DbHelper(this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.niveles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNiveles.setAdapter(adapter);

        findViewById(R.id.btnSelecionarIMG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(PICK_IMAGE_REQUEST);
            }
        });

        findViewById(R.id.btnSubirAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(PICK_AUDIO_REQUEST);
            }
        });

        findViewById(R.id.btnGuardarIMG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWord();
            }
        });

        findViewById(R.id.btnvolverAgregarP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType(requestCode == PICK_IMAGE_REQUEST ? "image/*" : "audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_AUDIO_REQUEST) {
                audioUri = data.getData();
                Toast.makeText(this, "Audio seleccionado: " + audioUri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveWord() {
        String palabra = editTextPalabra.getText().toString().trim();
        String nivel = spinnerNiveles.getSelectedItem().toString();

        if (palabra.isEmpty() || imageUri == null || audioUri == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = convertUriToBytes(imageUri);
        byte[] audioBytes = convertUriToBytes(audioUri);

        switch (nivel) {
            case "Nivel 1":
                dbHelper.insertarDatosNivel1(palabra, imageBytes, audioBytes);
                break;
            case "Nivel 2":
                dbHelper.insertarDatosNivel2(palabra, imageBytes, audioBytes);
                break;
            case "Nivel 3":
                dbHelper.insertarDatosNivel3(palabra, imageBytes, audioBytes);
                break;
            default:
                Toast.makeText(this, "Nivel no válido", Toast.LENGTH_SHORT).show();
                return;
        }

        Toast.makeText(this, "Palabra guardada en " + nivel, Toast.LENGTH_SHORT).show();
        finish();
    }

    private byte[] convertUriToBytes(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

