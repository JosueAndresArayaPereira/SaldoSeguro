package com.example.saldoseguro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Categorias extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        Button crearCategoriaBtn = findViewById(R.id.buttonIngresar);
        crearCategoriaBtn.setOnClickListener(view -> {
            EditText nombreCategoriaET = findViewById(R.id.editTextNombreCategoria);
            String nombreCategoria = nombreCategoriaET.getText().toString().trim();
            crearCategoria(nombreCategoria);
        });

        obtenerCategorias();
    }

    public void crearCategoria (String nombreCategoria){
        if (nombreCategoria.isEmpty()) {
            Toast.makeText(this, "Porfavor ingresa un nombre para la categoria", Toast.LENGTH_SHORT).show();
            return;
        }
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();
        Map<String, Object> categoria = new HashMap<>();
        categoria.put("nombre", nombreCategoria);
        categoria.put("usuario", user);

        db.collection("categorias")
                .add(categoria)

                .addOnSuccessListener(documentReference -> {
                    finish();
                })
                .addOnFailureListener(e -> {

                });
    }

    public void obtenerCategorias(){
        dbHelper = new DataBaseHelper(this);
        String user = dbHelper.obtenerSesion();
        db.collection("categorias")
                .whereEqualTo("usuario", user)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        LinearLayout categoriasLayout = findViewById(R.id.categoriasLayout);
                        categoriasLayout.removeAllViews();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            View categoriaView = getLayoutInflater().inflate(R.layout.layout_categoria, null);

                            TextView nombreCategoria = categoriaView.findViewById(R.id.textView6);
                            nombreCategoria.setText(document.getString("nombre"));

                            Button eliminarCategoriaBtn = categoriaView.findViewById(R.id.buttonEliminar);
                            eliminarCategoriaBtn.setOnClickListener(view -> {
                                db.collection("categorias").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            obtenerCategorias();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Categorias", "Error deleting document", e);
                                        });
                            });

                            categoriasLayout.addView(categoriaView);
                        }

                    } else {
                        Log.d("Categorias", "Error getting documents: ", task.getException());
                    }
                });
    }
}