package com.icsm.minicientificos_android.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetTallerDetalleBinding;
import com.icsm.minicientificos_android.models.Taller;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

import java.util.Locale;

public class TallerDetalleBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_TALLER = "arg_taller";
    private BottomSheetTallerDetalleBinding binding;
    private Taller taller;
    private ExoPlayer exoPlayer;
    private boolean isMuted = true;
    private Handler progressHandler;
    private Runnable progressRunnable;
    private String modalidadSeleccionada;

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
        
        modalidadSeleccionada = taller.getModalidad();

        // Llenar datos principales
        binding.tvModalTitulo.setText(taller.getTituloCorto() != null ? taller.getTituloCorto() : taller.getTitulo());
        binding.tvModalObjetivoDesc.setText(taller.getObjetivo());
        binding.tvModalHorario.setText(taller.getHorario());
        binding.tvModalCupos.setText(taller.getVacantes());
        binding.tvModalPrecio.setText(String.format(Locale.US, "Inversión: S/ %.2f", taller.getPrecio()));
        binding.tvModalCategoria.setText(taller.getCategoria());
        binding.tvModalDuracion.setText(taller.getDuracion());
        binding.tvModalEdad.setText(taller.getEdadRecomendada());

        // Llenar listas (Temario y Materiales)
        poblarLista(binding.llTemarioContainer, taller.getTemario(), "• ");
        poblarLista(binding.llMaterialesContainer, taller.getMaterialesIncluidos(), "✔️ ");

        // Selector Interactivo Modalidad (Si es más de una)
        if (taller.getModalidad().contains("/") || taller.getModalidad().contains(" o ")) {
            binding.llSelectorModalidad.setVisibility(View.VISIBLE);
            binding.chipModalidadPresencial.setOnClickListener(v -> actualizarSeleccionModalidad("Presencial"));
            binding.chipModalidadVirtual.setOnClickListener(v -> actualizarSeleccionModalidad("Virtual"));
            // Seleccionar por defecto la primera
            actualizarSeleccionModalidad(taller.getModalidad().contains("Presencial") ? "Presencial" : "Virtual");
        } else {
            binding.llSelectorModalidad.setVisibility(View.GONE);
        }

        // Imagen vs Video
        int resId = requireContext().getResources().getIdentifier(taller.getDrawableName(), "drawable", requireContext().getPackageName());
        if (resId != 0) {
            binding.imgPosterDetalleFallback.setImageResource(resId);
        } else {
            binding.imgPosterDetalleFallback.setImageResource(R.drawable.ic_flask); // fallback
        }

        if (taller.getVideoRawName() != null && !taller.getVideoRawName().isEmpty()) {
            iniciarVideoHibrido(taller.getVideoRawName());
        }

        binding.btnCerrarDetalleTaller.setOnClickListener(v -> dismiss());

        binding.btnWhatsappInscribirse.setOnClickListener(v -> {
            // Actualizar la modalidad del taller con la seleccionada localmente
            Taller tallerInscripcion = new Taller(taller.getId(), taller.getTituloCorto(), taller.getPosterLinea1(), 
                    taller.getPosterLinea2(), taller.getCategoria(), modalidadSeleccionada, taller.getDescripcionCorta(), 
                    taller.getObjetivo(), taller.getTemario(), taller.getMaterialesIncluidos(), taller.getHorario(), 
                    taller.getEdadRecomendada(), taller.getDuracion(), taller.getVacantes(), taller.getPrecio(), 
                    taller.getDrawableName(), taller.getVideoRawName());
            WhatsAppHelper.abrirInscripcionTaller(requireContext(), tallerInscripcion);
            dismiss();
        });
    }
    
    private void actualizarSeleccionModalidad(String mod) {
        modalidadSeleccionada = mod;
        if (mod.equals("Presencial")) {
            binding.chipModalidadPresencial.setBackgroundResource(R.drawable.bg_filter_pill_green_active);
            binding.chipModalidadPresencial.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.chipModalidadVirtual.setBackgroundResource(R.drawable.bg_filter_pill_white_inactive);
            binding.chipModalidadVirtual.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_light));
        } else {
            binding.chipModalidadVirtual.setBackgroundResource(R.drawable.bg_filter_pill_green_active);
            binding.chipModalidadVirtual.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.chipModalidadPresencial.setBackgroundResource(R.drawable.bg_filter_pill_white_inactive);
            binding.chipModalidadPresencial.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_light));
        }
    }

    private void iniciarVideoHibrido(String videoRawName) {
        binding.layoutCustomVideoControls.setVisibility(View.VISIBLE);
        binding.playerViewDetalleTaller.setVisibility(View.VISIBLE);
        binding.pbVideoLoading.setVisibility(View.VISIBLE);

        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(requireContext()).build();
            binding.playerViewDetalleTaller.setPlayer(exoPlayer);
        }

        Uri uri;
        if (videoRawName.startsWith("http")) {
            uri = Uri.parse(videoRawName);
        } else {
            int rawId = requireContext().getResources().getIdentifier(videoRawName, "raw", requireContext().getPackageName());
            if (rawId != 0) {
                uri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + rawId);
            } else {
                uri = Uri.parse(videoRawName);
            }
        }

        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        exoPlayer.setVolume(0f); // Inicia silenciado
        exoPlayer.prepare();
        exoPlayer.play();
        
        configurarControlesCustomizados();

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    if (binding != null) {
                        binding.pbVideoLoading.setVisibility(View.GONE);
                        binding.imgPosterDetalleFallback.setVisibility(View.GONE);
                        binding.seekBarVideoTaller.setMax((int) exoPlayer.getDuration());
                    }
                }
            }
        });
    }
    
    private void configurarControlesCustomizados() {
        binding.btnPlayPauseVideo.setOnClickListener(v -> {
            if (exoPlayer != null) {
                if (exoPlayer.isPlaying()) {
                    exoPlayer.pause();
                    binding.btnPlayPauseVideo.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    exoPlayer.play();
                    binding.btnPlayPauseVideo.setImageResource(android.R.drawable.ic_media_pause);
                    // Ocultar botón después de 2.5s
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (binding != null && exoPlayer.isPlaying()) {
                            binding.btnPlayPauseVideo.setVisibility(View.GONE);
                        }
                    }, 2500);
                }
            }
        });
        
        binding.playerViewDetalleTaller.setOnClickListener(v -> {
            binding.btnPlayPauseVideo.setVisibility(binding.btnPlayPauseVideo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        binding.btnMuteUnmuteTaller.setOnClickListener(v -> {
            isMuted = !isMuted;
            exoPlayer.setVolume(isMuted ? 0f : 1f);
            binding.btnMuteUnmuteTaller.setText(isMuted ? "🔇 Sonido" : "🔊 Con audio");
        });
        
        binding.seekBarVideoTaller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && exoPlayer != null) {
                    exoPlayer.seekTo(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        progressHandler = new Handler(Looper.getMainLooper());
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && binding != null && exoPlayer.isPlaying()) {
                    binding.seekBarVideoTaller.setProgress((int) exoPlayer.getCurrentPosition());
                    binding.tvTiempoVideo.setText(formatearTiempo(exoPlayer.getCurrentPosition()) + " / " + formatearTiempo(exoPlayer.getDuration()));
                }
                if (progressHandler != null) {
                    progressHandler.postDelayed(this, 250);
                }
            }
        };
        progressHandler.post(progressRunnable);
    }
    
    private String formatearTiempo(long ms) {
        if (ms < 0) return "0:00";
        long s = ms / 1000;
        long m = s / 60;
        s %= 60;
        return String.format(Locale.US, "%d:%02d", m, s);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressHandler != null) {
            progressHandler.removeCallbacks(progressRunnable);
            progressHandler = null;
        }
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        binding = null;
    }
}