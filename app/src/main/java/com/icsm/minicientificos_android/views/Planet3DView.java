package com.icsm.minicientificos_android.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

public class Planet3DView extends View {

    public interface OnPlanetChangeListener {
        void onAngleChanged(float longitudeDeg);
    }

    private Paint oceanPaint;
    private Paint landPaint;
    private Paint shadowPaint;
    private Paint atmospherePaint;
    private Paint pinPaint;
    private Paint textPaint;

    private Path sphereClipPath;
    private Path continentPath;

    private float longitudeDeg = 0f; // 0 a 360 grados
    private float latitudeTiltDeg = 10f; // -30 a +30 grados

    private float lastTouchX = 0f;
    private float lastTouchY = 0f;

    private OnPlanetChangeListener listener;
    private ValueAnimator autoRotationAnimator;

    public Planet3DView(Context context) {
        super(context);
        init();
    }

    public Planet3DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Planet3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        oceanPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        landPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        landPaint.setColor(Color.parseColor("#4CAF50"));
        landPaint.setStyle(Paint.Style.FILL);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmospherePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        pinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pinPaint.setColor(Color.parseColor("#FDC500"));
        pinPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#0B2C5E"));
        textPaint.setTextSize(32f);
        textPaint.setFakeBoldText(true);

        sphereClipPath = new Path();
        continentPath = new Path();
    }

    public void setOnPlanetChangeListener(OnPlanetChangeListener listener) {
        this.listener = listener;
    }

    public float getLongitudeDeg() {
        return longitudeDeg;
    }

    public void rotateToLongitude(float targetDeg) {
        if (autoRotationAnimator != null && autoRotationAnimator.isRunning()) {
            autoRotationAnimator.cancel();
        }

        // Calcula el camino angular más corto
        float delta = (targetDeg - longitudeDeg) % 360f;
        if (delta > 180f) delta -= 360f;
        if (delta < -180f) delta += 360f;

        float start = longitudeDeg;
        float end = start + delta;

        autoRotationAnimator = ValueAnimator.ofFloat(start, end);
        autoRotationAnimator.setDuration(700);
        autoRotationAnimator.setInterpolator(new DecelerateInterpolator());
        autoRotationAnimator.addUpdateListener(animator -> {
            longitudeDeg = (float) animator.getAnimatedValue();
            invalidate();
            if (listener != null) {
                listener.onAngleChanged(longitudeDeg);
            }
        });
        autoRotationAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) return;

        float cx = width / 2f;
        float cy = height / 2f;
        float radius = Math.min(width, height) / 2f - 20f;

        // 1. Iluminación Esférica 3D del Océano
        RadialGradient oceanGradient = new RadialGradient(
                cx - radius * 0.3f, cy - radius * 0.3f, radius * 1.3f,
                new int[]{Color.parseColor("#33C2FF"), Color.parseColor("#0086BA"), Color.parseColor("#004A75")},
                new float[]{0f, 0.7f, 1f}, Shader.TileMode.CLAMP
        );
        oceanPaint.setShader(oceanGradient);

        // Clip circular de la esfera
        sphereClipPath.reset();
        sphereClipPath.addCircle(cx, cy, radius, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(sphereClipPath);

        // Dibujar Océano Esférico Base
        canvas.drawCircle(cx, cy, radius, oceanPaint);

        // 2. Dibujar Continentes con Proyección Esférica 3D
        drawSphericalContinents(canvas, cx, cy, radius);

        // 3. Sombra 3D en la curvatura de la Tierra (Efecto Noche / Profundidad)
        RadialGradient shadowGradient = new RadialGradient(
                cx + radius * 0.4f, cy + radius * 0.4f, radius * 1.1f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#AA0B2C5E")},
                new float[]{0.5f, 1f}, Shader.TileMode.CLAMP
        );
        shadowPaint.setShader(shadowGradient);
        canvas.drawCircle(cx, cy, radius, shadowPaint);

        canvas.restore();

        // 4. Brillo de Atmósfera Exterior
        RadialGradient atmosGradient = new RadialGradient(
                cx, cy, radius + 15f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#66B8E8FF"), Color.TRANSPARENT},
                new float[]{0.85f, 0.95f, 1f}, Shader.TileMode.CLAMP
        );
        atmospherePaint.setShader(atmosGradient);
        canvas.drawCircle(cx, cy, radius + 15f, atmospherePaint);
    }

    private void drawSphericalContinents(Canvas canvas, float cx, float cy, float radius) {
        // Renderiza Continentes con envolvente de longitud esférica (0° a 360°)
        // Cada continente se proyecta usando cos(lat) y sin(lon + offset)

        // Definición de parches de tierra (longitud base, latitud base, tamaño horizontal, tamaño vertical)
        float[][] landmasses = {
                {0f, 20f, 60f, 40f},     // América
                {80f, 30f, 50f, 45f},    // Europa / África
                {150f, 10f, 70f, 50f},   // Asia
                {220f, -20f, 45f, 35f},  // Oceanía
                {290f, -40f, 55f, 30f}   // Antártida / Sur
        };

        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (float[] land : landmasses) {
            float baseLon = land[0];
            float baseLat = land[1];
            float lonWidth = land[2];
            float latHeight = land[3];

            // Dibuja sub-puntos del continente proyectados esféricamente
            for (float dLon = -lonWidth / 2f; dLon <= lonWidth / 2f; dLon += 10f) {
                float currentLon = (baseLon + dLon + longitudeDeg) % 360f;
                if (currentLon < 0) currentLon += 360f;

                // Longitud visible en la cara frontal de la esfera 3D: [270°, 360°) y [0°, 90°)
                double radLon = Math.toRadians(currentLon);
                double cosLon = Math.cos(radLon);

                if (cosLon > 0) { // Está en la cara frontal visible
                    double sinLon = Math.sin(radLon);

                    for (float dLat = -latHeight / 2f; dLat <= latHeight / 2f; dLat += 10f) {
                        float currentLat = baseLat + dLat;
                        double radLat = Math.toRadians(currentLat);

                        // Coordenadas de Proyección Esférica Ortográfica 3D
                        float px = (float) (cx + radius * sinLon * Math.cos(radLat));
                        float py = (float) (cy - radius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                        // Tamaño de escala según la profundidad z
                        float spotRadius = (float) (radius * 0.12f * cosLon * Math.cos(radLat));
                        if (spotRadius > 2f) {
                            canvas.drawCircle(px, py, spotRadius, landPaint);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
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

                // Rotación esférica de longitud 3D (360° continuos sin aplanarse jamás)
                longitudeDeg = (longitudeDeg + deltaX * 0.5f) % 360f;
                if (longitudeDeg < 0) longitudeDeg += 360f;

                // Inclinación de latitud 3D [-30°, 30°]
                latitudeTiltDeg = Math.max(-30f, Math.min(30f, latitudeTiltDeg - deltaY * 0.3f));

                lastTouchX = x;
                lastTouchY = y;

                invalidate();
                if (listener != null) {
                    listener.onAngleChanged(longitudeDeg);
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
}