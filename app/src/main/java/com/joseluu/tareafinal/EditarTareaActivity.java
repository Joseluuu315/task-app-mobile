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

        
        if (tareaOriginal.getArchivosAdjuntos() != null) {
            viewModel.archivosAdjuntos.setValue(tareaOriginal.getArchivosAdjuntos());
        }
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
        if (tareaOriginal != null) {
            modificada.setId(tareaOriginal.getId());
        }

        com.joseluu.tareafinal.repository.TareaRepository repository = com.joseluu.tareafinal.repository.TareaRepository
                .getInstance(this);

        repository.updateTarea(modificada, result -> {
            if (result) {
                Intent data = new Intent();
                
                
                data.putExtra("TAREA_EDITADA", modificada);
                setResult(Activity.RESULT_OK, data);
                finish();
            } else {
                android.widget.Toast.makeText(this, "Error al actualizar la tarea", android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });
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
