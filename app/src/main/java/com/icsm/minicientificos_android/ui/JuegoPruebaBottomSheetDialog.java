package com.icsm.minicientificos_android.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.BottomSheetJuegoPruebaBinding;
import com.icsm.minicientificos_android.models.game.BancoPreguntasCatalog;
import com.icsm.minicientificos_android.models.game.MedallaCientifica;
import com.icsm.minicientificos_android.models.game.PreguntaCientifica;
import com.icsm.minicientificos_android.utils.GameProgressManager;

import java.util.List;

public class JuegoPruebaBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetJuegoPruebaBinding binding;
    private List<PreguntaCientifica> preguntas;
    private int indicePreguntaActual = 0;
    private PreguntaCientifica preguntaActual;

    public JuegoPruebaBottomSheetDialog() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetJuegoPruebaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preguntas = BancoPreguntasCatalog.obtenerTodasLasPreguntas();

        setupListeners();
        actualizarPerfilAgente();
        cargarPreguntaActual();
        renderizarMedallas();
    }

    private void setupListeners() {
        binding.btnCloseJuegoPrueba.setOnClickListener(v -> dismiss());

        binding.btnSiguientePregunta.setOnClickListener(v -> {
            if (preguntas != null && !preguntas.isEmpty()) {
                indicePreguntaActual = (indicePreguntaActual + 1) % preguntas.size();
                cargarPreguntaActual();
            }
        });

        binding.btnOp0.setOnClickListener(v -> evaluarRespuesta(0));
        binding.btnOp1.setOnClickListener(v -> evaluarRespuesta(1));
        binding.btnOp2.setOnClickListener(v -> evaluarRespuesta(2));
        binding.btnOp3.setOnClickListener(v -> evaluarRespuesta(3));

        binding.btnSimularVictoriaCupon.setOnClickListener(v -> {
            CartManager.getInstance().aplicarCupon("MINI10");
            Toast.makeText(requireContext(), "🎉 ¡Cupón 'MINI10' (10% OFF) aplicado en tu Carrito!", Toast.LENGTH_LONG).show();
        });
    }

    private void actualizarPerfilAgente() {
        if (getContext() == null || binding == null) return;

        GameProgressManager progressManager = GameProgressManager.getInstance(requireContext());
        String nombre = progressManager.getNombreAgenteGuardado();
        int xp = progressManager.getXpTotal();
        String rango = progressManager.getRangoCientificoActual();

        binding.tvAgenteNombre.setText("👤 Agente: " + nombre);
        binding.tvAgenteXp.setText("⚡ " + xp + " XP");
        binding.tvAgenteRango.setText("🏆 Rango: " + rango);
    }

    private void cargarPreguntaActual() {
        if (binding == null || preguntas == null || preguntas.isEmpty()) return;

        preguntaActual = preguntas.get(indicePreguntaActual);

        binding.tvPreguntaMundo.setText("🧪 MUNDO " + preguntaActual.getNivelId() + " · " + preguntaActual.getCategoria());
        binding.tvPreguntaTexto.setText(preguntaActual.getPregunta());

        String[] ops = preguntaActual.getOpciones();
        if (ops.length >= 4) {
            binding.btnOp0.setText("A) " + ops[0]);
            binding.btnOp1.setText("B) " + ops[1]);
            binding.btnOp2.setText("C) " + ops[2]);
            binding.btnOp3.setText("D) " + ops[3]);
        }

        // Resetear estilo de botones
        resetearEstiloBotones();
        binding.boxResultadoExplicacion.setVisibility(View.GONE);
    }

    private void resetearEstiloBotones() {
        binding.btnOp0.setBackgroundColor(Color.parseColor("#F8FAFC"));
        binding.btnOp1.setBackgroundColor(Color.parseColor("#F8FAFC"));
        binding.btnOp2.setBackgroundColor(Color.parseColor("#F8FAFC"));
        binding.btnOp3.setBackgroundColor(Color.parseColor("#F8FAFC"));

        binding.btnOp0.setEnabled(true);
        binding.btnOp1.setEnabled(true);
        binding.btnOp2.setEnabled(true);
        binding.btnOp3.setEnabled(true);
    }

    private void evaluarRespuesta(int indiceSeleccionado) {
        if (preguntaActual == null || binding == null) return;

        boolean esCorrecta = (indiceSeleccionado == preguntaActual.getIndiceRespuestaCorrecta());

        binding.boxResultadoExplicacion.setVisibility(View.VISIBLE);

        if (esCorrecta) {
            GameProgressManager.getInstance(requireContext()).sumarXp(100);
            actualizarPerfilAgente();
            renderizarMedallas();

            binding.tvResultadoTexto.setText("✨ ¡CORRECTO! +100 XP GANADOS 🎉");
            binding.tvResultadoTexto.setTextColor(Color.parseColor("#16A34A"));
            binding.boxResultadoExplicacion.setBackgroundColor(Color.parseColor("#DCFCE7"));
        } else {
            binding.tvResultadoTexto.setText("❌ ¡UPS! ESA NO ES LA RESPUESTA CORRECTA");
            binding.tvResultadoTexto.setTextColor(Color.parseColor("#DC2626"));
            binding.boxResultadoExplicacion.setBackgroundColor(Color.parseColor("#FEE2E2"));
        }

        binding.tvPistaTesla.setText("⚡ Pista de Tesla: " + preguntaActual.getPistaTesla());
        binding.tvSabiasQue.setText("💡 Sabías que: " + preguntaActual.getExplicacionSabiasQue());
    }

    private void renderizarMedallas() {
        if (getContext() == null || binding == null) return;

        binding.llContenedorMedallas.removeAllViews();
        List<MedallaCientifica> medallas = GameProgressManager.getInstance(requireContext()).getMedallasConEstadoActual();

        for (MedallaCientifica med : medallas) {
            MaterialCardView card = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(0, 8, 0, 8);
            card.setLayoutParams(lp);
            card.setRadius(24);
            card.setStrokeWidth(2);

            if (med.isDesbloqueada()) {
                card.setCardBackgroundColor(Color.parseColor("#E0F2FE"));
                card.setStrokeColor(Color.parseColor("#00A8E8"));
            } else {
                card.setCardBackgroundColor(Color.parseColor("#334155"));
                card.setStrokeColor(Color.parseColor("#475569"));
            }

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(24, 20, 24, 20);

            TextView emoji = new TextView(requireContext());
            emoji.setText(med.getIconoEmoji());
            emoji.setTextSize(26);

            LinearLayout col = new LinearLayout(requireContext());
            col.setOrientation(LinearLayout.VERTICAL);
            col.setPadding(16, 0, 0, 0);

            TextView title = new TextView(requireContext());
            title.setText(med.getTitulo() + (med.isDesbloqueada() ? " 🔓" : " 🔒"));
            title.setTextSize(14);
            title.setTextColor(med.isDesbloqueada() ? Color.parseColor("#0B2C5E") : Color.WHITE);
            title.setTypeface(null, Typeface.BOLD);

            TextView desc = new TextView(requireContext());
            desc.setText(med.getDescripcion() + " (Requiere " + med.getXpRequerido() + " XP)");
            desc.setTextSize(12);
            desc.setTextColor(med.isDesbloqueada() ? Color.parseColor("#334155") : Color.parseColor("#94A3B8"));

            col.addView(title);
            col.addView(desc);

            row.addView(emoji);
            row.addView(col);

            card.addView(row);
            binding.llContenedorMedallas.addView(card);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}