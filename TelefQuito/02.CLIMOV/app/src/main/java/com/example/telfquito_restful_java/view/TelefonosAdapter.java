package com.example.telfquito_restful_java.view;

import static android.graphics.Color.LTGRAY;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.controller.ImageController;
import com.example.telfquito_restful_java.controller.TelefonoController;
import com.example.telfquito_restful_java.models.Carrito;
import com.example.telfquito_restful_java.models.CarritoSingleton;
import com.example.telfquito_restful_java.models.TelefonoModel;
import com.example.telfquito_restful_java.models.TelefonoCarrito;

import java.io.Serializable;
import java.util.List;

public class TelefonosAdapter extends RecyclerView.Adapter<TelefonosAdapter.TelefonoViewHolder> {

    private List<TelefonoModel> telefonos;
    private Context context;
    private ImageController imageController;
    private TelefonoController telefonoController;

    public TelefonosAdapter(List<TelefonoModel> telefonos, Context context) {
        this.telefonos = telefonos;
        this.context = context;
        this.imageController = new ImageController();
        this.telefonoController = new TelefonoController();
    }

    @NonNull
    @Override
    public TelefonoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_telefono, parent, false);
        return new TelefonoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TelefonoViewHolder holder, int position) {
        TelefonoModel telefono = telefonos.get(position);
        holder.tvMarca.setText(telefono.getMarca());
        holder.tvNombre.setText(telefono.getNombre());
        holder.tvPrecio.setText(String.format("$%.2f", Double.parseDouble(telefono.getPrecio())));

        // Load image using ImageController
        imageController.downloadImage(telefono.getImgUrl(), new ImageController.ImageDownloadCallback() {
            public void onDownloadSuccess(byte[] imageData) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                holder.imgTelefono.post(() -> holder.imgTelefono.setImageBitmap(bitmap));
            }

            public void onError(String errorMessage) {
                holder.imgTelefono.post(() -> holder.imgTelefono.setImageResource(R.drawable.estrella_triste2));
            }
        });

        updateButton(holder, telefono);

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditTelefonoActivity.class);
            intent.putExtra("telefono", (Serializable) telefono);
            context.startActivity(intent);
        });

        holder.btnDesactivar.setText(telefono.getDisponible() == 1 ? "Desactivar" : "Activar");
        holder.btnDesactivar.setOnClickListener(v -> {
            toggleDisponibilidad(telefono);
        });

        holder.itemView.setBackgroundColor(telefono.getDisponible() == 1 ? WHITE : LTGRAY);
    }

    private void updateButton(@NonNull TelefonoViewHolder holder, TelefonoModel telefono) {
        Carrito carrito = CarritoSingleton.getInstance();

        boolean isInCart = carrito.getTelefonos().stream()
                .anyMatch(t -> t.getTelefonoId() == telefono.getCodTelefono());

        holder.btnAdd.setText(isInCart ? "Ver Carrito" : "🛒");
        holder.btnAdd.setOnClickListener(v -> {
            if (isInCart) {
                context.startActivity(new Intent(context, CarritoActivity.class));
            } else {
                TelefonoCarrito telefonoCarrito = new TelefonoCarrito(telefono.getCodTelefono(), 1);
                carrito.agregarTelefono(telefonoCarrito);
                Toast.makeText(context, "Añadido al carrito", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    private void toggleDisponibilidad(TelefonoModel telefono) {
        telefono.setDisponible(telefono.getDisponible() == 1 ? 0 : 1);
        telefonoController.updateTelefono(telefono, new TelefonoController.TelefonoCallback<String>() {
            @Override
            public void onSuccess(String result) {
                notifyDataSetChanged();
                Toast.makeText(context, "Teléfono actualizado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return telefonos.size();
    }

    public static class TelefonoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarca, tvNombre, tvPrecio;
        ImageView imgTelefono;
        Button btnEditar, btnDesactivar, btnAdd;

        public TelefonoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imgTelefono = itemView.findViewById(R.id.imgTelefono);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDesactivar = itemView.findViewById(R.id.btnDesactivar);
            btnAdd = itemView.findViewById(R.id.btnVender);
        }
    }
}