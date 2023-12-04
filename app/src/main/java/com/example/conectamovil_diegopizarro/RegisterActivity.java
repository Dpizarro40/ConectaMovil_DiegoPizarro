package com.example.conectamovil_diegopizarro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText nombre, email, contrasenia, repetirContrasenia;
    Button registrar, inicio;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.txtNombreRegistro);
        email = findViewById(R.id.txtEmailRegistro);
        contrasenia = findViewById(R.id.txtContraseniaRegistro);
        repetirContrasenia = findViewById(R.id.txtRepetirContrasenia);

        registrar = findViewById(R.id.btnRegistrar);
        inicio = findViewById(R.id.btnRegresar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarYRegistrarUsuario();
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void registrarUsuario(String email, String contrasenia) {
        firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Puedes agregar más lógica aquí, como redirigir a la pantalla de inicio de sesión
                        } else {
                            // Si el registro falla, muestra un mensaje al usuario.
                            Toast.makeText(RegisterActivity.this, "Error en el registro: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void validarYRegistrarUsuario() {
        String nombreText = nombre.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String contraseniaText = contrasenia.getText().toString().trim();
        String repetirContraseniaText = repetirContrasenia.getText().toString().trim();

        // Verificar campos vacíos
        if (TextUtils.isEmpty(nombreText) || TextUtils.isEmpty(emailText) ||
                TextUtils.isEmpty(contraseniaText) || TextUtils.isEmpty(repetirContraseniaText)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si las contraseñas coinciden
        if (!contraseniaText.equals(repetirContraseniaText)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ahora puedes registrar al usuario
        registrarUsuario(emailText, contraseniaText);
    }

}