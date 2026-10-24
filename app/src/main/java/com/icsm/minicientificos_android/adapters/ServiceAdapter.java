package com.icsm.minicientificos_android.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.databinding.ItemServiceCardBinding;
import com.icsm.minicientificos_android.models.ServiceItem;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onServiceClick(ServiceItem item);
    }

    private final List<ServiceItem> items;
    private final OnServiceClickListener listener;

    public ServiceAdapter(List<ServiceItem> items, OnServiceClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServiceCardBinding binding = ItemServiceCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private final ItemServiceCardBinding binding;

        public ServiceViewHolder(@NonNull ItemServiceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ServiceItem item, OnServiceClickListener listener) {
            binding.tvServiceTitle.setText(item.getTitle());
            binding.tvServiceDesc.setText(item.getDescription());
            binding.tvServiceBadge.setText(item.getBadge());
            binding.ivServiceIcon.setImageResource(item.getIconResId());

            binding.btnServiceMore.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServiceClick(item);
                }
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServiceClick(item);
                }
            });
        }
    }
}