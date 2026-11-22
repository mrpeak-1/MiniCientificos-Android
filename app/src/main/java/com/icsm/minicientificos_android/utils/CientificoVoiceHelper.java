package com.icsm.minicientificos_android.utils;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Motor de Voz Sintetizada en Español para Titan y Tesla.
 * Utiliza android.speech.tts.TextToSpeech con ajuste de tono (pitch) y velocidad (rate)
 * para otorgar personalidad única a los héroes científicos del Instituto ICSM.
 */
public class CientificoVoiceHelper implements TextToSpeech.OnInitListener {

    private static final String TAG = "CientificoVoiceHelper";
    private static volatile CientificoVoiceHelper instance;

    private TextToSpeech tts;
    private boolean ttsListo = false;
    private final Context appContext;

    private CientificoVoiceHelper(Context context) {
        this.appContext = context.getApplicationContext();
        try {
            this.tts = new TextToSpeech(appContext, this);
        } catch (Throwable e) {
            Log.e(TAG, "Error al inicializar TextToSpeech: " + e.getMessage());
            this.ttsListo = false;
        }
    }

    public static synchronized CientificoVoiceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CientificoVoiceHelper(context);
        }
        return instance;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && tts != null) {
            Locale localePeru = new Locale("es", "PE");
            int result = tts.setLanguage(localePeru);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Locale localeEspanol = new Locale("es", "ES");
                result = tts.setLanguage(localeEspanol);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
            ttsListo = true;
            Log.d(TAG, "TextToSpeech inicializado correctamente en Español.");
        } else {
            ttsListo = false;
            Log.e(TAG, "Fallo al inicializar el servicio de TextToSpeech.");
        }
    }

    /**
     * Voz juvenil de Titan (Gato Astronauta entusiasta): Pitch 1.18f, Rate 1.05f.
     */
    public void hablarComoTitan(String texto) {
        if (!ttsListo || tts == null || texto == null || texto.trim().isEmpty()) {
            return;
        }
        try {
            detenerVoz();
            tts.setPitch(1.18f);
            tts.setSpeechRate(1.05f);

            String textoLimpio = limpiarEmojisYSimbolos(texto);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(textoLimpio, TextToSpeech.QUEUE_FLUSH, null, "titan_voice");
            } else {
                tts.speak(textoLimpio, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error en hablarComoTitan: " + e.getMessage());
        }
    }

    /**
     * Voz alegre de Tesla (Duende Científico andino): Pitch 1.30f, Rate 1.08f.
     */
    public void hablarComoTesla(String texto) {
        if (!ttsListo || tts == null || texto == null || texto.trim().isEmpty()) {
            return;
        }
        try {
            detenerVoz();
            tts.setPitch(1.30f);
            tts.setSpeechRate(1.08f);

            String textoLimpio = limpiarEmojisYSimbolos(texto);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(textoLimpio, TextToSpeech.QUEUE_FLUSH, null, "tesla_voice");
            } else {
                tts.speak(textoLimpio, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error en hablarComoTesla: " + e.getMessage());
        }
    }

    /**
     * Lectura directa con tono neutro.
     */
    public void hablar(String texto) {
        if (!ttsListo || tts == null || texto == null || texto.trim().isEmpty()) {
            return;
        }
        try {
            detenerVoz();
            tts.setPitch(1.0f);
            tts.setSpeechRate(1.0f);

            String textoLimpio = limpiarEmojisYSimbolos(texto);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(textoLimpio, TextToSpeech.QUEUE_FLUSH, null, "generic_voice");
            } else {
                tts.speak(textoLimpio, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error en hablar: " + e.getMessage());
        }
    }

    /**
     * Detiene inmediatamente cualquier locución en curso.
     */
    public void detenerVoz() {
        try {
            if (tts != null && tts.isSpeaking()) {
                tts.stop();
            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * Remueve emojis, símbolos Unicode ruidosos y formatos extraños para una locución fluida.
     */
    private String limpiarEmojisYSimbolos(String texto) {
        if (texto == null) return "";
        // Expresión regular que remueve símbolos y emojis comunes de unicode
        return texto.replaceAll("[\\p{So}\\p{Cn}\\u2700-\\u27BF\\uE000-\\uF8FF]", "")
                .replaceAll("[\"«»]", "")
                .trim();
    }

    /**
     * Libera los recursos de TextToSpeech para evitar memory leaks.
     */
    public void release() {
        try {
            detenerVoz();
            if (tts != null) {
                tts.shutdown();
                tts = null;
            }
            ttsListo = false;
            instance = null;
        } catch (Throwable ignored) {
        }
    }
}
