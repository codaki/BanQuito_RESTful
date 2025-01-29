package com.example.telfquito_restful_java.controller;

import android.util.Log;

import com.example.telfquito_restful_java.models.TelefonoModel;
import com.example.telfquito_restful_java.service.RetrofitClient;
import com.example.telfquito_restful_java.service.TelefonoService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelefonoController {
    private static final String TAG = "TelefonoController";
    private TelefonoService telefonoService;

    public TelefonoController() {
        this.telefonoService = RetrofitClient.getClient().create(TelefonoService.class);
    }

    public interface TelefonoCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    // Get all telefonos
    public void getAllTelefonos(final TelefonoCallback<List<TelefonoModel>> callback) {
        telefonoService.getAllTelefonos().enqueue(new Callback<List<TelefonoModel>>() {
            @Override
            public void onResponse(Call<List<TelefonoModel>> call, Response<List<TelefonoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TelefonoModel>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Get telefono by ID
    public void getTelefonoById(int id, final TelefonoCallback<TelefonoModel> callback) {
        telefonoService.getTelefonoById(id).enqueue(new Callback<TelefonoModel>() {
            @Override
            public void onResponse(Call<TelefonoModel> call, Response<TelefonoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TelefonoModel> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Insert a telefono
    public void insertTelefono(TelefonoModel telefono, final TelefonoCallback<String> callback) {
        telefonoService.insertTelefono(telefono).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
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
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Update a telefono
    public void updateTelefono(TelefonoModel telefono, final TelefonoCallback<String> callback) {
        telefonoService.updateTelefono(telefono).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
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
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
