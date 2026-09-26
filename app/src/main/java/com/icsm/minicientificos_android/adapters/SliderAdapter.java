package com.icsm.minicientificos_android.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.databinding.ItemHeroSliderBinding;
import com.icsm.minicientificos_android.models.SliderItem;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SliderItem item);
    }

    private final List<SliderItem> items;
    private final OnItemClickListener listener;

    public SliderAdapter(List<SliderItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHeroSliderBinding binding = ItemHeroSliderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SliderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        private final ItemHeroSliderBinding binding;

        public SliderViewHolder(@NonNull ItemHeroSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SliderItem item, OnItemClickListener listener) {
            binding.tvTitle.setText(item.getTitle());
            binding.tvSubtitle.setText(item.getSubtitle());
            binding.tvBadge.setText(item.getBadge());
            binding.btnAction.setText(item.getButtonText());
            binding.ivIcon.setImageResource(item.getIconResId());

            binding.btnAction.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}