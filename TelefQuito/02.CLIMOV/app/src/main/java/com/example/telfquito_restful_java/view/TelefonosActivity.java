package com.example.telfquito_restful_java.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.controller.TelefonoController;
import com.example.telfquito_restful_java.models.Carrito;
import com.example.telfquito_restful_java.models.CarritoSingleton;
import com.example.telfquito_restful_java.models.TelefonoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TelefonosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TelefonosAdapter adapter;
    private TelefonoController telefonoController;
    private Carrito carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefonos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carrito = CarritoSingleton.getInstance();

        // Use the controller instead of the service
        telefonoController = new TelefonoController();
        loadTelefonos();

        // Floating action button for the cart
        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(TelefonosActivity.this, CarritoActivity.class);
            intent.putExtra("carrito", carrito);
            startActivity(intent);
        });

        findViewById(R.id.btn_AgregarTelefonos).setOnClickListener(v -> {
            Intent intent = new Intent(TelefonosActivity.this, AddEditTelefonoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnConsultarCreditos).setOnClickListener(v -> {
            Intent intent = new Intent(TelefonosActivity.this, TablaActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTelefonos();
    }

    private void loadTelefonos() {
        telefonoController.getAllTelefonos(new TelefonoController.TelefonoCallback<List<TelefonoModel>>() {
            @Override
            public void onSuccess(List<TelefonoModel> result) {
                adapter = new TelefonosAdapter(result, TelefonosActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(TelefonosActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toggleDisponibilidad(TelefonoModel telefono) {
        telefono.setDisponible(telefono.getDisponible() == 1 ? 0 : 1);
        telefonoController.updateTelefono(telefono, new TelefonoController.TelefonoCallback<String>() {
            @Override
            public void onSuccess(String result) {
                loadTelefonos(); // Refresh the list after updating disponibilidad
                Toast.makeText(TelefonosActivity.this, "Teléfono actualizado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(TelefonosActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
