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

    // Datos dummy precargados con imágenes reales
    public static List<TallerMock> getTalleresPrueba() {
        List<TallerMock> lista = new ArrayList<>();

        lista.add(new TallerMock(
                1,
                "Pequeños Botánicos y Compostaje",
                "Aprende sobre la vida de las plantas, germinación y compostaje infantil.",
                "En este taller interactivo, los niños explorarán los secretos del reino vegetal, construirán su propio mini macetero con materiales reciclados y aprenderán el arte del compost en casa.",
                "Presencial",
                "Ecología",
                "Sábados 10:00 AM - 12:00 PM",
                "8 vacantes disponibles",
                "• Edad sugerida: 6 a 12 años\n• Incluye kit de semillas y tierra viva\n• Asistir con ropa cómoda",
                "S/ 90.00 / mes",
                R.drawable.compostaje
        ));

        lista.add(new TallerMock(
                2,
                "Robótica Educativa y Bobina Tesla",
                "Armado de prototipos robóticos sencillos y electromagnetismo con energía limpia.",
                "¡Ingresa al fascinante mundo de la mecatrónica! Construiremos robots exploradores utilizando bloques interactivos, motores DC y circuitos seguros.",
                "Híbrido",
                "Robótica",
                "Viernes 4:00 PM - 6:00 PM",
                "5 vacantes disponibles",
                "• Edad sugerida: 8 a 14 años\n• Traer laptop o tablet (opcional)\n• No requiere experiencia previa",
                "S/ 120.00 / mes",
                R.drawable.bobina
        ));

        lista.add(new TallerMock(
                3,
                "Inteligencia Artificial y Programación",
                "Crea tus propios asistentes virtuales y algoritmos interactivos simples.",
                "Aprende lógica de computación, aprendizaje automático básico y creación de proyectos de IA orientados a resolver retos ambientales.",
                "Virtual",
                "Robótica",
                "Domingos 11:00 AM - 12:30 PM",
                "15 vacantes disponibles",
                "• Edad sugerida: 8 a 14 años\n• Conexión Zoom y computadora\n• Entorno gráfico intuitivo por bloques",
                "S/ 85.00 / mes",
                R.drawable.ia
        ));

        lista.add(new TallerMock(
                4,
                "Arqueología y Paleontología Infantil",
                "Descubre fósiles, excavaciones arqueológicas y réplicas de dinosaurios.",
                "¡Sé un detective del pasado! Realizaremos simulaciones de excavaciones en bloques de yeso para extraer fósiles y aprender sobre las civilizaciones antiguas.",
                "Presencial",
                "Ciencia",
                "Sábados 3:00 PM - 5:00 PM",
                "10 vacantes disponibles",
                "• Edad sugerida: 6 a 11 años\n• Incluye kit de herramientas de excavación\n• Guantes de protección incluidos",
                "S/ 95.00 / mes",
                R.drawable.arqueologia
        ));

        lista.add(new TallerMock(
                5,
                "Astronomía y Exploración Espacial",
                "Construye cohetes de agua, observa constelaciones y descubre planetas.",
                "Viaja por las galaxias construyendo modelos a escala del sistema solar, proyectores de estrellas caseros y lanzadores aerodinámicos.",
                "Híbrido",
                "Física",
                "Jueves 4:30 PM - 6:00 PM",
                "12 vacantes disponibles",
                "• Edad sugerida: 7 a 13 años\n• Incluye telescopio didáctico en clase\n• Material impreso de constelaciones",
                "S/ 100.00 / mes",
                R.drawable.astronomia
        ));

        lista.add(new TallerMock(
                6,
                "Energías Renovables y Viento",
                "Arma mini turbinas eólicas y paneles solares funcionales.",
                "Comprende la transformación de la energía del sol y del viento en electricidad para encender LEDs y motores limpios sin contaminar el planeta.",
                "Presencial",
                "Ecología",
                "Martes 4:00 PM - 5:30 PM",
                "7 vacantes disponibles",
                "• Edad sugerida: 8 a 12 años\n• Incluye panel solar de juguete y motor\n• Certificado de Eco-Científico",
                "S/ 110.00 / mes",
                R.drawable.energias_reno
        ));

        lista.add(new TallerMock(
                7,
                "Experimentos Asombrosos de Física",
                "Leyes del movimiento, gravedad y fluidos con experimentos divertidos.",
                "Demostraciones prácticas con giroscopios, fluidos no newtonianos y la física que explica los deportes y atracciones del parque.",
                "Virtual",
                "Física",
                "Miércoles 5:00 PM - 6:30 PM",
                "20 vacantes disponibles",
                "• Edad sugerida: 6 a 12 años\n• Sesiones interactivas en vivo\n• Materiales sencillos de hogar",
                "S/ 70.00 / mes",
                R.drawable.expe_fisi
        ));

        lista.add(new TallerMock(
                8,
                "Geología y Volcanes en Reacción",
                "Crea volcanes a escala y explora minerales y rocas volcánicas.",
                "Estudia la estructura interna de la Tierra, aprende cómo se forman las montañas y haz erupcionar maquetas volcánicas coloridas y seguras.",
                "Presencial",
                "Ciencia",
                "Sábados 9:00 AM - 10:30 AM",
                "6 vacantes disponibles",
                "• Edad sugerida: 5 a 10 años\n• Incluye muestras de minerales reales\n• Lentes de seguridad proporcionados",
                "S/ 85.00 / mes",
                R.drawable.geologia
        ));

        lista.add(new TallerMock(
                9,
                "Hidroponía y Cultivos Inteligentes",
                "Aprende a cultivar vegetales en agua sin necesidad de tierra.",
                "Descubre la agricultura del futuro construyendo tu propio sistema hidropónico vertical con nutrientes minerales y bombas de agua.",
                "Híbrido",
                "Ecología",
                "Viernes 3:00 PM - 4:30 PM",
                "9 vacantes disponibles",
                "• Edad sugerida: 7 a 13 años\n• Incluye soporte hidropónico de mesa\n• Muestras de lechugas orgánicas",
                "S/ 105.00 / mes",
                R.drawable.hidroponia
        ));

        lista.add(new TallerMock(
                10,
                "Microbiología y el Mundo Invisible",
                "Uso de microscopios para observar células, bacterias buenas y agua.",
                "Observa el fascinante microorganismo marino, láminas microscópicas y aprende la ciencia del lavado eficiente de manos y la higiene.",
                "Presencial",
                "Ciencia",
                "Lunes 4:00 PM - 5:30 PM",
                "8 vacantes disponibles",
                "• Edad sugerida: 7 a 12 años\n• Uso individual de microscopio óptico\n• Bata de laboratorio incluida",
                "S/ 115.00 / mes",
                R.drawable.microbiologia
        ));

        lista.add(new TallerMock(
                11,
                "Minería Sostenible y Cristales",
                "Procesos de cristalización, minerales del Perú y cuidado del entorno.",
                "Fabrica cristales brillantes en casa y aprende sobre los minerales metálicos y no metálicos, promoviendo el reciclaje de metales.",
                "Virtual",
                "Ciencia",
                "Sábados 4:00 PM - 5:30 PM",
                "15 vacantes disponibles",
                "• Edad sugerida: 6 a 12 años\n• Kit de sales para cristales enviado a casa\n• Guía ilustrada descargable",
                "S/ 75.00 / mes",
                R.drawable.mineria
        ));

        lista.add(new TallerMock(
                12,
                "Reciclaje Creativo y Eco-Arte",
                "Transforma plásticos, cartón y papel en inventos útiles.",
                "Fomenta la economía circular construyendo juguetes mecatrónicos, organizadores de escritorio y obras de arte reutilizando empaques.",
                "Presencial",
                "Ecología",
                "Domingos 10:00 AM - 11:30 AM",
                "12 vacantes disponibles",
                "• Edad sugerida: 5 a 10 años\n• Traer empaques limpios de casa\n• Pinturas e insumos incluidos",
                "S/ 65.00 / mes",
                R.drawable.reciclaje
        ));

        return lista;
    }
}