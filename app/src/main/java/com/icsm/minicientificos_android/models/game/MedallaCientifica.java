package com.icsm.minicientificos_android.models.game;

import java.io.Serializable;

public class MedallaCientifica implements Serializable {

    private String id;
    private String titulo;
    private String descripcion;
    private String iconoEmoji;
    private String colorHex;
    private int xpRequerido;
    private boolean desbloqueada;

    public MedallaCientifica() {
    }

    public MedallaCientifica(String id, String titulo, String descripcion, String iconoEmoji,
                             String colorHex, int xpRequerido, boolean desbloqueada) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.iconoEmoji = iconoEmoji;
        this.colorHex = colorHex;
        this.xpRequerido = xpRequerido;
        this.desbloqueada = desbloqueada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIconoEmoji() {
        return iconoEmoji;
    }

    public void setIconoEmoji(String iconoEmoji) {
        this.iconoEmoji = iconoEmoji;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public int getXpRequerido() {
        return xpRequerido;
    }

    public void setXpRequerido(int xpRequerido) {
        this.xpRequerido = xpRequerido;
    }

    public boolean isDesbloqueada() {
        return desbloqueada;
    }

    public void setDesbloqueada(boolean desbloqueada) {
        this.desbloqueada = desbloqueada;
    }
}