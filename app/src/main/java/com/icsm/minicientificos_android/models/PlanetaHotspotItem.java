package com.icsm.minicientificos_android.models;

import java.io.Serializable;

public class PlanetaHotspotItem implements Serializable {

    private final String id;
    private final String nombreExperimento;
    private final String emojiIcono;
    private final float anguloObjetivoGrados;
    private final float radioOrbitaPorcentaje;
    private final String colorHex;
    private final String tituloCuriosidadCientifica;
    private final String explicacionParaNinos;
    private final String datoSabiasQue;
    private final String tallerAsociadoNombre;
    private final String retoRapidoPregunta;
    private final boolean respuestaCorrectaVerdadera;
    private final int drawableResId;

    public PlanetaHotspotItem(String id, String nombreExperimento, String emojiIcono,
                              float anguloObjetivoGrados, float radioOrbitaPorcentaje,
                              String colorHex, String tituloCuriosidadCientifica,
                              String explicacionParaNinos, String datoSabiasQue,
                              String tallerAsociadoNombre, String retoRapidoPregunta,
                              boolean respuestaCorrectaVerdadera, int drawableResId) {
        this.id = id;
        this.nombreExperimento = nombreExperimento;
        this.emojiIcono = emojiIcono;
        this.anguloObjetivoGrados = anguloObjetivoGrados;
        this.radioOrbitaPorcentaje = radioOrbitaPorcentaje;
        this.colorHex = colorHex;
        this.tituloCuriosidadCientifica = tituloCuriosidadCientifica;
        this.explicacionParaNinos = explicacionParaNinos;
        this.datoSabiasQue = datoSabiasQue;
        this.tallerAsociadoNombre = tallerAsociadoNombre;
        this.retoRapidoPregunta = retoRapidoPregunta;
        this.respuestaCorrectaVerdadera = respuestaCorrectaVerdadera;
        this.drawableResId = drawableResId;
    }

    public String getId() { return id; }
    public String getNombreExperimento() { return nombreExperimento; }
    public String getEmojiIcono() { return emojiIcono; }
    public float getAnguloObjetivoGrados() { return anguloObjetivoGrados; }
    public float getRadioOrbitaPorcentaje() { return radioOrbitaPorcentaje; }
    public String getColorHex() { return colorHex; }
    public String getTituloCuriosidadCientifica() { return tituloCuriosidadCientifica; }
    public String getExplicacionParaNinos() { return explicacionParaNinos; }
    public String getDatoSabiasQue() { return datoSabiasQue; }
    public String getTallerAsociadoNombre() { return tallerAsociadoNombre; }
    public String getRetoRapidoPregunta() { return retoRapidoPregunta; }
    public boolean isRespuestaCorrectaVerdadera() { return respuestaCorrectaVerdadera; }
    public int getDrawableResId() { return drawableResId; }
}