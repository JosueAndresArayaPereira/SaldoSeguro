package com.example.saldoseguro;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class historial_movimientos extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DataBaseHelper dbHelper;
    private TableLayout tableLayoutMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_movimientos);
        tableLayoutMovimientos = findViewById(R.id.tableLayoutMovimientos); // Referencia al TableLayout
        obtenerUltimosDiezMovimientos();
    }

    public void obtenerUltimosDiezMovimientos() {
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();

        db.collection("movimientos")
                .whereEqualTo("usuario", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Crear una lista temporal para almacenar los movimientos
                        List<QueryDocumentSnapshot> documentos = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            documentos.add(document);
                        }

                        // Ordenar los documentos por fecha de forma descendente
                        Collections.sort(documentos, (d1, d2) -> {
                            String fecha1 = d1.getString("fecha");
                            String fecha2 = d2.getString("fecha");
                            return fecha2.compareTo(fecha1); // Orden descendente
                        });

                        // Iterar solo sobre los primeros diez documentos después de ordenar
                        for (int i = 0; i < Math.min(10, documentos.size()); i++) {
                            QueryDocumentSnapshot document = documentos.get(i);

                            // Obtener los datos del documento
                            String categoria = document.getString("categoria");
                            double cantidad = document.getDouble("cantidad");
                            String fechaDate = document.getString("fecha");

                            // Agregar una nueva fila a la tabla
                            agregarFilaATabla(categoria, cantidad, fechaDate);
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }


    private void agregarFilaATabla(String categoria, double cantidad, String fecha) {
        TableRow fila = new TableRow(this);

        // Crear y configurar las celdas de la fila
        TextView textViewCategoria = new TextView(this);
        textViewCategoria.setText(categoria);
        textViewCategoria.setPadding(8, 8, 8, 8);
        fila.addView(textViewCategoria);

        TextView textViewCantidad = new TextView(this);
        textViewCantidad.setText(String.valueOf(cantidad));
        textViewCantidad.setPadding(8, 8, 8, 8);
        fila.addView(textViewCantidad);

        TextView textViewFecha = new TextView(this);
        textViewFecha.setText(fecha);
        textViewFecha.setPadding(8, 8, 8, 8);
        fila.addView(textViewFecha);


        // Agregar la fila al TableLayout
        tableLayoutMovimientos.addView(fila);
    }
}
