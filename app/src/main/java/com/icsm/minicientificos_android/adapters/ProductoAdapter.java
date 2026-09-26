package com.icsm.minicientificos_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.ItemProductoBinding;
import com.icsm.minicientificos_android.models.ProductoMock;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    public interface OnProductoClickListener {
        void onComprarClick(ProductoMock producto);
    }

    private final List<ProductoMock> listaOriginal;
    private final List<ProductoMock> listaFiltrada;
    private final OnProductoClickListener listener;

    public ProductoAdapter(List<ProductoMock> lista, OnProductoClickListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductoBinding binding = ItemProductoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.bind(listaFiltrada.get(position));
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filtrar(String texto, String categoria) {
        String q = texto != null ? texto.toLowerCase().trim() : "";
        listaFiltrada.clear();

        for (ProductoMock item : listaOriginal) {
            boolean coincideTexto = item.getNombre().toLowerCase().contains(q)
                    || item.getDescripcion().toLowerCase().contains(q);

            boolean coincideCategoria = categoria.equals("Todas")
                    || item.getCategoria().equalsIgnoreCase(categoria);

            if (coincideTexto && coincideCategoria) {
                listaFiltrada.add(item);
            }
        }
        notifyDataSetChanged();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductoBinding binding;

        public ProductoViewHolder(@NonNull ItemProductoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductoMock item) {
            Context context = itemView.getContext();
            binding.tvProductoNombre.setText(item.getNombre());
            binding.tvProductoCategoria.setText(item.getCategoria());
            binding.tvProductoPrecio.setText(item.getPrecioFormateado());
            binding.tvProductoStockBadge.setText(item.getDisponibilidad());
            binding.ivProductoImagen.setImageResource(item.getImagenResId());

            if ("En Stock".equalsIgnoreCase(item.getDisponibilidad())) {
                binding.tvProductoStockBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_stock_bg));
                binding.tvProductoStockBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_stock_text));
            } else {
                binding.tvProductoStockBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_pocas_unidades_bg));
                binding.tvProductoStockBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_pocas_unidades_text));
            }

            binding.btnProductoComprar.setOnClickListener(v -> {
                if (listener != null) listener.onComprarClick(item);
            });
        }
    }
}