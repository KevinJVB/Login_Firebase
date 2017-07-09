package com.firbaseexameple.kevin;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {


    //Clases: FirbaseAuth para que pueda ser registrado el usuario
    //Listener FirbaseAuth.AuthListener
    //FirebaseUser cacha todos los datos el usuario
    //Al escuchar FirbaseAuth.AuthListener si hace login o logut devuelve un callback FirebaseUser

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button btnCreateAccount;
    private Button btnSignIn;
    private EditText edtEmail;
    private EditText editPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Firebase Login");
        btnCreateAccount = (Button) findViewById(R.id.btnCrear);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        edtEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        inicializar();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtEmail.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "No puede dejar el correo vacio", Toast.LENGTH_SHORT).show();
                } else if (editPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Debe introducir una contraseña", Toast.LENGTH_SHORT).show();
                } else if (editPassword.getTextSize() < 6) {
                    Toast.makeText(MainActivity.this, "La contraseña debe ser mayor a 6 digitos", Toast.LENGTH_SHORT).show();

                } else {
                    createAccount(edtEmail.getText().toString(), editPassword.getText().toString());
                }

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigIn(edtEmail.getText().toString(), editPassword.getText().toString());

            }
        });
    }

    public void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance(); //Instancea el objeto
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getUid());
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getEmail());

                } else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");

                }
            }
        };
    }

    private void createAccount(final String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (firebaseAuth.getCurrentUser().getEmail().equals(email)) {
                    Toast.makeText(MainActivity.this, "Ya existe esta cuenta", Toast.LENGTH_SHORT).show();
                } else if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Creado exitosamente, puedes iniciar sesion", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No se pudo crear la cuenta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sigIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this,SecondActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this, "Iniciado correctametne", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Verifica tus datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
