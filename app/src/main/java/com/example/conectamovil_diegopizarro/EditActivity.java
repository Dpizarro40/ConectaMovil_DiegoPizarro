package com.example.conectamovil_diegopizarro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conectamovil_diegopizarro.Modelo.Usuario;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.HashMap;


public class EditActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    String userId;
    Button guardar;
    FloatingActionButton cambiar;
    ImageView fotoperfil;
    EditText editnombre;
    TextView name;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        //userId = firebaseAuth.getCurrentUser().getUid();
        userId = getIntent().getStringExtra("userId");

        if (userId == null) {
            Log.e("EditActivity", "userId is null");
            // Manejar el caso en el que userId es nulo, como mostrar un mensaje de error o volver a la actividad anterior.
            return;
        }

        guardar = findViewById(R.id.btnGuardarCambio);
        fotoperfil = findViewById(R.id.ivFotoEdit);
        editnombre = findViewById(R.id.txtEditNombre);
        cambiar = findViewById(R.id.btnCambiarFoto);
        name = findViewById(R.id.tvNameEdit);

        cargarYMostrarDatosEnPerfil();
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhoto();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevoNombre = editnombre.getText().toString();
                if (!TextUtils.isEmpty(nuevoNombre)) {
                    // Actualiza el nombre en la base de datos
                    DatabaseReference usuarioReference = databaseReference.child("Usuarios").child(userId);
                    usuarioReference.child("nombre").setValue(nuevoNombre);

                    // Muestra un mensaje de éxito
                    Toast.makeText(EditActivity.this, "Nombre actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                } else {
                    // Muestra un mensaje de error si el nuevo nombre está vacío
                    Toast.makeText(EditActivity.this, "Por favor, introduce un nuevo nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void cargarYMostrarDatosEnPerfil() {
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

                        // Imprime el nombre
                        name.setText("Nombre completo: " + nombreUsuario);

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
        Picasso.get().invalidate(imageUrl);

        Picasso.get().load(imageUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(fotoperfil, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("EditActivity", "Imagen cargada exitosamente desde Storage");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("EditActivity", "Error al cargar la imagen desde Storage: " + e.getMessage());
                        fotoperfil.setImageResource(R.drawable.person); // Puedes establecer una imagen de respaldo en caso de error
                    }
                });
    }

    private void updatePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    // Añade este método para manejar el resultado de la selección de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            subirNuevaFoto(filePath);
        }
    }



    private void subirNuevaFoto(Uri filePath) {

        if (databaseReference != null && userId != null) {
            DatabaseReference usuarioReference = databaseReference.child("Usuarios").child(userId);
            // Resto del código...
        } else {
            Log.e("EditActivity", "databaseReference or userId is null");
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("perfil_imagenes/" + userId + "/imagen.jpg");

        UploadTask uploadTask = imageRef.putFile(filePath);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();

            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Actualiza la URL de la foto en el nodo del usuario que estás editando
                DatabaseReference usuarioReference = databaseReference.child("Usuarios").child(userId);
                usuarioReference.child("urlFoto").setValue(uri.toString());

                // Carga y muestra la imagen actualizada
                cargarImagenDesdeStorage(uri.toString());
            }).addOnFailureListener(e -> {
                Log.e("ProfileActivity", "Error al obtener la URL de descarga: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity", "Error al subir la imagen: " + e.getMessage());
        });
    }
}