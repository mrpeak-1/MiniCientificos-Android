package com.icsm.minicientificos_android.models;

import java.util.ArrayList;
import java.util.List;

public class PildorasTallerHomeCatalog {

    public static List<PildoraTallerHome> getPildoras() {
        List<PildoraTallerHome> list = new ArrayList<>();

        // Fila 1
        list.add(new PildoraTallerHome("Astronomía", "PRESENCIAL", "#EA580C", "#F3F4FF", "#818CF8", "🔭"));
        list.add(new PildoraTallerHome("Microbiología", "VIRTUAL", "#0284C7", "#F0F9FF", "#38BDF8", "🔬"));

        // Fila 2
        list.add(new PildoraTallerHome("Bobina Tesla", "PRESENCIAL", "#EA580C", "#FFFBEB", "#F59E0B", "⚡"));
        list.add(new PildoraTallerHome("Minería del Futuro", "VIRTUAL", "#0284C7", "#FEF2F2", "#F87171", "⛑️"));

        // Fila 3
        list.add(new PildoraTallerHome("Arqueología", "VIRTUAL", "#0284C7", "#FFF7ED", "#FB923C", "⛏️"));
        list.add(new PildoraTallerHome("Reciclaje Ecológico", "VIRTUAL", "#0284C7", "#F0FDF4", "#4ADE80", "🌱"));

        // Fila 4
        list.add(new PildoraTallerHome("Geología Divertida", "VIRTUAL", "#0284C7", "#FAF5FF", "#C084FC", "🧭"));
        list.add(new PildoraTallerHome("Mini Huerto", "VIRTUAL", "#0284C7", "#F7FEE7", "#A3E635", "🍎"));

        // Fila 5
        list.add(new PildoraTallerHome("Física Loca", "VIRTUAL", "#0284C7", "#ECFDF5", "#34D399", "🧪"));
        list.add(new PildoraTallerHome("Hidroponía", "VIRTUAL", "#0284C7", "#EFF6FF", "#60A5FA", "💧"));

        // Fila 6
        list.add(new PildoraTallerHome("Inteligencia Artificial", "VIRTUAL", "#0284C7", "#FDF2F8", "#F472B6", "🤖"));
        list.add(new PildoraTallerHome("Energías Renovables", "VIRTUAL", "#0284C7", "#FEFCE8", "#FACC15", "☀️"));

        return list;
    }
}