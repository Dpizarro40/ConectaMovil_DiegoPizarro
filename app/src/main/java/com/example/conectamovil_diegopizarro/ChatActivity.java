package com.example.conectamovil_diegopizarro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.conectamovil_diegopizarro.Proveedor.MQTTHandler;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton enviar;
    TextView tvMensajePropio,tvMensajeRecibido;
    EditText txtMensaje;
    private static final String BROKER_URL = "tcp://your-broker-url:1883";
    private static final String CLIENT_ID = "your_client_id";
    private MQTTHandler mqttHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mqttHandler = new MQTTHandler();

        // Conectar al servidor MQTT y suscribirse al tema del chat
        mqttHandler.connect(BROKER_URL, CLIENT_ID);
        mqttHandler.subscribe("chat");
        mqttHandler.setMensajeListener(new MQTTHandler.MensajeListener() {
            @Override
            public void onMensajeRecibido(String topic, String message) {
                // Mostrar el mensaje recibido en la interfaz de usuario
                mostrarMensajeRecibido(message);
            }
        });

        enviar = findViewById(R.id.btnEnviar);
        tvMensajePropio = findViewById(R.id.tvMensajePropio);
        tvMensajeRecibido = findViewById(R.id.tvMensajeRecibido);
        txtMensaje = findViewById(R.id.txtMensaje);
        String nombreContacto = getIntent().getStringExtra("nombre");
        setTitle(nombreContacto);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeTexto = txtMensaje.getText().toString().trim();
                if (!mensajeTexto.isEmpty()) {
                    enviarMensaje(mensajeTexto);
                    txtMensaje.setText("");
                }
            }
        });

    }
    private void enviarMensaje(String mensaje) {
        // Usa tu instancia de MQTTHandler para publicar el mensaje en el tema del chat
        mqttHandler.publish("chat", mensaje);
        // Puedes agregar lógica adicional, como actualizar la interfaz de usuario
        mostrarMensajePropio(mensaje);
    }


    private void mostrarMensajeRecibido(String mensajeRecibido) {
        // Actualiza la interfaz de usuario para mostrar el mensaje recibido
        tvMensajeRecibido.setText(tvMensajeRecibido.getText() + "\n" + mensajeRecibido);
    }

    private void mostrarMensajePropio(String mensajePropio) {
        // Mostrar el mensaje propio en la interfaz de usuario
        tvMensajePropio.setText(tvMensajePropio.getText() + "\n" + "Yo: " + mensajePropio);
    }
    @Override
    protected void onDestroy() {
        // Desconectar al salir de la actividad
        mqttHandler.disconnect();
        super.onDestroy();
    }
}