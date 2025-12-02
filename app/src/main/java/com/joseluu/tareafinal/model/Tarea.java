package com.joseluu.tareafinal.model;

import java.util.Date;

public class Tarea {

    private String titulo, descripcion;
    private int progreso;
    private Date fechaCreacion, fechaObjectivo;
    private boolean prioritario;

    public Tarea(String titulo, String descripcion, int progreso, Date fechaCreacion, Date fechaObjectivo, boolean prioritario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjectivo = fechaObjectivo;
        this.prioritario = prioritario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaObjectivo() {
        return fechaObjectivo;
    }

    public void setFechaObjectivo(Date fechaObjectivo) {
        this.fechaObjectivo = fechaObjectivo;
    }

    public boolean isPrioritario() {
        return prioritario;
    }

    public void setPrioritario(boolean prioritario) {
        this.prioritario = prioritario;
    }
}
