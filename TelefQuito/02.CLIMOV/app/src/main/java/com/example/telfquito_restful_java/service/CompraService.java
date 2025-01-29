package com.example.telfquito_restful_java.service;

import com.example.telfquito_restful_java.models.Carrito;
import com.example.telfquito_restful_java.models.FacturaModel;
import com.example.telfquito_restful_java.models.TablaModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CompraService {
    @POST("wscompra/efectivo")
    Call<ResponseBody> comprarEfectivo(@Body Carrito carrito, @Query("cedula") String cedula);

    @POST("wscompra/credito")
    Call<ResponseBody> comprarCredito(@Body Carrito carrito, @Query("cedula") String cedula, @Query("plazoMeses") int plazoMeses);

    @GET("wscompra/factura")
    Call<List<FacturaModel>> obtenerFactura(@Query("cedula") String cedula);

    @GET("wscompra/tablaAmortizacion")
    Call<List<TablaModel>> consultarTablaAmortizacion(@Query("cedula") String cedula);
}
