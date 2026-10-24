package com.icsm.minicientificos_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.databinding.BottomSheetPlanetaExperimentoBinding;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PlanetaExperimentoBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_TITULO = "arg_titulo";
    private static final String ARG_CONCEPTO = "arg_concepto";
    private static final String ARG_EXPERIMENTO = "arg_experimento";
    private static final String ARG_IMG_RES = "arg_img_res";

    private BottomSheetPlanetaExperimentoBinding binding;

    public static PlanetaExperimentoBottomSheet newInstance(String titulo, String concepto, String experimento, int imgResId) {
        PlanetaExperimentoBottomSheet fragment = new PlanetaExperimentoBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_TITULO, titulo);
        args.putString(ARG_CONCEPTO, concepto);
        args.putString(ARG_EXPERIMENTO, experimento);
        args.putInt(ARG_IMG_RES, imgResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetPlanetaExperimentoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            String titulo = getArguments().getString(ARG_TITULO, "Ecosistema Científico");
            String concepto = getArguments().getString(ARG_CONCEPTO, "Concepto de ciencia interactiva.");
            String experimento = getArguments().getString(ARG_EXPERIMENTO, "Instrucciones de experimento casero.");
            int imgRes = getArguments().getInt(ARG_IMG_RES, 0);

            binding.tvPlanetaTitulo.setText(titulo);
            binding.tvPlanetaDescripcion.setText(concepto);
            binding.tvPlanetaExperimentoPasos.setText(experimento);

            if (imgRes != 0) {
                binding.ivPlanetaEcosistemaImg.setImageResource(imgRes);
            }

            binding.btnPlanetaWhatsapp.setOnClickListener(v -> abrirWhatsapp(titulo));
        }

        binding.btnClosePlanetaSheet.setOnClickListener(v -> dismiss());
    }

    private void abrirWhatsapp(String ecosistema) {
        try {
            String mensaje = "Hola Mini Científicos, deseo más información del taller sobre el ecosistema: *" + ecosistema + "*.";
            String url = "https://api.whatsapp.com/send?phone=51930754024&text=" + URLEncoder.encode(mensaje, StandardCharsets.UTF_8.name());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}