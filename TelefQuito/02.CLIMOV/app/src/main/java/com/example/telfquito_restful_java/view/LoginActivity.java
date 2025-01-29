package com.example.telfquito_restful_java.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.controller.LoginController;
import com.example.telfquito_restful_java.models.AuthRequest;
import com.example.telfquito_restful_java.models.AuthResponse;
import com.example.telfquito_restful_java.service.RetrofitClient;
import com.example.telfquito_restful_java.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private LoginController loginController;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController = new LoginController();

        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese usuario y contraseña",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        loginController.attemptLogin(username, password, new LoginController.AuthCallback() {
            @Override
            public void onLoginSuccess(String message) {
                if ("Autenticación exitosa".equals(message)) {
                    startActivity(new Intent(LoginActivity.this, TelefonosActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Usuario o Contraseña Incorrecto",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoginError(String errorMessage) {
                Toast.makeText(LoginActivity.this,
                        "Error: " + errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
