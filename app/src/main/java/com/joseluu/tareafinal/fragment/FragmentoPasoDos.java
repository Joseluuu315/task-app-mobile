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

        view.findViewById(R.id.btnVolver).setOnClickListener(v ->
                ((CrearTareaActivity) requireActivity()).volverPaso1()
        );

        view.findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarTarea());
    }

    private void guardarTarea() {
        // Asegurarse de no pasar null
        String titulo = viewModel.titulo.getValue() != null ? viewModel.titulo.getValue() : "";
        String descripcion = edtDescripcion.getText().toString();
        int progreso = viewModel.progreso.getValue() != null ? viewModel.progreso.getValue() : 0;
        Date fechaCreacion = viewModel.fechaCreacion.getValue() != null ? viewModel.fechaCreacion.getValue() : new Date();
        Date fechaObjetivo = viewModel.fechaObjetivo.getValue() != null ? viewModel.fechaObjetivo.getValue() : new Date();
        boolean prioritaria = viewModel.prioritaria.getValue() != null && viewModel.prioritaria.getValue();

        Tarea nueva = new Tarea(titulo, descripcion, progreso, fechaCreacion, fechaObjetivo, prioritaria);

        ((CrearTareaActivity) requireActivity()).guardarTareaYSalir(nueva);
    }
}
