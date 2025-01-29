package com.example.telfquito_restful_java.controller;

import android.util.Base64;
import android.util.Log;

import com.example.telfquito_restful_java.models.ImageRequest;
import com.example.telfquito_restful_java.models.ImageResponse;
import com.example.telfquito_restful_java.service.ImagenService;
import com.example.telfquito_restful_java.service.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageController {
    private static final String TAG = "ImageController";
    private ImagenService imagenService;

    public ImageController() {
        this.imagenService = RetrofitClient.getClient().create(ImagenService.class);
    }

    // Callback interfaces
    public interface ImageCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }

    public interface ImageDownloadCallback {
        void onDownloadSuccess(byte[] imageData);
        void onError(String errorMessage);
    }

    /**
     * Upload an image to the server
     */
    public void uploadImage(ImageRequest imageRequest, final ImageCallback callback) {
        imagenService.uploadImage(imageRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Convert response body to plain text
                        String message = response.body().string();
                        Log.d("ImageController", "Upload Success: " + message);
                        callback.onSuccess(message);
                    } catch (IOException e) {
                        callback.onError("Error reading response: " + e.getMessage());
                    }
                } else {
                    callback.onError("Upload failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }


    /**
     * Download an image from the server
     */
    public void downloadImage(String fileName, final ImageDownloadCallback callback) {
        imagenService.downloadImage(fileName).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Decode Base64 response into a byte array
                        byte[] imageData = Base64.decode(response.body().getBase64Data(), Base64.DEFAULT);
                        Log.d(TAG, "Image downloaded successfully.");
                        callback.onDownloadSuccess(imageData);
                    } catch (Exception e) {
                        Log.e(TAG, "Error decoding image: " + e.getMessage());
                        callback.onError("Error decoding image: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Failed to download image: " + response.message());
                    callback.onError("Failed to download image: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.e(TAG, "Download failed: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
