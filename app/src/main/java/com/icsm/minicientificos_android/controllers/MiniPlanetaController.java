package com.icsm.minicientificos_android.controllers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.ChipGroup;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.ui.PlanetaExperimentoBottomSheet;

public class MiniPlanetaController {

    private final View container;
    private final ImageView ivPlaneta;
    private final TextView tvPin;
    private final ChipGroup chipGroup;
    private final FragmentManager fragmentManager;

    // Ángulos 3D (Eje Y = Rotación horizontal, Eje X = Inclinación vertical)
    private float currentRotationY = 0f;
    private float currentRotationX = 0f;

    private float lastTouchX = 0f;
    private float lastTouchY = 0f;

    public MiniPlanetaController(View container, ImageView ivPlaneta, TextView tvPin,
                                ChipGroup chipGroup, FragmentManager fragmentManager) {
        this.container = container;
        this.ivPlaneta = ivPlaneta;
        this.tvPin = tvPin;
        this.chipGroup = chipGroup;
        this.fragmentManager = fragmentManager;
    }

    public void setup() {
        setup3DPerspective();
        setupTouchGesture();
        setupChips();
    }

    private void setup3DPerspective() {
        if (ivPlaneta == null || container == null) return;
        // Ajusta la distancia de cámara para dar perspectiva 3D realista sin deformación
        float distance = 10000 * container.getResources().getDisplayMetrics().density;
        ivPlaneta.setCameraDistance(distance);
    }

    private void setupTouchGesture() {
        if (container == null || ivPlaneta == null) return;

        container.setOnTouchListener((v, event) -> {
            // Evita que el ScrollView padre intercepte el gesto táctil 3D
            if (v.getParent() != null) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = x;
                    lastTouchY = y;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - lastTouchX;
                    float deltaY = y - lastTouchY;

                    // Giro 3D Horizontal (Eje Y)
                    currentRotationY += deltaX * 0.6f;
                    
                    // Inclinación 3D Vertical (Eje X) clamp a [-45°, 45°]
                    currentRotationX -= deltaY * 0.4f;
                    currentRotationX = Math.max(-45f, Math.min(45f, currentRotationX));

                    ivPlaneta.setRotationY(currentRotationY);
                    ivPlaneta.setRotationX(currentRotationX);

                    lastTouchX = x;
                    lastTouchY = y;

                    actualizarPinSegunAngulo3D(currentRotationY);
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
                rotar3DYMostrar("🌱 Mini Huerto", "Concepto de germinación y compost orgánico en casa.", 
                        "1. Junta cáscaras de vegetales.\n2. Mezcla con tierra viva.\n3. Observa el abono en 10 días.", 
                        R.drawable.compostaje, 0f, 10f);
            } else if (chipId == R.id.chip_eco_energia) {
                rotar3DYMostrar("⚡ Energías Renovables", "Transformación del sol y viento en luz limpia.", 
                        "1. Conecta un mini panel solar a un LED.\n2. Colócalo al sol.\n3. Encenderá sin baterías.", 
                        R.drawable.energias_reno, 51f, -15f);
            } else if (chipId == R.id.chip_eco_reciclaje) {
                rotar3DYMostrar("♻️ Reciclaje Creativo", "Economía circular y transformación de plásticos.", 
                        "1. Reutiliza tapas plásticas para hacer engranajes.\n2. Pinta y arma un robot.", 
                        R.drawable.reciclaje, 102f, 12f);
            } else if (chipId == R.id.chip_eco_geologia) {
                rotar3DYMostrar("🌋 Geología & Volcanes", "Rocas volcánicas y erupción maquetada segura.", 
                        "1. Mezcla vinagre con bicarbonato en la maqueta.\n2. Añade tinte rojo.", 
                        R.drawable.geologia, 154f, -20f);
            } else if (chipId == R.id.chip_eco_hidroponia) {
                rotar3DYMostrar("💧 Hidroponía", "Cultivos verticales de vegetales sumergidos en agua.", 
                        "1. Coloca brotes en agua con sales de nutrientes.\n2. Mantenlo en luz indirecta.", 
                        R.drawable.hidroponia, 205f, 15f);
            } else if (chipId == R.id.chip_eco_astronomia) {
                rotar3DYMostrar("🚀 Astronomía", "Órbitas planetarias y cohetes de propulsión.", 
                        "1. Llena 1/3 de botella con agua.\n2. Bombea aire y lanza el cohete.", 
                        R.drawable.astronomia, 257f, -10f);
            } else if (chipId == R.id.chip_eco_fisica) {
                rotar3DYMostrar("🔬 Física Loca", "Leyes de la fuerza, gravedad y fluidos no newtonianos.", 
                        "1. Mezcla fécula de maíz con agua.\n2. Golpea rápido (sólido) o lento (líquido).", 
                        R.drawable.expe_fisi, 308f, 18f);
            }
        });
    }

    private void rotar3DYMostrar(String titulo, String concepto, String experimento, int imgResId, float targetY, float targetX) {
        ObjectAnimator animY = ObjectAnimator.ofFloat(ivPlaneta, "rotationY", currentRotationY, targetY);
        ObjectAnimator animX = ObjectAnimator.ofFloat(ivPlaneta, "rotationX", currentRotationX, targetX);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animY, animX);
        set.setDuration(700);
        set.setInterpolator(new OvershootInterpolator(0.8f));
        set.start();

        currentRotationY = targetY;
        currentRotationX = targetX;

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