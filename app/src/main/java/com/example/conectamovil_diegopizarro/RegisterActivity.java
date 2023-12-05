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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText nombre, email, contrasenia, repetirContrasenia;
    Button registrar, inicio;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
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
    private void registrarUsuario(String email, String contrasenia, final String nombre) {
        firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            String userId = firebaseAuth.getCurrentUser().getUid();

                            // Guardar información adicional en Realtime Database
                            guardarInformacionAdicional(userId, nombre, email);
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
        registrarUsuario(emailText, contraseniaText, nombreText);
    }

    private void guardarInformacionAdicional(String userId, String nombre, String email) {
        // Obtener la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        // Crear un mapa para almacenar la información del usuario
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("nombre", nombre);
        userInfo.put("email", email);

        // Guardar la información del usuario en la base de datos
        databaseReference.child(userId).setValue(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Información adicional guardada con éxito
                        Toast.makeText(RegisterActivity.this, "Información adicional guardada con éxito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al guardar la información adicional
                        Toast.makeText(RegisterActivity.this, "Error al guardar información adicional: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}