package com.example.telfquito_restful_java.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.controller.CompraController;
import com.example.telfquito_restful_java.models.FacturaModel;

import java.util.List;

public class FacturaActivity extends AppCompatActivity {

    private TextView tvNombreCliente, tvMarcaTelefono, tvNombreTelefono, tvPreciofinal, tvFecha, tvFormaPago, tvBanco, tvCedula, tvDescuento;
    private Button btnAceptar;
    private CompraController compraController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        tvNombreCliente = findViewById(R.id.tvNombreCliente);
        tvMarcaTelefono = findViewById(R.id.tvMarcaTelefono);
        tvNombreTelefono = findViewById(R.id.tvNombreTelefono);
        tvPreciofinal = findViewById(R.id.tvPreciofinal);
        tvFecha = findViewById(R.id.tvFecha);
        tvFormaPago = findViewById(R.id.tvFormaPago);
        tvBanco = findViewById(R.id.tvBancoV);
        btnAceptar = findViewById(R.id.btnAceptar);
        tvCedula = findViewById(R.id.tvCedulaCliente);
        tvDescuento = findViewById(R.id.tvDescuento);

        // Initialize controller
        compraController = new CompraController();

        // Retrieve cedula from intent
        String cedula = getIntent().getStringExtra("cedula");
        if (cedula != null) {
            fetchFactura(cedula);
        } else {
            Toast.makeText(this, "Error: No se proporcionó una cédula", Toast.LENGTH_SHORT).show();
        }

        // Return to main screen
        btnAceptar.setOnClickListener(v -> {
            Intent intent = new Intent(FacturaActivity.this, TelefonosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void fetchFactura(String cedula) {
        compraController.obtenerFactura(cedula, new CompraController.CompraCallback<List<FacturaModel>>() {
            @Override
            public void onSuccess(List<FacturaModel> facturas) {
                if (!facturas.isEmpty()) {
                    FacturaModel latestFactura = facturas.get(facturas.size() - 1);
                    tvNombreCliente.setText(latestFactura.getNombreCliente());
                    tvMarcaTelefono.setText(latestFactura.getMarcaTelefono());
                    tvNombreTelefono.setText(latestFactura.getNombreTelefono());
                    tvPreciofinal.setText(String.format("$%.2f", latestFactura.getPreciofinal()));
                    tvFecha.setText(latestFactura.getFecha());
                    tvFormaPago.setText(latestFactura.getFormaPago());
                    tvCedula.setText(cedula);
                    tvDescuento.setText(String.format("%.0f%%", latestFactura.getDescuento()));

                    if ("Efectivo".equals(latestFactura.getFormaPago())) {
                        tvBanco.setVisibility(View.GONE);
                    } else {
                        tvBanco.setText("Banco BanQuito");
                    }
                } else {
                    Toast.makeText(FacturaActivity.this, "No se encontraron facturas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(FacturaActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
