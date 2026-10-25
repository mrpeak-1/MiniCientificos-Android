package com.icsm.minicientificos_android.models;

import java.util.ArrayList;
import java.util.List;

public class PildorasTallerHomeCatalog {

    public static List<PildoraTallerHome> getPildoras() {
        List<PildoraTallerHome> list = new ArrayList<>();

        list.add(new PildoraTallerHome("Astronomía", "🔭", "#7C4DFF"));
        list.add(new PildoraTallerHome("Microbiología", "🔬", "#00A8E8"));
        list.add(new PildoraTallerHome("Bobina Tesla", "⚡", "#FFB300"));
        list.add(new PildoraTallerHome("Minería del Futuro", "⛑️", "#FF5252"));
        list.add(new PildoraTallerHome("Arqueología", "⛏️", "#FB8C00"));
        list.add(new PildoraTallerHome("Reciclaje Ecológico", "🌱", "#43A047"));
        list.add(new PildoraTallerHome("Geología Divertida", "🧭", "#8E24AA"));
        list.add(new PildoraTallerHome("Mini Huerto", "🍎", "#7CB342"));
        list.add(new PildoraTallerHome("Física Loca", "🧪", "#00BFA5"));
        list.add(new PildoraTallerHome("Hidroponía", "💧", "#039BE5"));
        list.add(new PildoraTallerHome("Inteligencia Artificial", "🤖", "#EC407A"));
        list.add(new PildoraTallerHome("Energías Renovables", "☀️", "#FDD835"));

        return list;
    }
}