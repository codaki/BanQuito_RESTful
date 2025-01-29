package com.example.telfquito_restful_java.service;

import com.example.telfquito_restful_java.models.ImageRequest;
import com.example.telfquito_restful_java.models.ImageResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ImagenService {
    @POST("images/upload")
    Call<ResponseBody> uploadImage(@Body ImageRequest imageRequest);

    @GET("images/download")
    Call<ImageResponse> downloadImage(@Query("fileName") String fileName);
}
