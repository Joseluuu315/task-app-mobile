package com.joseluu.tareafinal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.adapter.TareaAdapter;
import com.joseluu.tareafinal.model.Tarea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ListadoTareasActivity extends AppCompatActivity {
    private final ArrayList<Tarea> datos = new ArrayList<>();
    private RecyclerView rvTareas;
    private TextView txtNoTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        // Referencias al layout
        rvTareas = findViewById(R.id.rvTareas);
        txtNoTareas = findViewById(R.id.txtNoTareas);

        // Crear datos de ejemplo
        init();

        // Configurar RecyclerView
        TareaAdapter adaptador = new TareaAdapter(datos);
        rvTareas.setAdapter(adaptador);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        // Mostrar / ocultar según si hay tareas
        actualizarVisibilidad();

        rvTareas.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                // No necesitas nada aquí, pero está bien definido
            }
        });

    }

    /*
    In this methods i can create data to see RecyclerView
     */
    public void init(){
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {

            // Fecha creación: en los últimos 30 días
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -random.nextInt(30));  // 0 a 29 días atrás
            Date fechaCreacion = cal.getTime();

            // Fecha objetivo: 1 a 30 días después de la creación
            cal.add(Calendar.DAY_OF_YEAR, random.nextInt(30) + 1); // 1 a 30 días después
            Date fechaObjetivo = cal.getTime();

            datos.add(new Tarea(
                    "Tarea " + i,
                    "Descripción de la tarea número " + i,
                    (i * 5) % 100,           // progreso 0 a 100
                    fechaCreacion,           // aleatoria
                    fechaObjetivo,           // aleatoria coherente
                    i % 2 == 0               // prioridad alternada
            ));
        }

    }


        private void actualizarVisibilidad() {
        if (datos.isEmpty()) {
            rvTareas.setVisibility(View.GONE);
            txtNoTareas.setVisibility(View.VISIBLE);
        } else {
            rvTareas.setVisibility(View.VISIBLE);
            txtNoTareas.setVisibility(View.GONE);
        }
    }
}