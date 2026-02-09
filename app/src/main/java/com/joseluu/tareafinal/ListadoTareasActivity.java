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

        // Inicializar preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        datos = ManagerMethods.getInstance().getDatos();

        rvTareas = findViewById(R.id.rvTareas);
        txtNoTareas = findViewById(R.id.txtNoTareas);

        adaptador = new TareaAdapter(datos);
        rvTareas.setAdapter(adaptador);
        rvTareas.setLayoutManager(new LinearLayoutManager(this));

        // Aplicar ordenación inicial
        aplicarOrdenacion();
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

                                // Reordenar después de añadir
                                aplicarOrdenacion();
                                adaptador.notifyDataSetChanged();
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
                                datos.set(posicionEditando, editada);

                                // Reordenar después de editar
                                aplicarOrdenacion();
                                adaptador.notifyDataSetChanged();
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
    protected void onResume() {
        super.onResume();
        // Reaplicar ordenación al volver de Preferencias
        aplicarOrdenacion();
        adaptador.notifyDataSetChanged();
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
        // Si cambian las preferencias de ordenación, reaplicar
        if (key.equals("criterio") || key.equals("orden")) {
            aplicarOrdenacion();
            adaptador.notifyDataSetChanged();
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

        return super.onOptionsItemSelected(item);
    }

    /**
     * Aplica la ordenación según las preferencias guardadas
     */
    private void aplicarOrdenacion() {
        String criterioStr = prefs.getString("criterio", "2");
        int criterio = Integer.parseInt(criterioStr);
        boolean ascendente = prefs.getBoolean("orden", true);

        Comparator<Tarea> comparador = null;

        switch (criterio) {
            case 1: // Alfabético
                comparador = (t1, t2) -> {
                    int result = t1.getTitulo().compareToIgnoreCase(t2.getTitulo());
                    return ascendente ? result : -result;
                };
                break;

            case 2: // Fecha de creación (default)
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
            Collections.sort(datos, comparador);
        }
    }

    /**
     * Calcula los días restantes hasta la fecha objetivo
     */
    private long calcularDiasRestantes(Tarea tarea) {
        LocalDate fechaObj = tarea.getFechaObjectivo().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate hoy = LocalDate.now();
        return ChronoUnit.DAYS.between(hoy, fechaObj);
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

        // Aplicar ordenación después de filtrar
        aplicarOrdenacion();
        adaptador.notifyDataSetChanged();
        actualizerVisibilities();
    }
}
