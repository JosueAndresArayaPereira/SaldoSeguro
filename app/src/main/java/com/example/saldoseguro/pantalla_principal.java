package com.example.saldoseguro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.Locale;

public class pantalla_principal extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        obtenerCuentas(findViewById(R.id.textView2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerCuentas(findViewById(R.id.textView2));
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
    public void obtenerCuentas(TextView textViewDineroGeneral) {
        final int[] dineroGeneral = {0};
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();
        db.collection("cuentas")
                .whereEqualTo("usuario", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        LinearLayout linearLayoutCuentas = findViewById(R.id.linearLayoutCuentas);
                        linearLayoutCuentas.removeAllViews();

                        // Contador para verificar si hay cuentas
                        boolean hayCuentas = false;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            hayCuentas = true; // Si hay al menos una cuenta, cambia a true
                            String nombreCuenta = document.getString("nombre");
                            int saldoCuenta = document.getLong("Saldo").intValue();

                            dineroGeneral[0] += saldoCuenta;

                            View cuentaView = getLayoutInflater().inflate(R.layout.layout_cuenta, linearLayoutCuentas, false);

                            // Obtener referencias a los TextViews y establecer el texto
                            TextView textViewNombre = cuentaView.findViewById(R.id.textViewNombreCuenta);
                            TextView textViewSaldo = cuentaView.findViewById(R.id.textViewSaldoCuenta);

                            textViewNombre.setText(nombreCuenta);
                            // Usar el método formatearDinero para mostrar el saldo formateado
                            textViewSaldo.setText(formatearDinero(saldoCuenta));

                            linearLayoutCuentas.addView(cuentaView);
                        }

                        // Actualizar el TextView con el total de dinero general, formateado
                        textViewDineroGeneral.setText(formatearDinero(dineroGeneral[0]));

                    } else {
                        Log.d("cuentas", "Error getting documents: ", task.getException());
                    }
                });
    }

    // Método para formatear el dinero
    public String formatearDinero(int dinero) {
        NumberFormat formato = NumberFormat.getInstance(Locale.forLanguageTag("es-CL"));
        return "$" + formato.format(dinero);
    }
}