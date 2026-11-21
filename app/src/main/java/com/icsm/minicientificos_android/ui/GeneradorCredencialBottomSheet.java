package com.icsm.minicientificos_android.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetGeneradorCredencialBinding;
import com.icsm.minicientificos_android.utils.CredencialBitmapRenderer;
import com.icsm.minicientificos_android.utils.GameProgressManager;

public class GeneradorCredencialBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetGeneradorCredencialBinding binding;
    private Bitmap currentBitmap;

    public GeneradorCredencialBottomSheet() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetGeneradorCredencialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String nombreGuardado = GameProgressManager.getInstance(requireContext()).getNombreAgenteGuardado();
        binding.etNombreNinoCredencial.setText(nombreGuardado);

        setupListeners();
        actualizarVistaPrevia();
    }

    private void setupListeners() {
        binding.btnCloseCredencial.setOnClickListener(v -> dismiss());

        binding.etNombreNinoCredencial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GameProgressManager.getInstance(requireContext()).setNombreAgenteGuardado(s.toString());
                actualizarVistaPrevia();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.chipGroupTipoDoc.setOnCheckedStateChangeListener((group, checkedIds) -> actualizarVistaPrevia());
        binding.chipGroupCompanero.setOnCheckedStateChangeListener((group, checkedIds) -> actualizarVistaPrevia());
        binding.chipGroupEspecialidad.setOnCheckedStateChangeListener((group, checkedIds) -> actualizarVistaPrevia());

        binding.btnActualizarVistaPrevia.setOnClickListener(v -> actualizarVistaPrevia());

        binding.btnCompartirCredencialPng.setOnClickListener(v -> {
            if (currentBitmap != null) {
                String nombreNino = obtenerNombreActual();
                String nombreArchivo = "Credencial_" + nombreNino.replaceAll("\\s+", "_") + ".png";
                CredencialBitmapRenderer.compartirBitmapComoPng(requireContext(), currentBitmap, nombreArchivo);
            }
        });
    }

    private void actualizarVistaPrevia() {
        if (getContext() == null || binding == null) return;

        int tipoDoc = binding.chipTipoDiploma.isChecked() ?
                CredencialBitmapRenderer.TIPO_DIPLOMA : CredencialBitmapRenderer.TIPO_CARNET;

        String nombreNino = obtenerNombreActual();
        String companero = binding.chipTesla.isChecked() ? "⚡ Tesla Ecológico" : "🐱 Titan Astronauta";
        String especialidad = obtenerEspecialidadSeleccionada();

        currentBitmap = CredencialBitmapRenderer.renderizarDocumento(
                requireContext(),
                tipoDoc,
                nombreNino,
                companero,
                especialidad
        );

        if (currentBitmap != null && binding != null) {
            binding.imgPreviewCredencialCanvas.setImageBitmap(currentBitmap);
        }
    }

    private String obtenerNombreActual() {
        if (binding == null || binding.etNombreNinoCredencial.getText() == null) {
            return "Mini Científico(a)";
        }
        String nombre = binding.etNombreNinoCredencial.getText().toString().trim();
        return nombre.isEmpty() ? "Mini Científico(a)" : nombre;
    }

    private String obtenerEspecialidadSeleccionada() {
        if (binding == null) return "🔭 Astronomía";

        if (binding.chipQuimica.isChecked()) {
            return "🧪 Química Loca";
        } else if (binding.chipBiohuerto.isChecked()) {
            return "🌱 Biohuerto y Agua";
        } else if (binding.chipRobotica.isChecked()) {
            return "🤖 Robótica e IA";
        }
        return "🔭 Astronomía";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}