package com.icsm.minicientificos_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.cart.CartItem;

import java.util.List;
import java.util.Locale;

public class CheckoutResumenAdapter extends RecyclerView.Adapter<CheckoutResumenAdapter.ResumenViewHolder> {

    private final List<CartItem> items;

    public CheckoutResumenAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ResumenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_resumen_row, parent, false);
        return new ResumenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumenViewHolder holder, int position) {
        CartItem item = items.get(position);
        if (item == null || item.getProducto() == null) return;

        holder.tvNombre.setText(item.getProducto().getNombre());
        holder.tvPrecioUnitario.setText(String.format(Locale.US, "S/ %.2f c/u", item.getProducto().getPrecio()));
        holder.tvCantidadBadge.setText(String.valueOf(item.getCantidad()));
        holder.tvSubtotal.setText(String.format(Locale.US, "S/ %.2f", item.getSubtotal()));

        if (item.getProducto().getDrawableResId() != 0) {
            holder.imgIcon.setImageResource(item.getProducto().getDrawableResId());
        } else {
            holder.imgIcon.setImageResource(R.drawable.ic_flask);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ResumenViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvNombre;
        TextView tvPrecioUnitario;
        TextView tvCantidadBadge;
        TextView tvSubtotal;

        public ResumenViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_checkout_row_icon);
            tvNombre = itemView.findViewById(R.id.tv_checkout_row_nombre);
            tvPrecioUnitario = itemView.findViewById(R.id.tv_checkout_row_precio_unitario);
            tvCantidadBadge = itemView.findViewById(R.id.tv_checkout_row_cantidad_badge);
            tvSubtotal = itemView.findViewById(R.id.tv_checkout_row_subtotal);
        }
    }
}