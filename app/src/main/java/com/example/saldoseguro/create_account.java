package com.example.saldoseguro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class create_account extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button crearCuentaBtn = findViewById(R.id.buttonSignUp);

        // Obtener referencias a los EditText
        EditText usernameTextV = findViewById(R.id.editTextUsername);
        EditText emailTextV = findViewById(R.id.editTextEMail);
        EditText passwordTextV = findViewById(R.id.editTextPassword);

        crearCuentaBtn.setOnClickListener(view -> {
            // Obtener los datos cuando se hace clic en el botón
            String username = usernameTextV.getText().toString().trim();
            String email = emailTextV.getText().toString().trim();
            String password = passwordTextV.getText().toString().trim();

            // Validación simple
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                guardarCuenta(username, email, password);
            }
        });
    }

    public void guardarCuenta(String nombre, String email, String password) {
        Log.i("Datos", nombre + " " + email + " " + password);

        // Crear un mapa para los datos
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombre", nombre);
        usuario.put("email", email);
        usuario.put("password", password);

        // Agregar a la colección "usuarios"
        db.collection("usuarios")
                .add(usuario)
                .addOnSuccessListener(documentReference -> {
                    String userId = documentReference.getId(); // Obtener el ID del documento creado
                    crearCuentaEfectivo(userId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al crear la cuenta :c", Toast.LENGTH_SHORT).show();
                });
    }


    public void crearCuentaEfectivo(String userID){

        Map<String, Object> cuenta = new HashMap<>();
        cuenta.put("nombre", "efectivo");
        cuenta.put("Saldo", 0);
        cuenta.put("usuario", userID);

        db.collection("cuentas")
                .add(cuenta)

                .addOnSuccessListener(documentReference -> {
                    finish();
                })
                .addOnFailureListener(e -> {

                });
    }
}
