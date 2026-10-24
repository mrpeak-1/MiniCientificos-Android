package com.icsm.minicientificos_android.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.models.PlanetaHotspotCatalog;
import com.icsm.minicientificos_android.models.PlanetaHotspotItem;

import java.util.List;

public class Planet3DView extends View {

    public interface OnPlanetChangeListener {
        void onAngleChanged(float longitudeDeg);
        void onHotspotTapped(PlanetaHotspotItem item);
    }

    private Paint oceanPaint;
    private Paint landPaint;
    private Paint desertPaint;
    private Paint icePaint;
    private Paint shadowPaint;
    private Paint atmospherePaint;
    private Paint orbBorderPaint;
    private Paint orbActiveBorderPaint;
    private Paint orbGlowPaint;

    private Path sphereClipPath;
    private Path orbClipPath;

    private float longitudeDeg = 0f; // 0 a 360 grados
    private float latitudeTiltDeg = 12f; // -30 a +30 grados

    private float lastTouchX = 0f;
    private float lastTouchY = 0f;

    private OnPlanetChangeListener listener;
    private ValueAnimator autoRotationAnimator;

    private final List<PlanetaHotspotItem> hotspots = PlanetaHotspotCatalog.getNodes();
    private final Bitmap[] hotspotBitmaps = new Bitmap[7];
    private final RectF orbRect = new RectF();

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
        landPaint.setColor(Color.parseColor("#388E3C"));
        landPaint.setStyle(Paint.Style.FILL);

        desertPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        desertPaint.setColor(Color.parseColor("#FBC02D"));
        desertPaint.setStyle(Paint.Style.FILL);

        icePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        icePaint.setColor(Color.parseColor("#FFFFFF"));
        icePaint.setStyle(Paint.Style.FILL);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmospherePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        orbBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbBorderPaint.setColor(Color.parseColor("#FFFFFF"));
        orbBorderPaint.setStyle(Paint.Style.STROKE);
        orbBorderPaint.setStrokeWidth(3f);

        orbActiveBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbActiveBorderPaint.setColor(Color.parseColor("#FDC500"));
        orbActiveBorderPaint.setStyle(Paint.Style.STROKE);
        orbActiveBorderPaint.setStrokeWidth(5f);

        orbGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbGlowPaint.setColor(Color.parseColor("#80FDC500"));
        orbGlowPaint.setStyle(Paint.Style.FILL);

        sphereClipPath = new Path();
        orbClipPath = new Path();

