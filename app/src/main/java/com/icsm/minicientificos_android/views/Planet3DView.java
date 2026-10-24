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
    private Paint icePaint;
    private Paint shadowPaint;
    private Paint atmospherePaint;
    private Paint highlightPaint;
    private Paint orbBorderPaint;
    private Paint orbActiveBorderPaint;
    private Paint orbGlowPaint;
    private Paint labelBgPaint;
    private Paint labelTextPaint;

    private Path sphereClipPath;
    private Path orbClipPath;
    private Path continentPath;

    private float longitudeDeg = 0f; // 0 a 360 grados
    private float latitudeTiltDeg = 10f; // -30 a +30 grados

    private float lastTouchX = 0f;
    private float lastTouchY = 0f;

    private OnPlanetChangeListener listener;
    private ValueAnimator autoRotationAnimator;

    private final List<PlanetaHotspotItem> hotspots = PlanetaHotspotCatalog.getNodes();
    private final Bitmap[] hotspotBitmaps = new Bitmap[7];

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
        landPaint.setColor(Color.parseColor("#2E7D32")); // Verde boscoso realista
        landPaint.setStyle(Paint.Style.FILL);

        icePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        icePaint.setColor(Color.parseColor("#F5F5F5")); // Blanco glaciar
        icePaint.setStyle(Paint.Style.FILL);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmospherePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        orbBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbBorderPaint.setColor(Color.parseColor("#FFFFFF"));
        orbBorderPaint.setStyle(Paint.Style.STROKE);
        orbBorderPaint.setStrokeWidth(3.5f);

        orbActiveBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbActiveBorderPaint.setColor(Color.parseColor("#FDC500"));
        orbActiveBorderPaint.setStyle(Paint.Style.STROKE);
        orbActiveBorderPaint.setStrokeWidth(6f);

        orbGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbGlowPaint.setColor(Color.parseColor("#90FDC500"));
        orbGlowPaint.setStyle(Paint.Style.FILL);

        labelBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelBgPaint.setColor(Color.parseColor("#CC0B2C5E"));
        labelBgPaint.setStyle(Paint.Style.FILL);

        labelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelTextPaint.setColor(Color.parseColor("#FFFFFF"));
        labelTextPaint.setTextSize(26f);
        labelTextPaint.setFakeBoldText(true);
        labelTextPaint.setTextAlign(Paint.Align.CENTER);

        sphereClipPath = new Path();
        orbClipPath = new Path();
        continentPath = new Path();

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
                // Ignore
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

        // 1. Océano Fotorrealista con Iluminación Esférica 3D
        RadialGradient oceanGradient = new RadialGradient(
                cx - radius * 0.35f, cy - radius * 0.35f, radius * 1.35f,
                new int[]{Color.parseColor("#4AD3FF"), Color.parseColor("#0077C8"), Color.parseColor("#002B49")},
                new float[]{0f, 0.65f, 1f}, Shader.TileMode.CLAMP
        );
        oceanPaint.setShader(oceanGradient);

        sphereClipPath.reset();
        sphereClipPath.addCircle(cx, cy, radius, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(sphereClipPath);

        // Dibujar Océano Esférico Base
        canvas.drawCircle(cx, cy, radius, oceanPaint);

        // 2. Continentes Fotorrealistas Continuos (Sin círculos ni puntos vacíos)
        drawRealistic3DContinentPaths(canvas, cx, cy, radius);

        // 3. Casquetes Polares (Ártico y Antártida)
        drawPolarGlaciers(canvas, cx, cy, radius);

        // 4. Brillo Especular de Sol 3D (Esquina superior izquierda)
        RadialGradient highlightGradient = new RadialGradient(
                cx - radius * 0.40f, cy - radius * 0.40f, radius * 0.60f,
                new int[]{Color.parseColor("#60FFFFFF"), Color.TRANSPARENT},
                new float[]{0f, 1f}, Shader.TileMode.CLAMP
        );
        highlightPaint.setShader(highlightGradient);
        canvas.drawCircle(cx, cy, radius, highlightPaint);

        // 5. Sombra de Profundidad Nocturna Curva 3D
        RadialGradient shadowGradient = new RadialGradient(
                cx + radius * 0.45f, cy + radius * 0.45f, radius * 1.15f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#D50B2C5E")},
                new float[]{0.45f, 1f}, Shader.TileMode.CLAMP
        );
        shadowPaint.setShader(shadowGradient);
        canvas.drawCircle(cx, cy, radius, shadowPaint);

        canvas.restore();

        // 6. Resplandor de Atmósfera Exterior
        RadialGradient atmosGradient = new RadialGradient(
                cx, cy, radius + 18f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#90B8E8FF"), Color.TRANSPARENT},
                new float[]{0.82f, 0.94f, 1f}, Shader.TileMode.CLAMP
        );
        atmospherePaint.setShader(atmosGradient);
        canvas.drawCircle(cx, cy, radius + 18f, atmospherePaint);

        // 7. Orbes Fotorrealistas 3D de los 7 Ecosistemas
        drawOrbitingHotspotOrbs(canvas, cx, cy, radius);
    }

    private void drawRealistic3DContinentPaths(Canvas canvas, float cx, float cy, float radius) {
        // Renderiza trazos continentales continuos y orgánicos con proyección esférica 3D
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        // Definición de las 4 masas continentales principales [longitudCentral, latitudCentral, anchoLon, altoLat]
        float[][] continents = {
                {0f, 15f, 90f, 70f},    // América del Norte y Sur
                {90f, 20f, 80f, 80f},   // Europa y África
                {170f, 30f, 100f, 75f},  // Asia y Oceanía
                {270f, 10f, 70f, 60f}   // Pacífico e Islas
        };

        for (float[] cont : continents) {
            float baseLon = cont[0];
            float baseLat = cont[1];
            float lonWidth = cont[2];
            float latHeight = cont[3];

            continentPath.reset();
            boolean pathStarted = false;

            int stepsLon = 16;
            int stepsLat = 12;

            for (int i = 0; i <= stepsLon; i++) {
                float lonRatio = (float) i / stepsLon - 0.5f; // [-0.5, 0.5]
                float curLon = (baseLon + lonRatio * lonWidth + longitudeDeg) % 360f;
                if (curLon < 0) curLon += 360f;

                double radLon = Math.toRadians(curLon);
                double cosLon = Math.cos(radLon);

                if (cosLon > 0) { // Cara visible 3D
                    double sinLon = Math.sin(radLon);

                    for (int j = 0; j <= stepsLat; j++) {
                        float latRatio = (float) j / stepsLat - 0.5f;
                        float curLat = baseLat + latRatio * latHeight;
                        double radLat = Math.toRadians(curLat);

                        float px = (float) (cx + radius * sinLon * Math.cos(radLat));
                        float py = (float) (cy - radius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                        if (!pathStarted) {
                            continentPath.moveTo(px, py);
                            pathStarted = true;
                        } else {
                            continentPath.lineTo(px, py);
                        }
                    }
                }
            }

            if (pathStarted) {
                continentPath.close();
                canvas.drawPath(continentPath, landPaint);
            }
        }
    }

    private void drawPolarGlaciers(Canvas canvas, float cx, float cy, float radius) {
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        // Polo Norte
        float pyNorth = (float) (cy - radius * (Math.sin(Math.toRadians(70)) * Math.cos(radTilt)));
        canvas.drawCircle(cx, pyNorth, radius * 0.38f, icePaint);

        // Polo Sur
        float pySouth = (float) (cy - radius * (Math.sin(Math.toRadians(-70)) * Math.cos(radTilt)));
        canvas.drawCircle(cx, pySouth, radius * 0.32f, icePaint);
    }

    private void drawOrbitingHotspotOrbs(Canvas canvas, float cx, float cy, float radius) {
        if (hotspots == null || hotspots.isEmpty()) return;

        float orbitRadius = radius * 0.85f;
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (int i = 0; i < hotspots.size(); i++) {
            PlanetaHotspotItem item = hotspots.get(i);
            float targetAngle = item.getAnguloObjetivoGrados();

            float currentLon = (targetAngle + longitudeDeg) % 360f;
            if (currentLon < 0) currentLon += 360f;

            double radLon = Math.toRadians(currentLon);
            double cosLon = Math.cos(radLon);

            if (cosLon > -0.2f) { // Cara visible 3D
                double sinLon = Math.sin(radLon);
                double radLat = Math.toRadians(0);

                float px = (float) (cx + orbitRadius * sinLon * Math.cos(radLat));
                float py = (float) (cy - orbitRadius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                float depthFactor = (float) Math.max(0.45f, (cosLon + 0.2f) / 1.2f);
                float orbSize = getOrbBaseSizePx() * depthFactor;

                boolean isFrontActive = Math.abs(currentLon) < 25f || Math.abs(currentLon - 360f) < 25f;

                if (isFrontActive) {
                    orbSize *= 1.30f;
                    canvas.drawCircle(px, py, orbSize + 10f, orbGlowPaint);
                }

                Bitmap bitmap = (i < hotspotBitmaps.length) ? hotspotBitmaps[i] : null;
                if (bitmap != null && !bitmap.isRecycled()) {
                    canvas.save();
                    orbClipPath.reset();
                    orbClipPath.addCircle(px, py, orbSize, Path.Direction.CW);
                    canvas.clipPath(orbClipPath);

                    Matrix matrix = new Matrix();
                    float scaleX = (orbSize * 2f) / bitmap.getWidth();
                    float scaleY = (orbSize * 2f) / bitmap.getHeight();
                    matrix.setScale(scaleX, scaleY);
                    matrix.postTranslate(px - orbSize, py - orbSize);

                    canvas.drawBitmap(bitmap, matrix, null);
                    canvas.restore();
                }

                Paint border = isFrontActive ? orbActiveBorderPaint : orbBorderPaint;
                canvas.drawCircle(px, py, orbSize, border);

                // Etiqueta de Texto si está enfocado
                if (isFrontActive) {
                    String label = item.getEmojiIcono() + " " + item.getNombreExperimento();
                    float textWidth = labelTextPaint.measureText(label);
                    RectF bgRect = new RectF(
                            px - textWidth / 2f - 14f,
                            py + orbSize + 8f,
                            px + textWidth / 2f + 14f,
                            py + orbSize + 42f
                    );
                    canvas.drawRoundRect(bgRect, 20f, 20f, labelBgPaint);
                    canvas.drawText(label, px, py + orbSize + 32f, labelTextPaint);
                }
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
        float orbitRadius = radius * 0.85f;
        float radTilt = (float) Math.toRadians(latitudeTiltDeg);

        for (int i = 0; i < hotspots.size(); i++) {
            PlanetaHotspotItem item = hotspots.get(i);
            float targetAngle = item.getAnguloObjetivoGrados();

            float currentLon = (targetAngle + longitudeDeg) % 360f;
            if (currentLon < 0) currentLon += 360f;

            double radLon = Math.toRadians(currentLon);
            double cosLon = Math.cos(radLon);

            if (cosLon > 0) {
                double sinLon = Math.sin(radLon);
                double radLat = Math.toRadians(0);

                float px = (float) (cx + orbitRadius * sinLon * Math.cos(radLat));
                float py = (float) (cy - orbitRadius * (Math.sin(radLat) * Math.cos(radTilt) - Math.cos(radLat) * Math.sin(radTilt) * cosLon));

                float depthFactor = (float) Math.max(0.45f, (cosLon + 0.2f) / 1.2f);
                float orbSize = getOrbBaseSizePx() * depthFactor;

                float dist = (float) Math.hypot(touchX - px, touchY - py);
                if (dist <= orbSize * 1.5f) {
                    listener.onHotspotTapped(item);
                    rotateToLongitude(360f - targetAngle);
                    break;
                }
            }
        }
    }
}