package com.icsm.minicientificos_android.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetTallerDetalleBinding;
import com.icsm.minicientificos_android.models.Taller;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

public class TallerDetalleBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_TALLER = "arg_taller";
    private BottomSheetTallerDetalleBinding binding;
    private Taller taller;
    private ExoPlayer exoPlayer;

    public static TallerDetalleBottomSheet newInstance(Taller taller) {
        TallerDetalleBottomSheet fragment = new TallerDetalleBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TALLER, taller);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taller = (Taller) getArguments().getSerializable(ARG_TALLER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetTallerDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (taller == null) {
            dismiss();
            return;
        }

        binding.tvModalTitulo.setText(taller.getTitulo());
        binding.tvModalObjetivoDesc.setText(taller.getObjetivo());
        binding.tvModalHorario.setText(taller.getHorario());
        binding.tvModalCupos.setText(taller.getVacantes());
        binding.tvModalPrecio.setText(String.format("Inversión: S/ %.2f", taller.getPrecio()));
        binding.tvModalCategoria.setText(taller.getCategoria());
        binding.tvModalModalidad.setText(taller.getModalidad());

        // Llenar listas (Temario y Materiales)
        poblarLista(binding.llTemarioContainer, taller.getTemario(), "• ");
        poblarLista(binding.llMaterialesContainer, taller.getMaterialesIncluidos(), "✔️ ");

        configurarBadgeModalidad(taller.getModalidad());

        // Imagen vs Video
        int resId = requireContext().getResources().getIdentifier(taller.getImageName(), "drawable", requireContext().getPackageName());
        if (resId != 0) {
            binding.ivModalTallerImage.setImageResource(resId);
        } else {
            binding.ivModalTallerImage.setImageResource(R.drawable.ic_flask); // fallback
        }

        if (taller.getVideoUrl() != null && !taller.getVideoUrl().isEmpty()) {
            binding.btnPlayVideo.setVisibility(View.VISIBLE);
            binding.btnPlayVideo.setOnClickListener(v -> playVideo(taller.getVideoUrl()));
        } else {
            binding.btnPlayVideo.setVisibility(View.GONE);
        }

        binding.btnCloseSheet.setOnClickListener(v -> dismiss());

        binding.btnWhatsappInscribirse.setOnClickListener(v -> {
            WhatsAppHelper.abrirInscripcionTaller(requireContext(), taller);
            dismiss();
        });
    }

    private void playVideo(String videoUrl) {
        binding.btnPlayVideo.setVisibility(View.GONE);
        binding.ivModalTallerImage.setVisibility(View.GONE);
        binding.exoPlayerView.setVisibility(View.VISIBLE);
        binding.pbVideoLoading.setVisibility(View.VISIBLE);

        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(requireContext()).build();
            binding.exoPlayerView.setPlayer(exoPlayer);
        }

        Uri uri;
        if (videoUrl.startsWith("http")) {
            uri = Uri.parse(videoUrl);
        } else {
            // Recurso local en raw, ej: "android.resource://com.icsm.minicientificos_android/raw/video_talleres"
            int rawId = requireContext().getResources().getIdentifier(videoUrl, "raw", requireContext().getPackageName());
            if (rawId != 0) {
                uri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + rawId);
            } else {
                uri = Uri.parse(videoUrl); // Fallback
            }
        }

        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        
        // Simular ocultar progressbar cuando esté listo (Idealmente con Listener de Player)
        exoPlayer.addListener(new androidx.media3.common.Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == androidx.media3.common.Player.STATE_READY) {
                    if (binding != null) binding.pbVideoLoading.setVisibility(View.GONE);
                }
            }
        });
    }

    private void poblarLista(LinearLayout container, String[] items, String prefix) {
        container.removeAllViews();
        if (items != null) {
            for (String item : items) {
                TextView tv = new TextView(requireContext());
                tv.setText(prefix + item);
                tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tv.setPadding(0, 0, 0, 8);
                container.addView(tv);
            }
        }
    }

    private void configurarBadgeModalidad(String modalidad) {
        if (modalidad.contains("Presencial") && !modalidad.contains("Híbrido")) {
            binding.tvModalModalidad.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.badge_presencial_bg));
            binding.tvModalModalidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.badge_presencial_text));
        } else if (modalidad.contains("Virtual")) {
            binding.tvModalModalidad.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.badge_virtual_bg));
            binding.tvModalModalidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.badge_virtual_text));
        } else {
            binding.tvModalModalidad.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.badge_hibrido_bg));
            binding.tvModalModalidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.badge_hibrido_text));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        binding = null;
    }
}