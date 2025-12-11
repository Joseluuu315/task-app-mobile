package com.joseluu.tareafinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.joseluu.tareafinal.fragment.FragmentoPasoDos;
import com.joseluu.tareafinal.fragment.FragmentoPasoUno;
import com.joseluu.tareafinal.model.Tarea;
import com.joseluu.tareafinal.view.FormularioViewModel;

public class EditarTareaActivity extends AppCompatActivity {

    private FormularioViewModel viewModel;
    private Tarea tareaOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        viewModel = new ViewModelProvider(this).get(FormularioViewModel.class);

        tareaOriginal = getIntent().getParcelableExtra("TAREA_EDITAR");

        if (tareaOriginal != null) {
            precargarDatosEnViewModel();
        }

        // Cargar los 2 fragmentos a la vez (modo edición)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorPaso1Editar, new FragmentoPasoUno())
                .replace(R.id.contenedorPaso2Editar, new FragmentoPasoDos())
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

    public void guardarTareaEditada(Tarea modificada) {
        Intent data = new Intent();
        data.putExtra("TAREA_EDITADA", modificada);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    // Métodos añadidos para evitar errores en los fragmentos
    public void cargarPaso2() {
        // En edición no hay navegación por pasos → no hacer nada
    }

    public void volverPaso1() {
        // En edición no hay navegación por pasos → no hacer nada
    }
}
