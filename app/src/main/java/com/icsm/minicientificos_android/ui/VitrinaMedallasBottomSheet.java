package com.icsm.minicientificos_android.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.BottomSheetVitrinaMedallasBinding;
import com.icsm.minicientificos_android.databinding.ItemMedallaVitrinaBinding;
import com.icsm.minicientificos_android.models.game.BancoPreguntasCatalog;
import com.icsm.minicientificos_android.models.game.MedallaCientifica;
import com.icsm.minicientificos_android.models.game.NivelJuego;
import com.icsm.minicientificos_android.utils.GameProgressManager;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Vitrina de Medallas, Rango Científico y Cupones Ganados del Videojuego.
 * Conectada directamente con GameProgressManager (Integrante 1) y
 * GameSoundAndVfxHelper (Integrante 2).
 */
public class VitrinaMedallasBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetVitrinaMedallasBinding binding;

    public VitrinaMedallasBottomSheet() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetVitrinaMedallasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarPerfilYMedallas();
        setupListeners();
    }

    private void cargarPerfilYMedallas() {
        if (getContext() == null || binding == null) return;

        GameProgressManager progressManager = GameProgressManager.getInstance(requireContext());
        String nombreAgente = progressManager.getNombreAgenteGuardado();
        String rangoActual = progressManager.getRangoCientificoActual();
        int xpTotal = progressManager.getXpTotal();
        int recordPuntos = progressManager.getRecordPuntaje();

        // Avatares de Titan y Tesla resueltos dinámicamente
        binding.ivVitrinaTitan.setImageResource(
                GameSoundAndVfxHelper.obtenerDrawablePersonajeEstado(requireContext(), "TITAN", "CELEBRANDO")
        );
        binding.ivVitrinaTesla.setImageResource(
                GameSoundAndVfxHelper.obtenerDrawablePersonajeEstado(requireContext(), "TESLA", "FELIZ")
        );

        binding.tvVitrinaAgenteNombre.setText("👤 Agente: " + nombreAgente);
        binding.tvVitrinaRangoActual.setText("🎖️ " + rangoActual);
        binding.tvVitrinaXpTotal.setText("⚡ " + xpTotal + " XP");
        binding.tvVitrinaRecordPuntos.setText("🌟 " + recordPuntos + " pts");

        // Cálculo exacto hacia el siguiente rango (umbrales: 300, 800, 1500)
        int progresoPct;
        String textoProximo;
        String textoMeta;
        if (xpTotal < 300) {
            progresoPct = (int) ((xpTotal * 100f) / 300f);
            textoProximo = "Próximo: Explorador de Laboratorio";
            textoMeta = xpTotal + " / 300 XP";
        } else if (xpTotal < 800) {
            progresoPct = (int) (((xpTotal - 300) * 100f) / 500f);
            textoProximo = "Próximo: Científico Estelar";
            textoMeta = xpTotal + " / 800 XP";
        } else if (xpTotal < 1500) {
            progresoPct = (int) (((xpTotal - 800) * 100f) / 700f);
            textoProximo = "Próximo: Maestro Inventor ICSM";
            textoMeta = xpTotal + " / 1500 XP";
        } else {
            progresoPct = 100;
            textoProximo = "¡Rango Máximo Alcanzado! 👑";
            textoMeta = xpTotal + " XP ★";
        }

        binding.tvVitrinaProgresoLabel.setText(textoProximo);
        binding.tvVitrinaProgresoMeta.setText(textoMeta);
        binding.pbVitrinaSiguienteRango.setProgress(Math.max(0, Math.min(100, progresoPct)));

        // Cargar las 6 Medallas Coleccionables desde GameProgressManager
        List<MedallaCientifica> medallas = progressManager.getMedallasConEstadoActual();
        int desbloqueadas = 0;
        for (MedallaCientifica m : medallas) {
            if (m.isDesbloqueada()) {
                desbloqueadas++;
            }
        }
        binding.tvVitrinaConteoMedallas.setText(desbloqueadas + " / " + medallas.size() + " desbloqueadas");

        binding.rvVitrinaMedallas.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvVitrinaMedallas.setAdapter(new MedallasVitrinaAdapter(medallas));

        actualizarBotonSonido();
    }

    private void setupListeners() {
        binding.btnCloseVitrina.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            dismiss();
        });

        binding.btnToggleSonidoVitrina.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().toggleSonido();
            actualizarBotonSonido();
        });

        binding.btnVitrinaGenerarCredencial.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playComodinActivado();
            GeneradorCredencialBottomSheet credencialDialog = new GeneradorCredencialBottomSheet();
            credencialDialog.show(getParentFragmentManager(), "GeneradorCredencialBottomSheet");
            dismiss();
        });

        binding.btnVitrinaVerCupones.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            mostrarDialogoCuponesGanados();
        });
    }

    private void actualizarBotonSonido() {
        if (binding == null) return;
        boolean activo = GameSoundAndVfxHelper.getInstance().isSonidoActivo();
        binding.btnToggleSonidoVitrina.setText(activo ? "🔊 ON" : "🔇 OFF");
    }

    private void mostrarDialogoCuponesGanados() {
        if (getContext() == null) return;

        GameProgressManager pm = GameProgressManager.getInstance(requireContext());
        List<NivelJuego> niveles = BancoPreguntasCatalog.obtenerNiveles();
        List<String> cuponesDesbloqueados = new ArrayList<>();

        StringBuilder resumen = new StringBuilder();
        String cuponActivo = CartManager.getInstance().getCodigoCuponActivo();
        if (cuponActivo != null && !cuponActivo.isEmpty()) {
            resumen.append("✅ Cupón activo en tu carrito: ").append(cuponActivo).append("\n\n");
        } else {
            resumen.append("🏷️ Aún no tienes un cupón activo en tu carrito.\n\n");
        }

        resumen.append("🎮 Recompensas por Mundos del Videojuego:\n");
        for (NivelJuego nivel : niveles) {
            int estrellas = pm.getEstrellasNivel(nivel.getId());
            String codigo = nivel.getCodigoCuponPremio();
            boolean ganado = estrellas >= 2 || pm.getXpTotal() >= nivel.getXpRequeridoParaDesbloquear() + 300;
            if (ganado && codigo != null && !cuponesDesbloqueados.contains(codigo)) {
                cuponesDesbloqueados.add(codigo);
            }
            resumen.append("• ")
                    .append(nivel.getIconoEmoji()).append(" ")
                    .append(nivel.getNumeroMundo()).append(" → ")
                    .append(codigo)
                    .append(ganado ? " (🔓 DESBLOQUEADO)\n" : " (🔒 Completa con 2+ ★)\n");
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("🛒 Cupones Ganados para la Tienda")
                .setMessage(resumen.toString())
                .setNegativeButton("Cerrar", (d, w) -> d.dismiss());

        if (!cuponesDesbloqueados.isEmpty()) {
            String mejorCupon = cuponesDesbloqueados.get(cuponesDesbloqueados.size() - 1);
            builder.setPositiveButton("Aplicar " + mejorCupon + " y Ver Carrito", (d, w) -> {
                CartManager.getInstance().aplicarCupon(mejorCupon);
                GameSoundAndVfxHelper.getInstance().playVictoriaNivel(requireContext());
                Toast.makeText(requireContext(), "🎉 ¡Cupón " + mejorCupon + " aplicado en tu carrito!", Toast.LENGTH_LONG).show();
                CartBottomSheetDialog cartSheet = new CartBottomSheetDialog();
                cartSheet.show(getParentFragmentManager(), "CartBottomSheet");
                dismiss();
            });
        } else {
            builder.setPositiveButton("Ir al Carrito", (d, w) -> {
                CartBottomSheetDialog cartSheet = new CartBottomSheetDialog();
                cartSheet.show(getParentFragmentManager(), "CartBottomSheet");
                dismiss();
            });
        }

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // =========================================================================
    // ADAPTER INTERNO PARA LAS 6 MEDALLAS COLECCIONABLES
    // =========================================================================
    private class MedallasVitrinaAdapter extends RecyclerView.Adapter<MedallasVitrinaAdapter.MedallaViewHolder> {

        private final List<MedallaCientifica> lista;

        MedallasVitrinaAdapter(List<MedallaCientifica> lista) {
            this.lista = lista != null ? lista : new ArrayList<>();
        }

        @NonNull
        @Override
        public MedallaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMedallaVitrinaBinding itemBinding = ItemMedallaVitrinaBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new MedallaViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull MedallaViewHolder holder, int position) {
            holder.bind(lista.get(position));
        }

        @Override
        public int getItemCount() {
            return lista.size();
        }

        class MedallaViewHolder extends RecyclerView.ViewHolder {
            private final ItemMedallaVitrinaBinding itemBinding;

            MedallaViewHolder(ItemMedallaVitrinaBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            void bind(MedallaCientifica medalla) {
                itemBinding.tvMedallaEmoji.setText(medalla.getIconoEmoji());
                itemBinding.tvMedallaTitulo.setText(medalla.getTitulo());
                itemBinding.tvMedallaDesc.setText(medalla.getDescripcion());

                int colorAcento = Color.parseColor("#00A8E8");
                try {
                    if (medalla.getColorHex() != null && !medalla.getColorHex().isEmpty()) {
                        colorAcento = Color.parseColor(medalla.getColorHex());
                    }
                } catch (IllegalArgumentException ignored) {
                }

                if (medalla.isDesbloqueada()) {
                    itemBinding.cardMedallaItem.setAlpha(1.0f);
                    itemBinding.cardMedallaItem.setCardBackgroundColor(Color.WHITE);
                    itemBinding.cardMedallaItem.setStrokeColor(colorAcento);
                    itemBinding.cardMedallaItem.setStrokeWidth(5);

                    itemBinding.tvMedallaEmoji.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FEF9C3")));
                    itemBinding.tvMedallaEstadoBadge.setText("✓ DESBLOQUEADA");
                    itemBinding.tvMedallaEstadoBadge.setTextColor(Color.parseColor("#15803D"));
                    itemBinding.tvMedallaEstadoBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCFCE7")));
                } else {
                    itemBinding.cardMedallaItem.setAlpha(0.72f);
                    itemBinding.cardMedallaItem.setCardBackgroundColor(Color.parseColor("#F1F5F9"));
                    itemBinding.cardMedallaItem.setStrokeColor(Color.parseColor("#CBD5E1"));
                    itemBinding.cardMedallaItem.setStrokeWidth(3);

                    itemBinding.tvMedallaEmoji.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
                    itemBinding.tvMedallaEstadoBadge.setText("🔒 Requiere " + medalla.getXpRequerido() + " XP");
                    itemBinding.tvMedallaEstadoBadge.setTextColor(Color.parseColor("#475569"));
                    itemBinding.tvMedallaEstadoBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
                }

                itemBinding.cardMedallaItem.setOnClickListener(v -> {
                    if (getContext() == null) return;
                    if (medalla.isDesbloqueada()) {
                        GameSoundAndVfxHelper.getInstance().playRespuestaCorrecta(requireContext());
                        GameSoundAndVfxHelper.animarPulsoExito(itemBinding.cardMedallaItem);
                        Toast.makeText(requireContext(), "🏅 ¡Medalla " + medalla.getTitulo() + " desbloqueada!", Toast.LENGTH_SHORT).show();
                    } else {
                        GameSoundAndVfxHelper.getInstance().playRespuestaIncorrecta(requireContext());
                        GameSoundAndVfxHelper.animarSacudidaError(itemBinding.cardMedallaItem);
                        Toast.makeText(requireContext(), "🔒 Sigue jugando para alcanzar " + medalla.getXpRequerido() + " XP", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
