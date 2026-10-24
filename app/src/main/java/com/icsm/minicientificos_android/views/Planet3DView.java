package com.icsm.minicientificos_android.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Paint forestPaint;
    private Paint junglePaint;
    private Paint desertPaint;
    private Paint icePaint;
    private Paint mountainPaint;
    private Paint volcanoPaint;
    private Paint cloudPaint;
    private Paint shadowPaint;
    private Paint atmospherePaint;
    private Paint facePaint;
    private Paint blushPaint;
    private Paint rocketPaint;

    private Path sphereClipPath;

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

        forestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        forestPaint.setColor(Color.parseColor("#4CAF50"));
        forestPaint.setStyle(Paint.Style.FILL);

        junglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        junglePaint.setColor(Color.parseColor("#2E7D32"));
        junglePaint.setStyle(Paint.Style.FILL);

        desertPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        desertPaint.setColor(Color.parseColor("#FBC02D"));
        desertPaint.setStyle(Paint.Style.FILL);

        icePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        icePaint.setColor(Color.parseColor("#FFFFFF"));
        icePaint.setStyle(Paint.Style.FILL);

        mountainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mountainPaint.setColor(Color.parseColor("#795548"));
        mountainPaint.setStyle(Paint.Style.FILL);

        volcanoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        volcanoPaint.setColor(Color.parseColor("#FF3D00"));
        volcanoPaint.setStyle(Paint.Style.FILL);

        cloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cloudPaint.setColor(Color.parseColor("#DDFFFFFF"));
        cloudPaint.setStyle(Paint.Style.FILL);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmospherePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        facePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        facePaint.setColor(Color.parseColor("#0B2C5E"));
        facePaint.setStrokeWidth(3f);
        facePaint.setStrokeCap(Paint.Cap.ROUND);
        facePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        blushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blushPaint.setColor(Color.parseColor("#FF80AB"));
        blushPaint.setStyle(Paint.Style.FILL);

        rocketPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        sphereClipPath = new Path();
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
        float radius = Math.min(width, height) / 2f - 24f;

        // 1. Iluminación Esférica 3D del Océano
        RadialGradient oceanGradient = new RadialGradient(
                cx - radius * 0.35f, cy - radius * 0.35f, radius * 1.35f,
                new int[]{Color.parseColor("#38D1FF"), Color.parseColor("#0096D6"), Color.parseColor("#003865")},
                new float[]{0f, 0.65f, 1f}, Shader.TileMode.CLAMP
        );
        oceanPaint.setShader(oceanGradient);

        // Clip circular de la esfera
        sphereClipPath.reset();
        sphereClipPath.addCircle(cx, cy, radius, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(sphereClipPath);

        // Océano Esférico Base
        canvas.drawCircle(cx, cy, radius, oceanPaint);

        // 2. Continentes Detallados con Biomas (Bosques, Desiertos, Montañas)
        drawDetailed3DContinents(canvas, cx, cy, radius);

        // 3. Casquetes Polares (Norte y Sur)
        drawPolarIceCaps(canvas, cx, cy, radius);

        // 4. Nubes Flotantes en Órbita Parallax
        drawOrbitingClouds(canvas, cx, cy, radius);

        // 5. Rostro Kawaii del Planeta
        drawPlanetFace(canvas, cx, cy, radius);

        // 6. Sombra 3D de Profundidad Nocturna
        RadialGradient shadowGradient = new RadialGradient(
                cx + radius * 0.45f, cy + radius * 0.45f, radius * 1.15f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#B30B2C5E")},
                new float[]{0.45f, 1f}, Shader.TileMode.CLAMP
        );
        shadowPaint.setShader(shadowGradient);
        canvas.drawCircle(cx, cy, radius, shadowPaint);

        canvas.restore();

        // 7. Resplandor de Atmósfera Exterior
        RadialGradient atmosGradient = new RadialGradient(
                cx, cy, radius + 18f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#80B8E8FF"), Color.TRANSPARENT},
                new float[]{0.82f, 0.94f, 1f}, Shader.TileMode.CLAMP
        );
        atmospherePaint.setShader(atmosGradient);
        canvas.drawCircle(cx, cy, radius + 18f, atmospherePaint);

        // 8. Cohete Espacial Orbitando
        drawOrbitingRocket(canvas, cx, cy, radius);
    }

    private void drawDetailed3DContinents(Canvas canvas, float cx, float cy, float radius) {
        // [baseLon, baseLat, lonSpan, latSpan, biomeType (0:Forest, 1:Jungle, 2:Desert, 3:Mountain, 4:Volcano)]
        float[][] landmasses = {
                // América del Norte y Sur
                {0f, 35f, 50f, 35f, 0},
                {-10f, 25f, 30f, 20f, 2},
                {10f, -15f, 45f, 45f, 1},
                {5f, -25f, 25f, 20f, 3},

                // Europa y África
                {75f, 45f, 40f, 30f, 0},
                {80f, 15f, 45f, 25f, 2},
                {85f, -10f, 50f, 40f, 1},

                // Asia y Volcanes
                {140f, 40f, 60f, 45f, 0},
                {150f, 28f, 35f, 20f, 3},
                {160f, 10f, 30f, 25f, 4},

                // Oceanía
                {225f, -25f, 45f, 35f, 2},
                {235f, -18f, 25f, 20f, 0},

                // Islas y Arrecifes
                {290f, 20f, 35f, 25f, 1},
                {320f, -30f, 30f, 20f, 0}
        };

        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (float[] land : landmasses) {
            float baseLon = land[0];
            float baseLat = land[1];
            float lonSpan = land[2];
            float latSpan = land[3];
            int biome = (int) land[4];

            Paint paintToUse;
            switch (biome) {
                case 1: paintToUse = junglePaint; break;
                case 2: paintToUse = desertPaint; break;
                case 3: paintToUse = mountainPaint; break;
                case 4: paintToUse = volcanoPaint; break;
                default: paintToUse = forestPaint; break;
            }

            for (float dLon = -lonSpan / 2f; dLon <= lonSpan / 2f; dLon += 8f) {
                float currentLon = (baseLon + dLon + longitudeDeg) % 360f;
                if (currentLon < 0) currentLon += 360f;

                double radLon = Math.toRadians(currentLon);
                double cosLon = Math.cos(radLon);

                if (cosLon > 0) { // Cara visible 3D
                    double sinLon = Math.sin(radLon);

                    for (float dLat = -latSpan / 2f; dLat <= latSpan / 2f; dLat += 8f) {
                        float currentLat = baseLat + dLat;
                        double radLat = Math.toRadians(currentLat);

                        float px = (float) (cx + radius * sinLon * Math.cos(radLat));
                        float py = (float) (cy - radius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                        float spotRadius = (float) (radius * 0.10f * cosLon * Math.cos(radLat));
                        if (spotRadius > 1.5f) {
                            canvas.drawCircle(px, py, spotRadius, paintToUse);

                            // Cima nevada en montañas
                            if (biome == 3 && spotRadius > 4f) {
                                canvas.drawCircle(px, py - 2f, spotRadius * 0.4f, icePaint);
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawPolarIceCaps(Canvas canvas, float cx, float cy, float radius) {
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        // Polo Norte
        float pyNorth = (float) (cy - radius * (Math.sin(Math.toRadians(75)) * Math.cos(radTilt)));
        canvas.drawCircle(cx, pyNorth, radius * 0.35f, icePaint);

        // Polo Sur
        float pySouth = (float) (cy - radius * (Math.sin(Math.toRadians(-75)) * Math.cos(radTilt)));
        canvas.drawCircle(cx, pySouth, radius * 0.30f, icePaint);
    }

    private void drawOrbitingClouds(Canvas canvas, float cx, float cy, float radius) {
        // Nubes en Parallax (se mueven 1.25x más rápido)
        float cloudOffset = (longitudeDeg * 1.25f) % 360f;

        float[][] cloudPuffs = {
                {30f, 30f}, {110f, -20f}, {200f, 40f}, {280f, -10f}
        };

        for (float[] cloud : cloudPuffs) {
            float currentLon = (cloud[0] + cloudOffset) % 360f;
            if (currentLon < 0) currentLon += 360f;

            double radLon = Math.toRadians(currentLon);
            double cosLon = Math.cos(radLon);

            if (cosLon > 0) {
                double radLat = Math.toRadians(cloud[1]);
                float px = (float) (cx + radius * Math.sin(radLon) * Math.cos(radLat));
                float py = (float) (cy - radius * Math.sin(radLat));

                float cloudSize = (float) (radius * 0.14f * cosLon);
                if (cloudSize > 3f) {
                    canvas.drawCircle(px, py, cloudSize, cloudPaint);
                    canvas.drawCircle(px + cloudSize * 0.5f, py - cloudSize * 0.2f, cloudSize * 0.8f, cloudPaint);
                    canvas.drawCircle(px - cloudSize * 0.5f, py + cloudSize * 0.1f, cloudSize * 0.7f, cloudPaint);
                }
            }
        }
    }

    private void drawPlanetFace(Canvas canvas, float cx, float cy, float radius) {
        // Ojos Kawaii fijos en el centro del frente
        float eyeOffset = radius * 0.20f;
        float eyeY = cy - radius * 0.05f;

        // Ojo Izquierdo
        canvas.drawCircle(cx - eyeOffset, eyeY, 5f, facePaint);
        // Ojo Derecho
        canvas.drawCircle(cx + eyeOffset, eyeY, 5f, facePaint);

        // Sonrisa
        Path smilePath = new Path();
        smilePath.moveTo(cx - 8f, eyeY + 12f);
        smilePath.quadTo(cx, eyeY + 20f, cx + 8f, eyeY + 12f);
        canvas.drawPath(smilePath, facePaint);

        // Mejillas rosadas
        canvas.drawCircle(cx - eyeOffset - 6f, eyeY + 8f, 5f, blushPaint);
        canvas.drawCircle(cx + eyeOffset + 6f, eyeY + 8f, 5f, blushPaint);
    }

    private void drawOrbitingRocket(Canvas canvas, float cx, float cy, float radius) {
        // Cohete orbitando a 1.2x del radio del planeta
        double rocketAngle = Math.toRadians((longitudeDeg * 1.5f) % 360f);
        float orbitRadius = radius + 18f;

        float rx = (float) (cx + orbitRadius * Math.cos(rocketAngle));
        float ry = (float) (cy + orbitRadius * 0.35f * Math.sin(rocketAngle));

        rocketPaint.setColor(Color.parseColor("#FFC107"));
        canvas.drawCircle(rx, ry, 6f, rocketPaint);

        rocketPaint.setColor(Color.parseColor("#FF3D00"));
        canvas.drawCircle(rx - 4f, ry, 3f, rocketPaint);
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

                longitudeDeg = (longitudeDeg + deltaX * 0.5f) % 360f;
                if (longitudeDeg < 0) longitudeDeg += 360f;

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