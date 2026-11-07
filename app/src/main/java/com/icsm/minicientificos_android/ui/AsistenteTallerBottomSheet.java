package com.icsm.minicientificos_android.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetAsistenteTallerBinding;
import com.icsm.minicientificos_android.models.Taller;
import com.icsm.minicientificos_android.models.TalleresCatalogData;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

import java.util.List;

public class AsistenteTallerBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetAsistenteTallerBinding binding;
    private List<Taller> todosTalleres;
    private Taller tallerRecomendadoActual;

    public AsistenteTallerBottomSheet() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAsistenteTallerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        todosTalleres = TalleresCatalogData.getTalleres();

        binding.btnCerrarAsistente.setOnClickListener(v -> dismiss());

        setupListenersChips();

        binding.btnVerTallerRecomendado.setOnClickListener(v -> abrirTallerRecomendado());
        binding.btnWhatsappAsesor.setOnClickListener(v -> hablarConAsesor());

        actualizarRecomendacion();
    }

    private void setupListenersChips() {
        Chip[] chipsP1 = new Chip[]{
                binding.chipEspacioRobots,
                binding.chipExperimentosLocos,
                binding.chipPlantasNaturaleza,
                binding.chipRocasFosiles
        };

        Chip[] chipsP2 = new Chip[]{
                binding.chipPresencial,
                binding.chipVirtual,
                binding.chipCualquiera
        };

        for (Chip c : chipsP1) {
            c.setOnClickListener(v -> {
                binding.cgTemaInteres.check(c.getId());
                actualizarRecomendacion();
            });
        }

        for (Chip c : chipsP2) {
            c.setOnClickListener(v -> {
                binding.cgModalidadPref.check(c.getId());
                actualizarRecomendacion();
            });
        }

        binding.cgTemaInteres.setOnCheckedStateChangeListener((group, checkedIds) -> actualizarRecomendacion());
        binding.cgModalidadPref.setOnCheckedStateChangeListener((group, checkedIds) -> actualizarRecomendacion());
    }

    private void actualizarRecomendacion() {
        aplicarEfectoVisibilidadChips();

        int temaId = binding.cgTemaInteres.getCheckedChipId();
        int modalidadId = binding.cgModalidadPref.getCheckedChipId();

        String tituloBuscado = "Astronomía";

        if (temaId == R.id.chipEspacioRobots) {
            if (modalidadId == R.id.chipPresencial) {
                tituloBuscado = "Astronomía";
            } else if (modalidadId == R.id.chipVirtual) {
                tituloBuscado = "Inteligencia Artificial";
            } else {
                tituloBuscado = "Bobina Tesla";
            }
        } else if (temaId == R.id.chipExperimentosLocos) {
            if (modalidadId == R.id.chipPresencial) {
                tituloBuscado = "Bobina Tesla";
            } else if (modalidadId == R.id.chipVirtual) {
                tituloBuscado = "Microbiología";
            } else {
                tituloBuscado = "Física Loca";
            }
        } else if (temaId == R.id.chipPlantasNaturaleza) {
            if (modalidadId == R.id.chipPresencial) {
                tituloBuscado = "Reciclaje Ecológico";
            } else if (modalidadId == R.id.chipVirtual) {
                tituloBuscado = "Hidroponía";
            } else {
                tituloBuscado = "Mini Huerto";
            }
        } else if (temaId == R.id.chipRocasFosiles) {
            if (modalidadId == R.id.chipPresencial) {
                tituloBuscado = "Arqueología";
            } else if (modalidadId == R.id.chipVirtual) {
                tituloBuscado = "Minería del Futuro";
            } else {
                tituloBuscado = "Geología Divertida";
            }
        }

        tallerRecomendadoActual = buscarTallerPorTitulo(tituloBuscado);
        if (tallerRecomendadoActual == null && todosTalleres != null && !todosTalleres.isEmpty()) {
            tallerRecomendadoActual = todosTalleres.get(0);
        }

        if (tallerRecomendadoActual != null) {
            binding.tvRecomendacionTitulo.setText(tallerRecomendadoActual.getTitulo());
            binding.tvRecomendacionDesc.setText(tallerRecomendadoActual.getDescripcionCorta());
            binding.tvRecomendacionEmoji.setText(obtenerEmojiParaTaller(tallerRecomendadoActual));
            binding.tvRecomendacionModalidad.setText(tallerRecomendadoActual.getModalidad().toUpperCase());
        }
    }

    private void aplicarEfectoVisibilidadChips() {
        if (binding == null) return;

        int selectedTemaId = binding.cgTemaInteres.getCheckedChipId();
        int selectedModalidadId = binding.cgModalidadPref.getCheckedChipId();

        Chip[] chipsP1 = new Chip[]{
                binding.chipEspacioRobots,
                binding.chipExperimentosLocos,
                binding.chipPlantasNaturaleza,
                binding.chipRocasFosiles
        };

        Chip[] chipsP2 = new Chip[]{
                binding.chipPresencial,
                binding.chipVirtual,
                binding.chipCualquiera
        };

        actualizarGrupoChipsPorId(chipsP1, selectedTemaId);
        actualizarGrupoChipsPorId(chipsP2, selectedModalidadId);
    }

    private void actualizarGrupoChipsPorId(Chip[] chips, int selectedId) {
        if (chips == null) return;
        int goldenColor = Color.parseColor("#FDC500");

        for (Chip chip : chips) {
            if (chip == null) continue;

            boolean isSelected = (chip.getId() == selectedId);

            if (isSelected) {
                chip.setChipStrokeColor(ColorStateList.valueOf(goldenColor));
                chip.setChipStrokeWidth(10.0f);
                chip.setAlpha(1.0f);
                chip.setElevation(10.0f);
                chip.animate().scaleX(1.12f).scaleY(1.12f).setDuration(180).start();
            } else {
                chip.setChipStrokeWidth(0.0f);
                chip.setAlpha(0.40f);
                chip.setElevation(0.0f);
                chip.animate().scaleX(0.90f).scaleY(0.90f).setDuration(180).start();
            }
        }
    }

    private String obtenerEmojiParaTaller(Taller taller) {
        if (taller == null) return "🔬";
        String t = taller.getTitulo().toLowerCase();
        if (t.contains("astro")) return "🔭";
        if (t.contains("tesla") || t.contains("física") || t.contains("fisica")) return "⚡";
        if (t.contains("microbio")) return "🔬";
        if (t.contains("minería") || t.contains("mineria")) return "⛑️";
        if (t.contains("arqueo")) return "⛏️";
        if (t.contains("reciclaje")) return "🌱";
        if (t.contains("geolo")) return "🧭";
        if (t.contains("huerto")) return "🍎";
        if (t.contains("hidro")) return "💧";
        if (t.contains("inteligencia") || t.contains("ia") || t.contains("robot")) return "🤖";
        if (t.contains("energía") || t.contains("energia")) return "☀️";
        return "🔬";
    }

    private Taller buscarTallerPorTitulo(String query) {
        if (todosTalleres == null) return null;
        for (Taller t : todosTalleres) {
            if (t.getTitulo().toLowerCase().contains(query.toLowerCase()) ||
                t.getTituloCorto().toLowerCase().contains(query.toLowerCase())) {
                return t;
            }
        }
        return null;
    }

    private void abrirTallerRecomendado() {
        dismiss();
        if (getActivity() != null && tallerRecomendadoActual != null) {
            WorkshopsFragment fragment = WorkshopsFragment.newInstanceWithTaller(tallerRecomendadoActual.getId());

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

            BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
            if (bnv != null) {
                bnv.setSelectedItemId(R.id.nav_workshops);
            }
        }
    }

    private void hablarConAsesor() {
        if (getContext() != null && tallerRecomendadoActual != null) {
            WhatsAppHelper.abrirInscripcionTaller(requireContext(), tallerRecomendadoActual);
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}