        // Carga Bitmaps reales para cada uno de los 7 Ecosistemas
        loadHotspotBitmaps();
    }

    private void loadHotspotBitmaps() {
        int[] resIds = {
                R.drawable.compostaje,      // 0: Mini Huerto
                R.drawable.energias_reno,   // 1: Energías Renovables
                R.drawable.reciclaje,       // 2: Reciclaje
                R.drawable.geologia,        // 3: Geología
                R.drawable.hidroponia,      // 4: Hidroponía
                R.drawable.astronomia,      // 5: Astronomía
                R.drawable.expe_fisi        // 6: Física Loca
        };

        for (int i = 0; i < resIds.length; i++) {
            try {
                hotspotBitmaps[i] = BitmapFactory.decodeResource(getResources(), resIds[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        float radius = Math.min(width, height) / 2f - 40f;

        // 1. Océano Fotorrealista 3D con Gradiente Radial de Brillo Esférico
        RadialGradient oceanGradient = new RadialGradient(
                cx - radius * 0.35f, cy - radius * 0.35f, radius * 1.35f,
                new int[]{Color.parseColor("#4AD3FF"), Color.parseColor("#0081C9"), Color.parseColor("#002B49")},
                new float[]{0f, 0.65f, 1f}, Shader.TileMode.CLAMP
        );
        oceanPaint.setShader(oceanGradient);

        // Clip circular de la esfera
        sphereClipPath.reset();
        sphereClipPath.addCircle(cx, cy, radius, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(sphereClipPath);

        // Dibujar Océano Esférico
        canvas.drawCircle(cx, cy, radius, oceanPaint);

        // 2. Continentes Realistas
        drawRealistic3DContinents(canvas, cx, cy, radius);

        // 3. Sombra 3D de Profundidad Nocturna Curva
        RadialGradient shadowGradient = new RadialGradient(
                cx + radius * 0.45f, cy + radius * 0.45f, radius * 1.15f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#C80B2C5E")},
                new float[]{0.45f, 1f}, Shader.TileMode.CLAMP
        );
        shadowPaint.setShader(shadowGradient);
        canvas.drawCircle(cx, cy, radius, shadowPaint);

        canvas.restore();

        // 4. Brillo de Atmósfera Exterior
        RadialGradient atmosGradient = new RadialGradient(
                cx, cy, radius + 20f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#90B8E8FF"), Color.TRANSPARENT},
                new float[]{0.82f, 0.94f, 1f}, Shader.TileMode.CLAMP
        );
        atmospherePaint.setShader(atmosGradient);
        canvas.drawCircle(cx, cy, radius + 20f, atmospherePaint);

        // 5. Orbes Fotorrealistas 3D de los 7 Ecosistemas Orbitando sobre el Planeta
        drawOrbitingHotspotOrbs(canvas, cx, cy, radius);
    }

    private void drawRealistic3DContinents(Canvas canvas, float cx, float cy, float radius) {
        // Renderiza masas continentales continuas usando proyección esférica 3D
        float[][] landmasses = {
                {0f, 25f, 55f, 40f, 0},     // América del Norte
                {10f, -20f, 45f, 50f, 0},    // América del Sur
                {75f, 40f, 45f, 35f, 0},     // Europa
                {85f, 0f, 50f, 45f, 1},      // África / Sahara (Desierto)
                {145f, 35f, 65f, 50f, 0},    // Asia
                {225f, -25f, 45f, 35f, 1},   // Australia / Desierto
                {290f, 15f, 35f, 25f, 0}     // Islas Pacífico
        };

        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (float[] land : landmasses) {
            float baseLon = land[0];
            float baseLat = land[1];
            float lonSpan = land[2];
            float latSpan = land[3];
            boolean isDesert = land[4] == 1;

            Paint paintToUse = isDesert ? desertPaint : landPaint;

            for (float dLon = -lonSpan / 2f; dLon <= lonSpan / 2f; dLon += 6f) {
                float currentLon = (baseLon + dLon + longitudeDeg) % 360f;
                if (currentLon < 0) currentLon += 360f;

                double radLon = Math.toRadians(currentLon);
                double cosLon = Math.cos(radLon);

                if (cosLon > 0) { // Cara frontal visible de la esfera
                    double sinLon = Math.sin(radLon);

                    for (float dLat = -latSpan / 2f; dLat <= latSpan / 2f; dLat += 6f) {
                        float currentLat = baseLat + dLat;
                        double radLat = Math.toRadians(currentLat);

                        float px = (float) (cx + radius * sinLon * Math.cos(radLat));
                        float py = (float) (cy - radius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                        float spotRadius = (float) (radius * 0.08f * cosLon * Math.cos(radLat));
                        if (spotRadius > 1.2f) {
                            canvas.drawCircle(px, py, spotRadius, paintToUse);
                        }
                    }
                }
            }
        }

        // Casquetes polares glaciares de nieve
        float pyNorth = (float) (cy - radius * (Math.sin(Math.toRadians(75)) * Math.cos(radTilt)));
        canvas.drawCircle(cx, pyNorth, radius * 0.35f, icePaint);

        float pySouth = (float) (cy - radius * (Math.sin(Math.toRadians(-75)) * Math.cos(radTilt)));
        canvas.drawCircle(cx, pySouth, radius * 0.30f, icePaint);
    }

    private void drawOrbitingHotspotOrbs(Canvas canvas, float cx, float cy, float radius) {
        if (hotspots == null || hotspots.isEmpty()) return;

        float orbitRadius = radius * 0.88f; // Radio dentro de la superficie esférica
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (int i = 0; i < hotspots.size(); i++) {
            PlanetaHotspotItem item = hotspots.get(i);
            float targetAngle = item.getAnguloObjetivoGrados();

            // Ángulo de longitud actual en la esfera
            float currentLon = (targetAngle + longitudeDeg) % 360f;
            if (currentLon < 0) currentLon += 360f;

            double radLon = Math.toRadians(currentLon);
            double cosLon = Math.cos(radLon);

            // Se dibuja si está en la cara frontal visible (cosLon > -0.2f)
            if (cosLon > -0.2f) {
                double sinLon = Math.sin(radLon);
                double radLat = Math.toRadians(0); // Ecuador

                float px = (float) (cx + orbitRadius * sinLon * Math.cos(radLat));
                float py = (float) (cy - orbitRadius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                // Escala de tamaño 3D según la profundidad Z
                float depthFactor = (float) Math.max(0.4f, (cosLon + 0.2f) / 1.2f);
                float orbSize = getOrbBaseSizePx() * depthFactor;

                // Es el ítem más enfocado al frente
                boolean isFrontActive = Math.abs(currentLon) < 25f || Math.abs(currentLon - 360f) < 25f;

                if (isFrontActive) {
                    orbSize *= 1.25f;
                    // Resplandor de oro
                    canvas.drawCircle(px, py, orbSize + 8f, orbGlowPaint);
                }

                orbRect.set(px - orbSize, py - orbSize, px + orbSize, py + orbSize);

                // Dibuja la foto real del ecosistema dentro del orbe de cristal 3D
                Bitmap bitmap = (i < hotspotBitmaps.length) ? hotspotBitmaps[i] : null;
                if (bitmap != null && !bitmap.isRecycled()) {
                    canvas.save();
                    orbClipPath.reset();
                    orbClipPath.addCircle(px, py, orbSize, Path.Direction.CW);
                    canvas.clipPath(orbClipPath);

                    // Escala y dibuja el bitmap dentro del círculo
                    Matrix matrix = new Matrix();
                    float scaleX = (orbSize * 2f) / bitmap.getWidth();
                    float scaleY = (orbSize * 2f) / bitmap.getHeight();
                    matrix.setScale(scaleX, scaleY);
                    matrix.postTranslate(px - orbSize, py - orbSize);

                    canvas.drawBitmap(bitmap, matrix, null);
                    canvas.restore();
                }

                // Anillo de cristal del Orbe
                Paint border = isFrontActive ? orbActiveBorderPaint : orbBorderPaint;
                canvas.drawCircle(px, py, orbSize, border);
            }
        }
    }

    private float getOrbBaseSizePx() {
        return 22f * getResources().getDisplayMetrics().density;
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
                // Verifica si tocó directamente uno de los Orbes 3D
                checkOrbsClick(x, y);
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

    private void checkOrbsClick(float touchX, float touchY) {
        if (hotspots == null || listener == null) return;

        int width = getWidth();
        int height = getHeight();
        float cx = width / 2f;
        float cy = height / 2f;
        float radius = Math.min(width, height) / 2f - 40f;
        float orbitRadius = radius * 0.88f;
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (int i = 0; i < hotspots.size(); i++) {
            PlanetaHotspotItem item = hotspots.get(i);
            float targetAngle = item.getAnguloObjetivoGrados();

            float currentLon = (targetAngle + longitudeDeg) % 360f;
            if (currentLon < 0) currentLon += 360f;

            double radLon = Math.toRadians(currentLon);
            double cosLon = Math.cos(radLon);

            if (cosLon > 0) { // En la cara frontal
                double sinLon = Math.sin(radLon);
                double radLat = Math.toRadians(0);

                float px = (float) (cx + orbitRadius * sinLon * Math.cos(radLat));
                float py = (float) (cy - orbitRadius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                float depthFactor = (float) Math.max(0.4f, (cosLon + 0.2f) / 1.2f);
                float orbSize = getOrbBaseSizePx() * depthFactor;

                // Distancia euclidiana del toque al orbe
                float dist = (float) Math.hypot(touchX - px, touchY - py);
                if (dist <= orbSize * 1.4f) {
                    listener.onHotspotTapped(item);
                    rotateToLongitude(360f - targetAngle);
                    break;
                }
            }
        }
    }
}