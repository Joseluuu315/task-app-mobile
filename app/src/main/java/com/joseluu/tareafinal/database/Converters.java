package com.joseluu.tareafinal.database;

import androidx.room.TypeConverter;

import com.joseluu.tareafinal.model.ArchivoAdjunto;

import java.util.Date;

/**
 * Type converters for ROOM database
 * Converts complex types to/from database primitives
 */
public class Converters {

    /**
     * Convert timestamp to Date object
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convert Date to timestamp
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Convert string to TipoArchivo enum
     */
    @TypeConverter
    public static ArchivoAdjunto.TipoArchivo fromString(String value) {
        return value == null ? null : ArchivoAdjunto.TipoArchivo.valueOf(value);
    }

    /**
     * Convert TipoArchivo enum to string
     */
    @TypeConverter
    public static String tipoArchivoToString(ArchivoAdjunto.TipoArchivo tipo) {
        return tipo == null ? null : tipo.name();
    }
}
