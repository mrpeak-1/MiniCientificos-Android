package com.icsm.minicientificos_android.models.game;

import java.util.ArrayList;
import java.util.List;

public class BancoPreguntasCatalog {

    public static List<NivelJuego> obtenerNiveles() {
        List<NivelJuego> niveles = new ArrayList<>();

        niveles.add(new NivelJuego(
                1,
                "MUNDO 1",
                "Laboratorio de Física y Química",
                "Descubre la magia de la materia, reacciones luminosas y circuitos eléctricos.",
                "🧪",
                "#00A8E8",
                0,
                "MINI10"
        ));

        niveles.add(new NivelJuego(
                2,
                "MUNDO 2",
                "Guardianes de la Naturaleza",
                "Aprende sobre hidroponía, microbiología, compostaje y fotocatálisis del agua.",
                "🌱",
                "#16A34A",
                300,
                "EXPLORADOR15"
        ));

        niveles.add(new NivelJuego(
                3,
                "MUNDO 3",
                "Futuro, Espacio y Tecnología",
                "Explora telescopios, energía solar, robótica e inteligencia artificial.",
                "🚀",
                "#FDC500",
                600,
                "CIENCIA20"
        ));

        niveles.add(new NivelJuego(
                4,
                "MUNDO 4",
                "Reto Maestro del Ing. Andy",
                "El desafío científico supremo para verdaderos genios del Instituto ICSM.",
                "🏆",
                "#0B2C5E",
                1000,
                "CIENCIA20"
        ));

        return niveles;
    }

    public static List<MedallaCientifica> obtenerCatalogoMedallas() {
        List<MedallaCientifica> medallas = new ArrayList<>();

        medallas.add(new MedallaCientifica(
                "med_1",
                "Guardián del Biohuerto",
                "Completa tus primeras lecciones de botanica e hidroponia.",
                "🌱",
                "#16A34A",
                100,
                false
        ));

        medallas.add(new MedallaCientifica(
                "med_2",
                "Aprendiz de Nikola Tesla",
                "Domina los circuitos y experimentos de electromagnetismo.",
                "⚡",
                "#FDC500",
                300,
                false
        ));

        medallas.add(new MedallaCientifica(
                "med_3",
                "Explorador de Galaxias",
                "Observa los astros y comprende la luz del universo.",
                "🔭",
                "#00A8E8",
                600,
                false
        ));

        medallas.add(new MedallaCientifica(
                "med_4",
                "Maestro del Agua Limpia",
                "Resuelve los retos ambientales y la fotocatalisis de contaminantes.",
                "💧",
                "#0284C7",
                900,
                false
        ));

        medallas.add(new MedallaCientifica(
                "med_5",
                "Genio de la Robótica e IA",
                "Crea algoritmos y automatizaciones sostenibles.",
                "🤖",
                "#9333EA",
                1200,
                false
        ));

        medallas.add(new MedallaCientifica(
                "med_6",
                "Mini Científico Legendario ICSM",
                "Alcanza el nivel supremo de conocimiento cientifico.",
                "🏆",
                "#0B2C5E",
                1500,
                false
        ));

        return medallas;
    }

