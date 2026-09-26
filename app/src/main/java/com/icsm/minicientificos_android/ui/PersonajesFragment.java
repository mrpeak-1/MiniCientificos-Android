package com.icsm.minicientificos_android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icsm.minicientificos_android.databinding.FragmentPersonajesBinding;

public class PersonajesFragment extends Fragment {

    private FragmentPersonajesBinding binding;

    public PersonajesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonajesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnTitanInteract.setOnClickListener(v -> mostrarSuperpoderTitan());
        binding.cardTitanFicha.setOnClickListener(v -> mostrarSuperpoderTitan());

        binding.btnTeslaInteract.setOnClickListener(v -> mostrarSecretoTesla());
        binding.cardTeslaFicha.setOnClickListener(v -> mostrarSecretoTesla());
    }

    private void mostrarSuperpoderTitan() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("🚀 Superpoder de Titan: ¡Reacción Espacial!")
                .setMessage("Titan ha activado una reacción química de espuma gigante y cohetes hidráulicos.\n\n" +
                        "• Especialidad: Física experimental y Astronomía.\n" +
                        "• Frase favorita: «¡La ciencia es la aventura más grande de la galaxia!»")
                .setPositiveButton("¡Excelente!", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void mostrarSecretoTesla() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("✨ El Secreto de Tesla: Duende de los Andes")
                .setMessage("Tesla canaliza la energía limpia de las montañas andinas y el sol para alimentar sus inventos comunitarios.\n\n" +
                        "• Especialidad: Robótica, Energía Renovable y Biohuertos.\n" +
                        "• Frase favorita: «Allin Kawsay: La tecnología debe cuidar a la Pachamama».")
                .setPositiveButton("¡Inspirador!", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}