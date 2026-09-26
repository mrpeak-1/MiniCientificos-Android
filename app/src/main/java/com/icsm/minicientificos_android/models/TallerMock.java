package com.icsm.minicientificos_android.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.icsm.minicientificos_android.R;

public class TallerMock implements Serializable {
    private int id;
    private String titulo;
    private String descripcionCorta;
    private String descripcionCompleta;
    private String modalidad; // "Presencial", "Virtual", "Híbrido"
    private String categoria; // "Ecología", "Robótica", "Química", "Física"
    private String fecha;
    private String cupos;
    private String requisitos;
    private String precio;
    private int imagenResId;

    public TallerMock(int id, String titulo, String descripcionCorta, String descripcionCompleta,
                      String modalidad, String categoria, String fecha, String cupos,
                      String requisitos, String precio, int imagenResId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionCorta = descripcionCorta;
        this.descripcionCompleta = descripcionCompleta;
        this.modalidad = modalidad;
        this.categoria = categoria;
        this.fecha = fecha;
        this.cupos = cupos;
        this.requisitos = requisitos;
        this.precio = precio;
        this.imagenResId = imagenResId;
    }

    // Getters and Setters
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

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcionCompleta() {
        return descripcionCompleta;
    }

    public void setDescripcionCompleta(String descripcionCompleta) {
        this.descripcionCompleta = descripcionCompleta;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCupos() {
        return cupos;
    }

    public void setCupos(String cupos) {
        this.cupos = cupos;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    // Datos dummy precargados
    public static List<TallerMock> getTalleresPrueba() {
        List<TallerMock> lista = new ArrayList<>();

        lista.add(new TallerMock(
                1,
                "Pequeños Botánicos y Biohuertos",
                "Aprende sobre la vida de las plantas, germinación y creación de compostaje infantil.",
                "En este taller interactivo, los niños explorarán los secretos del reino vegetal, construirán su propio mini macetero con materiales reciclados y aprenderán el arte del compost en casa.",
                "Presencial",
                "Ecología",
                "Sábados 10:00 AM - 12:00 PM",
                "8 vacantes disponibles",
                "• Edad sugerida: 6 a 12 años\n• Incluye kit de semillas y tierra viva\n• Asistir con ropa cómoda",
                "S/ 90.00 / mes",
                R.drawable.ic_flask
        ));

        lista.add(new TallerMock(
                2,
                "Robótica Educativa y Sensores Sci-Tech",
                "Armado de prototipos robóticos sencillos con motores y programación por bloques.",
                "¡Ingresa al fascinante mundo de la mecatrónica! Construiremos robots exploradores utilizando bloques interactivos, motores DC y sensores ultrasónicos.",
                "Híbrido",
                "Robótica",
                "Viernes 4:00 PM - 6:00 PM",
                "5 vacantes disponibles",
                "• Edad sugerida: 8 a 14 años\n• Traer laptop o tablet (opcional)\n• No requiere experiencia previa",
                "S/ 120.00 / mes",
                R.drawable.ic_tesla_avatar
        ));

        lista.add(new TallerMock(
                3,
                "Química Explosiva y Reacciones Espumosas",
                "Experimentos dinámicos y seguros con slime, volcanes y tintes ecológicos.",
                "Una aventura llena de color y burbujas. Los pequeños científicos aprenderán sobre estados de la materia, densidad y mezclas realizando reacciones totalmente seguras.",
                "Virtual",
                "Química",
                "Domingos 11:00 AM - 12:30 PM",
                "15 vacantes disponibles",
                "• Edad sugerida: 5 a 10 años\n• Conexión Zoom\n• Se envía guía de materiales caseros previa",
                "S/ 60.00 / mes",
                R.drawable.ic_titan_avatar
        ));

        return lista;
    }
}