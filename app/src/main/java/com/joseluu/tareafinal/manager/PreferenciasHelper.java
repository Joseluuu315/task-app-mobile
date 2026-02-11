package com.joseluu.tareafinal.manager;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.joseluu.tareafinal.model.Tarea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PreferenciasHelper {

    
    public static float getTamanoFuente(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String fuente = prefs.getString("fuente", "2");

        switch (fuente) {
            case "1": 
                return 12f;
            case "3": 
                return 20f;
            case "2": 
            default:
                return 16f;
        }
    }

    
    public static float getTamanoFuenteTitulo(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String fuente = prefs.getString("fuente", "2");

        switch (fuente) {
            case "1": 
                return 16f;
            case "3": 
                return 24f;
            case "2": 
            default:
                return 20f;
        }
    }

    
    public static boolean isTemaClaro(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("tema", true);
    }

    
    public static boolean isOrdenAscendente(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("orden", true);
    }

    
    public static int getCriterioOrdenacion(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String criterio = prefs.getString("criterio", "2");
        return Integer.parseInt(criterio);
    }

    
    public static boolean usarAlmacenamientoSD(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("sd", false);
    }

    
    public static void ordenarTareas(Context context, ArrayList<Tarea> tareas) {
        int criterio = getCriterioOrdenacion(context);
        boolean ascendente = isOrdenAscendente(context);

        Comparator<Tarea> comparador = null;

        switch (criterio) {
            case 1: 
                comparador = (t1, t2) -> {
                    int result = t1.getTitulo().compareToIgnoreCase(t2.getTitulo());
                    return ascendente ? result : -result;
                };
                break;

            case 2: 
                comparador = (t1, t2) -> {
                    int result = t1.getFechaCreacion().compareTo(t2.getFechaCreacion());
                    return ascendente ? result : -result;
                };
                break;

            case 3: 
                comparador = (t1, t2) -> {
                    long dias1 = calcularDiasRestantes(t1);
                    long dias2 = calcularDiasRestantes(t2);
                    int result = Long.compare(dias1, dias2);
                    return ascendente ? result : -result;
                };
                break;

            case 4: 
                comparador = (t1, t2) -> {
                    int result = Integer.compare(t1.getProgreso(), t2.getProgreso());
                    return ascendente ? result : -result;
                };
                break;
        }

        if (comparador != null) {
            Collections.sort(tareas, comparador);
        }
    }

    
    private static long calcularDiasRestantes(Tarea tarea) {
        long diffMillis = tarea.getFechaObjectivo().getTime() - System.currentTimeMillis();
        return diffMillis / (1000 * 60 * 60 * 24);
    }

    
    public static String getRutaAlmacenamiento(Context context) {
        if (usarAlmacenamientoSD(context)) {
            
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                return context.getExternalFilesDir(null).getAbsolutePath();
            }
        }
        
        return context.getFilesDir().getAbsolutePath();
    }
}
