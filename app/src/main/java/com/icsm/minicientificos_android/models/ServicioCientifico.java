package com.icsm.minicientificos_android.models;

import java.util.List;

public class ServicioCientifico {
    private String id;
    private String titulo;
    private String descripcionCorta;
    private String descripcionExtendida;
    private int iconoResId;
    private String colorAcentoHex;
    private List<String> beneficiosList;
    private String edades;
    private String duracion;
    private String modalidad;
    private String mensajeWhatsApp;

    public ServicioCientifico(String id, String titulo, String descripcionCorta, String descripcionExtendida,
                              int iconoResId, String colorAcentoHex, List<String> beneficiosList,
                              String edades, String duracion, String modalidad, String mensajeWhatsApp) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionCorta = descripcionCorta;
        this.descripcionExtendida = descripcionExtendida;
        this.iconoResId = iconoResId;
        this.colorAcentoHex = colorAcentoHex;
        this.beneficiosList = beneficiosList;
        this.edades = edades;
        this.duracion = duracion;
        this.modalidad = modalidad;
        this.mensajeWhatsApp = mensajeWhatsApp;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public String getDescripcionExtendida() { return descripcionExtendida; }
    public int getIconoResId() { return iconoResId; }
    public String getColorAcentoHex() { return colorAcentoHex; }
    public List<String> getBeneficiosList() { return beneficiosList; }
    public String getEdades() { return edades; }
    public String getDuracion() { return duracion; }
    public String getModalidad() { return modalidad; }
    public String getMensajeWhatsApp() { return mensajeWhatsApp; }
}