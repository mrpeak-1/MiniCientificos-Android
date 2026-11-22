package com.icsm.minicientificos_android.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * ImageView interactivo con Pinch-to-Zoom continuo (1.0x a 4.0x),
 * doble toque animado (1.0x <-> 2.5x) y arrastre suave con límites usando Matrix.
 */
public class ZoomableImageView extends AppCompatImageView {

    private static final float MIN_SCALE = 1.0f;
    private static final float DOUBLE_TAP_SCALE = 2.5f;
    private static final float MAX_SCALE = 4.0f;

    private final Matrix matrix = new Matrix();
    private final float[] matrixValues = new float[9];

    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;

    private float baseScale = 1.0f;
    private float currentZoom = 1.0f;

    private final PointF lastTouch = new PointF();
    private boolean isDragging = false;
    private ValueAnimator zoomAnimator;

    public ZoomableImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ZoomableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        super.setScaleType(ScaleType.MATRIX);
        setClickable(true);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            fitImageToView();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        post(this::fitImageToView);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        post(this::fitImageToView);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        post(this::fitImageToView);
    }

    public void resetZoom() {
        cancelZoomAnimation();
        fitImageToView();
    }

    public float getCurrentZoom() {
        return currentZoom;
    }

    private void fitImageToView() {
        Drawable drawable = getDrawable();
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (drawable == null || viewWidth <= 0 || viewHeight <= 0) {
            return;
        }

        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            return;
        }

        float scaleX = (float) viewWidth / drawableWidth;
        float scaleY = (float) viewHeight / drawableHeight;
        baseScale = Math.min(scaleX, scaleY);
        currentZoom = MIN_SCALE;

        float dx = (viewWidth - drawableWidth * baseScale) * 0.5f;
        float dy = (viewHeight - drawableHeight * baseScale) * 0.5f;

        matrix.reset();
        matrix.postScale(baseScale, baseScale);
        matrix.postTranslate(dx, dy);
        setImageMatrix(matrix);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getDrawable() == null) {
            return super.onTouchEvent(event);
        }

        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                cancelZoomAnimation();
                lastTouch.set(event.getX(), event.getY());
                isDragging = true;
                actualizarIntercepcionPadre();
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging && !scaleDetector.isInProgress() && currentZoom > MIN_SCALE + 0.01f) {
                    float dx = event.getX() - lastTouch.x;
                    float dy = event.getY() - lastTouch.y;
                    matrix.postTranslate(dx, dy);
                    corregirLimitesTraslacion();
                    setImageMatrix(matrix);
                    lastTouch.set(event.getX(), event.getY());
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndex = event.getActionIndex();
                int newPointerIndex = (pointerIndex == 0) ? 1 : 0;
                if (newPointerIndex < event.getPointerCount()) {
                    lastTouch.set(event.getX(newPointerIndex), event.getY(newPointerIndex));
                }
                break;

            case MotionEvent.ACTION_UP:
                performClick();
                isDragging = false;
                if (currentZoom <= MIN_SCALE + 0.02f) {
                    fitImageToView();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                if (currentZoom <= MIN_SCALE + 0.02f) {
                    fitImageToView();
                }
                break;
        }
        return true;
    }

    private void actualizarIntercepcionPadre() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(currentZoom > MIN_SCALE + 0.02f);
        }
    }

    private void corregirLimitesTraslacion() {
        RectF rect = obtenerRectanguloImagenActual();
        if (rect == null) return;

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float deltaX = 0f;
        float deltaY = 0f;

        if (rect.width() <= viewWidth) {
            deltaX = (viewWidth - rect.width()) * 0.5f - rect.left;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
        }

        if (rect.height() <= viewHeight) {
            deltaY = (viewHeight - rect.height()) * 0.5f - rect.top;
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom;
        }

        if (deltaX != 0f || deltaY != 0f) {
            matrix.postTranslate(deltaX, deltaY);
        }
    }

    @Nullable
    private RectF obtenerRectanguloImagenActual() {
        Drawable drawable = getDrawable();
        if (drawable == null) return null;
        RectF rect = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(rect);
        return rect;
    }

    private float obtenerEscalaActualMatriz() {
        matrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    private void animarZoomHacia(float targetZoom, float focusX, float focusY) {
        cancelZoomAnimation();
        float startZoom = currentZoom;
        zoomAnimator = ValueAnimator.ofFloat(startZoom, targetZoom);
        zoomAnimator.setDuration(240);
        zoomAnimator.setInterpolator(new DecelerateInterpolator());
        zoomAnimator.addUpdateListener(anim -> {
            float valor = (float) anim.getAnimatedValue();
            float actualEscala = obtenerEscalaActualMatriz();
            if (actualEscala <= 0f || baseScale <= 0f) return;

            float factor = (valor * baseScale) / actualEscala;
            matrix.postScale(factor, factor, focusX, focusY);
            currentZoom = valor;
            corregirLimitesTraslacion();
            setImageMatrix(matrix);
        });
        zoomAnimator.start();
    }

    private void cancelZoomAnimation() {
        if (zoomAnimator != null && zoomAnimator.isRunning()) {
            zoomAnimator.cancel();
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float targetZoom = currentZoom * scaleFactor;

            if (targetZoom < MIN_SCALE) {
                scaleFactor = MIN_SCALE / currentZoom;
                currentZoom = MIN_SCALE;
            } else if (targetZoom > MAX_SCALE) {
                scaleFactor = MAX_SCALE / currentZoom;
                currentZoom = MAX_SCALE;
            } else {
                currentZoom = targetZoom;
            }

            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            corregirLimitesTraslacion();
            setImageMatrix(matrix);
            actualizarIntercepcionPadre();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            float targetZoom = (currentZoom > 1.25f) ? MIN_SCALE : DOUBLE_TAP_SCALE;
            animarZoomHacia(targetZoom, e.getX(), e.getY());
            return true;
        }
    }
}
