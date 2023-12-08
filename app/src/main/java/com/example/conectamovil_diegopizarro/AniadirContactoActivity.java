package com.example.conectamovil_diegopizarro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class AniadirContactoActivity extends AppCompatActivity {
    EditText nombre, email, numero;
    Button guardar;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_contacto);
        nombre = findViewById(R.id.txtNombreContacto);
        email = findViewById(R.id.txtEmailContacto);
        numero = findViewById(R.id.txtNumeroContacto);
        guardar = findViewById(R.id.btnGuardarContacto);

        databaseReference = FirebaseDatabase.getInstance().getReference("contactos");
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarContacto();
                Intent intent = new Intent(getApplicationContext(), ListaContactosActivity.class);
                startActivity(intent);
            }
        });
    }

    private void guardarContacto() {
        String nombreText = nombre.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String numeroText = numero.getText().toString().trim();

        // Verificar campos vacíos
        if (TextUtils.isEmpty(nombreText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(numeroText)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> contactoInfo = new HashMap<>();
        contactoInfo.put("nombre", nombreText);
        contactoInfo.put("email", emailText);
        contactoInfo.put("numero", numeroText);

        String contactoId = databaseReference.push().getKey();

        databaseReference.child(contactoId).setValue(contactoInfo)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //Información guardada con éxito.
                        Toast.makeText(AniadirContactoActivity.this, "Contacto guardado con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        //Si falla, muestra un mensaje al usuario.
                        Toast.makeText(AniadirContactoActivity.this, "Error al guardar el contacto: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

