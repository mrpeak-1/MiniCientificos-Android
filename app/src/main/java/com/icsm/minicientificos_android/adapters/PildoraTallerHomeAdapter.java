package com.icsm.minicientificos_android.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.models.PildoraTallerHome;

import java.util.List;

public class PildoraTallerHomeAdapter extends RecyclerView.Adapter<PildoraTallerHomeAdapter.PildoraViewHolder> {

    private final List<PildoraTallerHome> pildoraList;
    private final OnPildoraClickListener listener;

    public interface OnPildoraClickListener {
        void onPildoraClick(PildoraTallerHome pildora);
    }

    public PildoraTallerHomeAdapter(List<PildoraTallerHome> pildoraList, OnPildoraClickListener listener) {
        this.pildoraList = pildoraList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PildoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pildora_taller, parent, false);
        return new PildoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PildoraViewHolder holder, int position) {
        PildoraTallerHome pildora = pildoraList.get(position);
        holder.tvEmoji.setText(pildora.getIconoEmoji());
        holder.tvNombre.setText(pildora.getNombre());

        try {
            int colorTheme = Color.parseColor(pildora.getHexColor());
            holder.tvEmoji.setTextColor(colorTheme);
            holder.tvArrow.getBackground().setTint(colorTheme);

            if (holder.itemView.getBackground() instanceof RippleDrawable) {
                RippleDrawable ripple = (RippleDrawable) holder.itemView.getBackground();
                if (ripple.getDrawable(0) instanceof GradientDrawable) {
                    GradientDrawable bg = (GradientDrawable) ripple.getDrawable(0);
                    bg.setStroke(3, colorTheme);
                }
            }
        } catch (Exception ignored) {}

        holder.itemView.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.92f).scaleY(0.92f)
                    .setDuration(120)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1.0f).scaleY(1.0f)
                                .setDuration(220)
                                .setInterpolator(new OvershootInterpolator(1.2f))
                                .withEndAction(() -> {
                                    if (listener != null) {
                                        listener.onPildoraClick(pildora);
                                    }
                                }).start();
                    }).start();
        });
    }

    @Override
    public int getItemCount() {
        return pildoraList != null ? pildoraList.size() : 0;
    }

    static class PildoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji;
        TextView tvNombre;
        TextView tvArrow;

        public PildoraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tv_pildora_emoji);
            tvNombre = itemView.findViewById(R.id.tv_pildora_nombre);
            tvArrow = itemView.findViewById(R.id.tv_pildora_arrow);
        }
    }
}