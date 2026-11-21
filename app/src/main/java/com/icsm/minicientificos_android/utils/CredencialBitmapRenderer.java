package com.icsm.minicientificos_android.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.icsm.minicientificos_android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CredencialBitmapRenderer {

    public static final int TIPO_CARNET = 1;
    public static final int TIPO_DIPLOMA = 2;

    public static Bitmap renderizarDocumento(Context context, int tipoDoc, String nombreNino,
                                            String companero, String especialidad) {
        int width = 1200;
        int height = 800;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        String nombreFinal = (nombreNino == null || nombreNino.trim().isEmpty()) ?
                "Mini Científico(a)" : nombreNino.trim();

        GameProgressManager progressManager = GameProgressManager.getInstance(context);
        String rango = progressManager.getRangoCientificoActual();
        int xp = progressManager.getXpTotal();

        if (tipoDoc == TIPO_DIPLOMA) {
            dibujarDiploma(canvas, width, height, nombreFinal, companero, especialidad, rango, xp);
        } else {
            dibujarCarnet(context, canvas, width, height, nombreFinal, companero, especialidad, rango, xp);
        }

        return bitmap;
    }

    private static void dibujarCarnet(Context context, Canvas canvas, int w, int h,
                                      String nombreNino, String companero, String especialidad,
                                      String rango, int xp) {
        // 1. Fondo degradado espacial/laboratorio (#0B2C5E a #0077CC)
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient gradient = new LinearGradient(0, 0, w, h,
                Color.parseColor("#0B2C5E"), Color.parseColor("#0077CC"), Shader.TileMode.CLAMP);
        bgPaint.setShader(gradient);
        canvas.drawRect(0, 0, w, h, bgPaint);

        // 2. Patrones geométricos y órbitas atómicas
        Paint orbitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbitPaint.setColor(Color.parseColor("#1AFFFFFF"));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(4f);

        canvas.drawCircle(w * 0.85f, h * 0.25f, 180f, orbitPaint);
        canvas.drawCircle(w * 0.85f, h * 0.25f, 300f, orbitPaint);
        canvas.drawCircle(w * 0.1f, h * 0.8f, 220f, orbitPaint);

        RectF ovalOrbit = new RectF(w * 0.7f, h * 0.1f, w * 0.98f, h * 0.4f);
        canvas.drawOval(ovalOrbit, orbitPaint);

        // 3. Marco exterior del carnet
        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.parseColor("#FDC500"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(12f);
        RectF border = new RectF(20, 20, w - 20, h - 20);
        canvas.drawRoundRect(border, 32, 32, strokePaint);

        // 4. Cabecera oficial
        Paint headerBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerBgPaint.setColor(Color.parseColor("#123354"));
        RectF headerBox = new RectF(30, 30, w - 30, 130);
        canvas.drawRoundRect(headerBox, 24, 24, headerBgPaint);

        Paint textHeader = new Paint(Paint.ANTI_ALIAS_FLAG);
        textHeader.setColor(Color.parseColor("#FDC500"));
        textHeader.setTextSize(30f);
        textHeader.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textHeader.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("INSTITUTO CIENTÍFICO SOSTENIBLE MINERO (ICSM) · MINI CIENTÍFICOS", w / 2f, 75f, textHeader);

        Paint textSubHeader = new Paint(Paint.ANTI_ALIAS_FLAG);
        textSubHeader.setColor(Color.WHITE);
        textSubHeader.setTextSize(22f);
        textSubHeader.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🪪 CARNET OFICIAL DE AGENTE CIENTÍFICO HD", w / 2f, 110f, textSubHeader);

        // 5. Marco para la Foto del Personaje / Avatar
        float avatarLeft = 70f;
        float avatarTop = 170f;
        float avatarSize = 240f;

        Paint avatarBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        avatarBg.setColor(Color.WHITE);
        RectF avatarRect = new RectF(avatarLeft, avatarTop, avatarLeft + avatarSize, avatarTop + avatarSize);
        canvas.drawRoundRect(avatarRect, 24, 24, avatarBg);

        int avatarDrawableId = companero.contains("Tesla") ? R.drawable.teslita_png : R.drawable.titan_cientifico;
        Bitmap avatarBmp = BitmapFactory.decodeResource(context.getResources(), avatarDrawableId);
        if (avatarBmp != null) {
            Bitmap scaledAvatar = Bitmap.createScaledBitmap(avatarBmp, (int) avatarSize - 20, (int) avatarSize - 20, true);
            canvas.drawBitmap(scaledAvatar, avatarLeft + 10, avatarTop + 10, null);
        }

        // 6. Datos del Mini Científico
        float textLeft = avatarLeft + avatarSize + 40f;

        // Nombre del Niño con sombra dorada
        Paint namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setColor(Color.WHITE);
        namePaint.setTextSize(48f);
        namePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        namePaint.setShadowLayer(8f, 2f, 2f, Color.parseColor("#FDC500"));
        canvas.drawText(nombreNino, textLeft, 220f, namePaint);

        Paint infoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        infoPaint.setColor(Color.parseColor("#E0F2FE"));
        infoPaint.setTextSize(28f);
        infoPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("Especialidad: " + especialidad, textLeft, 275f, infoPaint);
        canvas.drawText("Compañero: " + companero, textLeft, 320f, infoPaint);
        canvas.drawText("Rango: " + rango + " (" + xp + " XP)", textLeft, 365f, infoPaint);

        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(new Date());
        canvas.drawText("Fecha Emisión: " + fecha, textLeft, 410f, infoPaint);

        // 7. Sello Circular Dorado de Aprobación LAB ICSM
        float sealX = w - 200f;
        float sealY = h - 220f;
        float sealRadius = 95f;

        Paint sealBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        sealBg.setColor(Color.parseColor("#FDC500"));
        canvas.drawCircle(sealX, sealY, sealRadius, sealBg);

        Paint sealStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        sealStroke.setColor(Color.parseColor("#0B2C5E"));
        sealStroke.setStyle(Paint.Style.STROKE);
        sealStroke.setStrokeWidth(6f);
        canvas.drawCircle(sealX, sealY, sealRadius - 10, sealStroke);

        Paint sealText = new Paint(Paint.ANTI_ALIAS_FLAG);
        sealText.setColor(Color.parseColor("#0B2C5E"));
        sealText.setTextSize(18f);
        sealText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        sealText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("★ APROBADO ★", sealX, sealY - 15, sealText);
        canvas.drawText("LAB ICSM", sealX, sealY + 15, sealText);
        canvas.drawText("2026", sealX, sealY + 40, sealText);

        // 8. Código de Verificación Único
        String codigoHash = String.format(Locale.US, "#ICSM-2026-%04d", Math.abs(nombreNino.hashCode() % 10000));
        Paint hashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hashPaint.setColor(Color.parseColor("#FDC500"));
        hashPaint.setTextSize(24f);
        hashPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        canvas.drawText("CÓDIGO AGENTE: " + codigoHash, 70f, h - 80f, hashPaint);

        // 9. Firma impresa del Fundador
        Paint signLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        signLine.setColor(Color.WHITE);
        signLine.setStrokeWidth(3f);
        canvas.drawLine(w - 450, h - 110, w - 70, h - 110, signLine);

        Paint signText = new Paint(Paint.ANTI_ALIAS_FLAG);
        signText.setColor(Color.WHITE);
        signText.setTextSize(20f);
        signText.setTextAlign(Paint.Align.CENTER);
        signText.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        canvas.drawText("Ing. Andy Salazar Sánchez - Fundador", w - 260, h - 75, signText);
    }

    private static void dibujarDiploma(Canvas canvas, int w, int h,
                                       String nombreNino, String companero, String especialidad,
                                       String rango, int xp) {
        // 1. Fondo Pergamino Crema (#FFFDF5)
        canvas.drawColor(Color.parseColor("#FFFDF5"));

        // 2. Doble Marco Dorado y Azul Marino
        Paint goldBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        goldBorder.setColor(Color.parseColor("#FDC500"));
        goldBorder.setStyle(Paint.Style.STROKE);
        goldBorder.setStrokeWidth(16f);
        canvas.drawRect(24, 24, w - 24, h - 24, goldBorder);

        Paint navyBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        navyBorder.setColor(Color.parseColor("#0B2C5E"));
        navyBorder.setStyle(Paint.Style.STROKE);
        navyBorder.setStrokeWidth(6f);
        canvas.drawRect(44, 44, w - 44, h - 44, navyBorder);

        // 3. Encabezado Oficial del Diploma
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.parseColor("#0B2C5E"));
        titlePaint.setTextSize(32f);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("INSTITUTO CIENTÍFICO SOSTENIBLE MINERO (ICSM)", w / 2f, 110f, titlePaint);

        Paint subTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        subTitlePaint.setColor(Color.parseColor("#0077CC"));
        subTitlePaint.setTextSize(46f);
        subTitlePaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
        subTitlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🎓 DIPLOMA DE HONOR OFICIAL", w / 2f, 180f, subTitlePaint);

        Paint otorgadoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        otorgadoPaint.setColor(Color.parseColor("#475569"));
        otorgadoPaint.setTextSize(24f);
        otorgadoPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        otorgadoPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Concede el presente reconocimiento con distinción a:", w / 2f, 240f, otorgadoPaint);

        // 4. Nombre del Niño en Grande
        Paint namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setColor(Color.parseColor("#0B2C5E"));
        namePaint.setTextSize(54f);
        namePaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
        namePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(nombreNino, w / 2f, 330f, namePaint);

        Paint lineName = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineName.setColor(Color.parseColor("#FDC500"));
        lineName.setStrokeWidth(4f);
        canvas.drawLine(w * 0.2f, 350f, w * 0.8f, 350f, lineName);

        // 5. Texto de mérito científico
        Paint textMerit = new Paint(Paint.ANTI_ALIAS_FLAG);
        textMerit.setColor(Color.parseColor("#1E293B"));
        textMerit.setTextSize(26f);
        textMerit.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Por haber demostrado excelente curiosidad, valores ambientales y destreza en la especialidad de", w / 2f, 410f, textMerit);

        Paint textEsp = new Paint(Paint.ANTI_ALIAS_FLAG);
        textEsp.setColor(Color.parseColor("#16A34A"));
        textEsp.setTextSize(32f);
        textEsp.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textEsp.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(especialidad + " · Rango: " + rango, w / 2f, 460f, textEsp);

        canvas.drawText("Acompañado por el mentor: " + companero + " (" + xp + " XP acumulados)", w / 2f, 510f, textMerit);

        // 6. Sello Dorado Oficial
        float sealX = 180f;
        float sealY = h - 170f;
        Paint sealGold = new Paint(Paint.ANTI_ALIAS_FLAG);
        sealGold.setColor(Color.parseColor("#FDC500"));
        canvas.drawCircle(sealX, sealY, 80f, sealGold);

        Paint sealInner = new Paint(Paint.ANTI_ALIAS_FLAG);
        sealInner.setColor(Color.parseColor("#0B2C5E"));
        sealInner.setStyle(Paint.Style.STROKE);
        sealInner.setStrokeWidth(5f);
        canvas.drawCircle(sealX, sealY, 70f, sealInner);

        Paint sealText = new Paint(Paint.ANTI_ALIAS_FLAG);
        sealText.setColor(Color.parseColor("#0B2C5E"));
        sealText.setTextSize(14f);
        sealText.setTextAlign(Paint.Align.CENTER);
        sealText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("SELLO OFICIAL", sealX, sealY - 10, sealText);
        canvas.drawText("MINI CIENTÍFICOS", sealX, sealY + 10, sealText);
        canvas.drawText("★ ICSM ★", sealX, sealY + 30, sealText);

        // 7. Firma del Fundador
        Paint signLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        signLine.setColor(Color.parseColor("#0B2C5E"));
        signLine.setStrokeWidth(3f);
        canvas.drawLine(w - 450, h - 160, w - 90, h - 160, signLine);

        Paint signText = new Paint(Paint.ANTI_ALIAS_FLAG);
        signText.setColor(Color.parseColor("#0B2C5E"));
        signText.setTextSize(22f);
        signText.setTextAlign(Paint.Align.CENTER);
        signText.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC));
        canvas.drawText("Ing. Andy Salazar Sánchez", w - 270, h - 125, signText);

        Paint signSub = new Paint(Paint.ANTI_ALIAS_FLAG);
        signSub.setColor(Color.parseColor("#475569"));
        signSub.setTextSize(18f);
        signSub.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Fundador & Director de Investigación ICSM", w - 270, h - 95, signSub);
    }

    public static void compartirBitmapComoPng(Context context, Bitmap bitmap, String nombreArchivo) {
        if (context == null || bitmap == null) return;

        try {
            String nombreFinal = (nombreArchivo == null || nombreArchivo.trim().isEmpty()) ?
                    "Credencial_MiniCientifico_" + System.currentTimeMillis() + ".png" : nombreArchivo.trim();

            File cachePath = new File(context.getCacheDir(), "images");
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            }
            File imageFile = new File(cachePath, nombreFinal);
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    imageFile
            );

            if (contentUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.setType("image/png");

                context.startActivity(Intent.createChooser(shareIntent, "📤 Compartir Credencial / Diploma HD"));
                Toast.makeText(context, "✨ Credencial HD lista para guardar o compartir", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al guardar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}