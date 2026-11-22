package com.icsm.minicientificos_android.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetPhotoBoothBinding;
import com.icsm.minicientificos_android.models.GaleriaCatalogData;
import com.icsm.minicientificos_android.models.GaleriaFoto;
import com.icsm.minicientificos_android.utils.CredencialBitmapRenderer;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Photo Booth / Marco Fotográfico Científico con renderizado nativo en Canvas.
 * Permite seleccionar una foto de la galería del celular o de la app, aplicar 1 de 3
 * marcos oficiales Kawaii con stickers de Titan y Tesla, y guardar o compartir en PNG.
 */
public class PhotoBoothCientificoBottomSheet extends BottomSheetDialogFragment {

    private static final int MARCO_TITAN_ESPACIAL = 1;
    private static final int MARCO_TESLA_ECOLOGICO = 2;
    private static final int MARCO_FERIA_ICSM = 3;

    private BottomSheetPhotoBoothBinding binding;
    private List<GaleriaFoto> fotosApp;
    private int indiceFotoApp = 0;
    private int marcoSeleccionado = MARCO_TITAN_ESPACIAL;

    private Bitmap bitmapOrigen;
    private Bitmap bitmapRenderizado;

    private final ActivityResultLauncher<String> selectorGaleriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && getContext() != null) {
                    Bitmap cargado = decodificarBitmapSeguroDesdeUri(requireContext(), uri, 1080);
                    if (cargado != null) {
                        bitmapOrigen = cargado;
                        GameSoundAndVfxHelper.getInstance().playRespuestaCorrecta(requireContext());
                        renderizarVistaPrevia();
                    } else {
                        Toast.makeText(requireContext(), "No se pudo cargar la imagen seleccionada.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public PhotoBoothCientificoBottomSheet() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetPhotoBoothBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fotosApp = GaleriaCatalogData.obtenerTodasLasFotos();
        cargarFotoAppActual();
        setupListeners();
        renderizarVistaPrevia();
    }

    private void setupListeners() {
        binding.btnClosePhotoBooth.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            dismiss();
        });

        binding.btnElegirFotoCelular.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            selectorGaleriaLauncher.launch("image/*");
        });

        binding.btnCambiarFotoApp.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            if (fotosApp != null && !fotosApp.isEmpty()) {
                indiceFotoApp = (indiceFotoApp + 1) % fotosApp.size();
                cargarFotoAppActual();
                renderizarVistaPrevia();
                GameSoundAndVfxHelper.animarPulsoExito(binding.ivPhotoBoothPreview);
            }
        });

        binding.chipGroupMarcosBooth.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.chipMarcoTesla) {
                marcoSeleccionado = MARCO_TESLA_ECOLOGICO;
            } else if (id == R.id.chipMarcoFeria) {
                marcoSeleccionado = MARCO_FERIA_ICSM;
            } else {
                marcoSeleccionado = MARCO_TITAN_ESPACIAL;
            }
            GameSoundAndVfxHelper.getInstance().playClick();
            renderizarVistaPrevia();
        });

        binding.etFrasePhotoBooth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                renderizarVistaPrevia();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnGuardarPhotoBooth.setOnClickListener(v -> {
            if (bitmapRenderizado == null || getContext() == null) return;
            boolean ok = guardarBitmapEnGaleria(requireContext(), bitmapRenderizado);
            if (ok) {
                GameSoundAndVfxHelper.getInstance().playVictoriaNivel(requireContext());
                GameSoundAndVfxHelper.animarPulsoExito(binding.btnGuardarPhotoBooth);
                Toast.makeText(requireContext(), "✅ Foto científica guardada en tu dispositivo", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "No se pudo guardar la foto en galería. Intenta compartirla.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCompartirPhotoBooth.setOnClickListener(v -> {
            if (bitmapRenderizado == null || getContext() == null) return;
            GameSoundAndVfxHelper.getInstance().playRespuestaCorrecta(requireContext());
            String nombreArchivo = "MiniCientificos_PhotoBooth_" + System.currentTimeMillis() + ".png";
            CredencialBitmapRenderer.compartirBitmapComoPng(requireContext(), bitmapRenderizado, nombreArchivo);
        });
    }

    private void cargarFotoAppActual() {
        if (getContext() == null || fotosApp == null || fotosApp.isEmpty()) return;
        GaleriaFoto foto = fotosApp.get(indiceFotoApp);
        int resId = foto.resolveDrawableResId(requireContext());
        bitmapOrigen = decodificarBitmapSeguroDesdeRes(requireContext(), resId, 960);
    }

    private void renderizarVistaPrevia() {
        if (getContext() == null || binding == null || bitmapOrigen == null) return;

        String frase = (binding.etFrasePhotoBooth.getText() != null)
                ? binding.etFrasePhotoBooth.getText().toString().trim()
                : "";
        if (frase.isEmpty()) {
            frase = "¡Hoy descubrí la magia de la ciencia!";
        }

        bitmapRenderizado = componerMarcoCientifico(requireContext(), bitmapOrigen, marcoSeleccionado, frase);
        if (bitmapRenderizado != null) {
            binding.ivPhotoBoothPreview.setImageBitmap(bitmapRenderizado);
        }
    }

    /**
     * Dibuja con Canvas y Bitmap un marco HD (1080x1240) con bordes redondeados,
     * stickers oficiales de Titan y Tesla, sello certificado y frase personalizada.
     */
    private Bitmap componerMarcoCientifico(Context ctx, Bitmap fotoOriginal, int tipoMarco, String frase) {
        final int width = 1080;
        final int height = 1240;

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);

        int colorStart;
        int colorEnd;
        int colorBorder;
        int colorBannerBg;
        String tituloMarco;

        if (tipoMarco == MARCO_TESLA_ECOLOGICO) {
            colorStart = Color.parseColor("#14532D");
            colorEnd = Color.parseColor("#16A34A");
            colorBorder = Color.parseColor("#FDC500");
            colorBannerBg = Color.parseColor("#0B3B24");
            tituloMarco = "🌱 GUARDIÁN ECOLÓGICO DE TESLA";
        } else if (tipoMarco == MARCO_FERIA_ICSM) {
            colorStart = Color.parseColor("#0077CC");
            colorEnd = Color.parseColor("#00A8E8");
            colorBorder = Color.parseColor("#FDC500");
            colorBannerBg = Color.parseColor("#0B2C5E");
            tituloMarco = "🏆 FERIA DE CIENCIAS ICSM";
        } else {
            colorStart = Color.parseColor("#0B2C5E");
            colorEnd = Color.parseColor("#1D4ED8");
            colorBorder = Color.parseColor("#38BDF8");
            colorBannerBg = Color.parseColor("#071C3B");
            tituloMarco = "🚀 LABORATORIO ESPACIAL DE TITAN";
        }

        // 1. Fondo degradado redondeado
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setShader(new LinearGradient(0, 0, width, height, colorStart, colorEnd, Shader.TileMode.CLAMP));
        RectF fullRect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(fullRect, 48f, 48f, bgPaint);

        // 2. Decoración geométrica sutil (órbitas / burbujas científicas)
        Paint decoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        decoPaint.setColor(Color.parseColor("#24FFFFFF"));
        decoPaint.setStyle(Paint.Style.STROKE);
        decoPaint.setStrokeWidth(5f);
        canvas.drawCircle(110f, 110f, 120f, decoPaint);
        canvas.drawCircle(width - 90f, 130f, 140f, decoPaint);
        canvas.drawCircle(width - 120f, height - 120f, 150f, decoPaint);

        // 3. Borde exterior grueso estilo marco polaroid kawaii
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(colorBorder);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(16f);
        canvas.drawRoundRect(new RectF(22, 22, width - 22, height - 22), 40f, 40f, borderPaint);

        // 4. Cabecera superior con título del marco
        Paint headerPillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerPillPaint.setColor(colorBannerBg);
        canvas.drawRoundRect(new RectF(56, 46, width - 56, 132), 34f, 34f, headerPillPaint);

        Paint headerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerTextPaint.setColor(Color.parseColor("#FDC500"));
        headerTextPaint.setTextSize(34f);
        headerTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        headerTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(tituloMarco, width / 2f, 102f, headerTextPaint);

        // 5. Ventana central para la fotografía (recorte redondeado center-crop)
        RectF photoWindow = new RectF(66f, 160f, width - 66f, 940f);
        Paint photoShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        photoShadow.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(56f, 150f, width - 56f, 950f), 34f, 34f, photoShadow);

        canvas.save();
        Path clipPath = new Path();
        clipPath.addRoundRect(photoWindow, 26f, 26f, Path.Direction.CW);
        canvas.clipPath(clipPath);
        dibujarBitmapCenterCrop(canvas, fotoOriginal, photoWindow);
        canvas.restore();

        // 6. Stickers circulares de Titan (esquina inferior izquierda) y Tesla (esquina inferior derecha)
        int titanRes = GameSoundAndVfxHelper.obtenerDrawablePersonajeEstado(ctx, "TITAN", "CELEBRANDO");
        int teslaRes = GameSoundAndVfxHelper.obtenerDrawablePersonajeEstado(ctx, "TESLA", "FELIZ");

        dibujarStickerCircular(canvas, ctx, titanRes, 132f, 880f, 82f, Color.parseColor("#00A8E8"));
        dibujarStickerCircular(canvas, ctx, teslaRes, width - 132f, 880f, 82f, Color.parseColor("#FDC500"));

        // 7. Sello oficial inferior
        Paint selloBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        selloBg.setColor(Color.parseColor("#FDC500"));
        canvas.drawRoundRect(new RectF(66f, 980f, width - 66f, 1054f), 30f, 30f, selloBg);

        Paint selloText = new Paint(Paint.ANTI_ALIAS_FLAG);
        selloText.setColor(Color.parseColor("#0B2C5E"));
        selloText.setTextSize(26f);
        selloText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        selloText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("★ EXPERIMENTO CERTIFICADO · MINI CIENTÍFICOS 2026 ★", width / 2f, 1026f, selloText);

        // 8. Frase personalizada del usuario
        Paint fraseBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        fraseBg.setColor(colorBannerBg);
        canvas.drawRoundRect(new RectF(66f, 1076f, width - 66f, 1188f), 28f, 28f, fraseBg);

        Paint frasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        frasePaint.setColor(Color.WHITE);
        frasePaint.setTextSize(32f);
        frasePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        frasePaint.setTextAlign(Paint.Align.CENTER);
        String fraseDecorada = "“" + frase + "”";
        // Ajuste dinámico si el texto supera el ancho disponible
        while (frasePaint.measureText(fraseDecorada) > (width - 170f) && frasePaint.getTextSize() > 20f) {
            frasePaint.setTextSize(frasePaint.getTextSize() - 2f);
        }
        canvas.drawText(fraseDecorada, width / 2f, 1144f, frasePaint);

        return out;
    }

    private void dibujarBitmapCenterCrop(Canvas canvas, Bitmap src, RectF dst) {
        if (src == null || src.isRecycled()) return;
        float srcRatio = (float) src.getWidth() / src.getHeight();
        float dstRatio = dst.width() / dst.height();

        Rect srcRect;
        if (srcRatio > dstRatio) {
            int newWidth = (int) (src.getHeight() * dstRatio);
            int left = (src.getWidth() - newWidth) / 2;
            srcRect = new Rect(left, 0, left + newWidth, src.getHeight());
        } else {
            int newHeight = (int) (src.getWidth() / dstRatio);
            int top = (src.getHeight() - newHeight) / 2;
            srcRect = new Rect(0, top, src.getWidth(), top + newHeight);
        }

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(src, srcRect, dst, p);
    }

    private void dibujarStickerCircular(Canvas canvas, Context ctx, int drawableRes,
                                        float cx, float cy, float radius, int ringColor) {
        Paint whiteBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        whiteBg.setColor(Color.WHITE);
        canvas.drawCircle(cx, cy, radius + 8f, whiteBg);

        Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setColor(ringColor);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(8f);
        canvas.drawCircle(cx, cy, radius + 4f, ringPaint);

        Bitmap sticker = decodificarBitmapSeguroDesdeRes(ctx, drawableRes, (int) (radius * 2));
        if (sticker != null) {
            canvas.save();
            Path circleClip = new Path();
            circleClip.addCircle(cx, cy, radius - 2f, Path.Direction.CW);
            canvas.clipPath(circleClip);
            RectF dst = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
            dibujarBitmapCenterCrop(canvas, sticker, dst);
            canvas.restore();
        }
    }

    private boolean guardarBitmapEnGaleria(Context ctx, Bitmap bitmap) {
        String filename = "MiniCientificos_Marco_" + System.currentTimeMillis() + ".png";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = ctx.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MiniCientificos");
                Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (OutputStream out = resolver.openOutputStream(uri)) {
                        if (out != null) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            return true;
                        }
                    }
                }
            } else {
                File dir = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MiniCientificos");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File outFile = new File(dir, filename);
                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @Nullable
    private Bitmap decodificarBitmapSeguroDesdeRes(Context ctx, int resId, int maxDim) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(ctx.getResources(), resId, opts);

            opts.inSampleSize = calcularInSampleSize(opts.outWidth, opts.outHeight, maxDim);
            opts.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(ctx.getResources(), resId, opts);
        } catch (Throwable t) {
            return null;
        }
    }

    @Nullable
    private Bitmap decodificarBitmapSeguroDesdeUri(Context ctx, Uri uri, int maxDim) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            try (InputStream is1 = ctx.getContentResolver().openInputStream(uri)) {
                if (is1 != null) {
                    BitmapFactory.decodeStream(is1, null, opts);
                }
            }

            opts.inSampleSize = calcularInSampleSize(opts.outWidth, opts.outHeight, maxDim);
            opts.inJustDecodeBounds = false;
            try (InputStream is2 = ctx.getContentResolver().openInputStream(uri)) {
                if (is2 != null) {
                    return BitmapFactory.decodeStream(is2, null, opts);
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private int calcularInSampleSize(int width, int height, int maxDim) {
        int inSampleSize = 1;
        if (width > maxDim || height > maxDim) {
            int halfW = width / 2;
            int halfH = height / 2;
            while ((halfW / inSampleSize) >= maxDim && (halfH / inSampleSize) >= maxDim) {
                inSampleSize *= 2;
            }
        }
        return Math.max(1, inSampleSize);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
