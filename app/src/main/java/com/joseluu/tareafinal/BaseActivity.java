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
    protected void attachBaseContext(android.content.Context newBase) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        
        String fuente = prefs.getString("fuente", "2");

        float escala;
        switch (fuente) {
            case "1": escala = 0.85f; break; 
            case "3": escala = 1.30f; break; 
            default:  escala = 1.0f;  break; 
        }

        android.content.res.Configuration config = new android.content.res.Configuration(newBase.getResources().getConfiguration());
        config.fontScale = escala;

        
        android.content.Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        aplicarTema();
        
        super.onCreate(savedInstanceState);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        aplicarTamanoFuente();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (prefs != null) {
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("tema") || key.equals("fuente")) {
            
            
            recreate();
        }
    }

    
    private void aplicarTema() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaClaro = prefs.getBoolean("tema", true);
        
        if (temaClaro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    
    private void aplicarTamanoFuente() {
        String fuente = prefs.getString("fuente", "2");
        float tamano = 16f; 

        switch (fuente) {
            case "1": 
                tamano = 12f;
                break;
            case "3": 
                tamano = 20f;
                break;
        }
        
        
        
        
        
        
        
        
    }

    
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
