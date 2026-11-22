package com.icsm.minicientificos_android.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;

import com.icsm.minicientificos_android.R;

import java.util.Locale;

/**
 * Motor Singleton 100% libre de crashes para audio arcade sintetizado,
 * retroalimentación háptica y efectos visuales (VFX) del Videojuego Mini Científicos.
 */
public class GameSoundAndVfxHelper {

    private static volatile GameSoundAndVfxHelper instance;

    private final Handler mainHandler;
    private ToneGenerator toneGenerator;
    private boolean sonidoActivo = true;

    private GameSoundAndVfxHelper() {
        this.mainHandler = new Handler(Looper.getMainLooper());
        inicializarToneGeneratorSeguro();
    }

    public static synchronized GameSoundAndVfxHelper getInstance() {
        if (instance == null) {
            instance = new GameSoundAndVfxHelper();
        }
        return instance;
    }

    public static synchronized GameSoundAndVfxHelper getInstance(Context context) {
        return getInstance();
    }

    private void inicializarToneGeneratorSeguro() {
        try {
            if (toneGenerator == null) {
                toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
            }
        } catch (Throwable ignored) {
            toneGenerator = null;
        }
    }

    public boolean isSonidoActivo() {
        return sonidoActivo;
    }

    public void setSonidoActivo(boolean activo) {
        this.sonidoActivo = activo;
    }

    public void toggleSonido() {
        this.sonidoActivo = !this.sonidoActivo;
        if (this.sonidoActivo) {
            playClick();
        }
    }

    // =========================================================================
    // EFECTOS DE SONIDO ARCADE + HÁPTICA
    // =========================================================================

    /**
     * Tono corto ágil para botones y selecciones (TONE_PROP_BEEP, 40ms).
     */
    public void playClick() {
        if (!sonidoActivo) return;
        reproducirTonoSeguro(ToneGenerator.TONE_PROP_BEEP, 40);
    }

    /**
     * Secuencia alegre de 2 tonos ascendentes + vibración suave de 50ms.
     */
    public void playRespuestaCorrecta(Context ctx) {
        if (sonidoActivo) {
            if (!reproducirRawSiExiste(ctx, "sfx_correcto")) {
                reproducirTonoSeguro(ToneGenerator.TONE_PROP_ACK, 90);
                mainHandler.postDelayed(() -> {
                    if (sonidoActivo) {
                        reproducirTonoSeguro(ToneGenerator.TONE_PROP_BEEP2, 130);
                    }
                }, 110);
            }
        }
        vibrarSimple(ctx, 50L);
    }

    /**
     * Tono grave de error (TONE_SUP_ERROR, 180ms) + doble vibración de alerta.
     */
    public void playRespuestaIncorrecta(Context ctx) {
        if (sonidoActivo) {
            if (!reproducirRawSiExiste(ctx, "sfx_incorrecto")) {
                reproducirTonoSeguro(ToneGenerator.TONE_SUP_ERROR, 180);
            }
        }
        vibrarPatron(ctx, new long[]{0L, 65L, 60L, 90L});
    }

    /**
     * Efecto especial ascendente de poder científico al activar un comodín.
     */
    public void playComodinActivado() {
        if (!sonidoActivo) return;
        reproducirTonoSeguro(ToneGenerator.TONE_DTMF_5, 70);
        mainHandler.postDelayed(() -> {
            if (sonidoActivo) {
                reproducirTonoSeguro(ToneGenerator.TONE_DTMF_8, 70);
            }
        }, 85);
        mainHandler.postDelayed(() -> {
            if (sonidoActivo) {
                reproducirTonoSeguro(ToneGenerator.TONE_PROP_BEEP2, 120);
            }
        }, 170);
    }

    /**
     * Fanfarria de 3 tonos triunfales + vibración de celebración.
     */
    public void playVictoriaNivel(Context ctx) {
        if (sonidoActivo) {
            if (!reproducirRawSiExiste(ctx, "sfx_victoria")) {
                reproducirTonoSeguro(ToneGenerator.TONE_DTMF_6, 95);
                mainHandler.postDelayed(() -> {
                    if (sonidoActivo) {
                        reproducirTonoSeguro(ToneGenerator.TONE_DTMF_9, 95);
                    }
                }, 115);
                mainHandler.postDelayed(() -> {
                    if (sonidoActivo) {
                        reproducirTonoSeguro(ToneGenerator.TONE_PROP_BEEP2, 220);
                    }
                }, 230);
            }
        }
        vibrarPatron(ctx, new long[]{0L, 60L, 50L, 60L, 50L, 140L});
    }

    private void reproducirTonoSeguro(int toneType, int durationMs) {
        try {
            if (toneGenerator == null) {
                inicializarToneGeneratorSeguro();
            }
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
        } catch (Throwable ignored) {
            // Protege contra errores de hardware de audio o emuladores sin salida PCM
        }
    }

