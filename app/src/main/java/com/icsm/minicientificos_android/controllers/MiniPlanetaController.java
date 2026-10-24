package com.icsm.minicientificos_android.controllers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.ChipGroup;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.ui.PlanetaExperimentoBottomSheet;
import com.icsm.minicientificos_android.views.Planet3DView;

public class MiniPlanetaController {

    private final View container;
    private final View planetView;
    private final TextView tvPin;
    private final ChipGroup chipGroup;
    private final FragmentManager fragmentManager;

    private float currentRotationY = 0f;

    public MiniPlanetaController(View container, View planetView, TextView tvPin,
                                ChipGroup chipGroup, FragmentManager fragmentManager) {
        this.container = container;
        this.planetView = planetView;
        this.tvPin = tvPin;
        this.chipGroup = chipGroup;
        this.fragmentManager = fragmentManager;
    }

    public void setup() {
        if (planetView instanceof Planet3DView) {
            Planet3DView pView = (Planet3DView) planetView;
            pView.setOnPlanetChangeListener(new Planet3DView.OnPlanetChangeListener() {
                @Override
                public void onAngleChanged(float longitudeDeg) {
                    actualizarPinSegunAngulo3D(longitudeDeg);
                }

                @Override
                public void onHotspotTapped(com.icsm.minicientificos_android.models.PlanetaHotspotItem item) {
                    if (fragmentManager != null) {
                        PlanetaExperimentoBottomSheet bottomSheet = PlanetaExperimentoBottomSheet.newInstance(
                                item.getNombreExperimento(),
                                item.getExplicacionParaNinos(),
                                item.getRetoRapidoPregunta(),
                                item.getDrawableResId()
                        );
                        bottomSheet.show(fragmentManager, "PlanetaExperimentoBottomSheet");
                    }
                }
            });
        }
        setupChips();
    }

    private void setupChips() {
        if (chipGroup == null) return;

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int chipId = checkedIds.get(0);
            if (chipId == R.id.chip_eco_huerto) {
                rotar3DYMostrar("🌱 Mini Huerto", "Concepto de germinación y compost orgánico en casa.", 
                        "1. Junta cáscaras de vegetales.\n2. Mezcla con tierra viva.\n3. Observa el abono en 10 días.", 
                        R.drawable.compostaje, 0f);
            } else if (chipId == R.id.chip_eco_energia) {
                rotar3DYMostrar("⚡ Energías Renovables", "Transformación del sol y viento en luz limpia.", 
                        "1. Conecta un mini panel solar a un LED.\n2. Colócalo al sol.\n3. Encenderá sin baterías.", 
                        R.drawable.energias_reno, 51f);
            } else if (chipId == R.id.chip_eco_reciclaje) {
                rotar3DYMostrar("♻️ Reciclaje Creativo", "Economía circular y transformación de plásticos.", 
                        "1. Reutiliza tapas plásticas para hacer engranajes.\n2. Pinta y arma un robot.", 
                        R.drawable.reciclaje, 102f);
            } else if (chipId == R.id.chip_eco_geologia) {
                rotar3DYMostrar("🌋 Geología & Volcanes", "Rocas volcánicas y erupción maquetada segura.", 
                        "1. Mezcla vinagre con bicarbonato en la maqueta.\n2. Añade tinte rojo.", 
                        R.drawable.geologia, 154f);
            } else if (chipId == R.id.chip_eco_hidroponia) {
                rotar3DYMostrar("💧 Hidroponía", "Cultivos verticales de vegetales sumergidos en agua.", 
                        "1. Coloca brotes en agua con sales de nutrientes.\n2. Mantenlo en luz indirecta.", 
                        R.drawable.hidroponia, 205f);
            } else if (chipId == R.id.chip_eco_astronomia) {
                rotar3DYMostrar("🚀 Astronomía", "Órbitas planetarias y cohetes de propulsión.", 
                        "1. Llena 1/3 de botella con agua.\n2. Bombea aire y lanza el cohete.", 
                        R.drawable.astronomia, 257f);
            } else if (chipId == R.id.chip_eco_fisica) {
                rotar3DYMostrar("🔬 Física Loca", "Leyes de la fuerza, gravedad y fluidos no newtonianos.", 
                        "1. Mezcla fécula de maíz con agua.\n2. Golpea rápido (sólido) o lento (líquido).", 
                        R.drawable.expe_fisi, 308f);
            }
        });
    }

    private void rotar3DYMostrar(String titulo, String concepto, String experimento, int imgResId, float targetY) {
        if (planetView instanceof Planet3DView) {
            ((Planet3DView) planetView).rotateToLongitude(targetY);
        } else if (planetView != null) {
            ObjectAnimator animY = ObjectAnimator.ofFloat(planetView, "rotationY", currentRotationY, targetY);
            animY.setDuration(700);
            animY.setInterpolator(new OvershootInterpolator(0.8f));
            animY.start();
        }

        currentRotationY = targetY;

        if (tvPin != null) {
            tvPin.setText("📍 " + titulo);
        }

        if (fragmentManager != null) {
            PlanetaExperimentoBottomSheet bottomSheet = PlanetaExperimentoBottomSheet.newInstance(
                    titulo, concepto, experimento, imgResId
            );
            bottomSheet.show(fragmentManager, "PlanetaExperimentoBottomSheet");
        }
    }

    private void actualizarPinSegunAngulo3D(float angleY) {
        if (tvPin == null) return;
        float norm = (angleY % 360 + 360) % 360;

        if (norm >= 0 && norm < 45) {
            tvPin.setText("📍 🌱 Mini Huerto");
        } else if (norm >= 45 && norm < 90) {
            tvPin.setText("📍 ⚡ Energías Renovables");
        } else if (norm >= 90 && norm < 135) {
            tvPin.setText("📍 ♻️ Reciclaje Creativo");
        } else if (norm >= 135 && norm < 180) {
            tvPin.setText("📍 🌋 Geología & Volcanes");
        } else if (norm >= 180 && norm < 225) {
            tvPin.setText("📍 💧 Hidroponía");
        } else if (norm >= 225 && norm < 270) {
            tvPin.setText("📍 🚀 Astronomía");
        } else {
            tvPin.setText("📍 🔬 Física Loca");
        }
    }
}