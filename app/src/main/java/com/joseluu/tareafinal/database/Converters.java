package com.joseluu.tareafinal.database;

import androidx.room.TypeConverter;

import com.joseluu.tareafinal.model.ArchivoAdjunto;

import java.util.Date;


public class Converters {

    
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    
    @TypeConverter
    public static ArchivoAdjunto.TipoArchivo fromString(String value) {
        return value == null ? null : ArchivoAdjunto.TipoArchivo.valueOf(value);
    }

    
    @TypeConverter
    public static String tipoArchivoToString(ArchivoAdjunto.TipoArchivo tipo) {
        return tipo == null ? null : tipo.name();
    }
}
