package com.icsm.minicientificos_android.models.game;

import java.io.Serializable;

public class PreguntaCientifica implements Serializable {

    private int id;
    private int nivelId;
    private String categoria; // "CIENCIA", "NATURALEZA", "TECNOLOGIA"
    private String pregunta;
    private String[] opciones;
    private int indiceRespuestaCorrecta; // 0 a 3
    private String pistaTesla;
    private String explicacionSabiasQue;
    private int puntosXp;

    public PreguntaCientifica() {
    }

    public PreguntaCientifica(int id, int nivelId, String categoria, String pregunta,
                              String[] opciones, int indiceRespuestaCorrecta,
                              String pistaTesla, String explicacionSabiasQue, int puntosXp) {
        this.id = id;
        this.nivelId = nivelId;
        this.categoria = categoria;
        this.pregunta = pregunta;
        this.opciones = opciones;
        this.indiceRespuestaCorrecta = indiceRespuestaCorrecta;
        this.pistaTesla = pistaTesla;
        this.explicacionSabiasQue = explicacionSabiasQue;
        this.puntosXp = puntosXp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(int nivelId) {
        this.nivelId = nivelId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public void setOpciones(String[] opciones) {
        this.opciones = opciones;
    }

    public int getIndiceRespuestaCorrecta() {
        return indiceRespuestaCorrecta;
    }

    public void setIndiceRespuestaCorrecta(int indiceRespuestaCorrecta) {
        this.indiceRespuestaCorrecta = indiceRespuestaCorrecta;
    }

    public String getPistaTesla() {
        return pistaTesla;
    }

    public void setPistaTesla(String pistaTesla) {
        this.pistaTesla = pistaTesla;
    }

    public String getExplicacionSabiasQue() {
        return explicacionSabiasQue;
    }

    public void setExplicacionSabiasQue(String explicacionSabiasQue) {
        this.explicacionSabiasQue = explicacionSabiasQue;
    }

    public int getPuntosXp() {
        return puntosXp;
    }

    public void setPuntosXp(int puntosXp) {
        this.puntosXp = puntosXp;
    }
}