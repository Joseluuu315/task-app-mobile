package com.joseluu.tareafinal;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;


public class BaseActivity extends AppCompatActivity 
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar tema antes de super.onCreate()
        aplicarTema();
        
        super.onCreate(savedInstanceState);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Registrar listener para cambios en las preferencias
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aplicar tamaño de fuente cuando la actividad se reanuda
        aplicarTamanoFuente();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistrar listener para evitar memory leaks
        if (prefs != null) {
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    /**
     * Se llama cuando cambia alguna preferencia
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("tema")) {
            // Recrear la actividad para aplicar el nuevo tema
            recreate();
        } else if (key.equals("fuente")) {
            // Aplicar el nuevo tamaño de fuente
            aplicarTamanoFuente();
        }
    }

    /**
     * Aplica el tema según la preferencia guardada
     */
    private void aplicarTema() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaClaro = prefs.getBoolean("tema", true);
        
        if (temaClaro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    /**
     * Aplica el tamaño de fuente a todos los TextViews de la actividad
     * Este es un ejemplo básico. Para un control más fino, considera usar estilos.
     */
    private void aplicarTamanoFuente() {
        String fuente = prefs.getString("fuente", "2");
        float tamano = 16f; // Mediana por defecto
        
        switch (fuente) {
            case "1": // Pequeña
                tamano = 12f;
                break;
            case "3": // Grande
                tamano = 20f;
                break;
        }
        
        // Este es un ejemplo simple. En una aplicación real, podrías
        // recorrer todas las vistas y aplicar el tamaño, o mejor aún,
        // usar estilos y themes personalizados.
        
        // Nota: Para aplicar a vistas específicas, hazlo en cada Activity:
        // TextView textView = findViewById(R.id.miTextView);
        // textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamano);
    }

    /**
     * Método helper para obtener el tamaño de fuente actual
     * @return Tamaño en SP
     */
    protected float getTamanoFuenteActual() {
        String fuente = prefs.getString("fuente", "2");
        switch (fuente) {
            case "1":
                return 12f;
            case "3":
                return 20f;
            default:
                return 16f;
        }
    }

    /**
     * Método helper para obtener el tamaño de fuente para títulos
     * @return Tamaño en SP
     */
    protected float getTamanoFuenteTituloActual() {
        String fuente = prefs.getString("fuente", "2");
        switch (fuente) {
            case "1":
                return 16f;
            case "3":
                return 24f;
            default:
                return 20f;
        }
    }
}
