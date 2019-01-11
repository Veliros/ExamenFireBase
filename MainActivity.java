package com.example.marta.fbandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author marta
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText correo, pass;

    private String email, password;
    Button btnReg, btnLogin;

    /**
     *  onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.correo);
        pass = (EditText) findViewById(R.id.pass);

        // Formulario de registro:
        btnReg = (Button) findViewById(R.id.btnRegistro);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
            }
        });

        // Login:
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = correo.getText().toString();
                password = pass.getText().toString();

                login(email, password);
            }
        });
    }

    /**
     * Método para el login:
     *
     * @param mCorreo
     * @param mPass
     */
    public void login(String mCorreo, String mPass){
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(mCorreo, mPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Autenticación con éxito.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);

                            Intent i = new Intent(getApplicationContext(), QuickTrade.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(MainActivity.this, "Autenticación fallida.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    /**
     * En caso de que este vacío mostrar el botón de registro:
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        if(user != null){
            findViewById(R.id.btnRegistro).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnRegistro).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Al empezar app:
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }
}
