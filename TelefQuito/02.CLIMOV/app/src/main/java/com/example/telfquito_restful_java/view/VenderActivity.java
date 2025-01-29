package com.example.telfquito_restful_java.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.controller.CompraController;
import com.example.telfquito_restful_java.controller.TelefonoController;
import com.example.telfquito_restful_java.models.Carrito;
import com.example.telfquito_restful_java.models.CarritoSingleton;
import com.example.telfquito_restful_java.models.TelefonoCarrito;
import com.example.telfquito_restful_java.models.TelefonoModel;

import java.util.ArrayList;
import java.util.List;

public class VenderActivity extends AppCompatActivity {

    private Spinner spinnerOperacion;
    private EditText etCedulaCliente, etMeses;
    private TextView tvDescuento, tvTotal;
    private Button btnVerificarCredito, btnPagar;
    private RecyclerView recyclerViewCompra;
    private CompraAdapter adapter;
    private LinearLayout layoutDiferido;

    private List<TelefonoCarrito> carritoTelefonos;
    private List<TelefonoModel> telefonoModels;
    private TelefonoController telefonoController;
    private CompraController compraController;

    private static final String TAG = "VenderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        spinnerOperacion = findViewById(R.id.spinnerOperacion);
        etCedulaCliente = findViewById(R.id.etCedulaCliente);
        etMeses = findViewById(R.id.etMeses);
        tvDescuento = findViewById(R.id.tvDescuento);
        tvTotal = findViewById(R.id.tvTotal);
        btnVerificarCredito = findViewById(R.id.btnVerificarCredito);
        btnPagar = findViewById(R.id.btnPagar);
        recyclerViewCompra = findViewById(R.id.recyclerViewCompra);
        layoutDiferido = findViewById(R.id.layoutDiferido);

        // Initialize controllers
        telefonoController = new TelefonoController();
        compraController = new CompraController();

        // Fetch carrito data
        carritoTelefonos = CarritoSingleton.getInstance().getTelefonos();
        if (carritoTelefonos.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "El carrito está vacío");
            return;
        }

        telefonoModels = new ArrayList<>();

        // Setup RecyclerView
        recyclerViewCompra.setLayoutManager(new LinearLayoutManager(this));
        fetchTelefonos();

        // Handle spinner selection
        spinnerOperacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Efectivo
                    layoutDiferido.setVisibility(View.GONE);
                    calculateDiscountedTotal(0.42); // 42% discount for cash
                } else { // Crédito
                    layoutDiferido.setVisibility(View.VISIBLE);
                    calculateDiscountedTotal(0); // No discount
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // Handle credit verification
        btnVerificarCredito.setOnClickListener(v -> {
            String mesesText = etMeses.getText().toString();
            if (mesesText.isEmpty() || Integer.parseInt(mesesText) < 3 || Integer.parseInt(mesesText) > 18) {
                Toast.makeText(this, "El plazo debe ser entre 3 y 18 meses", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Plazo válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle payment action
        btnPagar.setOnClickListener(v -> processPayment());
    }

    private void calculateDiscountedTotal(double discountRate) {
        double total = 0;

        for (int i = 0; i < telefonoModels.size(); i++) {
            TelefonoCarrito carrito = carritoTelefonos.get(i);
            TelefonoModel detalles = telefonoModels.get(i);
            total += Double.parseDouble(detalles.getPrecio()) * carrito.getCantidad();
        }

        total *= (1 - discountRate); // Apply discount
        tvDescuento.setText(String.format("%.0f%%", discountRate * 100));
        tvTotal.setText(String.format("Total: $%.2f", total));
    }

    private void fetchTelefonos() {
        Log.d(TAG, "Fetching telefonos for carrito...");
        adapter = new CompraAdapter(carritoTelefonos, telefonoModels);
        recyclerViewCompra.setAdapter(adapter);

        for (TelefonoCarrito item : carritoTelefonos) {
            Log.d(TAG, "Fetching details for telefono ID: " + item.getTelefonoId());

            telefonoController.getTelefonoById(item.getTelefonoId(), new TelefonoController.TelefonoCallback<TelefonoModel>() {
                @Override
                public void onSuccess(TelefonoModel result) {
                    Log.d(TAG, "Loaded telefono: " + result.getNombre());
                    telefonoModels.add(result);
                    adapter.notifyItemChanged(telefonoModels.size() - 1);
                    calculateDiscountedTotal(spinnerOperacion.getSelectedItemPosition() == 0 ? 0.42 : 0);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(VenderActivity.this, "Error al cargar el teléfono: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void processPayment() {
        String cedula = etCedulaCliente.getText().toString();
        if (cedula.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la cédula del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        int plazoMeses = spinnerOperacion.getSelectedItemPosition() == 1 ? Integer.parseInt(etMeses.getText().toString()) : 0;

        // Wrap the list of TelefonoCarrito into a Carrito object
        Carrito carrito = new Carrito();
        carrito.setTelefonos(CarritoSingleton.getInstance().getTelefonos());

        if (carrito.getTelefonos().isEmpty()) {
            Toast.makeText(this, "El carrito está vacío. No se puede procesar la compra.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerOperacion.getSelectedItemPosition() == 0) { // Efectivo
            compraController.comprarEfectivo(carrito, cedula, new CompraController.CompraCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    CarritoSingleton.clearCarrito();
                    startFacturaActivity(cedula);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(VenderActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else { // Crédito
            compraController.comprarCredito(carrito, cedula, plazoMeses, new CompraController.CompraCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    CarritoSingleton.clearCarrito();
                    startFacturaActivity(cedula);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(VenderActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void startFacturaActivity(String cedula) {
        Intent intent = new Intent(this, FacturaActivity.class);
        intent.putExtra("cedula", cedula);
        startActivity(intent);
        finish();
    }
}
