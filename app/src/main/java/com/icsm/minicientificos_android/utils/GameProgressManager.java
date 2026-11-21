package com.icsm.minicientificos_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.models.game.BancoPreguntasCatalog;
import com.icsm.minicientificos_android.models.game.MedallaCientifica;
import com.icsm.minicientificos_android.models.game.NivelJuego;

import java.util.List;

public class GameProgressManager {

    private static final String PREF_NAME = "mini_cientificos_game_prefs";
    private static final String KEY_XP_TOTAL = "xp_total";
    private static final String KEY_RECORD_PUNTAJE = "record_puntaje";
    private static final String KEY_NOMBRE_AGENTE = "nombre_agente";
    private static final String KEY_ESTRELLAS_NIVEL_PREFIX = "estrellas_nivel_";

    private static GameProgressManager instance;
    private final SharedPreferences prefs;

    private GameProgressManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized GameProgressManager getInstance(Context context) {
        if (instance == null) {
            instance = new GameProgressManager(context);
        }
        return instance;
    }

    // ==========================================
    // GESTIÓN DE XP Y PUNTUACIÓN
    // ==========================================

    public int getXpTotal() {
        return prefs.getInt(KEY_XP_TOTAL, 0);
    }

    public void sumarXp(int xpGanado) {
        if (xpGanado <= 0) return;
        int nuevoXp = getXpTotal() + xpGanado;
        prefs.edit().putInt(KEY_XP_TOTAL, nuevoXp).apply();
    }

    public int getRecordPuntaje() {
        return prefs.getInt(KEY_RECORD_PUNTAJE, 0);
    }

    public void actualizarRecord(int nuevoPuntaje) {
        if (nuevoPuntaje > getRecordPuntaje()) {
            prefs.edit().putInt(KEY_RECORD_PUNTAJE, nuevoPuntaje).apply();
        }
    }

    // ==========================================
    // NIVELES Y ESTRELLAS
    // ==========================================

    public int getEstrellasNivel(int nivelId) {
        return prefs.getInt(KEY_ESTRELLAS_NIVEL_PREFIX + nivelId, 0);
    }

    public void guardarEstrellasNivel(int nivelId, int estrellas) {
        int estrellasActuales = getEstrellasNivel(nivelId);
        if (estrellas > estrellasActuales) {
            prefs.edit().putInt(KEY_ESTRELLAS_NIVEL_PREFIX + nivelId, Math.min(3, estrellas)).apply();
        }
    }

    // ==========================================
    // AGENTE Y RANGO CIENTÍFICO
    // ==========================================

    public String getNombreAgenteGuardado() {
        return prefs.getString(KEY_NOMBRE_AGENTE, "Mini Científico(a)");
    }

    public void setNombreAgenteGuardado(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Mini Científico(a)";
        }
        prefs.edit().putString(KEY_NOMBRE_AGENTE, nombre.trim()).apply();
    }

    public String getRangoCientificoActual() {
        int xp = getXpTotal();
        if (xp >= 1500) {
            return "Maestro Inventor ICSM";
        } else if (xp >= 800) {
            return "Científico Estelar";
        } else if (xp >= 300) {
            return "Explorador de Laboratorio";
        } else {
            return "Cadete Curioso";
        }
    }

    // ==========================================
    // MEDALLAS COLECCIONABLES
    // ==========================================

    public List<MedallaCientifica> getMedallasConEstadoActual() {
        List<MedallaCientifica> catalog = BancoPreguntasCatalog.obtenerCatalogoMedallas();
        int xpActual = getXpTotal();

        for (MedallaCientifica med : catalog) {
            if (xpActual >= med.getXpRequerido()) {
                med.setDesbloqueada(true);
            }
        }
        return catalog;
    }

    // ==========================================
    // ACTIVACIÓN AUTOMÁTICA DE CUPONES POR VICTORIA
    // ==========================================

    public String desbloquearCuponPorVictoria(int nivelId) {
        int estrellas = getEstrellasNivel(nivelId);
        if (estrellas < 2) {
            return null;
        }

        List<NivelJuego> niveles = BancoPreguntasCatalog.obtenerNiveles();
        for (NivelJuego nivel : niveles) {
            if (nivel.getId() == nivelId) {
                String cupon = nivel.getCodigoCuponPremio();
                if (cupon != null && !cupon.isEmpty()) {
                    CartManager.getInstance().aplicarCupon(cupon);
                    return cupon;
                }
            }
        }
        return null;
    }
}