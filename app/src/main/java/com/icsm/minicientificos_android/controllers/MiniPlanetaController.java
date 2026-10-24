package com.icsm.minicientificos_android.controllers;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.ChipGroup;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.ui.PlanetaExperimentoBottomSheet;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MiniPlanetaController {

    private final View container;
    private final ImageView ivPlaneta;
    private final TextView tvPin;
    private final ChipGroup chipGroup;
    private final FragmentManager fragmentManager;

    private float currentAngle = 0f;
    private float lastTouchAngle = 0f;

    public MiniPlanetaController(View container, ImageView ivPlaneta, TextView tvPin,
                                ChipGroup chipGroup, FragmentManager fragmentManager) {
        this.container = container;
        this.ivPlaneta = ivPlaneta;
        this.tvPin = tvPin;
        this.chipGroup = chipGroup;
        this.fragmentManager = fragmentManager;
    }

    public void setup() {
        setupTouchGesture();
        setupChips();
    }

    private void setupTouchGesture() {
        if (container == null || ivPlaneta == null) return;

        container.setOnTouchListener((v, event) -> {
            // REQUISITO TÉCNICO: Evita que el NestedScrollView padre robe el gesto táctil del planeta
            if (v.getParent() != null) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }

            float x = event.getX();
            float y = event.getY();
            float centerX = v.getWidth() / 2f;
            float centerY = v.getHeight() / 2f;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchAngle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float currentTouchAngle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
                    float deltaAngle = currentTouchAngle - lastTouchAngle;
                    
                    currentAngle += deltaAngle;
                    ivPlaneta.setRotation(currentAngle);
                    
                    lastTouchAngle = currentTouchAngle;
                    actualizarPinSegunAngulo(currentAngle);
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (v.getParent() != null) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return true;
            }
            return false;
        });
    }

    private void setupChips() {
        if (chipGroup == null) return;

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int chipId = checkedIds.get(0);
            if (chipId == R.id.chip_eco_huerto) {
                rotarYMostrar("🌱 Mini Huerto", "Concepto de germinación y compost orgánico en casa.", 
                        "1. Junta cáscaras de vegetales.\n2. Mezcla con tierra viva.\n3. Observa el abono en 10 días.", 
                        R.drawable.compostaje, 0f);
            } else if (chipId == R.id.chip_eco_energia) {
                rotarYMostrar("⚡ Energías Renovables", "Transformación del sol y viento en luz limpia.", 
                        "1. Conecta un mini panel solar a un LED.\n2. Colócalo al sol.\n3. Encenderá sin baterías.", 
                        R.drawable.energias_reno, 51f);
            } else if (chipId == R.id.chip_eco_reciclaje) {
                rotarYMostrar("♻️ Reciclaje Creativo", "Economía circular y transformación de plásticos.", 
                        "1. Reutiliza tapas plásticas para hacer engranajes.\n2. Pinta y arma un robot.", 
                        R.drawable.reciclaje, 102f);
            } else if (chipId == R.id.chip_eco_geologia) {
                rotarYMostrar("🌋 Geología & Volcanes", "Rocas volcánicas y erupción maquetada segura.", 
                        "1. Mezcla vinagre con bicarbonato en la maqueta.\n2. Añade tinte rojo.", 
                        R.drawable.geologia, 154f);
            } else if (chipId == R.id.chip_eco_hidroponia) {
                rotarYMostrar("💧 Hidroponía", "Cultivos verticales de vegetales sumergidos en agua.", 
                        "1. Coloca brotes en agua con sales de nutrientes.\n2. Mantenlo en luz indirecta.", 
                        R.drawable.hidroponia, 205f);
            } else if (chipId == R.id.chip_eco_astronomia) {
                rotarYMostrar("🚀 Astronomía", "Órbitas planetarias y cohetes de propulsión.", 
                        "1. Llena 1/3 de botella con agua.\n2. Bombea aire y lanza el cohete.", 
                        R.drawable.astronomia, 257f);
            } else if (chipId == R.id.chip_eco_fisica) {
                rotarYMostrar("🔬 Física Loca", "Leyes de la fuerza, gravedad y fluidos no newtonianos.", 
                        "1. Mezcla fécula de maíz con agua.\n2. Golpea rápido (sólido) o lento (líquido).", 
                        R.drawable.expe_fisi, 308f);
            }
        });
    }

    private void rotarYMostrar(String titulo, String concepto, String experimento, int imgResId, float targetAngle) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivPlaneta, "rotation", currentAngle, targetAngle);
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

        currentAngle = targetAngle;
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

    private void actualizarPinSegunAngulo(float angle) {
        if (tvPin == null) return;
        float norm = (angle % 360 + 360) % 360;

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