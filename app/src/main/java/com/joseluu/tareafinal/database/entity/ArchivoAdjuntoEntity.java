package com.joseluu.tareafinal.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.joseluu.tareafinal.model.ArchivoAdjunto;

/**
 * ROOM Entity representing file attachments in the database
 */
@Entity(tableName = "archivos_adjuntos", foreignKeys = @ForeignKey(entity = TareaEntity.class, parentColumns = "id", childColumns = "tarea_id", onDelete = ForeignKey.CASCADE), indices = {
        @Index("tarea_id") })
public class ArchivoAdjuntoEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "tarea_id")
    private int tareaId;

    @NonNull
    @ColumnInfo(name = "tipo")
    private ArchivoAdjunto.TipoArchivo tipo;

    @NonNull
    @ColumnInfo(name = "nombre_archivo")
    private String nombreArchivo;

    @NonNull
    @ColumnInfo(name = "ruta_archivo")
    private String rutaArchivo;

    // Constructor
    public ArchivoAdjuntoEntity(int tareaId, @NonNull ArchivoAdjunto.TipoArchivo tipo,
            @NonNull String nombreArchivo, @NonNull String rutaArchivo) {
        this.tareaId = tareaId;
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
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

    @NonNull
    public ArchivoAdjunto.TipoArchivo getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull ArchivoAdjunto.TipoArchivo tipo) {
        this.tipo = tipo;
    }

    @NonNull
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(@NonNull String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @NonNull
    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(@NonNull String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}
