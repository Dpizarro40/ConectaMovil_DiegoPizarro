package com.example.conectamovil_diegopizarro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaContactosActivity extends AppCompatActivity {
    ListView listaContactos;
    Button aniadir, buscar, eliminar, perfil;
    EditText busquedaPorNombre;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);
        listaContactos = findViewById(R.id.lvContactos);
        aniadir = findViewById(R.id.btnAgregar);
        buscar = findViewById(R.id.btnBuscar);
        eliminar = findViewById(R.id.btnEliminar);
        perfil = findViewById(R.id.btnProfile);
        busquedaPorNombre = findViewById(R.id.txtBusqueda);

        databaseReference = FirebaseDatabase.getInstance().getReference("contactos");
        firebaseAuth = FirebaseAuth.getInstance();
        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AniadirContactoActivity.class);
                startActivity(intent);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreBusqueda = busquedaPorNombre.getText().toString().trim();
                if (!nombreBusqueda.isEmpty()) {
                    buscarContactoPorNombre(nombreBusqueda);
                } else {
                    Toast.makeText(ListaContactosActivity.this, "Ingresa un nombre para buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el nombre del contacto desde el EditText
                String nombreContactoEliminar = busquedaPorNombre.getText().toString().trim();

                // Verificar si el nombre del contacto está vacío
                if (!nombreContactoEliminar.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListaContactosActivity.this);
                    builder.setTitle("Confirmar eliminación");
                    builder.setMessage("¿Estás seguro de que quieres eliminar este contacto?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Lógica para eliminar el contacto usando el nombre almacenado
                            eliminarContactoPorNombre(nombreContactoEliminar);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // No hacer nada si el usuario cancela la eliminación
                        }
                    });
                    builder.create().show();
                } else {
                    Toast.makeText(ListaContactosActivity.this, "Ingresa un nombre para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        listaContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Obtener la información del contacto seleccionado
                String contactoInfo = (String) adapterView.getItemAtPosition(position);
                String nombre = contactoInfo.split("\n")[0].substring(8);
                abrirChatConContacto(nombre);
            }
        });

        cargarListaContactos();
    }
    private void cargarListaContactos() {
        // Obtener una referencia a la colección de contactos en la base de datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> listaContactosArray = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener información de cada contacto
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String numero = snapshot.child("numero").getValue(String.class);

                    // Construir una cadena con la información del contacto
                    String contactoInfo = "Nombre: " + nombre + "\nEmail: " + email + "\nNúmero: " + numero;
                    listaContactosArray.add(contactoInfo);
                }

                // Mostrar la lista de contactos en el ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListaContactosActivity.this, android.R.layout.simple_list_item_1, listaContactosArray);
                listaContactos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Toast.makeText(ListaContactosActivity.this, "Error al leer la base de datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void abrirChatConContacto(final String nombreContacto) {
        // Aquí puedes agregar lógica adicional según tus necesidades

        // Por ejemplo, podrías pasar el nombre del contacto a la actividad de Chat
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("nombre", nombreContacto);
        startActivity(intent);
    }

    private void buscarContactoPorNombre(final String nombreBusqueda) {
        databaseReference.orderByChild("nombre").equalTo(nombreBusqueda).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> resultadosBusqueda = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener información de cada contacto que coincide con la búsqueda
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String numero = snapshot.child("numero").getValue(String.class);

                    // Construir una cadena con la información del contacto
                    String contactoInfo = "Nombre: " + nombre + "\nEmail: " + email + "\nNúmero: " + numero;
                    resultadosBusqueda.add(contactoInfo);
                }

                // Mostrar los resultados de la búsqueda en el ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListaContactosActivity.this, android.R.layout.simple_list_item_1, resultadosBusqueda);
                listaContactos.setAdapter(adapter);

                if (resultadosBusqueda.isEmpty()) {
                    Toast.makeText(ListaContactosActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListaContactosActivity.this, "Error al buscar en la base de datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarContactoPorNombre(final String nombre) {
        databaseReference.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue(); // Eliminar el contacto
                    Toast.makeText(ListaContactosActivity.this, "Contacto eliminado correctamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListaContactosActivity.this, "Error al eliminar el contacto: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}