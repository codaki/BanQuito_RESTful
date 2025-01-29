package com.example.telfquito_restful_java.controller;

import android.util.Log;

import com.example.telfquito_restful_java.service.RetrofitClient;
import com.example.telfquito_restful_java.service.AuthService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {
    private AuthService authService;
    private static final String TAG = "LoginController";

    public LoginController() {
        this.authService = RetrofitClient.getClient().create(AuthService.class);
    }

    public interface AuthCallback {
        void onLoginSuccess(String message);
        void onLoginError(String errorMessage);
    }

    public void attemptLogin(String username, String password, final AuthCallback callback) {
        authService.authenticate(username, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String message = response.body().string().trim(); // Read response as plain text
                        Log.d(TAG, "onResponse: " + message);

                        if (message.equals("Autenticación exitosa")) {
                            callback.onLoginSuccess(message);
                        } else {
                            callback.onLoginError("Login failed: " + message);
                        }
                    } else {
                        Log.d(TAG, "onResponse: " + response.message());
                        callback.onLoginError("Error: " + response.message());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response: " + e.getMessage());
                    callback.onLoginError("Error parsing response");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                callback.onLoginError("Network error: " + t.getMessage());
            }
        });
    }
}