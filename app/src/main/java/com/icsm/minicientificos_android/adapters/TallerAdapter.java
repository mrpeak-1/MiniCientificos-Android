package com.icsm.minicientificos_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.ItemTallerBinding;
import com.icsm.minicientificos_android.models.TallerMock;

import java.util.ArrayList;
import java.util.List;

public class TallerAdapter extends RecyclerView.Adapter<TallerAdapter.TallerViewHolder> {

    public interface OnTallerClickListener {
        void onVerDetalleClick(TallerMock taller);
    }

    private final List<TallerMock> listaOriginal;
    private final List<TallerMock> listaFiltrada;
    private final OnTallerClickListener listener;
    private String textoFiltroActual = "";
    private String categoriaFiltroActual = "Todas";

    public TallerAdapter(List<TallerMock> lista, OnTallerClickListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TallerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTallerBinding binding = ItemTallerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TallerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TallerViewHolder holder, int position) {
        holder.bind(listaFiltrada.get(position));
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void aplicarFiltros(String texto, String categoria) {
        this.textoFiltroActual = texto != null ? texto.toLowerCase().trim() : "";
        if (categoria != null) {
            this.categoriaFiltroActual = categoria;
        }

        listaFiltrada.clear();
        for (TallerMock item : listaOriginal) {
            boolean coincideTexto = item.getTitulo().toLowerCase().contains(textoFiltroActual)
                    || item.getDescripcionCorta().toLowerCase().contains(textoFiltroActual);

            boolean coincideCategoria = categoriaFiltroActual.equals("Todas")
                    || item.getModalidad().equalsIgnoreCase(categoriaFiltroActual)
                    || item.getCategoria().equalsIgnoreCase(categoriaFiltroActual);

            if (coincideTexto && coincideCategoria) {
                listaFiltrada.add(item);
            }
        }
        notifyDataSetChanged();
    }

    class TallerViewHolder extends RecyclerView.ViewHolder {

        private final ItemTallerBinding binding;

        public TallerViewHolder(@NonNull ItemTallerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TallerMock item) {
            Context context = itemView.getContext();
            binding.tvTallerTitulo.setText(item.getTitulo());
            binding.tvTallerDescripcion.setText(item.getDescripcionCorta());
            binding.tvTallerFecha.setText(item.getFecha());
            binding.tvTallerCupos.setText(item.getCupos());
            binding.tvModalidadBadge.setText(item.getModalidad());
            binding.ivTallerImage.setImageResource(item.getImagenResId());

            // Estilos del Badge según la modalidad
            if ("Presencial".equalsIgnoreCase(item.getModalidad())) {
                binding.tvModalidadBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_presencial_bg));
                binding.tvModalidadBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_presencial_text));
            } else if ("Virtual".equalsIgnoreCase(item.getModalidad())) {
                binding.tvModalidadBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_virtual_bg));
                binding.tvModalidadBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_virtual_text));
            } else {
                binding.tvModalidadBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_hibrido_bg));
                binding.tvModalidadBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_hibrido_text));
            }

            // Click listener
            binding.btnVerDetalle.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerDetalleClick(item);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerDetalleClick(item);
                }
            });
        }
    }
}