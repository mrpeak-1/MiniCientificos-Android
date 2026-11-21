package com.icsm.minicientificos_android.models.game;

import java.io.Serializable;

public class NivelJuego implements Serializable {

    private int id;
    private String numeroMundo;
    private String titulo;
    private String descripcion;
    private String iconoEmoji;
    private String colorHex;
    private int xpRequeridoParaDesbloquear;
    private String codigoCuponPremio;

    public NivelJuego() {
    }

    public NivelJuego(int id, String numeroMundo, String titulo, String descripcion,
                      String iconoEmoji, String colorHex, int xpRequeridoParaDesbloquear,
                      String codigoCuponPremio) {
        this.id = id;
        this.numeroMundo = numeroMundo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.iconoEmoji = iconoEmoji;
        this.colorHex = colorHex;
        this.xpRequeridoParaDesbloquear = xpRequeridoParaDesbloquear;
        this.codigoCuponPremio = codigoCuponPremio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroMundo() {
        return numeroMundo;
    }

    public void setNumeroMundo(String numeroMundo) {
        this.numeroMundo = numeroMundo;
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

    public int getXpRequeridoParaDesbloquear() {
        return xpRequeridoParaDesbloquear;
    }

    public void setXpRequeridoParaDesbloquear(int xpRequeridoParaDesbloquear) {
        this.xpRequeridoParaDesbloquear = xpRequeridoParaDesbloquear;
    }

    public String getCodigoCuponPremio() {
        return codigoCuponPremio;
    }

    public void setCodigoCuponPremio(String codigoCuponPremio) {
        this.codigoCuponPremio = codigoCuponPremio;
    }
}