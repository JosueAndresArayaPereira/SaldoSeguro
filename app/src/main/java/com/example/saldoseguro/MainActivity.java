package com.example.saldoseguro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.buttonLogin);
        //nombre de usuario
        EditText nombreUserET = findViewById(R.id.editTextUsername);
        //contraseña de el usuario
        EditText passwordET = findViewById(R.id.editTextPassword);

        loginBtn.setOnClickListener(view -> {
            String username = nombreUserET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            login(username, password);
        });

    }

    //button create account
    public void create_account(View view){
        Intent create_account_intent = new Intent(this, create_account.class);
        startActivity(create_account_intent);
    }




    public void login(String userU, String passwordU) {
        if (userU.isEmpty() || passwordU.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean userFound = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String password = document.getString("password");
                            String userID = document.getId();
                            if (userU.equals(nombre) && passwordU.equals(password)) {
                                userFound = true;
                                Intent createMainScreen = new Intent(this, pantalla_principal.class);
                                createMainScreen.putExtra("USER_ID", userID);
                                startActivity(createMainScreen);
                                break;
                            }
                        }
                        if (!userFound) {
                            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w("Error", "Error al obtener documentos.", task.getException());
                    }
                });
    }


}