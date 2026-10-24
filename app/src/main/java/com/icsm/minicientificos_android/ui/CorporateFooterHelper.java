package com.icsm.minicientificos_android.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.icsm.minicientificos_android.databinding.LayoutCorporateFooterBinding;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

public class CorporateFooterHelper {

    public static void setupFooter(Context context, LayoutCorporateFooterBinding binding) {
        if (context == null || binding == null) return;

        // Soporte Telefónico / WhatsApp Directo
        binding.tvFooterPhone.setOnClickListener(v -> {
            WhatsAppHelper.abrirSoporteDirecto(context, "Hola Mini Científicos, requiero soporte o información sobre los cursos.");
        });

        // Web Oficial
        binding.tvFooterWeb.setOnClickListener(v -> abrirUrl(context, "https://minicientificos-proces.vercel.app/"));

        // Redes Sociales
        binding.tvFooterFacebook.setOnClickListener(v -> abrirUrl(context, "https://facebook.com/minicientificosoficial"));
        binding.tvFooterInstagram.setOnClickListener(v -> abrirUrl(context, "https://instagram.com/minicientificosoficial"));
    }

    private static void abrirUrl(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "No se pudo abrir el enlace: " + url, Toast.LENGTH_SHORT).show();
        }
    }
}