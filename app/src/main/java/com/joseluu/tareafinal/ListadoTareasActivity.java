package com.joseluu.tareafinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private ActivityResultLauncher<Intent> editarTareaLauncher;

    private int posicionEditando = -1;
    private boolean mostrandoPrioritarias = false;
    private ArrayList<Tarea> copiaCompleta;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listado, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Lista Tareas");
        }

        datos = ManagerMethods.getInstance().getDatos();

        rvTareas = findViewById(R.id.rvTareas);
        txtNoTareas = findViewById(R.id.txtNoTareas);

        adaptador = new TareaAdapter(datos);
        rvTareas.setAdapter(adaptador);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        actualizerVisibilities();

        // CREAR
        crearTareaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null && data.hasExtra("TAREA_NUEVA")) {
                            Tarea nueva = data.getParcelableExtra("TAREA_NUEVA");

                            if (nueva != null) {
                                ManagerMethods.getInstance().addTarea(nueva);

                                adaptador.notifyItemInserted(0);
                                rvTareas.scrollToPosition(0);
                                actualizerVisibilities();
                            }
                        }
                    }
                }
        );

        // EDITAR
        editarTareaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null && data.hasExtra("TAREA_EDITADA") && posicionEditando != -1) {

                            Tarea editada = data.getParcelableExtra("TAREA_EDITADA");

                            if (editada != null) {
                                // Reemplazar en la lista
                                datos.set(posicionEditando, editada);

                                // Notificar al adaptador
                                adaptador.notifyItemChanged(posicionEditando);
                                actualizerVisibilities();
                            }
                        }
                    }
                }
        );

        // Pulsar botón crear
        FloatingActionButton btnCrearTarea = findViewById(R.id.btnCrearTarea);
        btnCrearTarea.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearTareaActivity.class);
            crearTareaLauncher.launch(intent);
        });

        adaptador.setOnEditListener((tarea, position, view) -> {
            posicionEditando = position;

            Intent intent = new Intent(this, EditarTareaActivity.class);
            intent.putExtra("TAREA_EDITAR", tarea);

            editarTareaLauncher.launch(intent);
        });

        adaptador.setOnDeleteListener(position -> {
            datos.remove(position);
            adaptador.notifyItemRemoved(position);
            actualizerVisibilities();
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.menu_add) {
            Intent intent = new Intent(this, CrearTareaActivity.class);
            crearTareaLauncher.launch(intent);
            return true;
        }

        if (id == R.id.menu_prioritarias) {
            alternarPrioritarias();
            return true;
        }

        if (id == R.id.menu_acerca) {
            mostrarAcercaDe();
            return true;
        }

        if (id == R.id.menu_salir) {
            Toast.makeText(this, "Hasta pronto", Toast.LENGTH_SHORT).show();
            finishAffinity();
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

    private void mostrarAcercaDe() {
        new AlertDialog.Builder(this)
                .setTitle("TrassTarea")
                .setMessage(
                        "TrassTarea\n\n" +
                                "IES Trassierra\n\n" +
                                "Autor: Jose Luque\n\n" +
                                "Año: 2025"
                )
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void alternarPrioritarias() {

        if (!mostrandoPrioritarias) {

            copiaCompleta = new ArrayList<>(datos);
            datos.clear();

            for (Tarea t : copiaCompleta) {
                if (t.isPrioritario()) {
                    datos.add(t);
                }
            }

            mostrandoPrioritarias = true;

        } else {

            datos.clear();
            datos.addAll(copiaCompleta);
            mostrandoPrioritarias = false;
        }

        adaptador.notifyDataSetChanged();
        actualizerVisibilities();
    }

}
