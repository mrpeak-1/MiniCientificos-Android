package com.icsm.minicientificos_android.models;

import java.io.Serializable;

public class Taller implements Serializable {
    private String id;
    private String tituloCorto;
    private String posterLinea1;
    private String posterLinea2;
    private String categoria;
    private String modalidad;
    private String descripcionCorta;
    private String objetivo;
    private String[] temario;
    private String[] materialesIncluidos;
    private String horario;
    private String edadRecomendada;
    private String duracion;
    private String vacantes;
    private double precio;
    private String drawableName;
    private String videoRawName;

    public Taller(String id, String tituloCorto, String posterLinea1, String posterLinea2, String categoria,
                  String modalidad, String descripcionCorta, String objetivo, String[] temario,
                  String[] materialesIncluidos, String horario, String edadRecomendada, String duracion,
                  String vacantes, double precio, String drawableName, String videoRawName) {
        this.id = id;
        this.tituloCorto = tituloCorto;
        this.posterLinea1 = posterLinea1;
        this.posterLinea2 = posterLinea2;
        this.categoria = categoria;
        this.modalidad = modalidad;
        this.descripcionCorta = descripcionCorta;
        this.objetivo = objetivo;
        this.temario = temario;
        this.materialesIncluidos = materialesIncluidos;
        this.horario = horario;
        this.edadRecomendada = edadRecomendada;
        this.duracion = duracion;
        this.vacantes = vacantes;
        this.precio = precio;
        this.drawableName = drawableName;
        this.videoRawName = videoRawName;
    }

    public String getId() { return id; }
    public String getTitulo() { return tituloCorto; } // Backward compatibility
    public String getTituloCorto() { return tituloCorto; }
    public String getPosterLinea1() { return posterLinea1; }
    public String getPosterLinea2() { return posterLinea2; }
    public String getCategoria() { return categoria; }
    public String getModalidad() { return modalidad; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public String getObjetivo() { return objetivo; }
    public String[] getTemario() { return temario; }
    public String[] getMaterialesIncluidos() { return materialesIncluidos; }
    public String getHorario() { return horario; }
    public String getEdadRecomendada() { return edadRecomendada; }
    public String getDuracion() { return duracion; }
    public String getVacantes() { return vacantes; }
    public double getPrecio() { return precio; }
    public String getDrawableName() { return drawableName; }
    public String getImageName() { return drawableName; } // Backward compatibility
    public String getVideoUrl() { return videoRawName; } // Backward compatibility
    public String getVideoRawName() { return videoRawName; }
}