package com.example.conectamovil_diegopizarro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    TextView nombre, email;
    Button editar, cerrarSesion, volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nombre = findViewById(R.id.tvNombre);
        email = findViewById(R.id.tvEmail);
        editar = findViewById(R.id.btnEditar);
        cerrarSesion = findViewById(R.id.btnCerrarSesion);
        volver = findViewById(R.id.btnVolver);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión con Firebase
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                // Redirigir al inicio de sesión u otra actividad si es necesario
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListaContactosActivity.class);
                startActivity(intent);
            }
        });
    }
}