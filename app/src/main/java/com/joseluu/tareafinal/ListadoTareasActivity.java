package com.joseluu.tareafinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.adapter.TareaAdapter;
import com.joseluu.tareafinal.fragment.FragmentoPasoDos;
import com.joseluu.tareafinal.model.Tarea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ListadoTareasActivity extends AppCompatActivity {
    private ArrayList<Tarea> datos = new ArrayList<>();
    private RecyclerView rvTareas;
    private TextView txtNoTareas;

    // Declarar un launcher
    private ActivityResultLauncher<Intent> crearTareaLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);
        datos = new MainActivity().getDatos();
        new MainActivity().setDatos(datos);

        rvTareas = findViewById(R.id.rvTareas);
        txtNoTareas = findViewById(R.id.txtNoTareas);


        TareaAdapter adaptador = new TareaAdapter(datos);
        rvTareas.setAdapter(adaptador);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        actualizarVisibilidad();

        crearTareaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("TAREA_NUEVA")) {
                            Tarea nueva = data.getParcelableExtra("TAREA_NUEVA");
                            if (nueva != null) {
                                datos.add(0, nueva); // agregar al inicio
                                rvTareas.getAdapter().notifyItemInserted(0);
                                actualizarVisibilidad();
                                rvTareas.scrollToPosition(0);
                            }
                        }
                    }
                }
        );


        com.google.android.material.floatingactionbutton.FloatingActionButton btnCrearTarea = findViewById(R.id.btnCrearTarea);
        btnCrearTarea.setOnClickListener(v -> {
            Intent intent = new Intent(ListadoTareasActivity.this, CrearTareaActivity.class);
            crearTareaLauncher.launch(intent);
        });


    }


    /*
    In this methods i can create data to see RecyclerView
     */
    public ArrayList<Tarea> init(){
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

        return datos;
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