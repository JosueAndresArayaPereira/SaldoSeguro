package com.example.saldoseguro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class agregar_gasto_ingreso extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DataBaseHelper dbHelper;
    private ArrayList<String> nombresCuentas = new ArrayList<>(); // Lista de nombres de cuentas
    private ArrayList<String> nombresCategorias = new ArrayList<>(); // Lista de nombres de categorías
    private Spinner spinnerCategorias, spinnerCuenta, spinnerIngresoGasto;
    private EditText editTextCantidad;
    private Button buttonIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_gasto_ingreso);

        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        spinnerCuenta = findViewById(R.id.spinnerCuenta);
        spinnerIngresoGasto = findViewById(R.id.spinnerIngresoGasto);
        editTextCantidad = findViewById(R.id.editTextCantidad);
        buttonIngresar = findViewById(R.id.buttonIngresar);

        // Obtener el ID del usuario en sesión
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();

        // Llamar al método para cargar las cuentas en el Spinner
        cargarCuentasEnSpinner(user);

        // Configurar el botón para llamar a insertarMovimiento al hacer clic
        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarMovimiento();
            }
        });

        cargarDatosDefaultSpinners();
    }

    private void cargarCuentasEnSpinner(String user) {
        db.collection("cuentas")
                .whereEqualTo("usuario", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            nombresCuentas.clear(); // Limpiar lista de nombres de cuentas

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreCuenta = document.getString("nombre");

                                // Verificar si el campo "saldo" existe y no es null
                                Long saldoCuentaLong = document.getLong("Saldo");
                                if (saldoCuentaLong != null) {
                                    int saldoCuenta = saldoCuentaLong.intValue();

                                    // Agregar el nombre de la cuenta al ArrayList
                                    nombresCuentas.add(nombreCuenta);

                                    // Verificar si el saldo es negativo
                                    if (saldoCuenta < 0) {
                                        // Si el saldo es negativo, muestra una advertencia o indica en rojo
                                        Toast.makeText(agregar_gasto_ingreso.this,
                                                "Advertencia: La cuenta '" + nombreCuenta + "' tiene saldo negativo.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.w("cuentas", "El campo 'saldo' es null para la cuenta: " + nombreCuenta);
                                }
                            }


                            // Crear y asignar el adaptador del Spinner con la lista de nombres de cuentas
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(agregar_gasto_ingreso.this,
                                    android.R.layout.simple_spinner_item, nombresCuentas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCuenta.setAdapter(adapter);
                        } else {
                            Log.d("cuentas", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("categorias")
                .whereEqualTo("usuario", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        nombresCategorias.clear(); // Limpiar lista de nombres de categorías
                        if (task.getResult().isEmpty()) {
                            Toast.makeText(this, "No hay categorías registradas", Toast.LENGTH_SHORT).show();
                            nombresCategorias.add("general");
                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreCategoria = document.getString("nombre");
                                nombresCategorias.add(nombreCategoria);
                            }
                        }

                    } else {
                        Log.d("categorias", "Error getting documents: ", task.getException());
                    }

                    // Crear y asignar el adaptador del Spinner con la lista de nombres de categorías
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(agregar_gasto_ingreso.this,
                            android.R.layout.simple_spinner_item, nombresCategorias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategorias.setAdapter(adapter);

                });
    }
    private void insertarMovimiento() {
        // Obtener los valores de los elementos de la interfaz
        String categoria = spinnerCategorias.getSelectedItem().toString();
        String cuenta = spinnerCuenta.getSelectedItem().toString();
        String tipoMovimiento = spinnerIngresoGasto.getSelectedItem().toString();

        // Validar que el campo de cantidad no esté vacío
        String cantidadTexto = editTextCantidad.getText().toString().trim();
        if (cantidadTexto.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir cantidad a número (double)
        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la ID del usuario en sesión
        String user = dbHelper.obtenerSesion();

        // Crear un mapa con los datos a insertar en Firestore
        Map<String, Object> movimiento = new HashMap<>();
        movimiento.put("categoria", categoria);
        movimiento.put("cuenta", cuenta);
        movimiento.put("tipoMovimiento", tipoMovimiento);
        movimiento.put("cantidad", cantidad);
        movimiento.put("usuario", user); // Relacionar movimiento con usuario

        // Insertar el documento en Firestore en la colección "movimientos"
        db.collection("movimientos")
                .add(movimiento)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Movimiento insertado exitosamente", Toast.LENGTH_SHORT).show();
                    // Limpiar el campo de cantidad después de la inserción exitosa
                    editTextCantidad.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al insertar movimiento", Toast.LENGTH_SHORT).show();
                });

        // Llamar al método para descontar o agregar la cantidad a la cuenta
        descontar_agregar();
    }

    public void descontar_agregar (){
        String cuenta = spinnerCuenta.getSelectedItem().toString();
        String tipoMovimiento = spinnerIngresoGasto.getSelectedItem().toString();

        // Validar que el campo de cantidad no esté vacío
        String cantidadTexto = editTextCantidad.getText().toString().trim();
        if (cantidadTexto.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir cantidad a número (double)
        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la ID del usuario en sesión
        String user = dbHelper.obtenerSesion();

        db.collection("cuentas")
                .whereEqualTo("usuario", user)
                .whereEqualTo("nombre", cuenta)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long saldoCuentaLong = document.getLong("Saldo");
                            if (saldoCuentaLong != null) {
                                int saldoCuenta = saldoCuentaLong.intValue();

                                if (tipoMovimiento.equals("Gasto")) {
                                    saldoCuenta -= cantidad;
                                } else {
                                    saldoCuenta += cantidad;
                                }

                                // Actualizar el saldo de la cuenta en Firestore
                                db.collection("cuentas")
                                        .document(document.getId())
                                        .update("Saldo", saldoCuenta)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Saldo actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Error al actualizar saldo", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Log.w("cuentas", "El campo 'saldo' es null para la cuenta: " + cuenta);
                            }
                        }
                    } else {
                        Log.d("cuentas", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void cargarDatosDefaultSpinners() {
        // Crear y asignar el adaptador del Spinner de Categorías
        String categorias[] = {"Alimentación", "Transporte", "Salud", "Educación", "Entretenimiento", "Otros"};
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapterCategorias);

        // Crear y asignar el adaptador del Spinner de Ingreso/Gasto
        String ingresoGasto[] = {"Ingreso", "Gasto"};
        ArrayAdapter<String> adapterIngresoGasto = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ingresoGasto);
        adapterIngresoGasto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIngresoGasto.setAdapter(adapterIngresoGasto);
    }

}
