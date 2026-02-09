package com.joseluu.tareafinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.joseluu.tareafinal.fragment.FragmentoPasoDos;
import com.joseluu.tareafinal.fragment.FragmentoPasoUno;
import com.joseluu.tareafinal.model.Tarea;
import com.joseluu.tareafinal.view.FormularioViewModel;

public class EditarTareaActivity extends BaseActivity {

    private FormularioViewModel viewModel;
    private Tarea tareaOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_tarea);

        viewModel = new ViewModelProvider(this).get(FormularioViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Editar Tarea");
        }

        tareaOriginal = getIntent().getParcelableExtra("TAREA_EDITAR");

        if (tareaOriginal != null) {
            precargarDatosEnViewModel();
        }

        // Cargar fragmento inicial (Paso 1)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmentos, new FragmentoPasoUno())
                .commit();
    }

    private void precargarDatosEnViewModel() {
        viewModel.titulo.setValue(tareaOriginal.getTitulo());
        viewModel.descripcion.setValue(tareaOriginal.getDescripcion());
        viewModel.progreso.setValue(tareaOriginal.getProgreso());
        viewModel.fechaCreacion.setValue(tareaOriginal.getFechaCreacion());
        viewModel.fechaObjetivo.setValue(tareaOriginal.getFechaObjectivo());
        viewModel.prioritaria.setValue(tareaOriginal.isPrioritario());
    }

    public void cargarPaso2() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmentos, new FragmentoPasoDos())
                .addToBackStack(null)
                .commit();
    }

    public void volverPaso1() {
        getSupportFragmentManager().popBackStack();
    }

    public void guardarTareaEditada(Tarea modificada) {

        // Devolver tarea editada
        Intent data = new Intent();
        data.putExtra("TAREA_EDITADA", modificada);

        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
