package com.icsm.minicientificos_android.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.databinding.ItemGaleriaBinding;
import com.icsm.minicientificos_android.models.GaleriaMock;

import java.util.ArrayList;
import java.util.List;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.GaleriaViewHolder> {

    public interface OnGaleriaClickListener {
        void onItemClick(GaleriaMock item);
    }

    private final List<GaleriaMock> listaOriginal;
    private final List<GaleriaMock> listaFiltrada;
    private final OnGaleriaClickListener listener;

    public GaleriaAdapter(List<GaleriaMock> lista, OnGaleriaClickListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    @NonNull
    @Override
    public GaleriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGaleriaBinding binding = ItemGaleriaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new GaleriaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GaleriaViewHolder holder, int position) {
        holder.bind(listaFiltrada.get(position));
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filtrarPorCategoria(String categoria) {
        listaFiltrada.clear();
        if (categoria == null || categoria.isEmpty() || categoria.equalsIgnoreCase("Todos")) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (GaleriaMock item : listaOriginal) {
                if (item.getCategoria().equalsIgnoreCase(categoria)) {
                    listaFiltrada.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    class GaleriaViewHolder extends RecyclerView.ViewHolder {
        private final ItemGaleriaBinding binding;

        public GaleriaViewHolder(@NonNull ItemGaleriaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GaleriaMock item) {
            binding.tvGaleriaTitulo.setText(item.getTitulo());
            binding.tvGaleriaBadge.setText(item.getCategoria());
            binding.tvGaleriaFecha.setText(item.getFecha());
            binding.ivGaleriaImagen.setImageResource(item.getImagenResId());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}