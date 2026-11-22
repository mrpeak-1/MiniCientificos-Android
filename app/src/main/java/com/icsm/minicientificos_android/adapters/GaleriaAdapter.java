package com.icsm.minicientificos_android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icsm.minicientificos_android.databinding.ItemGaleriaFotoBinding;
import com.icsm.minicientificos_android.models.GaleriaFoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Adapter de la Galería de 43 fotografías oficiales con caché LRU y
 * decodificación optimizada en segundo plano para desplazamiento fluido sin OOM.
 */
public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.GaleriaViewHolder> {

    public interface OnGaleriaFotoClickListener {
        void onFotoClick(GaleriaFoto foto, int position);
    }

    private final List<GaleriaFoto> listaFotos = new ArrayList<>();
    private final OnGaleriaFotoClickListener listener;

    private final LruCache<Integer, Bitmap> thumbnailCache;
    private final ExecutorService decodeExecutor = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GaleriaAdapter(List<GaleriaFoto> fotosIniciales, OnGaleriaFotoClickListener listener) {
        if (fotosIniciales != null) {
            this.listaFotos.addAll(fotosIniciales);
        }
        this.listener = listener;

        final int maxMemoryKb = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSizeKb = Math.max(4096, maxMemoryKb / 8);
        this.thumbnailCache = new LruCache<Integer, Bitmap>(cacheSizeKb) {
            @Override
            protected int sizeOf(@NonNull Integer key, @NonNull Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void actualizarFotos(List<GaleriaFoto> nuevasFotos) {
        listaFotos.clear();
        if (nuevasFotos != null) {
            listaFotos.addAll(nuevasFotos);
        }
        notifyDataSetChanged();
    }

    public List<GaleriaFoto> getFotosVisibles() {
        return new ArrayList<>(listaFotos);
    }

    public void liberarRecursos() {
        decodeExecutor.shutdownNow();
        thumbnailCache.evictAll();
    }

    @NonNull
    @Override
    public GaleriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGaleriaFotoBinding binding = ItemGaleriaFotoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new GaleriaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GaleriaViewHolder holder, int position) {
        holder.bind(listaFotos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listaFotos.size();
    }

    private Bitmap decodificarMiniatura(Context ctx, int resId, int targetPx) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(ctx.getResources(), resId, opts);

            int sample = 1;
            if (opts.outWidth > targetPx || opts.outHeight > targetPx) {
                int halfW = opts.outWidth / 2;
                int halfH = opts.outHeight / 2;
                while ((halfW / sample) >= targetPx && (halfH / sample) >= targetPx) {
                    sample *= 2;
                }
            }
            opts.inSampleSize = Math.max(1, sample);
            opts.inJustDecodeBounds = false;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(ctx.getResources(), resId, opts);
        } catch (Throwable t) {
            return null;
        }
    }

    class GaleriaViewHolder extends RecyclerView.ViewHolder {
        private final ItemGaleriaFotoBinding binding;

        GaleriaViewHolder(@NonNull ItemGaleriaFotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(GaleriaFoto foto, int position) {
            Context ctx = itemView.getContext();
            int resId = foto.resolveDrawableResId(ctx);

            binding.tvFotoSubgrupoBadge.setText(foto.getSubgrupo());
            binding.tvFotoNumBadge.setText(String.format(Locale.US, "#%02d", foto.getId()));
            binding.tvFotoTitulo.setText(foto.getTitulo());
            binding.tvFotoCategoriaMeta.setText(foto.getCategoriaPrincipal() + " · " + foto.getFechaLugar());

            binding.ivGaleriaFoto.setTag(resId);
            Bitmap cached = thumbnailCache.get(resId);
            if (cached != null && !cached.isRecycled()) {
                binding.ivGaleriaFoto.setImageBitmap(cached);
            } else {
                binding.ivGaleriaFoto.setImageDrawable(null);
                Context appCtx = ctx.getApplicationContext();
                if (!decodeExecutor.isShutdown()) {
                    decodeExecutor.execute(() -> {
                        Bitmap bmp = decodificarMiniatura(appCtx, resId, 420);
                        if (bmp != null) {
                            thumbnailCache.put(resId, bmp);
                            mainHandler.post(() -> {
                                Object tag = binding.ivGaleriaFoto.getTag();
                                if (tag instanceof Integer && ((Integer) tag) == resId) {
                                    binding.ivGaleriaFoto.setImageBitmap(bmp);
                                }
                            });
                        }
                    });
                }
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int adapterPos = getAdapterPosition();
                    int posFinal = adapterPos != RecyclerView.NO_POSITION ? adapterPos : position;
                    listener.onFotoClick(foto, posFinal);
                }
            });
        }
    }
}
