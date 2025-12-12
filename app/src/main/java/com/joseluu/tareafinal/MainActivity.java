package com.joseluu.tareafinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.joseluu.tareafinal.manager.ManagerMethods;
import com.joseluu.tareafinal.model.Tarea;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> crearTareaDesdeMainLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
