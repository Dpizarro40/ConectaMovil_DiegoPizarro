package com.example.conectamovil_diegopizarro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conectamovil_diegopizarro.Proveedor.MQTTHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email,pass;
    Button iniciarSesion, registro;
    FirebaseAuth firebaseAuth;
    private static final String BROKER_URL = "tcp://your-broker-url:1883";
    private static final String CLIENT_ID = "your_client_id";
    private MQTTHandler mqttHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        mqttHandler = new MQTTHandler();
        mqttHandler.connect(BROKER_URL,CLIENT_ID);
        publishMessage("chat", "Bienvenido");

        email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtContrasenia);

        iniciarSesion = findViewById(R.id.btnIniciarSesion);
        registro = findViewById(R.id.btnRegistro);

        firebaseAuth = FirebaseAuth.getInstance();

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPass = pass.getText().toString().trim();

                if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass)) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // La autenticación fue exitosa
                                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                        // Puedes redirigir a otra actividad aquí si es necesario
                                        Intent intent = new Intent(getApplicationContext(), ListaContactosActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // La autenticación falló
                                        Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();

    }
    private void publishMessage(String topic, String message){
        Toast.makeText(this, "Publishing message: " + message, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic,message);
    }
    private void subscribeToTopic(String topic){
        Toast.makeText(this, "Subscribing to topic "+ topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }
}