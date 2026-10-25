package com.icsm.minicientificos_android.models;

import java.io.Serializable;

public class Taller implements Serializable {
    private final int id;
    private final String titulo;
    private final String categoria;
    private final String modalidad;
    private final String descripcionCorta;
    private final String objetivo;
    private final String[] temario;
    private final String[] materialesIncluidos;
    private final String horario;
    private final String vacantes;
    private final double precio;
    private final String imageName;
    private final String videoUrl; // Puede ser URL http o nombre de resource raw

    public Taller(int id, String titulo, String categoria, String modalidad, String descripcionCorta,
                  String objetivo, String[] temario, String[] materialesIncluidos, String horario,
                  String vacantes, double precio, String imageName, String videoUrl) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.modalidad = modalidad;
        this.descripcionCorta = descripcionCorta;
        this.objetivo = objetivo;
        this.temario = temario;
        this.materialesIncluidos = materialesIncluidos;
        this.horario = horario;
        this.vacantes = vacantes;
        this.precio = precio;
        this.imageName = imageName;
        this.videoUrl = videoUrl;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getCategoria() { return categoria; }
    public String getModalidad() { return modalidad; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public String getObjetivo() { return objetivo; }
    public String[] getTemario() { return temario; }
    public String[] getMaterialesIncluidos() { return materialesIncluidos; }
    public String getHorario() { return horario; }
    public String getVacantes() { return vacantes; }
    public double getPrecio() { return precio; }
    public String getImageName() { return imageName; }
    public String getVideoUrl() { return videoUrl; }
}