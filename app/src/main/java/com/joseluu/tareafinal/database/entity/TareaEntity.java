package com.joseluu.tareafinal.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "tareas")
public class TareaEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    @NonNull
    @ColumnInfo(name = "fecha_creacion")
    private Date fechaCreacion;

    @NonNull
    @ColumnInfo(name = "fecha_objetivo")
    private Date fechaObjetivo;

    @ColumnInfo(name = "progreso", defaultValue = "0")
    private int progreso;

    @ColumnInfo(name = "prioritaria", defaultValue = "0")
    private boolean prioritaria;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    
    @ColumnInfo(name = "url_doc")
    private String urlDoc;

    @ColumnInfo(name = "url_img")
    private String urlImg;

    @ColumnInfo(name = "url_aud")
    private String urlAud;

    @ColumnInfo(name = "url_vid")
    private String urlVid;

    
    public TareaEntity(@NonNull String titulo, @NonNull Date fechaCreacion, @NonNull Date fechaObjetivo,
            int progreso, boolean prioritaria, String descripcion,
            String urlDoc, String urlImg, String urlAud, String urlVid) {
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjetivo = fechaObjetivo;
        this.progreso = progreso;
        this.prioritaria = prioritaria;
        this.descripcion = descripcion;
        this.urlDoc = urlDoc;
        this.urlImg = urlImg;
        this.urlAud = urlAud;
        this.urlVid = urlVid;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NonNull String titulo) {
        this.titulo = titulo;
    }

    @NonNull
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(@NonNull Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @NonNull
    public Date getFechaObjetivo() {
        return fechaObjetivo;
    }

    public void setFechaObjetivo(@NonNull Date fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlDoc() {
        return urlDoc;
    }

    public void setUrlDoc(String urlDoc) {
        this.urlDoc = urlDoc;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getUrlAud() {
        return urlAud;
    }

    public void setUrlAud(String urlAud) {
        this.urlAud = urlAud;
    }

    public String getUrlVid() {
        return urlVid;
    }

    public void setUrlVid(String urlVid) {
        this.urlVid = urlVid;
    }
}
