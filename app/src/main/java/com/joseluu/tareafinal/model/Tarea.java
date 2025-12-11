package com.joseluu.tareafinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Tarea implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeInt(progreso);
        dest.writeLong(fechaCreacion != null ? fechaCreacion.getTime() : 0);
        dest.writeLong(fechaObjectivo != null ? fechaObjectivo.getTime() : 0);
        dest.writeByte((byte) (prioritario ? 1 : 0));
    }

    protected Tarea(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        progreso = in.readInt();
        fechaCreacion = new Date(in.readLong());
        fechaObjectivo = new Date(in.readLong());
        prioritario = in.readByte() != 0;
    }

    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };
}
