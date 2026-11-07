package com.icsm.minicientificos_android.models;

public class PildoraTallerHome {
    private String nombre;
    private String modalidad;
    private String modalidadColorHex;
    private String bgHexColor;
    private String borderHexColor;
    private String iconoEmoji;

    public PildoraTallerHome(String nombre, String modalidad, String modalidadColorHex,
                             String bgHexColor, String borderHexColor, String iconoEmoji) {
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.modalidadColorHex = modalidadColorHex;
        this.bgHexColor = bgHexColor;
        this.borderHexColor = borderHexColor;
        this.iconoEmoji = iconoEmoji;
    }

    public String getNombre() {
        return nombre;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getModalidadColorHex() {
        return modalidadColorHex;
    }

    public String getBgHexColor() {
        return bgHexColor;
    }

    public String getBorderHexColor() {
        return borderHexColor;
    }

    public String getIconoEmoji() {
        return iconoEmoji;
    }

    // Compatibilidad con código anterior si usaba getHexColor()
    public String getHexColor() {
        return borderHexColor;
    }
}