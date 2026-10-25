package com.icsm.minicientificos_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.ItemTallerBinding;
import com.icsm.minicientificos_android.models.Taller;

import java.util.ArrayList;
import java.util.List;

public class TallerAdapter extends RecyclerView.Adapter<TallerAdapter.TallerViewHolder> {

    public interface OnTallerClickListener {
        void onVerDetalleClick(Taller taller);
    }

    private final List<Taller> listaOriginal;
    private final List<Taller> listaFiltrada;
    private final OnTallerClickListener listener;
    private String textoFiltroActual = "";
    private String categoriaFiltroActual = "Todas";
    private String modalidadFiltroActual = "Todas";

    public TallerAdapter(List<Taller> lista, OnTallerClickListener listener) {
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

    public void aplicarFiltros(String texto, String categoria, String modalidad) {
        this.textoFiltroActual = texto != null ? texto.toLowerCase().trim() : "";
        if (categoria != null) {
            this.categoriaFiltroActual = categoria;
        }
        if (modalidad != null) {
            this.modalidadFiltroActual = modalidad;
        }

        listaFiltrada.clear();
        for (Taller item : listaOriginal) {
            boolean coincideTexto = item.getTitulo().toLowerCase().contains(textoFiltroActual)
                    || item.getDescripcionCorta().toLowerCase().contains(textoFiltroActual);

            boolean coincideCategoria = categoriaFiltroActual.equalsIgnoreCase("Todas")
                    || item.getCategoria().equalsIgnoreCase(categoriaFiltroActual);

            boolean coincideModalidad = modalidadFiltroActual.equalsIgnoreCase("Todas")
                    || item.getModalidad().toLowerCase().contains(modalidadFiltroActual.toLowerCase());

            if (coincideTexto && coincideCategoria && coincideModalidad) {
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

        public void bind(Taller item) {
            Context context = itemView.getContext();
            
            binding.tvPosterLinea1.setText(item.getPosterLinea1());
            binding.tvPosterLinea2.setText(item.getPosterLinea2());
            binding.tvBadgeModalidadCard.setText(item.getModalidad());
            
            binding.tvTituloCortoTaller.setText(item.getTituloCorto());
            binding.tvDescripcionCortaTaller.setText(item.getDescripcionCorta());
            
            String infoRow = "📅 " + item.getHorario() + " · 👥 " + item.getVacantes();
            binding.tvInfoRowTaller.setText(infoRow);
            
            int resId = context.getResources().getIdentifier(item.getDrawableName(), "drawable", context.getPackageName());
            if (resId != 0) {
                binding.ivTallerImage.setImageResource(resId);
            } else {
                binding.ivTallerImage.setImageResource(R.drawable.ic_flask);
            }

            if (item.getModalidad().contains("Presencial") && !item.getModalidad().contains("Híbrido")) {
                binding.tvBadgeModalidadCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_presencial_bg));
                binding.tvBadgeModalidadCard.setTextColor(ContextCompat.getColor(context, R.color.badge_presencial_text));
            } else if (item.getModalidad().contains("Virtual")) {
                binding.tvBadgeModalidadCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_virtual_bg));
                binding.tvBadgeModalidadCard.setTextColor(ContextCompat.getColor(context, R.color.badge_virtual_text));
            } else {
                binding.tvBadgeModalidadCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.badge_hibrido_bg));
                binding.tvBadgeModalidadCard.setTextColor(ContextCompat.getColor(context, R.color.badge_hibrido_text));
            }

            binding.btnVerDetalleTaller.setOnClickListener(v -> {
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