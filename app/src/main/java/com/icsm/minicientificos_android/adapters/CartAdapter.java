package com.icsm.minicientificos_android.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.cart.CartItem;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.ItemCartProductBinding;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> items;
    private final CartManager cartManager;

    public CartAdapter(List<CartItem> items) {
        this.items = items;
        this.cartManager = CartManager.getInstance();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartProductBinding binding = ItemCartProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private final ItemCartProductBinding binding;

        public CartViewHolder(@NonNull ItemCartProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem item) {
            binding.tvCartProductoNombre.setText(item.getProducto().getNombre());
            String precioUnitarioStr = String.format(Locale.US, "S/ %.2f c/u", item.getProducto().getPrecio());
            binding.tvCartProductoPrecio.setText(precioUnitarioStr);
            binding.tvCantidadItem.setText(String.valueOf(item.getCantidad()));
            binding.tvSubtotalItem.setText(item.getSubtotalFormateado());
            binding.ivCartProductoImagen.setImageResource(item.getProducto().getDrawableResId());

            // Incremento [+]
            binding.btnIncrementarItem.setOnClickListener(v -> {
                cartManager.actualizarCantidad(
                        String.valueOf(item.getProducto().getId()),
                        item.getCantidad() + 1
                );
            });

            // Decremento [-]
            binding.btnDecrementarItem.setOnClickListener(v -> {
                cartManager.actualizarCantidad(
                        String.valueOf(item.getProducto().getId()),
                        item.getCantidad() - 1
                );
            });

            // Eliminar [🗑️]
            binding.btnEliminarItem.setOnClickListener(v -> {
                cartManager.quitarProducto(String.valueOf(item.getProducto().getId()));
            });
        }
    }
}