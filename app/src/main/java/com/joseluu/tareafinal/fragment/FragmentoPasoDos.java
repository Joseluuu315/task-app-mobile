package com.joseluu.tareafinal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.joseluu.tareafinal.CrearTareaActivity;
import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.model.Tarea;
import com.joseluu.tareafinal.view.FormularioViewModel;

import java.util.Date;

public class FragmentoPasoDos extends Fragment {
    private EditText edtDescripcion;
    private FormularioViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crear_fragmento_dos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FormularioViewModel.class);
        edtDescripcion = view.findViewById(R.id.edtDescripcion);

        precargarDescripcion();

        view.findViewById(R.id.btnVolver).setOnClickListener(v -> {
            ((CrearTareaActivity) requireActivity()).volverPaso1();
        });

        view.findViewById(R.id.btnGuardar).setOnClickListener(v -> guardar());
    }

    private void precargarDescripcion() {
        if (viewModel.descripcion.getValue() != null)
            edtDescripcion.setText(viewModel.descripcion.getValue());
    }

    private void guardar() {
        String titulo = viewModel.titulo.getValue();
        String descripcion = edtDescripcion.getText().toString();
        int progreso = viewModel.progreso.getValue();
        Date fechaCreacion = viewModel.fechaCreacion.getValue();
        Date fechaObjetivo = viewModel.fechaObjetivo.getValue();
        boolean prioritaria = viewModel.prioritaria.getValue();

        Tarea tarea = new Tarea(titulo, descripcion, progreso, fechaCreacion, fechaObjetivo, prioritaria);

            ((CrearTareaActivity) requireActivity()).guardarTareaYSalir(tarea);
    }
}

