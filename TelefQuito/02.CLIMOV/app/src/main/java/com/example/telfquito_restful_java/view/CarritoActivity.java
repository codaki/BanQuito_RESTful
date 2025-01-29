package com.example.telfquito_restful_java.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.models.Carrito;
import com.example.telfquito_restful_java.models.CarritoSingleton;
import com.example.telfquito_restful_java.models.TelefonoCarrito;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private Carrito carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Retrieve the singleton carrito instance
        carrito = CarritoSingleton.getInstance();

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with a listener for quantity changes
        adapter = new CarritoAdapter(carrito.getTelefonos(), this, (position, newQuantity) -> {
            TelefonoCarrito telefonoCarrito = carrito.getTelefonos().get(position);
            telefonoCarrito.setCantidad(newQuantity);

            if (newQuantity == 0) {
                carrito.getTelefonos().remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(this, "Item removed from the carrito", Toast.LENGTH_SHORT).show();
            } else {
                adapter.notifyItemChanged(position);
                Toast.makeText(this, "Quantity updated: " + newQuantity, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Back button to return to TelefonosActivity
        findViewById(R.id.btnBackToTelefonos).setOnClickListener(v -> finish());

        // Proceed to VenderActivity
        findViewById(R.id.btnProceed).setOnClickListener(v -> {
            if (carrito.getTelefonos().isEmpty()) {
                Toast.makeText(this, "Carrito is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CarritoActivity.this, VenderActivity.class);
            intent.putExtra("carrito", carrito);
            startActivity(intent);
        });
    }
}
