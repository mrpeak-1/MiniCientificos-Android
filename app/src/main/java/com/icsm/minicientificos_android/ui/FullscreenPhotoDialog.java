package com.icsm.minicientificos_android.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.icsm.minicientificos_android.databinding.DialogFullscreenPhotoBinding;
import com.icsm.minicientificos_android.models.GaleriaCatalogData;
import com.icsm.minicientificos_android.models.GaleriaFoto;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Visor Fullscreen interactivo con Pinch-to-Zoom real, navegación entre fotos
 * filtradas y consulta directa por WhatsApp.
 */
public class FullscreenPhotoDialog extends DialogFragment {

    private static final String ARG_FOTOS = "arg_fotos";
    private static final String ARG_POSICION = "arg_posicion";

    private DialogFullscreenPhotoBinding binding;
    private List<GaleriaFoto> fotos = new ArrayList<>();
    private int currentIndex = 0;

    public FullscreenPhotoDialog() {
        // Constructor público vacío requerido
    }

    public static FullscreenPhotoDialog newInstance(List<GaleriaFoto> fotosFiltradas, int posicionInicial) {
        FullscreenPhotoDialog dialog = new FullscreenPhotoDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOTOS, new ArrayList<>(fotosFiltradas));
        args.putInt(ARG_POSICION, posicionInicial);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        if (getArguments() != null) {
            Serializable serializable = getArguments().getSerializable(ARG_FOTOS);
            if (serializable instanceof List<?>) {
                List<?> raw = (List<?>) serializable;
                fotos = new ArrayList<>();
                for (Object o : raw) {
                    if (o instanceof GaleriaFoto) {
                        fotos.add((GaleriaFoto) o);
                    }
                }
            }
            currentIndex = getArguments().getInt(ARG_POSICION, 0);
        }

        if (fotos == null || fotos.isEmpty()) {
            fotos = GaleriaCatalogData.obtenerTodasLasFotos();
            currentIndex = 0;
        }
        if (currentIndex < 0 || currentIndex >= fotos.size()) {
            currentIndex = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogFullscreenPhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
        renderFotoActual();
    }

    private void setupListeners() {
        binding.btnCloseFullscreen.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            dismiss();
        });

        binding.btnPrevPhoto.setOnClickListener(v -> {
            if (fotos.size() <= 1) return;
            GameSoundAndVfxHelper.getInstance().playClick();
            currentIndex = (currentIndex - 1 + fotos.size()) % fotos.size();
            renderFotoActual();
        });

        binding.btnNextPhoto.setOnClickListener(v -> {
            if (fotos.size() <= 1) return;
            GameSoundAndVfxHelper.getInstance().playClick();
            currentIndex = (currentIndex + 1) % fotos.size();
            renderFotoActual();
        });

        binding.btnFullscreenWhatsApp.setOnClickListener(v -> {
            if (fotos.isEmpty() || getContext() == null) return;
            GameSoundAndVfxHelper.getInstance().playClick();
            GaleriaFoto foto = fotos.get(currentIndex);
            String mensaje = "¡Hola Mini Científicos! 🔬 Vi la fotografía \"" + foto.getTitulo() +
                    "\" del subgrupo *" + foto.getSubgrupo() + "* (" + foto.getCategoriaPrincipal() +
                    ") en la galería de la app. ¿Me brindan información sobre cómo participar en este taller o evento?";
            WhatsAppHelper.abrirSoporteDirecto(requireContext(), mensaje);
        });
    }

    private void renderFotoActual() {
        if (binding == null || getContext() == null || fotos.isEmpty()) return;

        GaleriaFoto foto = fotos.get(currentIndex);
        int resId = foto.resolveDrawableResId(requireContext());

        binding.tvPhotoCounter.setText("Foto " + (currentIndex + 1) + " de " + fotos.size());
        binding.tvFullscreenSubgrupo.setText(foto.getSubgrupo());
        binding.tvFullscreenFechaLugar.setText(foto.getFechaLugar());
        binding.tvFullscreenTitulo.setText(foto.getTitulo());
        binding.tvFullscreenDescripcion.setText(foto.getDescripcionActividad());

        binding.ivFullscreenZoom.setImageResource(resId);
        binding.ivFullscreenZoom.resetZoom();

        boolean mostrarFlechas = fotos.size() > 1;
        binding.btnPrevPhoto.setVisibility(mostrarFlechas ? View.VISIBLE : View.GONE);
        binding.btnNextPhoto.setVisibility(mostrarFlechas ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
