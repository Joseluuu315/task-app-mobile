package com.joseluu.tareafinal.manager;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.joseluu.tareafinal.model.Tarea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Clase de utilidad para aplicar las preferencias guardadas
 * en la aplicación de gestión de tareas
 */
public class PreferenciasHelper {

    /**
     * Obtiene el tamaño de fuente según la preferencia guardada
     * @param context Contexto de la aplicación
     * @return El tamaño de fuente en SP (12, 16 o 20)
     */
    public static float getTamanoFuente(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String fuente = prefs.getString("fuente", "2");

        switch (fuente) {
            case "1": // Pequeña
                return 12f;
            case "3": // Grande
                return 20f;
            case "2": // Mediana (default)
            default:
                return 16f;
        }
    }

    /**
     * Obtiene el tamaño de fuente para títulos según la preferencia guardada
     * @param context Contexto de la aplicación
     * @return El tamaño de fuente para títulos en SP
     */
    public static float getTamanoFuenteTitulo(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String fuente = prefs.getString("fuente", "2");

        switch (fuente) {
            case "1": // Pequeña
                return 16f;
            case "3": // Grande
                return 24f;
            case "2": // Mediana (default)
            default:
                return 20f;
        }
    }

    /**
     * Verifica si el tema es claro u oscuro
     * @param context Contexto de la aplicación
     * @return true si es tema claro, false si es oscuro
     */
    public static boolean isTemaClaro(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("tema", true);
    }

    /**
     * Verifica si el orden es ascendente o descendente
     * @param context Contexto de la aplicación
     * @return true si es ascendente, false si es descendente
     */
    public static boolean isOrdenAscendente(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("orden", true);
    }

    /**
     * Obtiene el criterio de ordenación seleccionado
     * @param context Contexto de la aplicación
     * @return El criterio (1=alfabético, 2=fecha creación, 3=días restantes, 4=progreso)
     */
    public static int getCriterioOrdenacion(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String criterio = prefs.getString("criterio", "2");
        return Integer.parseInt(criterio);
    }

    /**
     * Verifica si se debe usar almacenamiento en SD
     * @param context Contexto de la aplicación
     * @return true si se debe usar SD, false para memoria interna
     */
    public static boolean usarAlmacenamientoSD(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("sd", false);
    }

    /**
     * Ordena una lista de tareas según las preferencias guardadas
     * @param context Contexto de la aplicación
     * @param tareas Lista de tareas a ordenar
     */
    public static void ordenarTareas(Context context, ArrayList<Tarea> tareas) {
        int criterio = getCriterioOrdenacion(context);
        boolean ascendente = isOrdenAscendente(context);

        Comparator<Tarea> comparador = null;

        switch (criterio) {
            case 1: // Alfabético
                comparador = (t1, t2) -> {
                    int result = t1.getTitulo().compareToIgnoreCase(t2.getTitulo());
                    return ascendente ? result : -result;
                };
                break;

            case 2: // Fecha de creación
                comparador = (t1, t2) -> {
                    int result = t1.getFechaCreacion().compareTo(t2.getFechaCreacion());
                    return ascendente ? result : -result;
                };
                break;

            case 3: // Días restantes
                comparador = (t1, t2) -> {
                    long dias1 = calcularDiasRestantes(t1);
                    long dias2 = calcularDiasRestantes(t2);
                    int result = Long.compare(dias1, dias2);
                    return ascendente ? result : -result;
                };
                break;

            case 4: // Progreso
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

    /**
     * Calcula los días restantes hasta la fecha objetivo
     * @param tarea Tarea de la que calcular los días
     * @return Número de días restantes (negativo si ha pasado la fecha)
     */
    private static long calcularDiasRestantes(Tarea tarea) {
        long diffMillis = tarea.getFechaObjectivo().getTime() - System.currentTimeMillis();
        return diffMillis / (1000 * 60 * 60 * 24);
    }

    /**
     * Obtiene la ruta de almacenamiento según la preferencia
     * @param context Contexto de la aplicación
     * @return Ruta de almacenamiento
     */
    public static String getRutaAlmacenamiento(Context context) {
        if (usarAlmacenamientoSD(context)) {
            // Intentar obtener almacenamiento externo
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                return context.getExternalFilesDir(null).getAbsolutePath();
            }
        }
        // Almacenamiento interno por defecto
        return context.getFilesDir().getAbsolutePath();
    }
}
