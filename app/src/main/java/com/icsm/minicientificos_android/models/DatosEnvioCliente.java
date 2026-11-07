package com.icsm.minicientificos_android.models;

public class DatosEnvioCliente {
    private String nombreCompleto;
    private String celular;
    private String correo;
    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;
    private boolean recordarDatos;

    public DatosEnvioCliente() {
        this.nombreCompleto = "";
        this.celular = "";
        this.correo = "";
        this.direccion = "";
        this.distrito = "";
        this.provincia = "";
        this.departamento = "";
        this.recordarDatos = false;
    }

    public DatosEnvioCliente(String nombreCompleto, String celular, String correo, String direccion,
                             String distrito, String provincia, String departamento, boolean recordarDatos) {
        this.nombreCompleto = nombreCompleto != null ? nombreCompleto : "";
        this.celular = celular != null ? celular : "";
        this.correo = correo != null ? correo : "";
        this.direccion = direccion != null ? direccion : "";
        this.distrito = distrito != null ? distrito : "";
        this.provincia = provincia != null ? provincia : "";
        this.departamento = departamento != null ? departamento : "";
        this.recordarDatos = recordarDatos;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public boolean isRecordarDatos() {
        return recordarDatos;
    }

    public void setRecordarDatos(boolean recordarDatos) {
        this.recordarDatos = recordarDatos;
    }
}