    private boolean reproducirRawSiExiste(Context ctx, String rawName) {
        if (ctx == null || rawName == null || rawName.isEmpty()) {
            return false;
        }
        try {
            int rawId = ctx.getResources().getIdentifier(rawName, "raw", ctx.getPackageName());
            if (rawId == 0) {
                return false;
            }
            MediaPlayer mp = MediaPlayer.create(ctx.getApplicationContext(), rawId);
            if (mp == null) {
                return false;
            }
            mp.setOnCompletionListener(MediaPlayer::release);
            mp.setOnErrorListener((player, what, extra) -> {
                player.release();
                return true;
            });
            mp.start();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private void vibrarSimple(Context ctx, long duracionMs) {
        Vibrator vibrator = obtenerVibrador(ctx);
        if (vibrator == null) return;
        try {
            if (!vibrator.hasVibrator()) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duracionMs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(duracionMs);
            }
        } catch (Throwable ignored) {
            // Ignora silenciosamente si no existe permiso o soporte háptico
        }
    }

    private void vibrarPatron(Context ctx, long[] pattern) {
        Vibrator vibrator = obtenerVibrador(ctx);
        if (vibrator == null) return;
        try {
            if (!vibrator.hasVibrator()) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
            } else {
                vibrator.vibrate(pattern, -1);
            }
        } catch (Throwable ignored) {
            // Ignora silenciosamente si no existe permiso o soporte háptico
        }
    }

    private Vibrator obtenerVibrador(Context ctx) {
        if (ctx == null) return null;
        try {
            Context appCtx = ctx.getApplicationContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager manager = (VibratorManager) appCtx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                return manager != null ? manager.getDefaultVibrator() : null;
            } else {
                return (Vibrator) appCtx.getSystemService(Context.VIBRATOR_SERVICE);
            }
        } catch (Throwable ignored) {
            return null;
        }
    }

    public void liberarRecursos() {
        try {
            mainHandler.removeCallbacksAndMessages(null);
            if (toneGenerator != null) {
                toneGenerator.release();
                toneGenerator = null;
            }
        } catch (Throwable ignored) {
        }
    }

    // =========================================================================
    // EFECTOS VISUALES (VFX) Y RESOLUCIÓN DINÁMICA DE PERSONAJES
    // =========================================================================

    /**
     * Rebote elástico de celebración con OvershootInterpolator.
     */
    public static void animarPulsoExito(View v) {
        if (v == null) return;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1f, 1.14f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1f, 1.14f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(380);
        set.setInterpolator(new OvershootInterpolator(2.2f));
        set.start();
    }

    /**
     * Sacudida horizontal de error con CycleInterpolator(3).
     */
    public static void animarSacudidaError(View v) {
        if (v == null) return;
        ObjectAnimator shake = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, 0f, 16f);
        shake.setDuration(360);
        shake.setInterpolator(new CycleInterpolator(3f));
        shake.start();
    }

    /**
     * Devuelve el drawable de Titan o Tesla según el estado del juego:
     * "FELIZ", "PENSANDO", "CELEBRANDO", "ANIMANDO".
     * Utiliza getIdentifier() para garantizar cero errores de compilación.
     */
    public static int obtenerDrawablePersonajeEstado(Context ctx, String personaje, String estado) {
        if (ctx == null) {
            return R.drawable.titan_cientifico;
        }
        String p = (personaje == null) ? "TITAN" : personaje.trim().toUpperCase(Locale.ROOT);
        String e = (estado == null) ? "FELIZ" : estado.trim().toUpperCase(Locale.ROOT);

        boolean esTesla = p.contains("TESLA") || p.contains("TESLITA");
        String[] candidatos;

        if (esTesla) {
            switch (e) {
                case "CELEBRANDO":
                    candidatos = new String[]{"teslita_fullbody", "tesla_fullbody", "teslita_png"};
                    break;
                case "PENSANDO":
                    candidatos = new String[]{"teslita_head", "tesla_head", "teslita_png"};
                    break;
                case "ANIMANDO":
                    candidatos = new String[]{"tesla_fullbody", "teslita_png", "personajes"};
                    break;
                case "FELIZ":
                default:
                    candidatos = new String[]{"teslita_png", "teslita_head", "tesla_head"};
                    break;
            }
        } else {
            switch (e) {
                case "CELEBRANDO":
                    candidatos = new String[]{"titan_astronauta", "titan_cientifico", "personajes"};
                    break;
                case "PENSANDO":
                    candidatos = new String[]{"titan_cientifico", "titan_astronauta", "personajes"};
                    break;
                case "ANIMANDO":
                    candidatos = new String[]{"personajes", "titan_astronauta", "titan_cientifico"};
                    break;
                case "FELIZ":
                default:
                    candidatos = new String[]{"titan_cientifico", "titan_astronauta", "personajes"};
                    break;
            }
        }

        String pkg = ctx.getPackageName();
        for (String nombre : candidatos) {
            int resId = ctx.getResources().getIdentifier(nombre, "drawable", pkg);
            if (resId != 0) {
                return resId;
            }
        }
        return esTesla ? R.drawable.teslita_png : R.drawable.titan_cientifico;
    }
}
