package com.joseluu.tareafinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joseluu.tareafinal.adapter.TareaAdapter;
import com.joseluu.tareafinal.manager.ManagerMethods;
import com.joseluu.tareafinal.model.Tarea;
import com.joseluu.tareafinal.repository.TareaRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListadoTareasActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView rvTareas;
    private TextView txtNoTareas;

    private TareaAdapter adaptador;
    private ArrayList<Tarea> datos;

    private ActivityResultLauncher<Intent> crearTareaLauncher;
    private ActivityResultLauncher<Intent> editarTareaLauncher;

    private int posicionEditando = -1;
    private boolean mostrandoPrioritarias = false;
    private ArrayList<Tarea> copiaCompleta;

    private SharedPreferences prefs;

    private TareaRepository repository;

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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Lista Tareas");
        }

        // Inicializar repositorio
        repository = TareaRepository.getInstance(this);

        // Inicializar preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Inicializar lista vacía
        datos = new ArrayList<>();

        rvTareas = findViewById(R.id.rvTareas);
        txtNoTareas = findViewById(R.id.txtNoTareas);

        adaptador = new TareaAdapter(datos);
        rvTareas.setAdapter(adaptador);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos en onResume

        // CREAR
        crearTareaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // La tarea ya se guardó en BD, al volver onResume recargará la lista
                        Toast.makeText(this, "Tarea creada", Toast.LENGTH_SHORT).show();
                    }
                });

        // EDITAR
        editarTareaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // La tarea ya se actualizó en BD, al volver onResume recargará la lista
                        Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                    }
                });

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
            Tarea tarea = datos.get(position);
            repository.deleteTarea(tarea, result -> {
                if (result) {
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    cargarTareas(); // Recargar lista
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar tareas al volver de cualquier actividad (Crear, Editar, Preferencias)
        cargarTareas();
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
        // Si cambian las preferencias de ordenación, recargar
        if (key.equals("criterio") || key.equals("orden")) {
            cargarTareas();
        }
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

        if (id == R.id.menu_preferencias) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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

        // Handle "Estadísticas" menu item
        if (id == R.id.menu_estadisticas) {
            Intent intent = new Intent(this, EstadisticasActivity.class);
            startActivity(intent);
            return true;
        }
        // Assuming ID will be known once XML is updated, but for now checking title or
        // handled in BaseActivity
        // The instructions say "Añade una entrada en el menú principal... aparecerá en
        // tercera posición... llamada 'Estadísticas'"

        return super.onOptionsItemSelected(item);
    }

    /**
     * Carga las tareas desde el repositorio aplicando filtros y ordenación
     */
    private void cargarTareas() {
        String criterioStr = prefs.getString("criterio", "2");
        int criterio = Integer.parseInt(criterioStr);
        boolean ascendente = prefs.getBoolean("orden", true);

        repository.getTareas(criterio, ascendente, mostrandoPrioritarias, list -> {
            datos.clear();
            datos.addAll(list);
            adaptador.notifyDataSetChanged();
            actualizerVisibilities();

            // Update title or icon based on filter?
            if (mostrandoPrioritarias) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Tareas Prioritarias");
            } else {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Lista Tareas");
            }
        });
    }

    // Método obsoleto, reemplazado por cargarTareas() que delegar en repositorio
    private void aplicarOrdenacion() {
        cargarTareas();
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
                        "Task Final\n\n" +
                                "IES Trassierra\n\n" +
                                "Autor: Jose Luis Fuentes Parra\n\n" +
                                "Año: 2025")
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void alternarPrioritarias() {
        mostrandoPrioritarias = !mostrandoPrioritarias;
        cargarTareas();
    }
}
