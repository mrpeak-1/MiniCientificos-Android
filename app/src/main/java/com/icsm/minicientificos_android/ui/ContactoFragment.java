package com.icsm.minicientificos_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.icsm.minicientificos_android.MainActivity;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.FragmentContactoBinding;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

public class ContactoFragment extends Fragment {

    private FragmentContactoBinding binding;

    public ContactoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContactoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupMotivoDropdown();
        setupHeroButtons();
        setupQuickAccessCards();
        setupFormSubmit();
        setupCorporateFooter();
    }

    private void setupMotivoDropdown() {
        String[] opcionesMotivo = new String[]{
                "Talleres Científicos (Presencial / Virtual)",
                "Eventos Especiales / Cumpleaños Científicos",
                "Feria de Ciencias / Colegios",
                "Tienda Oficial / Kits Científicos",
                "Otra consulta espacial"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                opcionesMotivo
        );

        binding.actvContactoMotivo.setAdapter(adapter);

        binding.actvContactoMotivo.setOnClickListener(v -> binding.actvContactoMotivo.showDropDown());
        binding.btnFlechaMotivo.setOnClickListener(v -> binding.actvContactoMotivo.showDropDown());
    }

    private void setupHeroButtons() {
        // Botón "💬 Escríbenos ya": SmoothScroll al formulario y focus en nombre
        binding.btnHeroContactoEscribir.setOnClickListener(v -> {
            binding.scrollContacto.smoothScrollTo(0, binding.cardFormularioContacto.getTop());
            binding.etContactoNombre.requestFocus();
        });

        // Botón "🧪 Ver talleres": Navega a la pestaña de Talleres
        binding.btnHeroContactoVerTalleres.setOnClickListener(v -> navegarATalleres());
    }

    private void setupQuickAccessCards() {
        // Tarjeta WhatsApp directo
        binding.cardQuickWhatsapp.setOnClickListener(v -> 
            WhatsAppHelper.abrirSoporteDirecto(requireContext(), 
                "¡Hola Mini Científicos! 💬 Quisiera hacer una consulta rápida por WhatsApp.")
        );

        // Tarjeta Síguenos - Botones Sociales
        binding.btnSocialFacebook.setOnClickListener(v -> 
            abrirUrl("https://facebook.com/minicientificosoficial")
        );
        binding.btnSocialInstagram.setOnClickListener(v -> 
            abrirUrl("https://instagram.com/minicientificosoficial")
        );
        binding.btnSocialTiktok.setOnClickListener(v -> 
            abrirUrl("https://tiktok.com/@minicientificos")
        );

        // Tarjeta Talleres
        binding.cardQuickTalleres.setOnClickListener(v -> navegarATalleres());
    }

    private void navegarATalleres() {
        if (getActivity() instanceof MainActivity) {
            BottomNavigationView nav =
                    getActivity().findViewById(R.id.bottom_navigation);
            if (nav != null) {
                nav.setSelectedItemId(R.id.nav_workshops);
                return;
            }
        }
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new WorkshopsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void abrirUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "No se pudo abrir la dirección: " + url, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFormSubmit() {
        binding.btnEnviarContactoWhatsapp.setOnClickListener(v -> validarYEnviarFormulario());
    }

    private void validarYEnviarFormulario() {
        String nombre = binding.etContactoNombre.getText() != null ? binding.etContactoNombre.getText().toString().trim() : "";
        String celular = binding.etContactoCelular.getText() != null ? binding.etContactoCelular.getText().toString().trim() : "";
        String correo = binding.etContactoCorreo.getText() != null ? binding.etContactoCorreo.getText().toString().trim() : "";
        String motivo = binding.actvContactoMotivo.getText() != null ? binding.actvContactoMotivo.getText().toString().trim() : "";
        String mensaje = binding.etContactoMensaje.getText() != null ? binding.etContactoMensaje.getText().toString().trim() : "";

        // Validaciones
        if (TextUtils.isEmpty(nombre)) {
            binding.etContactoNombre.setError("Por favor ingresa tu nombre completo");
            binding.etContactoNombre.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(celular) || celular.length() < 7) {
            binding.etContactoCelular.setError("Ingresa un número de celular válido (mínimo 7 dígitos)");
            binding.etContactoCelular.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(correo) || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.etContactoCorreo.setError("Ingresa un correo electrónico válido (ejemplo@dominio.com)");
            binding.etContactoCorreo.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(motivo)) {
            Toast.makeText(requireContext(), "Por favor selecciona un motivo de consulta", Toast.LENGTH_SHORT).show();
            binding.actvContactoMotivo.showDropDown();
            return;
        }

        if (TextUtils.isEmpty(mensaje)) {
            binding.etContactoMensaje.setError("Por favor escribe tu consulta o mensaje");
            binding.etContactoMensaje.requestFocus();
            return;
        }

        // Si todo es válido -> Abrir WhatsApp con el mensaje estructurado
        WhatsAppHelper.abrirConsultaContacto(requireContext(), nombre, correo, celular, motivo, mensaje);

        // Limpiar campos
        binding.etContactoNombre.setText("");
        binding.etContactoCelular.setText("");
        binding.etContactoCorreo.setText("");
        binding.actvContactoMotivo.setText("");
        binding.etContactoMensaje.setText("");

        binding.getRoot().clearFocus();

        Toast.makeText(requireContext(),
                "🚀 ¡Abriendo WhatsApp para enviar tu mensaje!",
                Toast.LENGTH_LONG).show();
    }

    private void setupCorporateFooter() {
        if (binding.layoutCorporateFooter != null) {
            CorporateFooterHelper.setupFooter(requireContext(), binding.layoutCorporateFooter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}