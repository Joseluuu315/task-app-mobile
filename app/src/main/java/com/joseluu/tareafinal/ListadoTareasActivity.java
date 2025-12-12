package com.joseluu.tareafinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joseluu.tareafinal.adapter.TareaAdapter;
import com.joseluu.tareafinal.manager.ManagerMethods;
import com.joseluu.tareafinal.model.Tarea;

import java.util.ArrayList;

public class ListadoTareasActivity extends AppCompatActivity {

    private RecyclerView rvTareas;
    private TextView txtNoTareas;

    private TareaAdapter adaptador;
    private ArrayList<Tarea> datos;

    private ActivityResultLauncher<Intent> crearTareaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Lista Tareas");
        }

        // Obtener siempre la MISMA lista desde el Singleton
        datos = ManagerMethods.getInstance().getDatos();

        rvTareas = findViewById(R.id.rvTareas);
        txtNoTareas = findViewById(R.id.txtNoTareas);

        // Crear adaptador usando la misma lista (misma referencia)
        adaptador = new TareaAdapter(datos);
        rvTareas.setAdapter(adaptador);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        actualizerVisibilities();

        // Registrar launcher para recibir la tarea creada
        crearTareaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null && data.hasExtra("TAREA_NUEVA")) {

                            Tarea nueva = data.getParcelableExtra("TAREA_NUEVA");

                            if (nueva != null) {
                                // Añadir a la lista del Singleton (MISMA lista del adapter)
                                ManagerMethods.getInstance().addTarea(nueva);

                                // Notificar al adaptador
                                adaptador.notifyItemInserted(0);

                                rvTareas.scrollToPosition(0);
                                actualizerVisibilities();
                            }
                        }
                    }
                }
        );

        // Botón para crear tarea
        FloatingActionButton btnCrearTarea = findViewById(R.id.btnCrearTarea);
        btnCrearTarea.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearTareaActivity.class);
            crearTareaLauncher.launch(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void actualizerVisibilities() {
        if (datos.isEmpty()) {
            rvTareas.setVisibility(View.GONE);
            txtNoTareas.setVisibility(View.VISIBLE);
        } else {
            rvTareas.setVisibility(View.VISIBLE);
            txtNoTareas.setVisibility(View.GONE);
        }
    }
}
