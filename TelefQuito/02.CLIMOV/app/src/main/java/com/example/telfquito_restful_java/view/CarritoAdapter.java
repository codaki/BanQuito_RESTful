package com.example.telfquito_restful_java.view;

import android.content.Context;
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
import com.example.telfquito_restful_java.models.TelefonoCarrito;
import com.example.telfquito_restful_java.models.TelefonoModel;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<TelefonoCarrito> telefonos;
    private Context context;
    private OnQuantityChangeListener quantityChangeListener;
    private TelefonoController telefonoController;
    private ImageController imageController;

    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int newQuantity);
    }

    public CarritoAdapter(List<TelefonoCarrito> telefonos, Context context, OnQuantityChangeListener quantityChangeListener) {
        this.telefonos = telefonos;
        this.context = context;
        this.quantityChangeListener = quantityChangeListener;
        this.telefonoController = new TelefonoController();
        this.imageController = new ImageController();
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        TelefonoCarrito telefonoCarrito = telefonos.get(position);

        // Fetch full phone details by ID
        telefonoController.getTelefonoById(telefonoCarrito.getTelefonoId(), new TelefonoController.TelefonoCallback<TelefonoModel>() {
            @Override
            public void onSuccess(TelefonoModel telefono) {
                holder.tvMarca.setText("Marca: " + telefono.getMarca());
                holder.tvNombre.setText("Nombre: " + telefono.getNombre());
                try {
                    double precio = Double.parseDouble(telefono.getPrecio());
                    holder.tvPrecio.setText(String.format("Precio: $%.2f", precio));
                } catch (NumberFormatException e) {
                    holder.tvPrecio.setText("Precio: N/A");
                }

                holder.tvCantidad.setText("Cantidad: " + telefonoCarrito.getCantidad());

                // Fetch and display image
                imageController.downloadImage(telefono.getImgUrl(), new ImageController.ImageDownloadCallback() {
                    @Override
                    public void onDownloadSuccess(byte[] imageData) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        holder.imgTelefono.post(() -> holder.imgTelefono.setImageBitmap(bitmap));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        holder.imgTelefono.setImageResource(R.drawable.estrella_triste2);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Error loading phone details: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Decrease quantity
        holder.btnDecreaseCantidad.setOnClickListener(v -> {
            int newQuantity = telefonoCarrito.getCantidad() - 1;
            if (newQuantity > 0) {
                telefonoCarrito.setCantidad(newQuantity);
                notifyItemChanged(position);
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChange(position, newQuantity);
                }
            } else {
                telefonos.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Item removed from carrito", Toast.LENGTH_SHORT).show();
            }
        });

        // Increase quantity
        holder.btnIncreaseCantidad.setOnClickListener(v -> {
            int newQuantity = telefonoCarrito.getCantidad() + 1;
            telefonoCarrito.setCantidad(newQuantity);
            notifyItemChanged(position);
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChange(position, newQuantity);
            }
        });

        // Delete item
        holder.btnDeleteItem.setOnClickListener(v -> {
            telefonos.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Item removed from carrito", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return telefonos.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarca, tvNombre, tvPrecio, tvCantidad;
        ImageView imgTelefono;
        Button btnDecreaseCantidad, btnIncreaseCantidad, btnDeleteItem;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMarca = itemView.findViewById(R.id.tvMarcaCarrito);
            tvNombre = itemView.findViewById(R.id.tvNombreCarrito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCarrito);
            tvCantidad = itemView.findViewById(R.id.tvCantidadCarrito);
            imgTelefono = itemView.findViewById(R.id.imgTelefonoCarrito);
            btnDecreaseCantidad = itemView.findViewById(R.id.btnDecreaseCantidad);
            btnIncreaseCantidad = itemView.findViewById(R.id.btnIncreaseCantidad);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
