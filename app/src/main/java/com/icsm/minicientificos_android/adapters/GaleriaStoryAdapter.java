package com.icsm.minicientificos_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.ItemStoryCientificaBinding;
import com.icsm.minicientificos_android.models.GaleriaCatalogData;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter horizontal para las 5 Stories Científicas Interactivas en la cabecera de Galería.
 */
public class GaleriaStoryAdapter extends RecyclerView.Adapter<GaleriaStoryAdapter.StoryViewHolder> {

    public interface OnStoryClickListener {
        void onStoryClick(GaleriaCatalogData.StoryItem story);
    }

    private final List<GaleriaCatalogData.StoryItem> stories;
    private final OnStoryClickListener listener;

    public GaleriaStoryAdapter(List<GaleriaCatalogData.StoryItem> stories, OnStoryClickListener listener) {
        this.stories = stories != null ? new ArrayList<>(stories) : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoryCientificaBinding binding = ItemStoryCientificaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new StoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.bind(stories.get(position));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    private int resolverDrawableStory(Context ctx, String preferido, String fallback) {
        if (ctx == null) return R.drawable.expe_fisi;
        String pkg = ctx.getPackageName();
        if (preferido != null && !preferido.isEmpty()) {
            int id = ctx.getResources().getIdentifier(preferido, "drawable", pkg);
            if (id != 0) return id;
        }
        if (fallback != null && !fallback.isEmpty()) {
            int id = ctx.getResources().getIdentifier(fallback, "drawable", pkg);
            if (id != 0) return id;
        }
        return R.drawable.expe_fisi;
    }

    class StoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemStoryCientificaBinding binding;

        StoryViewHolder(@NonNull ItemStoryCientificaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(GaleriaCatalogData.StoryItem story) {
            Context ctx = itemView.getContext();
            int resId = resolverDrawableStory(ctx, story.getDrawablePortada(), story.getDrawableFallback());
            binding.ivStoryCover.setImageResource(resId);
            binding.tvStoryEmoji.setText(story.getEmoji());
            binding.tvStoryLabel.setText(story.getEtiquetaCorta());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStoryClick(story);
                }
            });
        }
    }
}
