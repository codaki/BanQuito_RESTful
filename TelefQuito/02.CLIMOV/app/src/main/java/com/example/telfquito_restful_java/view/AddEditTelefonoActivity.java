package com.example.telfquito_restful_java.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.controller.ImageController;
import com.example.telfquito_restful_java.controller.TelefonoController;
import com.example.telfquito_restful_java.models.ImageRequest;
import com.example.telfquito_restful_java.models.TelefonoModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditTelefonoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etMarca, etNombre, etPrecio;
    private Button btnSave, btnSelectImage;
    private TextView tvTitle;
    private ImageView imgTelefono;
    private TelefonoController telefonoController;
    private ImageController imageController;
    private TelefonoModel telefono;
    private byte[] selectedImageData;
    private String nombreImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_telefono);

        etMarca = findViewById(R.id.etMarca);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        btnSave = findViewById(R.id.btnSave);
        tvTitle = findViewById(R.id.tvTitle);
        imgTelefono = findViewById(R.id.imgLogo);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        telefonoController = new TelefonoController();
        imageController = new ImageController();

        telefono = (TelefonoModel) getIntent().getSerializableExtra("telefono");
        if (telefono != null) {
            etMarca.setText(telefono.getMarca());
            etNombre.setText(telefono.getNombre());
            etPrecio.setText(telefono.getPrecio());

            // Load image from API
            if (telefono.getImgUrl() != null && !telefono.getImgUrl().isEmpty()) {
                imageController.downloadImage(telefono.getImgUrl(), new ImageController.ImageDownloadCallback() {
                    @Override
                    public void onDownloadSuccess(byte[] imageData) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        imgTelefono.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        imgTelefono.setImageResource(R.drawable.estrella_triste2);
                    }
                });
            } else {
                imgTelefono.setImageResource(R.drawable.estrella_triste2);
            }
        }

        btnSelectImage.setOnClickListener(v -> selectImage());

        btnSave.setOnClickListener(v -> saveTelefono());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                nombreImagen = getFileName(imageUri);

                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Handle EXIF orientation
                bitmap = handleExifOrientation(bitmap, imageUri);

                // Resize the image to prevent large memory usage
                Bitmap resizedBitmap = resizeBitmap(bitmap, 1024, 1024);
                imgTelefono.setImageBitmap(resizedBitmap);

                // Convert Bitmap to Base64 for uploading
                selectedImageData = compressBitmap(resizedBitmap, 70);

            } catch (Exception e) {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap handleExifOrientation(Bitmap bitmap, Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        ExifInterface exif = new ExifInterface(inputStream);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap resizeBitmap(Bitmap original, int maxWidth, int maxHeight) {
        int width = original.getWidth();
        int height = original.getHeight();

        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;
        float scaleFactor = Math.min(scaleWidth, scaleHeight);

        int newWidth = Math.round(width * scaleFactor);
        int newHeight = Math.round(height * scaleFactor);

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String fileName = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return fileName;
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream); // Compress the image
        byte[] byteArray = outputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private byte[] compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream); // Compress to specified quality
        return outputStream.toByteArray();
    }

    private void saveTelefono() {
        if (telefono == null) {
            telefono = new TelefonoModel();
        }

        String marca = etMarca.getText().toString();
        String nombre = etNombre.getText().toString();
        String precio = etPrecio.getText().toString();

        if (marca.isEmpty() || nombre.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        telefono.setMarca(marca);
        telefono.setNombre(nombre);
        telefono.setPrecio(precio);
        telefono.setDisponible(1);

        // Generate image name
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String imageName = nombre.substring(0, Math.min(3, nombre.length())).toUpperCase() + "_" + currentDate + nombreImagen;
        telefono.setImgUrl(imageName);

        if (selectedImageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(selectedImageData, 0, selectedImageData.length);

            // Resize and compress before sending
            Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 800);
            String base64Image = convertBitmapToBase64(resizedBitmap);

            // Prepare ImageRequest
            ImageRequest imageRequest = new ImageRequest();
            imageRequest.setFileName(imageName);
            imageRequest.setBase64Data(base64Image);

            // Upload image
            imageController.uploadImage(imageRequest, new ImageController.ImageCallback() {
                @Override
                public void onSuccess(String response) {
                    saveTelefonoModel();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(AddEditTelefonoActivity.this, "Error al subir la imagen: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            saveTelefonoModel();
        }
    }

    private void saveTelefonoModel() {
        if (telefono.getCodTelefono() == 0) {
            telefonoController.insertTelefono(telefono, new TelefonoController.TelefonoCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(AddEditTelefonoActivity.this, "Teléfono agregado con éxito", Toast.LENGTH_SHORT).show();
                    refreshTelefonosList();
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(AddEditTelefonoActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            telefonoController.updateTelefono(telefono, new TelefonoController.TelefonoCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(AddEditTelefonoActivity.this, "Teléfono actualizado con éxito", Toast.LENGTH_SHORT).show();
                    refreshTelefonosList();
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(AddEditTelefonoActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void refreshTelefonosList() {
        Intent intent = new Intent(this, TelefonosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
