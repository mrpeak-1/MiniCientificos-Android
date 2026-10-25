package com.icsm.minicientificos_android.models;

import com.icsm.minicientificos_android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiciosCatalogData {

    public static List<ServicioCientifico> getServicios() {
        List<ServicioCientifico> list = new ArrayList<>();

        list.add(new ServicioCientifico(
                "talleres",
                "Talleres Científicos",
                "¡Aprende jugando con experimentos divertidos! Cursos presenciales y virtuales para pequeños científicos como tú. ¡La ciencia nunca fue tan emocionante!",
                "Nuestros talleres científicos están diseñados para despertar la curiosidad natural de los niños mediante experimentos prácticos e interactivos.",
                R.drawable.ic_flask,
                "#00A8E8",
                Arrays.asList(
                        "Experimentos 100% prácticos y seguros",
                        "Materiales incluidos para cada participante",
                        "Desarrollo del pensamiento crítico y STEM",
                        "Instructores capacitados y apasionados",
                        "Entrega de Certificado de Pequeño Científico"
                ),
                "5 a 13 años",
                "1h 30min por sesión",
                "En colegios, sedes o domicilio",
                "Hola Mini Científicos, deseo cotizar el servicio de Talleres Científicos."
        ));

        list.add(new ServicioCientifico(
                "eventos",
                "Eventos Científicos",
                "¡Celebramos la ciencia con eventos increíbles! Demostraciones, shows y actividades especiales donde tú eres el protagonista. ¡Cada evento es una nueva aventura!",
                "Convertimos tus fiestas y eventos corporativos o escolares en un espectáculo científico inolvidable lleno de reacciones sorprendentes y aprendizaje.",
                R.drawable.ic_nav_workshops,
                "#FF2A85",
                Arrays.asList(
                        "Show interactivo con nitrógeno líquido y espuma gigante",
                        "Cumpleaños temáticos inolvidables",
                        "Activaciones escolares y municipales",
                        "Involucramiento activo de todos los invitados",
                        "Recuerdos y experimentos para llevar a casa"
                ),
                "Todas las edades",
                "2 horas de animación temática",
                "Eventos privados, salones o colegios",
                "Hola Mini Científicos, deseo cotizar un Evento Científico especial."
        ));

        list.add(new ServicioCientifico(
                "ferias",
                "Feria Científica",
                "¡Participa en nuestra feria de ciencias! Exhibe tus proyectos, descubre inventos asombrosos y comparte tus ideas con otros pequeños genios. ¡Todos somos científicos!",
                "Diseñamos y organizamos ferias de ciencia integrales para instituciones educativas, fomentando la investigación y la creatividad estudiantil.",
                R.drawable.ic_nav_gallery,
                "#FDC500",
                Arrays.asList(
                        "Asesoría técnica y pedagógica en proyectos escolares",
                        "Stands interactivos y módulos de demostración",
                        "Jurados científicos calificados",
                        "Trofeos, medallas y diplomas para participantes",
                        "Fomento directo de la cultura STEM en la escuela"
                ),
                "Primaria y Secundaria",
                "Jornada completa o media jornada",
                "Instituciones educativas y auditorios",
                "Hola Mini Científicos, deseo información para organizar una Feria Científica."
        ));

        return list;
    }
}