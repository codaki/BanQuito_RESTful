package com.example.telfquito_restful_java.service;

import com.example.telfquito_restful_java.models.TelefonoModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TelefonoService {

    // Get all telefonos
    @GET("wstelefonos")
    Call<List<TelefonoModel>> getAllTelefonos();

    // Get a telefono by ID
    @GET("wstelefonos/{id}")
    Call<TelefonoModel> getTelefonoById(@Path("id") int id);

    // Insert a new telefono
    @POST("wstelefonos")
    Call<ResponseBody> insertTelefono(@Body TelefonoModel telefono);

    // Update an existing telefono (Returns plain text)
    @PUT("wstelefonos")
    Call<ResponseBody> updateTelefono(@Body TelefonoModel telefono);
}
