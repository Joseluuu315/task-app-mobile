package com.joseluu.tareafinal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.joseluu.tareafinal.manager.ManagerMethods;
import com.joseluu.tareafinal.model.Tarea;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> crearTareaDesdeMainLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();

        // Ocultar la barra de acción
        if (actionBar != null) {
            actionBar.hide();
        }


        Button btnEmpezar = findViewById(R.id.btnEmpezar);
        Button btnCrearActividad = findViewById(R.id.btnCrearTarea);
        Button btnChangeThemes = findViewById(R.id.btnThemesChange);

        crearTareaDesdeMainLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                if (data != null && data.hasExtra("TAREA_NUEVA")) {
                                    Tarea nueva = data.getParcelableExtra("TAREA_NUEVA");
                                    if (nueva != null) {
                                        // Añadir la tarea al Singleton
                                        ManagerMethods.getInstance().addTarea(nueva);
                                    }
                                }
                            }
                        }
                );

        // Ir a listado
        btnEmpezar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListadoTareasActivity.class);
            startActivity(intent);
        });

        // Crear tarea DESDE MAIN
        btnCrearActividad.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearTareaActivity.class);
            crearTareaDesdeMainLauncher.launch(intent);
        });

        // Cambiar tema
        btnChangeThemes.setOnClickListener(v -> {
            int current = AppCompatDelegate.getDefaultNightMode();

            if (current == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        Button btnLanguage = findViewById(R.id.btnLanguage);

        btnLanguage.setOnClickListener(v -> {
            Locale current = getResources().getConfiguration().getLocales().get(0);

            if (current.getLanguage().equals("es")) {
                setLocale("en");
            } else {
                setLocale("es");
            }
        });
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        resources.updateConfiguration(config, resources.getDisplayMetrics());

        recreate();
    }

}
