package com.icsm.minicientificos_android.models;

import java.util.List;

public class ServiceItem {
    private String id;
    private String titulo;
    private String descripcionCorta;
    private String descripcionExtendida;
    private int iconoResId;
    private String colorAcentoHex;
    private List<String> beneficiosList;
    private String categoriaGaleriaAsociada;
    private String mensajeWhatsApp;

    public ServiceItem(String id, String titulo, String descripcionCorta, String descripcionExtendida, int iconoResId, String colorAcentoHex, List<String> beneficiosList, String categoriaGaleriaAsociada, String mensajeWhatsApp) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionCorta = descripcionCorta;
        this.descripcionExtendida = descripcionExtendida;
        this.iconoResId = iconoResId;
        this.colorAcentoHex = colorAcentoHex;
        this.beneficiosList = beneficiosList;
        this.categoriaGaleriaAsociada = categoriaGaleriaAsociada;
        this.mensajeWhatsApp = mensajeWhatsApp;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public String getDescripcionExtendida() { return descripcionExtendida; }
    public int getIconoResId() { return iconoResId; }
    public String getColorAcentoHex() { return colorAcentoHex; }
    public List<String> getBeneficiosList() { return beneficiosList; }
    public String getCategoriaGaleriaAsociada() { return categoriaGaleriaAsociada; }
    public String getMensajeWhatsApp() { return mensajeWhatsApp; }
}