package com.example.saldoseguro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class pantalla_principal extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        obtenerCuentas();
    }

    public void Agregar_Quitar_Gasto(View view){
        Intent agregarQuitarGasto = new Intent(this, agregar_gasto_ingreso.class);
        startActivity(agregarQuitarGasto);
    }
    public void VerMovimientos(View view){
        Intent verMovimientoIntent = new Intent(this, historial_movimientos.class);
        startActivity(verMovimientoIntent);
    }

    public  void Configuracion(View view){
        Intent configuracionIntent = new Intent(this, configuracion.class);
        startActivity(configuracionIntent);
    }

    public void obtenerCuentas(){
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();
        db.collection("cuentas")
                .whereEqualTo("usuario", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("cuentas", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("cuentas", "Error getting documents: ", task.getException());
                    }
                });
    }
}