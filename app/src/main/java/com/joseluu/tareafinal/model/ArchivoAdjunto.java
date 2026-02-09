package com.joseluu.tareafinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Modelo para archivos adjuntos a tareas
 * Soporta 4 tipos: DOCUMENTO, IMAGEN, AUDIO, VIDEO
 */
public class ArchivoAdjunto implements Parcelable {

    private int id;
    private int tareaId; // Foreign key to Tarea
    private TipoArchivo tipo;
    private String nombreArchivo;
    private String rutaArchivo; // Absolute path to file

    /**
     * Tipos de archivos adjuntos soportados
     */
    public enum TipoArchivo {
        DOCUMENTO, IMAGEN, AUDIO, VIDEO
    }

    // Constructor
    public ArchivoAdjunto(int id, int tareaId, TipoArchivo tipo, String nombreArchivo, String rutaArchivo) {
        this.id = id;
        this.tareaId = tareaId;
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
    }

    // Constructor sin ID (para nuevos archivos)
    public ArchivoAdjunto(int tareaId, TipoArchivo tipo, String nombreArchivo, String rutaArchivo) {
        this(0, tareaId, tipo, nombreArchivo, rutaArchivo);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTareaId() {
        return tareaId;
    }

    public void setTareaId(int tareaId) {
        this.tareaId = tareaId;
    }

    public TipoArchivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoArchivo tipo) {
        this.tipo = tipo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tareaId);
        dest.writeString(tipo.name());
        dest.writeString(nombreArchivo);
        dest.writeString(rutaArchivo);
    }

    protected ArchivoAdjunto(Parcel in) {
        id = in.readInt();
        tareaId = in.readInt();
        tipo = TipoArchivo.valueOf(in.readString());
        nombreArchivo = in.readString();
        rutaArchivo = in.readString();
    }

    public static final Creator<ArchivoAdjunto> CREATOR = new Creator<ArchivoAdjunto>() {
        @Override
        public ArchivoAdjunto createFromParcel(Parcel in) {
            return new ArchivoAdjunto(in);
        }

        @Override
        public ArchivoAdjunto[] newArray(int size) {
            return new ArchivoAdjunto[size];
        }
    };
}
