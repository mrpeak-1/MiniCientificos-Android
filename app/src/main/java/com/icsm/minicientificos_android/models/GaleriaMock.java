package com.icsm.minicientificos_android.models;

import com.icsm.minicientificos_android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GaleriaMock implements Serializable {
    private int id;
    private String titulo;
    private String categoria; // "Fotocatálisis", "Minibiohuertos", "Minicompostaje", "Voluntariado"
    private String descripcion;
    private int imagenResId;
    private String fecha;

    public GaleriaMock(int id, String titulo, String categoria, String descripcion, int imagenResId, String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public int getImagenResId() { return imagenResId; }
    public String getFecha() { return fecha; }

    public static List<GaleriaMock> getGaleriaPrueba() {
        List<GaleriaMock> lista = new ArrayList<>();

        lista.add(new GaleriaMock(
                1,
                "Purificación con Fotocatálisis",
                "Fotocatálisis",
                "Experimento de degradación de contaminantes mediante luz solar y dióxido de titanio.",
                R.drawable.expe_fisi,
                "12 Octubre 2024"
        ));

        lista.add(new GaleriaMock(
                2,
                "Cosecha en Minibiohuerto",
                "Minibiohuertos",
                "Niños sembrando hortalizas orgánicas en macetas biodegradables.",
                R.drawable.hidroponia,
                "05 Noviembre 2024"
        ));

        lista.add(new GaleriaMock(
                3,
                "Taller de Vermicompostaje",
                "Minicompostaje",
                "Transformación de residuos orgánicos domésticos en compost nutritivo con lombrices.",
                R.drawable.compostaje,
                "18 Noviembre 2024"
        ));

        lista.add(new GaleriaMock(
                4,
                "Voluntariado Científico ICSM",
                "Voluntariado",
                "Jornada de divulgación en colegios públicos organizada por voluntarios universitarios.",
                R.drawable.astronomia,
                "01 Diciembre 2024"
        ));

        lista.add(new GaleriaMock(
                5,
                "Pinturas Fotocatalíticas",
                "Fotocatálisis",
                "Creación de murales ecológicos que descontaminan el aire de la comunidad.",
                R.drawable.microbiologia,
                "10 Diciembre 2024"
        ));

        lista.add(new GaleriaMock(
                6,
                "Sistema Hidropónico Escolar",
                "Minibiohuertos",
                "Instalación de tuberías NFT para cultivo eficiente de lechugas sin tierra.",
                R.drawable.energias_reno,
                "15 Diciembre 2024"
        ));

        lista.add(new GaleriaMock(
                7,
                "Compostaje en Botellas PET",
                "Minicompostaje",
                "Reutilización de botellas de plástico para monitorear el ciclo de compostaje doméstico.",
                R.drawable.reciclaje,
                "08 Enero 2025"
        ));

        lista.add(new GaleriaMock(
                8,
                "Feria de Ciencia Infantil",
                "Voluntariado",
                "Demostración interactiva de volcanes de espuma y energía solar en la plaza central.",
                R.drawable.geologia,
                "20 Enero 2025"
        ));

        return lista;
    }
}