package com.icsm.minicientificos_android.models;

import java.util.ArrayList;
import java.util.List;

public class TalleresCatalogData {
    public static List<Taller> getTalleres() {
        List<Taller> lista = new ArrayList<>();
        
        String defaultVideo = "video_talleres"; // Archivo raw/video_talleres.mp4, o fallback

        lista.add(new Taller("1", "Astronomía", "ASTRONOMÍA", "SOSTENIBLE PARA NIÑOS", "Ciencia", 
                "Presencial / Virtual", 
                "¡Viaja al espacio sin salir de casa! Conviértete en un astronauta, descubre planetas lejanos y mira las estrellas más brillantes del universo.",
                "Comprender la estructura del sistema solar, las fases lunares y la exploración espacial sostenible mediante modelos a escala y observación con telescopio.",
                new String[]{"Construcción de cohete hidropropulsado a escala", "Observación de constelaciones y cráteres lunares", "Simulación de gravedad y órbitas planetarias", "Misión Marte: Hábitats espaciales sostenibles"},
                new String[]{"Kit de cohete reciclable", "Mapa estelar giratorio (Planisferio)", "Lentes de observación astronómica", "Manual del pequeño astronauta"},
                "Sábados 4:00 PM - 6:00 PM", "6 a 12 años", "2 horas", "10 vacantes", 45.00, "astronomia", defaultVideo));

        lista.add(new Taller("2", "Bobina Tesla", "BOBINA DE TESLA", "PARA NIÑOS", "Tecnología", 
                "Presencial", 
                "¡Haz que los rayos bailen con tus manos! Descubre la magia de la electricidad y crea relámpagos seguros y divertidos.",
                "Descubrir los principios del electromagnetismo, la transmisión inalámbrica de energía de Nikola Tesla y los circuitos eléctricos de forma 100% segura.",
                new String[]{"¿Qué es la electricidad y cómo viajan los electrones?", "Encendido inalámbrico de focos con la Bobina de Tesla", "Arcos voltaicos musicales y plasma seguro", "Armado de mi primer circuito luminoso LED"},
                new String[]{"Mini kit de circuito LED", "Guía ilustrada de Nikola Tesla", "Guantes aislantes de laboratorio", "Certificado de guardián eléctrico"},
                "Viernes 4:00 PM - 6:00 PM", "6 a 12 años", "2 horas", "5 vacantes", 50.00, "bobina", defaultVideo));

        lista.add(new Taller("3", "Arqueología", "ARQUEOLOGÍA SOSTENIBLE", "PARA NIÑOS", "Ciencia", 
                "Presencial", 
                "¡A jugar a ser un explorador de tesoros perdidos! Excava, cepilla con cuidado y encuentra fósiles y reliquias secretas del pasado.",
                "Explorar las culturas milenarias del Perú y la paleontología mediante técnicas reales de excavación estratigráfica, datación y conservación.",
                new String[]{"Técnicas de cuadrícula y excavación con brocha y cincel", "Descubrimiento y limpieza de réplicas de fósiles y cerámicas", "Clasificación de hallazgos en el cuaderno de campo", "Restauración ecológica de piezas antiguas"},
                new String[]{"Bloque de excavación arqueológica", "Brocha, lupa y esteca de madera", "Libreta de campo de explorador", "Réplica coleccionable de fósil/huaco"},
                "Sábados 3:00 PM - 5:00 PM", "6 a 12 años", "2 horas", "10 vacantes", 45.00, "arqueologia", defaultVideo));

        lista.add(new Taller("4", "Geología Divertida", "GEOLOGÍA", "SOSTENIBLE PARA NIÑOS", "Naturaleza", 
                "Presencial / Híbrido", 
                "¡Toca rocas de colores extraños y descubre piedras mágicas! Un viaje súper divertido por los tesoros escondidos dentro de la Tierra.",
                "Identificar minerales, rocas ígneas, sedimentarias y metamórficas, comprendiendo la formación de las montañas y los volcanes.",
                new String[]{"El ciclo de las rocas y las capas internas de la Tierra", "Laboratorio de cristales y geodas brillantes", "Prueba de dureza e imantación de minerales reales", "Erupción volcánica controlada con espuma reactiva"},
                new String[]{"Caja coleccionadora con 6 muestras de minerales", "Lupa mineralógica de aumento", "Kit para hacer crecer cristales", "Ficha de identificación geológica"},
                "Sábados 9:00 AM - 10:30 AM", "6 a 12 años", "1.5 horas", "6 vacantes", 45.00, "geologia", defaultVideo));

        lista.add(new Taller("5", "Física Loca", "EXPERIMENTOS FISICOQUÍMICOS", "PARA NIÑOS", "Ciencia", 
                "Presencial / Virtual", 
                "¡Haz volar cohetes y crea mezclas locas! Aquí la ciencia se convierte en un juego lleno de sorpresas y mucha diversión.",
                "Experimentar con reacciones ácido-base, fluidos no newtonianos, densidad de líquidos y presión atmosférica mediante retos sorprendentes.",
                new String[]{"Creación de Slime Fluido No-Newtoniano magnético", "Torre arcoíris de densidades líquidas", "Volcanes de espuma catalítica (Pasta de dientes de elefante)", "Lanzamiento por presión de aire y principio de Acción-Reacción"},
                new String[]{"Tubos de ensayo y gradilla infantil", "Pipetas Pasteur y vasos precipitados", "Reactivos seguros de grado alimenticio y colorantes", "Lentes de seguridad y mandil"},
                "Miércoles 5:00 PM - 6:30 PM", "6 a 12 años", "1.5 horas", "20 vacantes", 45.00, "expe_fisi", defaultVideo));

        lista.add(new Taller("6", "Inteligencia Artificial", "INTELIGENCIA ARTIFICIAL", "SOSTENIBLE PARA NIÑOS", "Tecnología", 
                "Virtual", 
                "¡Crea tu propio robot amigo! Aprende cómo piensan las máquinas y dale superpoderes tecnológicos a tus ideas.",
                "Introducir a los niños en el pensamiento computacional, el entrenamiento de modelos básicos de visión/sonido y el uso ético y ecológico de la IA.",
                new String[]{"¿Cómo aprenden las computadoras? Redes neuronales explicadas con juegos", "Entrenando a una IA para clasificar residuos reciclables por cámara", "Creación de cuentos e ilustraciones científicas con IA", "Programación por bloques de un asistente ecológico"},
                new String[]{"Acceso a plataforma interactiva de IA para niños", "Plantillas de lógica y algoritmos desenchufados", "Guía de prompts seguros para pequeños genios", "Insignia digital de Programador IA"},
                "Domingos 11:00 AM - 12:30 PM", "6 a 12 años", "1.5 horas", "15 vacantes", 40.00, "ia", defaultVideo));

        lista.add(new Taller("7", "Microbiología", "MICROBIOLOGÍA", "PARA NIÑOS", "Ciencia", 
                "Presencial", 
                "¡Conviértete en un detective invisible! Mira a través del lente y descubre el asombroso mundo de los pequeños seres que viven a nuestro alrededor.",
                "Utilizar el microscopio óptico para observar células vegetales, microorganismos benéficos del agua y aprender sobre higiene y biotecnología.",
                new String[]{"Partes y manejo real del microscopio profesional", "Observación de células de epidermis de cebolla y cloroplastos", "Cazadores de microbios: vida oculta en una gota de agua de charco", "Bacterias buenas vs malas: fermentación y cultivo en placa Petri"},
                new String[]{"Placa Petri con medio de cultivo seguro", "Portaobjetos, cubreobjetos y gotero", "Cuaderno de dibujo microscópico", "Guantes de nitrilo y credencial de microbiólogo"},
                "Lunes 4:00 PM - 5:30 PM", "6 a 12 años", "1.5 horas", "8 vacantes", 50.00, "microbiologia", defaultVideo));

        lista.add(new Taller("8", "Minería del Futuro", "MINERÍA", "SOSTENIBLE PARA NIÑOS", "Tecnología", 
                "Presencial / Híbrido", 
                "¡Ayuda a cuidar el planeta mientras buscas tesoros! Descubre cómo los científicos extraen minerales usando inventos geniales y ecológicos.",
                "Conocer la importancia del cobre, litio y minerales en la tecnología diaria y cómo la ingeniería moderna protege el agua y el ecosistema.",
                new String[]{"¿De dónde salen los minerales de mi celular y de los paneles solares?", "Maqueta de una operación minera moderna y sostenible", "Experimento de biolixiviación y flotación limpia de minerales", "Revegetación y cierre ecológico de áreas naturales"},
                new String[]{"Casco minero infantil de seguridad", "Muestras de mineral de cobre y cuarzo", "Kit de filtrado y purificación de agua", "Manual con Tesla el Duende Minero"},
                "Sábados 4:00 PM - 5:30 PM", "6 a 12 años", "1.5 horas", "15 vacantes", 45.00, "mineria", defaultVideo));

        lista.add(new Taller("9", "Reciclaje", "RECICLAJE", "PARA NIÑOS", "Naturaleza", 
                "Presencial", 
                "¡Convierte la basura en superjuguetes! Aprende a dar una segunda vida a las cosas y conviértete en un superhéroe reciclador.",
                "Aplicar la economía circular y las 3R (Reducir, Reutilizar y Reciclar) transformando residuos plásticos, papel y cartón en inventos funcionales.",
                new String[]{"Código de colores de segregación en Puntos Ecológicos", "Fábrica de papel artesanal reciclado con semillas plantables", "Construcción de un auto impulsado por energía elástica con botellas PET", "Reto Cero Residuos en el colegio y en casa"},
                new String[]{"Bastidor para fabricar papel reciclado", "Semillas para papel germinable", "Kit de piezas para eco-juguete mecánico", "Bolsa ecológica reutilizable"},
                "Domingos 10:00 AM - 11:30 AM", "6 a 12 años", "1.5 horas", "12 vacantes", 40.00, "reciclaje", defaultVideo));

        lista.add(new Taller("10", "Mini Huerto", "COMPOSTAJE Y MINIHUERTO", "SOSTENIBLE PARA NIÑOS", "Naturaleza", 
                "Presencial", 
                "¡Planta tu propia huerta y mira cómo crece la magia! Siembra semillas, riégalas con amor y cosecha tus propias plantitas.",
                "Aprender el ciclo de vida de las plantas, el proceso de vermicompostaje con lombrices californianas y el diseño de biohuertos urbanos en casa.",
                new String[]{"De residuo orgánico a tierra fértil: ¿Cómo funciona el compostaje?", "Conociendo a nuestras amigas las lombrices rojas californianas", "Preparación de sustrato nutritivo y siembra de hortalizas", "Cuidados de riego, luz solar y control ecológico de plagas"},
                new String[]{"Maceta biodegradable y paleta de jardinería", "Bolsa de humus de lombriz y tierra preparada", "Sobres de semillas orgánicas (rabanito, lechuga, girasol)", "Etiquetas identificadoras de madera"},
                "Sábados 10:00 AM - 12:00 PM", "6 a 12 años", "2 horas", "8 vacantes", 45.00, "compostaje", defaultVideo));

        lista.add(new Taller("11", "Hidroponía", "HIDROPONÍA", "PARA NIÑOS", "Naturaleza", 
                "Presencial / Híbrido", 
                "¡Haz crecer plantas sin usar tierra! Descubre el divertido truco de cultivar hortalizas flotando en agua con nutrientes mágicos.",
                "Implementar un sistema hidropónico casero de raíz flotante, entendiendo cómo las raíces absorben sales minerales directamente del agua oxigenada.",
                new String[]{"¿Pueden las plantas vivir solo en agua? Ciencia de la hidroponía", "Preparación de solución nutritiva A y B y medición de pH", "Armado de mi sistema hidropónico de raíz flotante paso a paso", "Oxigenación manual y bitácora de crecimiento de raíces"},
                new String[]{"Contenedor hidropónico con plancha flotante perforada", "Esponjas agrícolas y plántulas listas para trasplante", "Frascos goteros de nutrientes hidropónicos seguros", "Guía de cosecha en 30 días"},
                "Viernes 3:00 PM - 4:30 PM", "6 a 12 años", "1.5 horas", "9 vacantes", 50.00, "hidroponia", defaultVideo));

        lista.add(new Taller("12", "Energías Renovables", "ENERGÍAS RENOVABLES", "PARA NIÑOS", "Tecnología", 
                "Presencial", 
                "¡Atrapa la fuerza del sol y del viento! Crea molinos y paneles que funcionan con energía limpia para cuidar nuestro planeta.",
                "Experimentar con la transformación de la radiación solar, el viento y la fuerza del agua en energía eléctrica limpia para combatir el cambio climático.",
                new String[]{"Energías limpias vs combustibles fósiles: salvando el clima", "Armado de un aerogenerador eólico que enciende una luz LED", "Experimento con mini panel fotovoltaico e impulsión de motor solar", "Purificación solar de agua mediante fotocatálisis"},
                new String[]{"Mini panel solar educativo con motor y hélice", "Piezas para armar molino eólico funcional", "Diodos LED de colores", "Diploma de Ingeniero en Energías Limpias"},
                "Martes 4:00 PM - 5:30 PM", "6 a 12 años", "1.5 horas", "7 vacantes", 50.00, "energias_reno", defaultVideo));

        return lista;
    }
}