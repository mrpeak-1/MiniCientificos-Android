package com.icsm.minicientificos_android.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.GaleriaAdapter;
import com.icsm.minicientificos_android.adapters.GaleriaStoryAdapter;
import com.icsm.minicientificos_android.databinding.FragmentGaleriaBinding;
import com.icsm.minicientificos_android.models.GaleriaCatalogData;
import com.icsm.minicientificos_android.models.GaleriaFoto;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla oficial de Galería con 43 fotografías científicas, filtros en 2 niveles,
 * carrusel interactivo de Stories Científicas, Photo Booth con Canvas,
 * acceso a la Vitrina de Medallas y Visor Fullscreen Pinch-to-Zoom.
 */
public class GaleriaFragment extends Fragment {

    private static final int TOTAL_OFICIAL_FOTOS = 43;

    private FragmentGaleriaBinding binding;
    private GaleriaAdapter galeriaAdapter;

    private String categoriaActiva = GaleriaCatalogData.CAT_TODO;
    private String subgrupoActivo = GaleriaCatalogData.SUB_TODOS;

    public GaleriaFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGaleriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupStoriesCarousel();
        setupAccionesNativas();
        setupFiltrosNivel1();
        setupGrillaFotos();
        setupFooter();

        aplicarFiltroCompleto(GaleriaCatalogData.CAT_TODO, GaleriaCatalogData.SUB_TODOS);
    }

    private void setupStoriesCarousel() {
        List<GaleriaCatalogData.StoryItem> stories = GaleriaCatalogData.obtenerStoriesCientificas();
        GaleriaStoryAdapter storyAdapter = new GaleriaStoryAdapter(stories, story -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            StoryViewerDialog dialog = StoryViewerDialog.newInstance(story);
            dialog.show(getChildFragmentManager(), "StoryViewerDialog");
        });

        binding.rvGaleriaStories.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvGaleriaStories.setAdapter(storyAdapter);
    }

    private void setupAccionesNativas() {
        binding.btnAbrirPhotoBooth.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            PhotoBoothCientificoBottomSheet sheet = new PhotoBoothCientificoBottomSheet();
            sheet.show(getChildFragmentManager(), "PhotoBoothCientificoBottomSheet");
        });

        binding.btnAbrirVitrinaMedallas.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            VitrinaMedallasBottomSheet vitrina = new VitrinaMedallasBottomSheet();
            vitrina.show(getChildFragmentManager(), "VitrinaMedallasBottomSheet");
        });
    }

    private void setupFiltrosNivel1() {
        binding.btnGaleriaTodo.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            aplicarFiltroCompleto(GaleriaCatalogData.CAT_TODO, GaleriaCatalogData.SUB_TODOS);
        });

        binding.btnGaleriaTalleres.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            aplicarFiltroCompleto(GaleriaCatalogData.CAT_TALLERES, GaleriaCatalogData.SUB_TODOS);
        });

        binding.btnGaleriaEventos.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            aplicarFiltroCompleto(GaleriaCatalogData.CAT_EVENTOS, GaleriaCatalogData.SUB_TODOS);
        });

        binding.btnGaleriaFerias.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            aplicarFiltroCompleto(GaleriaCatalogData.CAT_FERIAS, GaleriaCatalogData.SUB_TODOS);
        });
    }

    private void setupGrillaFotos() {
        int columnas = getResources().getConfiguration().smallestScreenWidthDp >= 600 ? 3 : 2;
        galeriaAdapter = new GaleriaAdapter(GaleriaCatalogData.obtenerTodasLasFotos(), (foto, position) -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            List<GaleriaFoto> visibles = galeriaAdapter.getFotosVisibles();
            FullscreenPhotoDialog viewer = FullscreenPhotoDialog.newInstance(visibles, position);
            viewer.show(getChildFragmentManager(), "FullscreenPhotoDialog");
        });

        binding.rvGaleriaFotos.setLayoutManager(new GridLayoutManager(requireContext(), columnas));
        binding.rvGaleriaFotos.setAdapter(galeriaAdapter);
    }

    private void aplicarFiltroCompleto(String categoria, String subgrupo) {
        if (binding == null || getContext() == null) return;

        this.categoriaActiva = categoria;
        this.subgrupoActivo = subgrupo;

        actualizarEstilosBotonesNivel1();
        reconstruirBarraSubgruposAnimada();

        List<GaleriaFoto> filtradas = GaleriaCatalogData.obtenerPorFiltro(categoriaActiva, subgrupoActivo);
        if (galeriaAdapter != null) {
            galeriaAdapter.actualizarFotos(filtradas);
        }

        binding.tvContadorGaleria.setText("Mostrando " + filtradas.size() + " de " + TOTAL_OFICIAL_FOTOS + " fotografías científicas");
        binding.tvGaleriaVacio.setVisibility(filtradas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void actualizarEstilosBotonesNivel1() {
        aplicarEstiloPildoraNivel1(binding.btnGaleriaTodo, GaleriaCatalogData.CAT_TODO.equals(categoriaActiva));
        aplicarEstiloPildoraNivel1(binding.btnGaleriaTalleres, GaleriaCatalogData.CAT_TALLERES.equals(categoriaActiva));
        aplicarEstiloPildoraNivel1(binding.btnGaleriaEventos, GaleriaCatalogData.CAT_EVENTOS.equals(categoriaActiva));
        aplicarEstiloPildoraNivel1(binding.btnGaleriaFerias, GaleriaCatalogData.CAT_FERIAS.equals(categoriaActiva));
    }

    private void aplicarEstiloPildoraNivel1(TextView boton, boolean activo) {
        if (getContext() == null || boton == null) return;
        float density = getResources().getDisplayMetrics().density;
        if (activo) {
            boton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_galeria_pill_active));
            boton.setTextColor(Color.WHITE);
            boton.setElevation(4f * density);
        } else {
            boton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_galeria_pill_inactive));
            boton.setTextColor(Color.parseColor("#0B2C5E"));
            boton.setElevation(0f);
        }
    }

    private void reconstruirBarraSubgruposAnimada() {
        if (binding == null || getContext() == null) return;

        TransitionManager.beginDelayedTransition(binding.llFiltrosGaleriaContainer);
        binding.layoutSubgruposGaleria.removeAllViews();

        List<SubgrupoChipOption> opciones = new ArrayList<>();

        if (GaleriaCatalogData.CAT_TALLERES.equals(categoriaActiva)) {
            opciones.add(new SubgrupoChipOption("Todos (19)", GaleriaCatalogData.SUB_TODOS));
            opciones.add(new SubgrupoChipOption("🪱 Minicompostaje (9)", GaleriaCatalogData.SUB_MINICOMPOSTAJE));
            opciones.add(new SubgrupoChipOption("💧 Tratamiento de Agua (10)", GaleriaCatalogData.SUB_TRATAMIENTO_AGUA));
        } else if (GaleriaCatalogData.CAT_EVENTOS.equals(categoriaActiva)) {
            opciones.add(new SubgrupoChipOption("Todos (14)", GaleriaCatalogData.SUB_TODOS));
            opciones.add(new SubgrupoChipOption("☀️ Fotocatálisis (4)", GaleriaCatalogData.SUB_FOTOCATALISIS));
            opciones.add(new SubgrupoChipOption("🌱 Minibiohuerto (10)", GaleriaCatalogData.SUB_MINIBIOHUERTO));
        } else if (GaleriaCatalogData.CAT_FERIAS.equals(categoriaActiva)) {
            opciones.add(new SubgrupoChipOption("Todos (10)", GaleriaCatalogData.SUB_TODOS));
            opciones.add(new SubgrupoChipOption("♻️ Punto Ecológico (10)", GaleriaCatalogData.SUB_PUNTO_ECOLOGICO));
        } else {
            opciones.add(new SubgrupoChipOption("Todos (43)", GaleriaCatalogData.SUB_TODOS));
            opciones.add(new SubgrupoChipOption("🪱 Minicompostaje (9)", GaleriaCatalogData.SUB_MINICOMPOSTAJE));
            opciones.add(new SubgrupoChipOption("💧 Tratamiento de Agua (10)", GaleriaCatalogData.SUB_TRATAMIENTO_AGUA));
            opciones.add(new SubgrupoChipOption("☀️ Fotocatálisis (4)", GaleriaCatalogData.SUB_FOTOCATALISIS));
            opciones.add(new SubgrupoChipOption("🌱 Minibiohuerto (10)", GaleriaCatalogData.SUB_MINIBIOHUERTO));
            opciones.add(new SubgrupoChipOption("♻️ Punto Ecológico (10)", GaleriaCatalogData.SUB_PUNTO_ECOLOGICO));
        }

        float density = getResources().getDisplayMetrics().density;
        int padH = (int) (14 * density);
        int altura = (int) (36 * density);
        int margenEnd = (int) (8 * density);
        Typeface nunitoFont = binding.tvContadorGaleria.getTypeface();
        if (nunitoFont == null) {
            nunitoFont = Typeface.SANS_SERIF;
        }

        for (SubgrupoChipOption opcion : opciones) {
            boolean activo = opcion.valorSubgrupo.equalsIgnoreCase(subgrupoActivo);

            TextView chip = new TextView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, altura);
            params.setMarginEnd(margenEnd);
            chip.setLayoutParams(params);
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(padH, 0, padH, 0);
            chip.setText(opcion.etiqueta);
            chip.setTextSize(12.5f);
            chip.setTypeface(nunitoFont != null ? nunitoFont : Typeface.DEFAULT, Typeface.BOLD);
            chip.setClickable(true);
            chip.setFocusable(true);

            if (activo) {
                chip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_subgrupo_chip_active));
                chip.setTextColor(Color.parseColor("#FDC500"));
                chip.setElevation(3f * density);
            } else {
                chip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_subgrupo_chip_inactive));
                chip.setTextColor(Color.parseColor("#0B2C5E"));
                chip.setElevation(0f);
            }

            chip.setOnClickListener(v -> {
                GameSoundAndVfxHelper.getInstance().playClick();
                aplicarFiltroCompleto(categoriaActiva, opcion.valorSubgrupo);
            });

            binding.layoutSubgruposGaleria.addView(chip);
        }
    }

    private void setupFooter() {
        if (getContext() != null && binding != null && binding.sectionCorporateFooterGaleria != null) {
            CorporateFooterHelper.setupFooter(requireContext(), binding.sectionCorporateFooterGaleria);
        }
    }

    @Override
    public void onDestroyView() {
        if (galeriaAdapter != null) {
            galeriaAdapter.liberarRecursos();
        }
        super.onDestroyView();
        binding = null;
    }

    private static class SubgrupoChipOption {
        final String etiqueta;
        final String valorSubgrupo;

        SubgrupoChipOption(String etiqueta, String valorSubgrupo) {
            this.etiqueta = etiqueta;
            this.valorSubgrupo = valorSubgrupo;
        }
    }
}
