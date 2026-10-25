package com.icsm.minicientificos_android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.models.ServicioCientifico;

public class ServicioHomeBottomSheet extends BottomSheetDialogFragment {

    private ServicioCientifico servicio;

    public ServicioHomeBottomSheet() {}

    public static ServicioHomeBottomSheet newInstance(ServicioCientifico servicio) {
        ServicioHomeBottomSheet fragment = new ServicioHomeBottomSheet();
        fragment.servicio = servicio;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_servicio_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (servicio == null) {
            dismiss();
            return;
        }

        ImageView imgIcon = view.findViewById(R.id.img_modal_servicio_icon);
        TextView tvTitulo = view.findViewById(R.id.tv_modal_servicio_titulo);
        TextView tvModalidad = view.findViewById(R.id.tv_modal_servicio_modalidad);
        TextView tvDesc = view.findViewById(R.id.tv_modal_servicio_desc);
        LinearLayout containerBeneficios = view.findViewById(R.id.container_modal_beneficios);
        View btnGaleria = view.findViewById(R.id.btn_modal_ver_galeria);
        View btnWhatsapp = view.findViewById(R.id.btn_modal_cotizar_whatsapp);

        imgIcon.setImageResource(servicio.getIconoResId());
        try {
            imgIcon.setColorFilter(Color.parseColor(servicio.getColorAcentoHex()));
        } catch (Exception ignored) {}

        tvTitulo.setText(servicio.getTitulo());
        tvModalidad.setText("Edad: " + servicio.getEdades() + " • Duración: " + servicio.getDuracion());
        tvDesc.setText(servicio.getDescripcionExtendida());

        if (servicio.getBeneficiosList() != null && containerBeneficios != null) {
            containerBeneficios.removeAllViews();
            for (String beneficio : servicio.getBeneficiosList()) {
                TextView item = new TextView(requireContext());
                item.setText("✅ " + beneficio);
                item.setTextColor(Color.parseColor("#0B2C5E"));
                item.setTextSize(13.5f);
                item.setPadding(0, 6, 0, 6);
                containerBeneficios.addView(item);
            }
        }

        btnGaleria.setOnClickListener(v -> {
            dismiss();
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new GaleriaFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnWhatsapp.setOnClickListener(v -> {
            dismiss();
            String url = "https://api.whatsapp.com/send?phone=51930754024&text=" + Uri.encode(servicio.getMensajeWhatsApp());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Abrir WhatsApp: " + url, Toast.LENGTH_SHORT).show();
            }
        });
    }
}