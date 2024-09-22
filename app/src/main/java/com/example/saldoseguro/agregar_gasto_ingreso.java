package com.example.saldoseguro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class agregar_gasto_ingreso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_gasto_ingreso);



        // Lista de categorías Ejemplo
        String[] categorias = {"Alimentación", "Transporte", "Entretenimiento", "Salud", "Educación", "Otros"};
        // Lista de Cuentas Ejemplo
        String[] cuentas = {"Banco Estado", "General", "Ahorros"};
        // Lista Tipo de movimiento
        String[] tipoMovimiento = {"Entrada", "Salida"};


        Spinner spinnerCuentas = findViewById(R.id.spinnerCuenta);
        //Adaptador para llenar el spinner
        ArrayAdapter<String> adapterCuentas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cuentas);
        adapterCuentas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Asignar adaptador al spinner
        spinnerCuentas.setAdapter(adapterCuentas);


        Spinner spinnerCategorias = findViewById(R.id.spinnerCategorias);
        // Adaptador para llenar el spinner
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Asignar adaptador al spinner
        spinnerCategorias.setAdapter(adapterCategorias);

        Spinner spinnerMovimiento = findViewById(R.id.spinnerIngresoGasto);
        // Adaptador para llenar el spinner
        ArrayAdapter<String> adapterMovimiento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoMovimiento);
        adapterMovimiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Asignar adaptador al spinner
        spinnerMovimiento.setAdapter(adapterMovimiento);
    }
}