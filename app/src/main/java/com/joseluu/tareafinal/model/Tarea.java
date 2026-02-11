package com.joseluu.tareafinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tarea implements Parcelable {

    private int id; 
    private String titulo, descripcion;
    private int progreso;
    private Date fechaCreacion, fechaObjectivo;
    private boolean prioritario;
    private List<ArchivoAdjunto> archivosAdjuntos; 

    public Tarea(String titulo, String descripcion, int progreso, Date fechaCreacion, Date fechaObjectivo,
            boolean prioritario) {
        this.id = 0; 
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjectivo = fechaObjectivo;
        this.prioritario = prioritario;
        this.archivosAdjuntos = new ArrayList<>();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ArchivoAdjunto> getArchivosAdjuntos() {
        return archivosAdjuntos;
    }

    public void setArchivosAdjuntos(List<ArchivoAdjunto> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }

    public void addArchivoAdjunto(ArchivoAdjunto archivo) {
        if (this.archivosAdjuntos == null) {
            this.archivosAdjuntos = new ArrayList<>();
        }
        this.archivosAdjuntos.add(archivo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeInt(progreso);
        dest.writeLong(fechaCreacion != null ? fechaCreacion.getTime() : 0);
        dest.writeLong(fechaObjectivo != null ? fechaObjectivo.getTime() : 0);
        dest.writeByte((byte) (prioritario ? 1 : 0));
        dest.writeTypedList(archivosAdjuntos);
    }

    protected Tarea(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        descripcion = in.readString();
        progreso = in.readInt();
        fechaCreacion = new Date(in.readLong());
        fechaObjectivo = new Date(in.readLong());
        prioritario = in.readByte() != 0;
        archivosAdjuntos = new ArrayList<>();
        in.readTypedList(archivosAdjuntos, ArchivoAdjunto.CREATOR);
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
