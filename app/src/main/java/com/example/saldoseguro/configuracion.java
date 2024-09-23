package com.example.saldoseguro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class configuracion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
    }

    public void crear_modificar_cuenta(View view){
        Intent intentoVentanaCrearModificarCuenta = new Intent(this, cuenta_crear_modificar.class);
        startActivity(intentoVentanaCrearModificarCuenta);
    }
}