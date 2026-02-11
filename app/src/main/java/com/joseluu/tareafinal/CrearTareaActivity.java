package com.joseluu.tareafinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.joseluu.tareafinal.fragment.FragmentoPasoDos;
import com.joseluu.tareafinal.fragment.FragmentoPasoUno;
import com.joseluu.tareafinal.model.Tarea;
import com.joseluu.tareafinal.view.FormularioViewModel;

import java.util.ArrayList;

public class CrearTareaActivity extends BaseActivity {

    FormularioViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        viewModel = new ViewModelProvider(this).get(FormularioViewModel.class);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); 
            actionBar.setTitle("Crear Tarea");
        }

        
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmentos, new FragmentoPasoUno())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void guardarTareaYSalir(Tarea nueva) {
        
        com.joseluu.tareafinal.repository.TareaRepository repository = com.joseluu.tareafinal.repository.TareaRepository
                .getInstance(this);

        repository.addTarea(nueva, result -> {
            if (result) {
                
                Intent data = new Intent();
                
                setResult(RESULT_OK, data);
                finish();
            } else {
                
                
                
                android.widget.Toast.makeText(this, "Error al guardar la tarea", android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
