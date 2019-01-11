package com.example.marta.fbandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * EXAMEN: Clase para visualizar los favoritos.
 */
public class Favoritos extends AppCompatActivity implements View.OnClickListener {
    // Elementos de view:
    private Spinner misFavs;
    private ListView lv;
    private Button anyadeFavs;

    // Base de datos:
    DatabaseReference bbdd, bbdd2;

    // Auxiliares:
    private ArrayList<String> listado = new ArrayList<>();
    private ArrayList<Favs> favoritos = new ArrayList<>();
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        // Elementos:
        misFavs = (Spinner) findViewById(R.id.spFavs);
        lv = (ListView) findViewById(R.id.lvProd);
        anyadeFavs = (Button) findViewById(R.id.btnAñade);

        // usuario actual:
        bbdd = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));
        bbdd.addValueEventListener(new ValueEventListener() {
            /**
             * Coger el usuario actual de entre todos.
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorremos todos  los Usuarios para despues almacenarlos en un ArrayList
                for(DataSnapshot datasnapshot : dataSnapshot.getChildren()){

                    Usuario oUsuario = datasnapshot.getValue(Usuario.class);
                    usuario = oUsuario.getUsuario();

                    ArrayAdapter<String> adaptador;
                    listado.add(usuario);

                    adaptador = new ArrayAdapter<>(Favoritos.this,android.R.layout.simple_list_item_1,listado);
                    misFavs.setAdapter(adaptador);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Hubo un error al detectar el usuario actual.",
                        Toast.LENGTH_LONG);
            }
        });

        // Acceso a favoritos:
        bbdd2 = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoFavs));
        bbdd2.addValueEventListener(new ValueEventListener() {
            /**
             * Lista de favoritos:
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adaptador;
                ArrayList<String> listadoString = new ArrayList<>();


                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Favs favorito = dataSnapshot.getValue(Favs.class);
                    String prod = favorito.getProductos();
                    String usu = favorito.getUsuario();

                    favoritos.add(new Favs(prod, usu));
                }
                // Iteramos el ArrayList para comparar dentro que elementos corresponden con lo que hemos insertado
                for (Favs f : favoritos) {
                    if (f.getUsuario().compareTo(usuario) == 0) {
                        listadoString.add(f.toString());
                    }
                }
                adaptador = new ArrayAdapter<>(Favoritos.this,android.R.layout.simple_list_item_1,listadoString);
                lv.setAdapter(adaptador);

            }

            /**
             *
             * @param databaseError
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Mensaje de error:
                Toast.makeText(Favoritos.this, "Hubo un problema al cargar los favoritos.",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * Va a activity de añadir a favoritos:
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(), AnyadirFavoritos.class);
        startActivity(i);
    }
}
