package com.example.telfquito_restful_java.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telfquito_restful_java.R;
import com.example.telfquito_restful_java.models.TelefonoCarrito;
import com.example.telfquito_restful_java.models.TelefonoModel;

import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    private List<TelefonoCarrito> telefonosCarrito;
    private List<TelefonoModel> telefonosDetalles;

    public CompraAdapter(List<TelefonoCarrito> telefonosCarrito, List<TelefonoModel> telefonosDetalles) {
        this.telefonosCarrito = telefonosCarrito;
        this.telefonosDetalles = telefonosDetalles;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        TelefonoCarrito carrito = telefonosCarrito.get(position);

        // Check if the corresponding TelefonoModel exists
        if (position < telefonosDetalles.size()) {
            TelefonoModel detalles = telefonosDetalles.get(position);

            holder.tvMarca.setText("Marca: " + detalles.getMarca());
            holder.tvNombre.setText("Nombre: " + detalles.getNombre());
            holder.tvPrecio.setText("Precio: $" + detalles.getPrecio());
            holder.tvCantidad.setText("Cantidad: " + carrito.getCantidad());
        } else {
            // Handle case where the details are not yet available
            holder.tvMarca.setText("Cargando...");
            holder.tvNombre.setText("Cargando...");
            holder.tvPrecio.setText("Cargando...");
            holder.tvCantidad.setText("Cantidad: " + carrito.getCantidad());
        }
    }


    @Override
    public int getItemCount() {
        return telefonosCarrito.size();
    }

    static class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarca, tvNombre, tvPrecio, tvCantidad;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarca = itemView.findViewById(R.id.tvMarcaCompra);
            tvNombre = itemView.findViewById(R.id.tvNombreCompra);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCompra);
            tvCantidad = itemView.findViewById(R.id.tvCantidadCompra);
        }
    }
}
