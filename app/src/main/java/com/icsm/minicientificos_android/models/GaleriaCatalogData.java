package com.icsm.minicientificos_android.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GaleriaCatalogData {

    public static final String CAT_TODO = "TODO";
    public static final String CAT_TALLERES = "TALLERES";
    public static final String CAT_EVENTOS = "EVENTOS";
    public static final String CAT_FERIAS = "FERIAS";

    public static final String SUB_TODOS = "TODOS";
    public static final String SUB_MINICOMPOSTAJE = "Minicompostaje";
    public static final String SUB_TRATAMIENTO_AGUA = "Tratamiento de Agua";
    public static final String SUB_FOTOCATALISIS = "Fotocatálisis";
    public static final String SUB_MINIBIOHUERTO = "Minibiohuerto";
    public static final String SUB_PUNTO_ECOLOGICO = "Punto Ecológico";

    public static class StoryItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String etiquetaCorta;
        private final String emoji;
        private final String subgrupoObjetivo;
        private final String drawablePortada;
        private final String drawableFallback;
        private final String mascotaGuia; // "TITAN" o "TESLA"
        private final String colorHex;

        public StoryItem(String etiquetaCorta,
                         String emoji,
                         String subgrupoObjetivo,
                         String drawablePortada,
                         String drawableFallback,
                         String mascotaGuia,
                         String colorHex) {
            this.etiquetaCorta = etiquetaCorta;
            this.emoji = emoji;
            this.subgrupoObjetivo = subgrupoObjetivo;
            this.drawablePortada = drawablePortada;
            this.drawableFallback = drawableFallback;
            this.mascotaGuia = mascotaGuia;
            this.colorHex = colorHex;
        }

        public String getEtiquetaCorta() {
            return etiquetaCorta;
        }

        public String getEmoji() {
            return emoji;
        }

        public String getSubgrupoObjetivo() {
            return subgrupoObjetivo;
        }

        public String getDrawablePortada() {
            return drawablePortada;
        }

        public String getDrawableFallback() {
            return drawableFallback;
        }

        public String getMascotaGuia() {
            return mascotaGuia;
        }

        public String getColorHex() {
            return colorHex;
        }
    }

    /**
     * Devuelve el catálogo oficial completo de las 43 fotografías científicas
     * organizadas en 3 categorías principales y 5 subgrupos temáticos.
     */
    public static List<GaleriaFoto> obtenerTodasLasFotos() {
        List<GaleriaFoto> fotos = new ArrayList<>(43);

        // =========================================================================
        // CATEGORÍA "TALLERES" (19 FOTOS): 9 Minicompostaje + 10 Tratamiento de Agua
        // =========================================================================

        // --- Subgrupo 1: Minicompostaje (IDs 1 a 9) ---
        String[] titulosCompost = {
                "Preparación del Sustrato Orgánico",
                "Conociendo Lombrices Californianas",
                "Capas de Compost en Acción",
                "Observación de Humus Natural",
                "Reciclaje de Residuos Vegetales",
                "Manos a la Tierra en Equipo",
                "Control de Humedad del Compost",
                "Aprendiendo el Ciclo de Nutrientes",
                "Pequeños Guardianes del Suelo"
        };
        String[] descCompost = {
                "Los niños clasifican cáscaras y restos vegetales para iniciar su propio minicompostador casero.",
                "Nuestros mini científicos conocen el rol ecológico de la lombriz roja californiana en el suelo fértil.",
                "Armado práctico por capas de materia verde y seca para acelerar la descomposición orgánica sin olores.",
                "Inspección visual y táctil del humus maduro rico en nutrientes para plantas y biohuertos.",
                "Demostración práctica de cómo transformar residuos de cocina en abono natural sostenible.",
                "Trabajo colaborativo en mesas de laboratorio infantil preparando mezclas biodegradables.",
                "Medición del equilibrio de aireación y humedad necesario para mantener vivos a los microorganismos.",
                "Explicación lúdica sobre cómo la naturaleza recicla cada hoja para crear nueva vida vegetal.",
                "Cierre del taller con cada participante llevando su muestra de compost orgánico certificado."
        };
        for (int i = 1; i <= 9; i++) {
            String fallback = (i % 2 == 1) ? "compostaje" : "reciclaje";
            fotos.add(new GaleriaFoto(
                    i,
                    titulosCompost[i - 1],
                    descCompost[i - 1],
                    CAT_TALLERES,
                    SUB_MINICOMPOSTAJE,
                    "Taller Presencial · Lima, Perú",
                    "minicompostaje_" + i,
                    fallback
            ));
        }

        // --- Subgrupo 2: Tratamiento de Agua (IDs 10 a 19) ---
        String[] titulosAgua = {
                "Cascos Blancos ICSM en Laboratorio",
                "Armado de Filtro Multicapa Casero",
                "Pruebas de Claridad y Turbidez",
                "Separación de Partículas con Arena",
                "Purificación con Carbón Activado",
                "Midiendo el Agua Cristalina",
                "Pequeños Ingenieros Hidráulicos",
                "Experimento de Coagulación Segura",
                "Compromiso por el Agua Limpia",
                "Graduación de Guardianes del Agua"
        };
        String[] descAgua = {
                "Equipos equipados con cascos blancos oficiales ICSM listos para investigar la calidad del agua.",
                "Construcción guiada de filtros con grava, arena fina y algodón para retener impurezas visibles.",
                "Comparación científica entre muestras de agua turbia y muestras filtradas por los propios niños.",
                "Observación del paso lento del líquido a través de capas minerales inspiradas en acuíferos naturales.",
                "Uso didáctico de carbón activado para reducir olores y mejorar la transparencia de la muestra.",
                "Evaluación del pH y limpieza del agua recuperada utilizando instrumentos seguros de medición.",
                "Trabajo en equipo resolviendo un reto ambiental real sobre el cuidado de ríos y cuencas.",
                "Demostración paso a paso de cómo las partículas suspendidas se agrupan y decantan en el fondo.",
                "Reflexión grupal sobre el ahorro diario de agua en el hogar y el colegio.",
                "Celebración final tras completar con éxito el circuito experimental de tratamiento de agua."
        };
        for (int i = 1; i <= 10; i++) {
            String fallback = (i % 2 == 1) ? "hidroponia" : "expe_fisi";
            fotos.add(new GaleriaFoto(
                    9 + i,
                    titulosAgua[i - 1],
                    descAgua[i - 1],
                    CAT_TALLERES,
                    SUB_TRATAMIENTO_AGUA,
                    "Taller ICSM · Laboratorio Infantil",
                    "tratamiento_agua_" + i,
                    fallback
            ));
        }

        // =========================================================================
        // CATEGORÍA "EVENTOS" (14 FOTOS): 4 Fotocatálisis + 10 Minibiohuerto
        // =========================================================================

        // --- Subgrupo 3: Fotocatálisis (IDs 20 a 23) ---
        String[] titulosFoto = {
                "Reactor Solar de Fotocatálisis",
                "Acción del Dióxido de Titanio",
                "Descontaminación con Luz UV",
                "Ciencia Ambiental de Vanguardia"
        };
        String[] descFoto = {
                "Presentación en vivo de reactores solares diseñados para purificar agua mediante energía luminosa.",
                "Explicación interactiva de cómo el dióxido de titanio (TiO2) actúa como catalizador limpio frente al sol.",
                "Observación de la degradación de colorantes y contaminantes orgánicos usando luz ultravioleta.",
                "Evento divulgativo del ICSM acercando tecnologías ambientales avanzadas a familias y escolares."
        };
        for (int i = 1; i <= 4; i++) {
            String fallback = (i % 2 == 1) ? "expe_fisi" : "microbiologia";
            fotos.add(new GaleriaFoto(
                    19 + i,
                    titulosFoto[i - 1],
                    descFoto[i - 1],
                    CAT_EVENTOS,
                    SUB_FOTOCATALISIS,
                    "Evento Científico · ICSM Perú",
                    "fotocatalisis_" + i,
                    fallback
            ));
        }

        // --- Subgrupo 4: Minibiohuerto (IDs 24 a 33) ---
        String[] titulosBio = {
                "Siembra en Jardines Verticales",
                "Semillas Orgánicas Seleccionadas",
                "Cuidado de Plántulas Escolares",
                "Riego Inteligente y Sostenible",
                "Trasplante de Hortalizas Frescas",
                "Explorando Raíces y Brotes",
                "Macetas Ecológicas Reutilizadas",
                "Biohuerto Urbano Comunitario",
                "Cosecha Verde con Sonrisas",
                "Embajadores del Minibiohuerto"
        };
        String[] descBio = {
                "Instalación participativa de jardines verticales que aprovechan pequeños espacios escolares y urbanos.",
                "Los niños eligen y siembran semillas de aromáticas y hortalizas siguiendo el calendario agrícola.",
                "Seguimiento del crecimiento de brotes tiernos aprendiendo sobre fotosíntesis y luz solar.",
                "Aplicación de técnicas de riego por goteo casero para cuidar cada gota de agua en el biohuerto.",
                "Trasplante cuidadoso de plántulas hacia sustrato abonado con humus natural producido en talleres.",
                "Observación cercana de la estructura radicular y cómo absorben minerales del sustrato.",
                "Reutilización creativa de recipientes para armar macetas ecológicas con drenaje adecuado.",
                "Jornada comunitaria integrando a padres, docentes y estudiantes en el mantenimiento verde.",
                "Recolección de hojas frescas y aromáticas cultivadas con dedicación por los pequeños científicos.",
                "Fotografía grupal celebrando un biohuerto escolar activo, saludable y 100% libre de pesticidas."
        };
        for (int i = 1; i <= 10; i++) {
            String fallback = (i % 2 == 1) ? "compostaje" : "hidroponia";
            fotos.add(new GaleriaFoto(
                    23 + i,
                    titulosBio[i - 1],
                    descBio[i - 1],
                    CAT_EVENTOS,
                    SUB_MINIBIOHUERTO,
                    "Evento Escolar · Biohuerto Verde",
                    "minibiohuerto_" + i,
                    fallback
            ));
        }

        // =========================================================================
        // CATEGORÍA "FERIAS" (10 FOTOS): 10 Punto Ecológico
        // =========================================================================

        // --- Subgrupo 5: Punto Ecológico (IDs 34 a 43) ---
        String[] titulosEco = {
                "Estación de Reciclaje Comunitario",
                "Clasificación por Colores NTP",
                "Juego de las 3R en el Parque",
                "Familias Reciclando con Alegría",
                "Módulo Educativo Mini Científicos",
                "Reto Rápido de Separación Verde",
                "Conciencia Ambiental en Colegios",
                "Voluntarios Guiando la Feria",
                "Transformando Residuos en Valor",
                "Compromiso Eco-Ciudadano 2026"
        };
        String[] descEco = {
                "Módulo abierto en parques y plazas enseñando a separar correctamente papel, plástico, vidrio y orgánicos.",
                "Aprendizaje dinámico del código oficial de colores para la gestión responsable de residuos sólidos.",
                "Dinámicas interactivas de Reducir, Reutilizar y Reciclar para niños y familias visitantes.",
                "Padres e hijos participando juntos en desafíos prácticos de economía circular y cuidado urbano.",
                "Stand principal de Mini Científicos compartiendo material educativo y experimentos al aire libre.",
                "Competencia amistosa donde los pequeños clasifican residuos cotidianos en tiempo récord.",
                "Activación ambiental en patios escolares promoviendo brigadas ecológicas estudiantiles.",
                "Equipo de mentores y voluntarios científicos orientando cada pregunta con entusiasmo.",
                "Muestra de objetos útiles elaborados a partir de materiales reciclados y compost natural.",
                "Cierre de feria celebrando el compromiso colectivo por ciudades más limpias y sostenibles."
        };
        for (int i = 1; i <= 10; i++) {
            String fallback = (i % 2 == 1) ? "reciclaje" : "energias_reno";
            fotos.add(new GaleriaFoto(
                    33 + i,
                    titulosEco[i - 1],
                    descEco[i - 1],
                    CAT_FERIAS,
                    SUB_PUNTO_ECOLOGICO,
                    "Feria Ambiental · Parques y Colegios",
                    "punto_ecologico_" + i,
                    fallback
            ));
        }

        return fotos;
    }

    public static List<GaleriaFoto> obtenerPorCategoria(String categoriaPrincipal) {
        List<GaleriaFoto> todas = obtenerTodasLasFotos();
        if (categoriaPrincipal == null || CAT_TODO.equalsIgnoreCase(categoriaPrincipal)) {
            return todas;
        }
        List<GaleriaFoto> filtradas = new ArrayList<>();
        for (GaleriaFoto foto : todas) {
            if (categoriaPrincipal.equalsIgnoreCase(foto.getCategoriaPrincipal())) {
                filtradas.add(foto);
            }
        }
        return filtradas;
    }

    public static List<GaleriaFoto> obtenerPorSubgrupo(String subgrupo) {
        List<GaleriaFoto> todas = obtenerTodasLasFotos();
        if (subgrupo == null || SUB_TODOS.equalsIgnoreCase(subgrupo)) {
            return todas;
        }
        List<GaleriaFoto> filtradas = new ArrayList<>();
        for (GaleriaFoto foto : todas) {
            if (subgrupo.equalsIgnoreCase(foto.getSubgrupo())) {
                filtradas.add(foto);
            }
        }
        return filtradas;
    }

    public static List<GaleriaFoto> obtenerPorFiltro(String categoriaPrincipal, String subgrupo) {
        List<GaleriaFoto> base = obtenerPorCategoria(categoriaPrincipal);
        if (subgrupo == null || SUB_TODOS.equalsIgnoreCase(subgrupo)) {
            return base;
        }
        List<GaleriaFoto> filtradas = new ArrayList<>();
        for (GaleriaFoto foto : base) {
            if (subgrupo.equalsIgnoreCase(foto.getSubgrupo())) {
                filtradas.add(foto);
            }
        }
        return filtradas;
    }

    /**
     * Devuelve las 5 Stories Científicas Interactivas para el carrusel de cabecera.
     */
    public static List<StoryItem> obtenerStoriesCientificas() {
        List<StoryItem> stories = new ArrayList<>(5);
        stories.add(new StoryItem("Fotocatálisis", "⚡", SUB_FOTOCATALISIS,
                "fotocatalisis_1", "expe_fisi", "TITAN", "#00A8E8"));
        stories.add(new StoryItem("Biohuerto", "🌱", SUB_MINIBIOHUERTO,
                "minibiohuerto_1", "compostaje", "TESLA", "#16A34A"));
        stories.add(new StoryItem("Compostaje", "🪱", SUB_MINICOMPOSTAJE,
                "minicompostaje_1", "compostaje", "TESLA", "#D97706"));
        stories.add(new StoryItem("Agua Limpia", "💧", SUB_TRATAMIENTO_AGUA,
                "tratamiento_agua_1", "hidroponia", "TITAN", "#0284C7"));
        stories.add(new StoryItem("Punto Eco", "♻️", SUB_PUNTO_ECOLOGICO,
                "punto_ecologico_1", "reciclaje", "TESLA", "#10B981"));
        return stories;
    }
}
