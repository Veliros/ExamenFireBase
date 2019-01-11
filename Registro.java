package com.example.marta.fbandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Clase de registro:
 */
public class Registro extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private EditText etCorreo, etPass, etNom, etApe, etDir, etUsu;
    private Button btnRegistro;
    private DatabaseReference dbr;
    private Usuario u;
    private int cont;

    private String email, password, nombre, apellidos, direccion, usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etCorreo = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        etNom = (EditText) findViewById(R.id.etNombre);
        etApe = (EditText) findViewById(R.id.etApe);
        etDir = (EditText) findViewById(R.id.etDir);
        etUsu = (EditText) findViewById(R.id.etUsuario);

        cont = 0;

        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etCorreo.getText().toString();
                password = etPass.getText().toString();
                nombre = etNom.getText().toString();
                apellidos = etApe.getText().toString();
                usuario = etUsu.getText().toString();
                direccion = etDir.getText().toString();

                dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu) );

                // En esta línea lo que hacemos es comprobar de que hay un usuario en la BD igual
                Query q = dbr.orderByChild(getString(R.string.usuario)).equalTo(usuario);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            cont++;
                        }

                        // Realizamos el registro si no hay usuario:
                        if (cont == 0) registro(nombre, apellidos, password, email, usuario, direccion);
                        else{
                            cont = 0;
                            Toast.makeText(Registro.this, "El usuario ya existe." ,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Registro.this, "Ha habido un error en el registro." ,
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    private void registro(final String mNombre, final String mApellidos, final String mPassword, final String mEmail,
                          final String mUsuario, final String mDireccion) {
        mAuth = FirebaseAuth.getInstance();
        Log.d("puta", "Variables: " + nombre + usuario);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Registro.this, "Registro creado. " + user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            // Desconecta:
                            mAuth.signOut();

                            // Nuevo usuario:
                            u = new Usuario(user.getUid(), mUsuario, mPassword, mNombre, mApellidos,
                                    mDireccion, mEmail);

                            //Genera clave:
                            String clave = dbr.push().getKey();
                            // Añadida clave a usuario:
                            dbr.child(clave).setValue(u);

                        } else {
                            Toast.makeText(Registro.this, "Registro fallido.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

}
