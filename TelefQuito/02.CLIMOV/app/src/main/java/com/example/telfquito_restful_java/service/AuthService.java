package com.example.telfquito_restful_java.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("wslogin")
    Call<ResponseBody> authenticate(
            @Query("username") String username,
            @Query("password") String password
    );
}
