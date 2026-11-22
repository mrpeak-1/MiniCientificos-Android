package com.icsm.minicientificos_android.models;

import android.content.Context;

import com.icsm.minicientificos_android.R;

import java.io.Serializable;

public class GaleriaFoto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String descripcionActividad;
    private String categoriaPrincipal; // "TALLERES", "EVENTOS", "FERIAS"
    private String subgrupo;           // "Minicompostaje", "Tratamiento de Agua", "Fotocatálisis", "Minibiohuerto", "Punto Ecológico"
    private String fechaLugar;
    private String drawableNamePreferido;
    private String drawableNameFallback;

    public GaleriaFoto() {
    }

    public GaleriaFoto(int id,
                       String titulo,
                       String descripcionActividad,
                       String categoriaPrincipal,
                       String subgrupo,
                       String fechaLugar,
                       String drawableNamePreferido,
                       String drawableNameFallback) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionActividad = descripcionActividad;
        this.categoriaPrincipal = categoriaPrincipal;
        this.subgrupo = subgrupo;
        this.fechaLugar = fechaLugar;
        this.drawableNamePreferido = drawableNamePreferido;
        this.drawableNameFallback = drawableNameFallback;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public String getCategoriaPrincipal() {
        return categoriaPrincipal;
    }

    public void setCategoriaPrincipal(String categoriaPrincipal) {
        this.categoriaPrincipal = categoriaPrincipal;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getFechaLugar() {
        return fechaLugar;
    }

    public void setFechaLugar(String fechaLugar) {
        this.fechaLugar = fechaLugar;
    }

    public String getDrawableNamePreferido() {
        return drawableNamePreferido;
    }

    public void setDrawableNamePreferido(String drawableNamePreferido) {
        this.drawableNamePreferido = drawableNamePreferido;
    }

    public String getDrawableNameFallback() {
        return drawableNameFallback;
    }

    public void setDrawableNameFallback(String drawableNameFallback) {
        this.drawableNameFallback = drawableNameFallback;
    }

    /**
     * Resuelve el drawable de manera dinámica y segura usando getIdentifier(),
     * priorizando el nombre oficial y recurriendo al fallback si no estuviera presente.
     */
    public int resolveDrawableResId(Context context) {
        if (context == null) {
            return R.drawable.expe_fisi;
        }
        String pkg = context.getPackageName();
        if (drawableNamePreferido != null && !drawableNamePreferido.trim().isEmpty()) {
            int resId = context.getResources().getIdentifier(drawableNamePreferido.trim(), "drawable", pkg);
            if (resId != 0) {
                return resId;
            }
        }
        if (drawableNameFallback != null && !drawableNameFallback.trim().isEmpty()) {
            int fallbackId = context.getResources().getIdentifier(drawableNameFallback.trim(), "drawable", pkg);
            if (fallbackId != 0) {
                return fallbackId;
            }
        }
        return R.drawable.expe_fisi;
    }
}
