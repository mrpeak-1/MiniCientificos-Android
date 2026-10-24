package com.icsm.minicientificos_android.models;

import com.icsm.minicientificos_android.R;

import java.util.ArrayList;
import java.util.List;

public class PlanetaHotspotCatalog {

    public static List<PlanetaHotspotItem> getNodes() {
        List<PlanetaHotspotItem> list = new ArrayList<>();

        list.add(new PlanetaHotspotItem(
                "huerto",
                "Mini Huerto",
                "🍎",
                0f,
                0.85f,
                "#4CAF50",
                "¡El Reino Vegetal y Compostaje Vivo!",
                "Las plantas transforman la luz del sol y el agua en oxígeno limpio para que podamos respirar. ¡El compost convierte cáscaras de frutas en súper abono nutrido!",
                "Las raíces de las plantas se comunican bajo tierra compartiendo nutrientes con los árboles vecinos.",
                "Pequeños Botánicos y Compostaje",
                "¿Las plantas producen el oxígeno que respiramos usando la luz del sol?",
                true,
                R.drawable.compostaje
        ));

        list.add(new PlanetaHotspotItem(
                "energias",
                "Energías Renovables",
                "💨",
                51f,
                0.85f,
                "#FFC107",
                "¡Poder Limpio del Sol y del Viento!",
                "Las turbinas eólicas giran con el aire para generar luz sin humo ni contaminación. El sol envía fotones mágicos que los paneles solares transforman en energía.",
                "El viento del planeta en un solo día podría abastecer de electricidad a todo el mundo por semanas.",
                "Energías Renovables y Viento",
                "¿Las turbinas eólicas necesitan quemar gasolina para generar electricidad?",
                false,
                R.drawable.energias_reno
        ));

        list.add(new PlanetaHotspotItem(
                "reciclaje",
                "Reciclaje Creativo",
                "♻️",
                102f,
                0.85f,
                "#00A8E8",
                "¡Economía Circular y Eco-Inventos!",
                "Reutilizar plásticos y cartones evita que terminen en los océanos. Con creatividad, una botella usada se convierte en un divertido robot o cohete.",
                "Un envase de plástico tarda más de 400 años en descomponerse si no se recicla adecuadamente.",
                "Reciclaje Creativo y Eco-Arte",
                "¿Reciclar plástico ayuda a proteger la vida marina en los océanos?",
                true,
                R.drawable.reciclaje
        ));

        list.add(new PlanetaHotspotItem(
                "geologia",
                "Geología & Volcanes",
                "🌋",
                154f,
                0.85f,
                "#FF5722",
                "¡El Corazón Ardiente de la Tierra!",
                "El magma bajo la corteza terrestre busca salir a la superficie a través de volcanes, formando magma, cenizas y rocas ígneas brillantes.",
                "Más del 80% de la superficie de la Tierra es de origen volcánico.",
                "Geología y Volcanes en Reacción",
                "¿Las erupciones volcánicas crean rocas y nuevos terrenos en el planeta?",
                true,
                R.drawable.geologia
        ));

        list.add(new PlanetaHotspotItem(
                "hidroponia",
                "Hidroponía",
                "💧",
                205f,
                0.85f,
                "#0288D1",
                "¡Cultivos Futuro en Agua Nutritiva!",
                "Las plantas hidropónicas no necesitan tierra para crecer; sólo agua enriquecida con nutrientes minerales y suficiente luz.",
                "La hidroponía consume hasta un 90% menos de agua que la agricultura tradicional en tierra.",
                "Hidroponía y Cultivos Inteligentes",
                "¿La hidroponía requiere toneladas de tierra negra para que crezcan las lechugas?",
                false,
                R.drawable.hidroponia
        ));

        list.add(new PlanetaHotspotItem(
                "astronomia",
                "Astronomía",
                "🚀",
                257f,
                0.85f,
                "#9C27B0",
                "¡Viaje Cósmico a las Estrellas!",
                "El universo alberga miles de millones de galaxias, estrellas fugaces y planetas fascinantes esperando ser explorados.",
                "Un día en el planeta Venus dura más que un año entero en la Tierra.",
                "Astronomía y Exploración Espacial",
                "¿Los cohetes espaciales necesitan superar la fuerza de gravedad para salir de la Tierra?",
                true,
                R.drawable.astronomia
        ));

        list.add(new PlanetaHotspotItem(
                "fisica",
                "Física Loca",
                "⚛️",
                308f,
                0.85f,
                "#E91E63",
                "¡Las Leyes Secretas del Universo!",
                "Desde los fluidos no newtonianos que se vuelven duros como piedra al golpearlos, hasta la gravedad que nos mantiene en el suelo.",
                "Si pudieras viajar a la velocidad de la luz, darías 7 vueltas completas a la Tierra en un solo segundo.",
                "Experimentos Asombrosos de Física",
                "¿La fuerza de gravedad es la que evita que flotemos hacia el espacio exterior?",
                true,
                R.drawable.expe_fisi
        ));

        return list;
    }
}