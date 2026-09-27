package com.icsm.minicientificos_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetTallerDetalleBinding;
import com.icsm.minicientificos_android.models.TallerMock;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TallerDetalleBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_TALLER = "arg_taller";
    private BottomSheetTallerDetalleBinding binding;
    private TallerMock taller;

    public static TallerDetalleBottomSheet newInstance(TallerMock taller) {
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
            taller = (TallerMock) getArguments().getSerializable(ARG_TALLER);
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

        // Llenar datos en las vistas
        binding.tvModalTitulo.setText(taller.getTitulo());
        binding.tvModalDescripcionCompleta.setText(taller.getDescripcionCompleta());
        binding.tvModalFecha.setText(taller.getFecha());
        binding.tvModalCupos.setText(taller.getCupos());
        binding.tvModalRequisitos.setText(taller.getRequisitos());
        binding.tvModalPrecio.setText("Inversión: " + taller.getPrecio());
        binding.tvModalCategoria.setText(taller.getCategoria());
        binding.tvModalModalidad.setText(taller.getModalidad());
        binding.ivModalTallerImage.setImageResource(taller.getImagenResId());

        // Colores según la modalidad
        configurarBadgeModalidad(taller.getModalidad());

        // Acción cerrar
        binding.btnCloseSheet.setOnClickListener(v -> dismiss());

        // Acción WhatsApp
        binding.btnWhatsappInscribirse.setOnClickListener(v -> abrirWhatsappInscripcion());
    }

    private void configurarBadgeModalidad(String modalidad) {
        if ("Presencial".equalsIgnoreCase(modalidad)) {
            binding.tvModalModalidad.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.badge_presencial_bg));
            binding.tvModalModalidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.badge_presencial_text));
        } else if ("Virtual".equalsIgnoreCase(modalidad)) {
            binding.tvModalModalidad.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.badge_virtual_bg));
            binding.tvModalModalidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.badge_virtual_text));
        } else {
            binding.tvModalModalidad.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.badge_hibrido_bg));
            binding.tvModalModalidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.badge_hibrido_text));
        }
    }

    private void abrirWhatsappInscripcion() {
        try {
            String mensaje = "Hola Mini Científicos, deseo inscribirme en el taller: *" + taller.getTitulo() + "* (" + taller.getModalidad() + ").";
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