    public static List<PreguntaCientifica> obtenerPreguntasPorNivel(int nivelId) {
        List<PreguntaCientifica> filtradas = new ArrayList<>();
        for (PreguntaCientifica p : obtenerTodasLasPreguntas()) {
            if (p.getNivelId() == nivelId) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public static List<PreguntaCientifica> obtenerTodasLasPreguntas() {
        List<PreguntaCientifica> lista = new ArrayList<>();

        // ==========================================
        // MUNDO 1: LABORATORIO DE FÍSICA Y QUÍMICA (PREGUNTAS 1 a 9)
        // ==========================================
        lista.add(new PreguntaCientifica(
                1, 1, "CIENCIA",
                "¿Qué le sucede a un fluido no newtoniano (como la maicena con agua) cuando le aplicas una fuerza rápida?",
                new String[]{"Se vuelve completamente gaseoso", "Se comporta como un sólido duro", "Se evapora de inmediato", "Cambia de color a azul"},
                1,
                "Tesla recuerda: ¡al presionar fuerte la viscosidad aumenta al instante!",
                "Los fluidos no newtonianos no siguen la ley de viscosidad de Newton. Al golpearlos o apretarlos, sus moléculas se juntan y resisten el movimiento como si fueran un sólido.",
                100
        ));

        lista.add(new PreguntaCientifica(
                2, 1, "CIENCIA",
                "En el experimento de la Bobina de Tesla, ¿cómo se transmite la energía a los focos fluorescentes cerca de ella?",
                new String[]{"Por cables invisibles de cobre", "Mediante un campo magnético de alta frecuencia", "Usando baterías alcalinas", "Por medio de calor directo"},
                1,
                "Nikola Tesla descubrió la transmisión inalámbrica de alta frecuencia.",
                "La Bobina de Tesla genera un campo electromagnético de alta frecuencia que excita los gases nobles dentro de los tubos de luz a distancia sin cables.",
                100
        ));

        lista.add(new PreguntaCientifica(
                3, 1, "CIENCIA",
                "¿Qué gas le da las burbujas efervescentes a la mezcla de bicarbonato de sodio y vinagre?",
                new String[]{"Oxígeno puro", "Dióxido de Carbono (CO2)", "Helio", "Hidrógeno"},
                1,
                "Es el mismo gas inofensivo que exhalamos al respirar.",
                "Al juntar un ácido (vinagre) con una base (bicarbonato), ocurre una reacción química que libera gas Dióxido de Carbono (CO2).",
                100
        ));

        lista.add(new PreguntaCientifica(
                4, 1, "CIENCIA",
                "¿Por qué flotan las burbujas de jabón de colores al estar llenas de aire tibio?",
                new String[]{"El aire caliente es más denso", "El aire caliente es menos denso que el aire frío", "Por atracción de la Luna", "Porque el jabón las empuja hacia arriba"},
                1,
                "Recuerda que el aire caliente siempre busca subir.",
                "El aire caliente se dilata, volviéndose menos denso que el aire circundante, lo que produce una fuerza de empuje hacia arriba.",
                100
        ));

        lista.add(new PreguntaCientifica(
                5, 1, "CIENCIA",
                "¿Qué propiedad física permite a los clips o insectos pararse sobre el agua sin hundirse?",
                new String[]{"Gravedad cero", "Tensión superficial", "Magnetismo acuático", "Punto de ebullición"},
                1,
                "Las moléculas de la superficie del agua se toman fuertemente de las manos.",
                "La tensión superficial es la fuerza de atracción entre las moléculas en la superficie de un líquido, creando una fina membrana elástica.",
                100
        ));

        lista.add(new PreguntaCientifica(
                6, 1, "CIENCIA",
                "¿Cuál es la diferencia principal entre un cambio físico y un cambio químico?",
                new String[]{"En el químico se forman nuevas sustancias", "En el físico desaparece la materia", "No existe ninguna diferencia", "El físico solo ocurre en el hielo"},
                0,
                "Piensa si puedes regresar al estado original fácilmente o si creaste algo totalmente nuevo.",
                "En un cambio físico la sustancia sigue siendo la misma (como congelar agua), mientras que en un cambio químico se transforman los enlaces formando nuevas sustancias.",
                100
        ));

        lista.add(new PreguntaCientifica(
                7, 1, "CIENCIA",
                "¿Qué tipo de energía tiene un carrito científico ubicado en la cima de una rampa antes de soltarlo?",
                new String[]{"Energía potencial gravitatoria", "Energía eólica", "Energía nuclear", "Energía sonora"},
                0,
                "Es energía almacenada a cierta altura lista para liberarse.",
                "La energía potencial es la energía almacenada en un cuerpo debido a su posición o altura en un campo gravitatorio.",
                100
        ));

        lista.add(new PreguntaCientifica(
                8, 1, "CIENCIA",
                "En el taller de pigmentos, ¿cómo se llama el método para separar los colores de una tinta con papel y agua?",
                new String[]{"Cromatografía", "Filtración al vacío", "Evaporación solar", "Destilación"},
                0,
                "Croma significa color en griego.",
                "La cromatografía de papel separa las mezclas de pigmentos al viajar el solvente a través de una fase estacionaria.",
                100
        ));

        lista.add(new PreguntaCientifica(
                9, 1, "CIENCIA",
                "¿Qué sucede con el volumen del agua cuando se congela en un frasco cerrado?",
                new String[]{"Disminuye", "Se expande y aumenta su volumen", "Sigue exactamente igual", "Se convierte en vapor"},
                1,
                "El hielo es de los pocos sólidos que ocupa más espacio que su líquido.",
                "Las moléculas de agua forman una estructura cristalina hexagonal al congelarse que ocupa más volumen que en estado líquido.",
                100
        ));


        // ==========================================
        // MUNDO 2: GUARDIANES DE LA NATURALEZA (PREGUNTAS 10 a 18)
        // ==========================================
        lista.add(new PreguntaCientifica(
                10, 2, "NATURALEZA",
                "En el taller de Hidroponía, ¿cómo crecen las plantitas sostenibles?",
                new String[]{"Solo en tierra negra fértil", "En agua enriquecida con nutrientes minerales sin tierra", "En arena de playa salada", "Sin agua ni luz solar"},
                1,
                "Hidro significa agua en griego.",
                "La hidroponía permite cultivar alimentos utilizando soluciones minerales en agua en lugar de suelo agrícola, ahorrando hasta un 90% de agua.",
                100
        ));

        lista.add(new PreguntaCientifica(
                11, 2, "NATURALEZA",
                "¿Qué especie de lombriz es la estrella del minicompostaje escolar?",
                new String[]{"Lombriz de tierra común", "Lombriz roja californiana (Eisenia foetida)", "Lombriz marina", "Lombriz luminosa"},
                1,
                "Es de color rojo brillante y convierte restos orgánicos en humus riquísimo.",
                "La lombriz roja californiana transforma los residuos orgánicos de cocina en humos o abono natural de alta calidad.",
                100
        ));

        lista.add(new PreguntaCientifica(
                12, 2, "NATURALEZA",
                "¿Cómo funciona la Fotocatálisis ambiental desarrollada por científicos del ICSM?",
                new String[]{"Congelando el agua con hielo seco", "Utilizando luz solar y un catalizador para degradar contaminantes", "Agregando cloro industrial en exceso", "Haciendo hervir la tierra"},
                1,
                "Foto significa luz y catálisis es acelerar una reacción limpia.",
                "La fotocatálisis activa un semiconductor (como TiO2) con luz solar para eliminar bacterias, virus y contaminantes del agua y del aire.",
                100
        ));

        lista.add(new PreguntaCientifica(
                13, 2, "NATURALEZA",
                "¿Qué organismo microscópico es responsable de la fermentación en alimentos saludables como el yogurt?",
                new String[]{"Bacterias benéficas (Lactobacillus)", "Virus de resfrío", "Hongos venenosos", "Parásitos marinos"},
                0,
                "No todos los microorganismos son dañinos; muchos protegen nuestro estómago.",
                "Los lactobacilos son bacterias probióticas que transforman la lactosa de la leche en ácido láctico beneficioso.",
                100
        ));

        lista.add(new PreguntaCientifica(
                14, 2, "NATURALEZA",
                "¿Qué capa de la atmósfera nos protege de los rayos ultravioleta (UV) del Sol?",
                new String[]{"La capa de Ozono", "El núcleo terrestre", "La corteza oceánica", "La tropopausa salada"},
                0,
                "Su fórmula química es O3.",
                "La capa de ozono filtra la radiación ultravioleta dañina del Sol, protegiendo a todos los seres vivos del planeta.",
                100
        ));

        lista.add(new PreguntaCientifica(
                15, 2, "NATURALEZA",
                "En la naturaleza, ¿cuál es el rol principal de las abejas en los cultivos?",
                new String[]{"Pintar las flores de colores", "La polinización para producir frutos y semillas", "Limpiar las hojas secas", "Producir viento"},
                1,
                "Al transportar el polen de flor en flor aseguran la biodiversidad.",
                "Las abejas son polinizadoras clave. De ellas depende más del 75% de las especies de plantas con flor en el planeta.",
                100
        ));

        lista.add(new PreguntaCientifica(
                16, 2, "NATURALEZA",
                "¿Qué gas absorben las plantas durante la fotosíntesis para expulsar oxígeno purificado?",
                new String[]{"Dióxido de Carbono (CO2)", "Metano", "Argón", "Monóxido de carbono"},
                0,
                "Es el gas sobrante que las plantas limpian de las ciudades.",
                "Durante la fotosíntesis, las plantas absorben CO2 y agua, usando energía solar para sintetizar glucosa y liberar oxígeno.",
                100
        ));

        lista.add(new PreguntaCientifica(
                17, 2, "NATURALEZA",
                "¿Qué es la huella de carbono?",
                new String[]{"Las pisadas de dinosaurio fosilizadas", "La cantidad total de gases de efecto invernadero emitidos por actividades humanas", "El polvo de carbón en una mina", "Una marca de zapato ecológico"},
                1,
                "Mide cuánto impactamos el planeta con nuestro consumo energético diario.",
                "La huella de carbono cuantifica las emisiones directas e indirectas de gases como CO2 generadas por una persona o institución.",
                100
        ));

        lista.add(new PreguntaCientifica(
                18, 2, "NATURALEZA",
                "En geología infantil, ¿cómo se llaman las rocas formadas por el enfriamiento del lava o magma volcánico?",
                new String[]{"Rocas Ígneas", "Rocas Sedimentarias", "Rocas Sintéticas", "Rocas de plástico"},
                0,
                "Viene de la palabra ignis que significa fuego.",
                "Las rocas ígneas o magmáticas se originan cuando el magma o la lava de los volcanes se enfría y cristaliza.",
                100
        ));


        // ==========================================
        // MUNDO 3: FUTURO, ESPACIO Y TECNOLOGÍA (PREGUNTAS 19 a 27)
        // ==========================================
        lista.add(new PreguntaCientifica(
                19, 3, "TECNOLOGIA",
                "En Astronomía para niños, ¿cuál es el planeta más grande de nuestro Sistema Solar?",
                new String[]{"Marte", "Júpiter", "Saturno", "Mercurio"},
                1,
                "Es el gigante gaseoso famoso por su Gran Mancha Roja.",
                "Júpiter es un gigante gaseoso tan masivo que cabrían más de 1,300 planetas Tierra en su interior.",
                100
        ));

        lista.add(new PreguntaCientifica(
                20, 3, "TECNOLOGIA",
                "¿Qué tipo de energía renovable aprovecha la luz del Sol mediante paneles solares fotovoltaicos?",
                new String[]{"Energía Geotérmica", "Energía Solar Fotovoltaica", "Energía Nuclear", "Energía Hidroeléctrica"},
                1,
                "Utiliza los fotones del astro rey para mover electrones.",
                "La energía solar convierte directamente los fotones de la luz en corriente eléctrica continua a través del efecto fotovoltaico.",
                100
        ));

        lista.add(new PreguntaCientifica(
                21, 3, "TECNOLOGIA",
                "¿Qué instrumento óptico utiliza lentes y espejos para observar estrellas y galaxias lejanas?",
                new String[]{"Microscopio electrónico", "Telescopio", "Estetoscopio", "Periscopio de submarino"},
                1,
                "Nos permite viajar con la mirada hacia las galaxias.",
                "El telescopio recolecta y magnifica la luz emitida por objetos celestes en el espacio exterior.",
                100
        ));

        lista.add(new PreguntaCientifica(
                22, 3, "TECNOLOGIA",
                "En la era digital, ¿qué significa las siglas 'IA' en la tecnología de los personajes Titan y Tesla?",
                new String[]{"Ilustración Avanzada", "Inteligencia Artificial", "Internet Automático", "Investigación Acuática"},
                1,
                "Sistemas capaces de aprender, razonar y ayudar a resolver problemas.",
                "La Inteligencia Artificial (IA) es la disciplina computacional que crea modelos capaces de realizar tareas que requieren razonamiento y aprendizaje.",
                100
        ));

        lista.add(new PreguntaCientifica(
                23, 3, "TECNOLOGIA",
                "¿Cuál de estos materiales es un gran conductor de la corriente eléctrica?",
                new String[]{"Madera seca", "Cobre", "Plástico reciclabile", "Vidrio de ventana"},
                1,
                "Se usa en el interior de los cables de casi todos los electrodomésticos.",
                "El cobre posee electrones libres en su estructura atómica que permiten el flujo rápido de electricidad con baja resistencia.",
                100
        ));

        lista.add(new PreguntaCientifica(
                24, 3, "TECNOLOGIA",
                "¿Cómo se llama la fuerza invisible de atracción que nos mantiene a todos con los pies sobre la Tierra?",
                new String[]{"Fuerza de Fricción", "Gravedad", "Magnetismo estático", "Presión atmosférica"},
                1,
                "Sir Isaac Newton la descubrió al ver caer una manzana.",
                "La fuerza de gravedad es la atracción mutua entre objetos con masa. En la Tierra nos atrae hacia el centro del planeta a 9.8 m/s².",
                100
        ));

        lista.add(new PreguntaCientifica(
                25, 3, "TECNOLOGIA",
                "¿Qué satélite natural de la Tierra influye en las mareas del océano?",
                new String[]{"El Sol", "La Luna", "Titan", "Fobos"},
                1,
                "Nuestra compañera nocturna con fases lunares.",
                "La atracción gravitacional de la Luna sobre los océanos terrestres genera el fenómeno periódico de las mareas.",
                100
        ));

        lista.add(new PreguntaCientifica(
                26, 3, "TECNOLOGIA",
                "En Arqueología científica, ¿qué técnica se usa para determinar la antigüedad de fósiles orgánicos antiguos?",
                new String[]{"Espectroscopía de luz", "Datación por Carbono-14", "Ultrasonido de cuarzo", "Prueba de densidad del agua"},
                1,
                "Mide la desintegración del isótopo radiactivo del carbono en restos orgánicos.",
                "El método del Carbono-14 calcula el tiempo transcurrido desde la muerte de un ser vivo midiendo la abundancia del isótopo C-14.",
                100
        ));

        lista.add(new PreguntaCientifica(
                27, 3, "TECNOLOGIA",
                "¿Qué diferencia a las energías renovables (como eólica o solar) de los combustibles fósiles?",
                new String[]{"No se agotan y son limpias para el ambiente", "Contaminan el doble de aire", "Son extremadamente escasas", "Solo funcionan de noche"},
                0,
                "Son la clave para frenar el calentamiento global.",
                "Las fuentes renovables se regeneran continuamente por medios naturales y reducen la emisión de gases de efecto invernadero.",
                100
        ));


        // ==========================================
        // MUNDO 4: RETO MAESTRO DEL ING. ANDY (PREGUNTAS 28 a 36)
        // ==========================================
        lista.add(new PreguntaCientifica(
                28, 4, "CIENCIA",
                "El Ing. Andy investiga el tratamiento de aguas contaminadas. ¿Qué significa pH 7 en una muestra de agua pura?",
                new String[]{"Agua sumamente ácida", "pH neutro (agua pura equilibrada)", "Agua fuertemente alcalina", "Agua salada de mar"},
                1,
                "El punto medio de la escala de pH de 0 a 14.",
                "El pH 7 representa la neutralidad exacta donde las concentraciones de iones H+ e OH- son idénticas en agua pura a 25°C.",
                100
        ));

        lista.add(new PreguntaCientifica(
                29, 4, "CIENCIA",
                "¿Qué es la nanotecnología?",
                new String[]{"Tecnología para naves espaciales gigantes", "Manipulación de la materia a escala de átomos y moléculas (1 a 100 nanómetros)", "Estudio de las nubes de lluvia", "Creación de juguetes de madera"},
                1,
                "Un nanómetro es la milmillonésima parte de un metro.",
                "La nanotecnología trabaja con estructuras a nivel atómico para crear nuevos materiales con propiedades ópticas, eléctricas y catalíticas únicas.",
                100
        ));

        lista.add(new PreguntaCientifica(
                30, 4, "NATURALEZA",
                "¿Cuál es la diferencia entre un mineral y una roca?",
                new String[]{"Un mineral es una sustancia química pura con estructura cristalina y la roca es una mezcla de minerales", "Son exactamente lo mismo", "Los minerales son líquidos y las rocas sólidas", "Las rocas son artificiales"},
                0,
                "Piensa en los minerales como ingredientes puros y las rocas como el pastel horneado.",
                "Un mineral tiene una composición química homogénea y estructura cristalina definida, mientras que una roca es un agregado inorgánico natural de uno o varios minerales.",
                100
        ));

        lista.add(new PreguntaCientifica(
                31, 4, "TECNOLOGIA",
                "¿Cómo funciona un sensor ultrasónico utilizado en los robots móviles de Mini Científicos?",
                new String[]{"Midiendo la temperatura ambiental", "Emitiendo ondas de sonido de alta frecuencia y calculando el tiempo de eco al rebotar", "Capturando fotos a color", "Usando la gravedad del suelo"},
                1,
                "Funciona igual a cómo se orientan los murciélagos en la oscuridad.",
                "El sensor ultrasónico calcula distancias emitiendo pulsos de sonido inaudibles y midiendo la velocidad con que el eco regresa tras chocar contra un obstáculo.",
                100
        ));

        lista.add(new PreguntaCientifica(
                32, 4, "CIENCIA",
                "¿Qué es la bioluminiscencia en la biología marina?",
                new String[]{"Pintar peces con pintura reflectante", "Capacidad de algunos seres vivos de producir su propia luz mediante reacciones químicas internas", "La luz reflejada por las escamas del sol", "Focos de acuario"},
                1,
                "La luciferina y la luciferasa crean destellos mágicos en el mar profundo.",
                "La bioluminiscencia es la producción de luz por parte de organismos vivos resultante de una reacción química entre la proteína luciferina y la enzima luciferasa.",
                100
        ));

        lista.add(new PreguntaCientifica(
                33, 4, "NATURALEZA",
                "¿Qué es el proceso de eutrofización en lagos o ríos?",
                new String[]{"La congelación total del agua", "El exceso de nutrientes (como nitrógeno y fósforo) que agota el oxígeno para los peces", "La evaporación rápida por calor", "La purificación natural por rocas"},
                1,
                "Demasiado fertilizante en el agua hace crecer algas que asfixian la vida acuática.",
                "La eutrofización ocurre por acumulación excesiva de nutrientes en cuerpos de agua, provocando la proliferación masiva de algas que consumen el oxígeno disuelto.",
                100
        ));

        lista.add(new PreguntaCientifica(
                34, 4, "TECNOLOGIA",
                "¿En qué principio físico se basan las prensas hidráulicas para levantar cargas pesadas con poca fuerza?",
                new String[]{"Principio de Pascal", "Ley de Ohm", "Efecto Doppler", "Principio de Incertidumbre"},
                0,
                "La presión ejercida en un fluido incompresible se transmite con igual intensidad en todas las direcciones.",
                "El Principio de Pascal establece que la presión aplicada a un fluido encerrado se transmite íntegramente a todos los puntos del fluido y a las paredes del recipiente.",
                100
        ));

        lista.add(new PreguntaCientifica(
                35, 4, "CIENCIA",
                "¿Qué es un catalizador en una reacción química de conservación ambiental?",
                new String[]{"Una sustancia que se consume totalmente durante el proceso", "Una sustancia que acelera la reacción química sin consumirse en el proceso", "Un combustible derivado del petróleo", "Un recipiente de plástico resistente"},
                1,
                "Es un ayudante estrella que agiliza la reacción y queda intacto al final.",
                "Un catalizador reduce la energía de activación necesaria para una reacción química, acelerando su velocidad sin sufrir cambios químicos permanentes.",
                100
        ));

        lista.add(new PreguntaCientifica(
                36, 4, "TECNOLOGIA",
                "¿Cuál es el lema fundamental del Ing. Andy Salazar para todos los Mini Científicos del Perú?",
                new String[]{"'Solo los adultos pueden investigar'", "'Todos podemos ser científicos si usamos nuestra imaginación, curiosidad y ganas de aprender'", "'La ciencia es solo teoría de libros'", "'No hagan preguntas curiosas'"},
                1,
                "¡Recuerda siempre la frase inspiradora del fundador del ICSM!",
                "El lema oficial de Mini Científicos impulsa la educación experimental y la curiosidad innata como motor de transformación de las futuras generaciones.",
                100
        ));

        return lista;
    }
}