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
            actionBar.setDisplayHomeAsUpEnabled(true); // habilita la flecha
            actionBar.setTitle("Crear Tarea");
        }

        // Cargar primer fragmento
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
        // En lugar de devolver la tarea por Intent, la guardamos en BD
        com.joseluu.tareafinal.repository.TareaRepository repository = com.joseluu.tareafinal.repository.TareaRepository
                .getInstance(this);

        repository.addTarea(nueva, result -> {
            if (result) {
                // Si se guardó correctamente, devolvemos OK
                Intent data = new Intent();
                // Opcional: devolver ID o algo, pero la lista se recargará
                setResult(RESULT_OK, data);
                finish();
            } else {
                // Manejar error (Toast, etc) -> Aunque aquí estamos en background callback ->
                // runOnUiThread
                // Pero el callback del repositorio ya vuelve al main thread.
                android.widget.Toast.makeText(this, "Error al guardar la tarea", android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
