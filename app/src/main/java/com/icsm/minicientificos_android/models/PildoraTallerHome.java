package com.icsm.minicientificos_android.models;

public class PildoraTallerHome {
    private String nombre;
    private String iconoEmoji;
    private String hexColor;

    public PildoraTallerHome(String nombre, String iconoEmoji, String hexColor) {
        this.nombre = nombre;
        this.iconoEmoji = iconoEmoji;
        this.hexColor = hexColor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIconoEmoji() {
        return iconoEmoji;
    }

    public String getHexColor() {
        return hexColor;
    }
}