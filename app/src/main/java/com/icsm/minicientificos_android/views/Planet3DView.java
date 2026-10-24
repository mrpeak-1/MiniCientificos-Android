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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.models.PlanetaHotspotCatalog;
import com.icsm.minicientificos_android.models.PlanetaHotspotItem;

import java.util.List;

public class Planet3DView extends View {

    public interface OnPlanetChangeListener {
        void onAngleChanged(float longitudeDeg);
        void onHotspotTapped(PlanetaHotspotItem item);
    }

    private Paint planetPaint;
    private Paint shadowPaint;
    private Paint highlightPaint;
    private Paint atmospherePaint;

    private Paint orbBorderPaint;
    private Paint orbActiveBorderPaint;
    private Paint orbGlowPaint;
    private Paint labelBgPaint;
    private Paint labelTextPaint;

    private Path sphereClipPath;
    private Path orbClipPath;

    private float longitudeDeg = 0f; // 0 a 360 grados
    private float latitudeTiltDeg = 10f; // -30 a +30 grados

    private float lastTouchX = 0f;
    private float lastTouchY = 0f;

    private OnPlanetChangeListener listener;
    private ValueAnimator autoRotationAnimator;

    private final List<PlanetaHotspotItem> hotspots = PlanetaHotspotCatalog.getNodes();
    private final Bitmap[] hotspotBitmaps = new Bitmap[7];
    private Bitmap hdPlanetBitmap;

    private final Matrix planetMatrix = new Matrix();

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
        planetPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmospherePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        orbBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbBorderPaint.setColor(Color.parseColor("#FFFFFF"));
        orbBorderPaint.setStyle(Paint.Style.STROKE);
        orbBorderPaint.setStrokeWidth(4f);

        orbActiveBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbActiveBorderPaint.setColor(Color.parseColor("#FDC500"));
        orbActiveBorderPaint.setStyle(Paint.Style.STROKE);
        orbActiveBorderPaint.setStrokeWidth(6f);

        orbGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbGlowPaint.setColor(Color.parseColor("#90FDC500"));
        orbGlowPaint.setStyle(Paint.Style.FILL);

        labelBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelBgPaint.setColor(Color.parseColor("#DD0B2C5E"));
        labelBgPaint.setStyle(Paint.Style.FILL);

        labelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelTextPaint.setColor(Color.parseColor("#FFFFFF"));
        labelTextPaint.setTextSize(26f);
        labelTextPaint.setFakeBoldText(true);
        labelTextPaint.setTextAlign(Paint.Align.CENTER);

        sphereClipPath = new Path();
        orbClipPath = new Path();

        loadResources();
    }

    private void loadResources() {
        // Carga HD Bitmap del Globo Terráqueo 3D
        try {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_planet_globe_vector);
            if (drawable != null) {
                int size = 512;
                hdPlanetBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(hdPlanetBitmap);
                drawable.setBounds(0, 0, size, size);
                drawable.draw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Carga Fotos HD para los 7 Orbes 3D
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
        float radius = Math.min(width, height) / 2f - 42f;

        // 1. Resplandor Atmosférico Exterior
        RadialGradient atmosGradient = new RadialGradient(
                cx, cy, radius + 22f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#90B8E8FF"), Color.TRANSPARENT},
                new float[]{0.80f, 0.94f, 1f}, Shader.TileMode.CLAMP
        );
        atmospherePaint.setShader(atmosGradient);
        canvas.drawCircle(cx, cy, radius + 22f, atmospherePaint);

        // Clip Esférico Perfecto
        sphereClipPath.reset();
        sphereClipPath.addCircle(cx, cy, radius, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(sphereClipPath);

        // 2. Renderizado del Globo Terráqueo HD 3D con Rotación Matriz
        if (hdPlanetBitmap != null && !hdPlanetBitmap.isRecycled()) {
            planetMatrix.reset();

            // Escala para ajustar al diámetro de la esfera
            float scale = (radius * 2f) / hdPlanetBitmap.getWidth();
            planetMatrix.postScale(scale, scale);

            // Centra el bitmap en (cx, cy)
            planetMatrix.postTranslate(cx - radius, cy - radius);

            // Aplica la rotación 3D en la matriz centrada
            planetMatrix.postRotate(longitudeDeg, cx, cy);

            canvas.drawBitmap(hdPlanetBitmap, planetMatrix, planetPaint);
        }

        // 3. Brillo Especular de Luz Solar 3D
        RadialGradient highlightGradient = new RadialGradient(
                cx - radius * 0.40f, cy - radius * 0.40f, radius * 0.70f,
                new int[]{Color.parseColor("#70FFFFFF"), Color.TRANSPARENT},
                new float[]{0f, 1f}, Shader.TileMode.CLAMP
        );
        highlightPaint.setShader(highlightGradient);
        canvas.drawCircle(cx, cy, radius, highlightPaint);

        // 4. Sombra 3D Curva de Profundidad Nocturna
        RadialGradient shadowGradient = new RadialGradient(
                cx + radius * 0.45f, cy + radius * 0.45f, radius * 1.15f,
                new int[]{Color.TRANSPARENT, Color.parseColor("#C50B2C5E")},
                new float[]{0.45f, 1f}, Shader.TileMode.CLAMP
        );
        shadowPaint.setShader(shadowGradient);
        canvas.drawCircle(cx, cy, radius, shadowPaint);

        canvas.restore();

        // 5. Orbes 3D de los 7 Ecosistemas Orbitando sobre la Esfera
        drawOrbitingHotspotOrbs(canvas, cx, cy, radius);
    }

    private void drawOrbitingHotspotOrbs(Canvas canvas, float cx, float cy, float radius) {
        if (hotspots == null || hotspots.isEmpty()) return;

        float orbitRadius = radius * 0.86f;
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