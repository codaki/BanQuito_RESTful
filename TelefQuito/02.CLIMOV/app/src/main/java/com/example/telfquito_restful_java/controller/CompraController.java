package com.example.telfquito_restful_java.controller;

import android.util.Log;

import com.example.telfquito_restful_java.models.Carrito;
import com.example.telfquito_restful_java.models.FacturaModel;
import com.example.telfquito_restful_java.models.TablaModel;
import com.example.telfquito_restful_java.service.CompraService;
import com.example.telfquito_restful_java.service.RetrofitClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompraController {
    private static final String TAG = "CompraController";
    private CompraService compraService;

    public CompraController() {
        this.compraService = RetrofitClient.getClient().create(CompraService.class);
    }

    // Callback interface for all Compra API calls
    public interface CompraCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    /**
     * Comprar en efectivo
     */
    public void comprarEfectivo(Carrito carrito, String cedula, final CompraCallback<String> callback) {
        compraService.comprarEfectivo(carrito, cedula).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string(); // Convert ResponseBody to String
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading response", e);
                        callback.onError("Error reading response");
                    }
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Comprar a crédito
     */
    public void comprarCredito(Carrito carrito, String cedula, int plazoMeses, final CompraCallback<String> callback) {
        compraService.comprarCredito(carrito, cedula, plazoMeses).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string(); // Convert ResponseBody to a String
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading response", e);
                        callback.onError("Error reading response");
                    }
                } else {
                    Log.e(TAG, "Error: " + response.message());
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Obtener facturas de un cliente
     */
    public void obtenerFactura(String cedula, final CompraCallback<List<FacturaModel>> callback) {
        compraService.obtenerFactura(cedula).enqueue(new Callback<List<FacturaModel>>() {
            @Override
            public void onResponse(Call<List<FacturaModel>> call, Response<List<FacturaModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Error: " + response.message());
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FacturaModel>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Consultar tabla de amortización
     */
    public void consultarTablaAmortizacion(String cedula, final CompraCallback<List<TablaModel>> callback) {
        compraService.consultarTablaAmortizacion(cedula).enqueue(new Callback<List<TablaModel>>() {
            @Override
            public void onResponse(Call<List<TablaModel>> call, Response<List<TablaModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Error: " + response.message());
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TablaModel>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
