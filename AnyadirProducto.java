package com.example.marta.fbandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * Añade un producto
 */
public class AnyadirProducto extends AppCompatActivity {
    private EditText etNom, etDesc, etPre;
    private String idUsu;

    // Base de datos:
    private DatabaseReference bbdd,bbdd2;

    // ArrayLists:
    private ArrayList<Usuario> listado = new ArrayList<>();
    private ArrayList<String> listadoOp = new ArrayList<>();

    // Spinner:
    private Spinner spinGc;

    // Firebase:
    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_producto);

        mAuth = FirebaseAuth.getInstance();
        // usuario actual:
        fbUser = mAuth.getCurrentUser();

        etNom =  findViewById(R.id.etNombre);
        etDesc =  findViewById(R.id.edDesc);
        spinGc =  findViewById(R.id.spinnerCateg);
        etPre =  findViewById(R.id.edPrecio);

        listadoOp.add("tecnologıa");
        listadoOp.add("coches");
        listadoOp.add("hogar");

        // Adaptador:
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(AnyadirProducto.this,
                android.R.layout.simple_list_item_1, listadoOp);
        spinGc.setAdapter(adaptador);

        bbdd = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));
        // Añadir valores:
        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorrer usuarios:
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Usuario usu = datasnapshot.getValue(Usuario.class);

                    String usuario = usu.getUsuario();
                    String nombre = usu.getNombre();
                    String apellido = usu.getApellidos();
                    String correo = usu.getCorreo();
                    String contrasenya = usu.getContraseña();
                    String sDireccion = usu.getDireccion();
                    String uid = usu.getId();

                    listado.add(new Usuario(usuario, correo, nombre, apellido, contrasenya, sDireccion, uid));
                }

                // Almacenando usuario:
                for (Usuario elemento : listado) {
                    if (elemento.getId().compareTo(fbUser.getUid()) == 0) {
                        idUsu = elemento.getUsuario();
                    }
                }

            }
             @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Acción de añadir:
        final Button btnAñadir = findViewById(R.id.btnAnyadir);
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String sNom = etNom.getText().toString();
                final String sDesc = etDesc.getText().toString();
                final String sCategoria = spinGc.getSelectedItem().toString();
                final String sPrecio = etPre.getText().toString();


                // Hacemos una comprobacion sobre el si los campos no estan vacios
                if (!TextUtils.isEmpty(sNom) ||  !TextUtils.isEmpty(sDesc) || !TextUtils.isEmpty(sCategoria)  || !TextUtils.isEmpty(sPrecio)){
                    // Creamos el Objeto Producto y Introducimos los valores de editText, ademas el usuario actual
                    Producto oProducto = new Producto(sNom,sDesc,sCategoria,sPrecio,idUsu);
                    bbdd2 = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoProd));//Obtenemos el Nodo

                    String clave = bbdd2.push().getKey();//Generamos una clave para el Nodo

                    bbdd2.child(clave).setValue(oProducto);// Insertamaos el Producto en la clave que hemos creado

                    Toast.makeText(AnyadirProducto.this, "Se ha añadido el Producto." ,
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AnyadirProducto.this, "Deves Introducir todos los valores" ,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }


