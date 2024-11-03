package com.example.saldoseguro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class cuenta_crear_modificar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_crear_modificar);

        Button crearCuentaBtn = findViewById(R.id.buttonCrearCuenta);
        crearCuentaBtn.setOnClickListener(view -> {
            EditText nombreCuentaET = findViewById(R.id.editTextNombreCuenta);
            String nombreCuenta = nombreCuentaET.getText().toString().trim();
            EditText saldoCuentaET = findViewById(R.id.editTextDinero);
            int saldoCuenta = Integer.parseInt(saldoCuentaET.getText().toString().trim());
            crearCuenta(nombreCuenta, saldoCuenta);
        });
    }

    public void crearCuenta (String nombreCuenta, int saldoCuenta){
        if (nombreCuenta.isEmpty()) {
            Toast.makeText(this, "Porfavor ingresa un nombre para la cuenta", Toast.LENGTH_SHORT).show();
            return;
        }
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();
        Map<String, Object> cuenta = new HashMap<>();
        cuenta.put("nombre", nombreCuenta);
        cuenta.put("Saldo", saldoCuenta);
        cuenta.put("usuario", user);

        db.collection("cuentas")
                .add(cuenta)

                .addOnSuccessListener(documentReference -> {
                    finish();
                })
                .addOnFailureListener(e -> {

                });
    }
}