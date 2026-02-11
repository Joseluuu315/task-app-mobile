package com.joseluu.tareafinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class ArchivoAdjunto implements Parcelable {

    private int id;
    private int tareaId; 
    private TipoArchivo tipo;
    private String nombreArchivo;
    private String rutaArchivo; 

    
    public enum TipoArchivo {
        DOCUMENTO, IMAGEN, AUDIO, VIDEO
    }

    
    public ArchivoAdjunto(int id, int tareaId, TipoArchivo tipo, String nombreArchivo, String rutaArchivo) {
        this.id = id;
        this.tareaId = tareaId;
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
    }

    
    public ArchivoAdjunto(int tareaId, TipoArchivo tipo, String nombreArchivo, String rutaArchivo) {
        this(0, tareaId, tipo, nombreArchivo, rutaArchivo);
    }

    
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
