package com.example.marta.fbandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BorrarProducto extends AppCompatActivity {
    Spinner spinGp;
    private String idUsuario;
    DatabaseReference bbdd, bbdd2;
    private ArrayList<Usuario> listadoUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_producto);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();// Obtenemos el valor del usuario logueado
        spinGp =  findViewById(R.id.spinner);

        // Cogemos la referencia del Nodo de Firebase
        bbdd = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));

        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorremos todos  los Usuarios para despues almacenarlos en un ArrayList
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Usuario oUsuario = datasnapshot.getValue(Usuario.class);

                    String usuario = oUsuario.getUsuario();
                    String nombre = oUsuario.getNombre();
                    String apellido = oUsuario.getApellidos();
                    String correo = oUsuario.getCorreo();
                    String contrasenya = oUsuario.getContraseña();
                    String sDireccion = oUsuario.getDireccion();
                    String userid = oUsuario.getId();

                    // Añadimos toda al ArrayLiadaptadorst la informacion de cada Usuario
                    listadoUsuario.add(new Usuario(usuario, correo, nombre, apellido, contrasenya, sDireccion, userid));
                }
                // Iteramos el ArrayList para mostrar la informacion de cada objeto
                for (Usuario elemento : listadoUsuario) {
                    // En el IF comprovamos de que el user logueado actualmente coincide con el que este en el ArrayList, y si esta que almacene/sette el texto de los valores
                    if (elemento.getId().compareTo(user.getUid()) == 0) {
                        idUsuario = elemento.getUsuario();// obtenemos el Usuario loqueado
                    }
                }
                bbdd2 = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoProd));//Obtenemos el Nodo Prodcuto

                bbdd2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        ArrayAdapter<String> adaptador;
                        ArrayList<String> listadoOptions = new ArrayList<>();


                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            Producto oProducto = datasnapshot.getValue(Producto.class);
                            String sNombre = oProducto.getNombre();
                            String sUsuario = oProducto.getUsuario();

                            if (sUsuario.equals(idUsuario)){
                                listadoOptions.add(sNombre);
                            }
                        }
                        adaptador = new ArrayAdapter<>(BorrarProducto.this,android.R.layout.simple_list_item_1,listadoOptions);
                        spinGp.setAdapter(adaptador);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        /**
         * Borramos el elemento
         */
        final Button btnBorrar = findViewById(R.id.btnBorrar);// Obtenemos la referencia el boton Eliminar
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Optenemos el Valor seleccionado del Spiner
                final String sNombre = spinGp.getSelectedItem().toString();

                // Hacemos una comprobacion sobre el si los campos no estan vacios
                if (!TextUtils.isEmpty(sNombre) ){

                    Query q = bbdd2.orderByChild("nombre").equalTo(sNombre); // Hacemos una Busqueda por nombre del Usuario
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                // clave obtiene el id de la fila del JSON
                                String clave =  datasnapshot.getKey(); // obtenemos la clave de la referencia

                                DatabaseReference ref = bbdd2.child(clave);// Pillamos la referencia
                                ref.removeValue();// Eliminamos
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }


    }


