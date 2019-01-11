package com.example.marta.fbandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusquedaProducto extends AppCompatActivity {
    Spinner spinGu;
    DatabaseReference bbdd, bbdd2;
    private ArrayList<String> listado = new ArrayList<>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_producto);

        spinGu =  findViewById(R.id.spinnerProd);
        lv = findViewById(R.id.lvProd) ;

        // Cogemos la referencia del Nodo de Firebase
        bbdd = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));

        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorremos todos  los Usuarios para despues almacenarlos en un ArrayList
                for(DataSnapshot datasnapshot : dataSnapshot.getChildren()){

                    Usuario oUsuario = datasnapshot.getValue(Usuario.class);// Obtenemos el objeto de usuario
                    String usuario = oUsuario.getUsuario(); // Obtenemos el usuario del objeto Usuario

                    ArrayAdapter<String> adaptador;
                    listado.add(usuario);// Añadimos al ArrayList la usuario

                    adaptador = new ArrayAdapter<>(BusquedaProducto.this,android.R.layout.simple_list_item_1,listado);
                    spinGu.setAdapter(adaptador);// Adaptamos para que visualize el Select con sus opciones
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bbdd2 = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoProd));
        final Button btnBusquedaPorCategoriaProducto = findViewById(R.id.btnBuscCat);
        btnBusquedaPorCategoriaProducto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bbdd2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String sUsuario = spinGu.getSelectedItem().toString();
                        ArrayAdapter<String> adaptador;
                        ArrayList<Producto> listado = new ArrayList<>();
                        ArrayList<String> listadoString = new ArrayList<>();


                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            Producto oProducto = datasnapshot.getValue(Producto.class);
                            String sNombre = oProducto.getNombre();
                            String sDescripcion = oProducto.getDescripcion();
                            String sPrecio = oProducto.getPrecio();
                            String usuario = oProducto.getUsuario();
                            String sCategoria = oProducto.getCategoria();


                            listado.add(new Producto(sNombre, sDescripcion, sCategoria, sPrecio, usuario)); // Añadimos el nuevo objeto de Producto al ArrayList

                        }
                        // Iteramos el ArrayList para comparar dentro que elementos corresponden con lo que hemos insertado
                        for (Producto elemento : listado) {
                            if (elemento.getUsuario().compareTo(sUsuario) == 0) { // Comprovamos de que el producto sea del usuario seleccionado
                                listadoString.add(elemento.toString());// Añadimos el producto ArrayList
                            }
                        }
                        adaptador = new ArrayAdapter<>(BusquedaProducto.this,android.R.layout.simple_list_item_1,listadoString);
                        lv.setAdapter(adaptador);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    }

