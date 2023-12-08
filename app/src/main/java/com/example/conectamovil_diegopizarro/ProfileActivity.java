package com.example.conectamovil_diegopizarro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    TextView nombre, email;
    ImageView fotoprofile;
    Button editar, cerrarSesion, volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nombre = findViewById(R.id.tvNombre);
        email = findViewById(R.id.tvEmail);
        editar = findViewById(R.id.btnEditar);
        fotoprofile = findViewById(R.id.ivFotoPerfil);

        cerrarSesion = findViewById(R.id.btnCerrarSesion);
        volver = findViewById(R.id.btnVolver);

        cargarYMostrarDatosEnPerfil();

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    // Ahora, puedes pasar 'userId' a la actividad siguiente.
                    Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    // El usuario no está autenticado, maneja este caso según tus necesidades.
                }

            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cerrar sesión con Firebase
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                //Redirigir al inicio de sesión
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
    private void cargarYMostrarDatosEnPerfil(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String correoUsuario = user.getEmail();
            DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

            usuariosRef.orderByChild("email").equalTo(correoUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DataSnapshot primerHijo = dataSnapshot.getChildren().iterator().next();

                        String nombreUsuario = primerHijo.child("nombre").getValue().toString();
                        String urlImagen = primerHijo.child("urlFoto").getValue().toString();

                        // Imprime el nombre y el correo electrónico
                        nombre.setText("Nombre: " + nombreUsuario);
                        email.setText("Correo electrónico: " + correoUsuario);

                        // Cargar y mostrar la imagen utilizando Picasso
                        cargarImagenDesdeStorage(urlImagen);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void cargarImagenDesdeStorage(String imageUrl) {
        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.person) // Imagen de respaldo mientras se carga la imagen
                .error(R.drawable.person) // Imagen de respaldo en caso de error
                .into(fotoprofile);
    }
}