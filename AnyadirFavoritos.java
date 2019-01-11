package com.example.marta.fbandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * EXAMEN AÑADIR FAVORITOS A LISTA
 */
public class AnyadirFavoritos extends AppCompatActivity implements View.OnClickListener{
    // Componentes de view:
    private Spinner favs;

    // Base de datos:
    private DatabaseReference bbdd,bbdd2;

    // Firebase:
    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;

    // Auxiliares:
    private String id, nomProd, usuario;
    private Favs fav;

    private ArrayList<String> productos = new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_favoritos);
        mAuth = FirebaseAuth.getInstance();
        // Cogemos al usuario actual:
        fbUser = mAuth.getCurrentUser();
        // Elementos:
        favs = (Spinner) findViewById(R.id.spFavs);

        // Adaptador:
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(AnyadirFavoritos.this, android.R.layout.simple_list_item_1, productos);
        favs.setAdapter(adaptador);

        // Coger el usuario actual:
        bbdd = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));
        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorrer usuarios:
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Usuario usu = datasnapshot.getValue(Usuario.class);

                    usuario = usu.getUsuario();
                    String nombre = usu.getNombre();
                    String apellido = usu.getApellidos();
                    String correo = usu.getCorreo();
                    String contrasenya = usu.getContraseña();
                    String sDireccion = usu.getDireccion();
                    String uid = usu.getId();

                    usuarios.add(new Usuario(usuario, correo, nombre, apellido, contrasenya, sDireccion, uid));
                }

                // Almacenando usuario correspondiente:
                for (Usuario usuario : usuarios) {
                    if (usuario.getId().compareTo(fbUser.getUid()) == 0) {
                        id = usuario.getUsuario();
                    }
                }

            }

            /**
             * Asegura que el usuario haya iniciado sesión:
             * @param databaseError
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Hubo un error al detectar el usuario actual.",
                        Toast.LENGTH_LONG);
            }
        });

    }

    /**
     * Al pulsar el botón añade a favoritos:
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAddfav){
            nomProd = favs.getSelectedItem().toString();

            // Si ha seleccionado producto:
            if(!TextUtils.isEmpty(nomProd)){
                fav = new Favs(nomProd, usuario);
                Añadirfavs(nomProd);
            }else {
            // Error:
            Toast.makeText(AnyadirFavoritos.this, "Introduce el producto que quieras añadir a favoritos." ,
                    Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método para añadir productos.
     *
     * @param producto
     */
    public void Añadirfavs(String producto){
        bbdd2 = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoFavs));

        // generación e inserción de clave:
        String clave = bbdd2.push().getKey();
        bbdd2.child(clave).setValue(fav);

        // Mensaje:
        Toast.makeText(AnyadirFavoritos.this, "El producto se ha añadido a favoritos.",
                Toast.LENGTH_SHORT).show();

    }
}
