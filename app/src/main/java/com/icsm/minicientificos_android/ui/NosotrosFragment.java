package com.icsm.minicientificos_android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icsm.minicientificos_android.databinding.FragmentNosotrosBinding;

public class NosotrosFragment extends Fragment {

    private FragmentNosotrosBinding binding;

    public NosotrosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNosotrosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupVoluntariosListeners();
    }

    private void setupVoluntariosListeners() {
        binding.cardVoluntario1.setOnClickListener(v -> mostrarDetalleVoluntario(
                "Dr. Juan Pérez", "Coordinador General ICSM", "Especialista en Fotocatálisis y Nanomateriales. Lidera el equipo de investigación y proyectos comunitarios."));

        binding.cardVoluntario2.setOnClickListener(v -> mostrarDetalleVoluntario(
                "Lic. María Quispe", "Educación STEAM", "Educadora apasionada por la enseñanza lúdica de las ciencias y diseño de experimentos para niños."));

        binding.cardVoluntario3.setOnClickListener(v -> mostrarDetalleVoluntario(
                "Ing. Carlos Mendoza", "Mentor de Robótica", "Ingeniero mecatrónico a cargo de los kits de bobinas, circuitos y robótica ambiental."));

        binding.cardVoluntario4.setOnClickListener(v -> mostrarDetalleVoluntario(
                "Sofía Torrico", "Líder de Voluntariado", "Estudiante de ingeniería ambiental que coordina las ferias y talleres comunitarios de compostaje."));
    }

    private void mostrarDetalleVoluntario(String nombre, String cargo, String bio) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("👨‍🔬 " + nombre)
                .setMessage("• Cargo: " + cargo + "\n\n" + bio)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}