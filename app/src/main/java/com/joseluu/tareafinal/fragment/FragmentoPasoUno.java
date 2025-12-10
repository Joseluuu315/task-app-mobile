package com.joseluu.tareafinal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.joseluu.tareafinal.CrearTareaActivity;
import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.manager.DatePickerFragment;
import com.joseluu.tareafinal.view.FormularioViewModel;

public class FragmentoPasoUno extends Fragment {
    private EditText edtTitulo, edtFechaCreacion, edtFechaObjetivo;
    private Spinner spinnerProgreso;
    private CheckBox cbPrioritaria;

    private FormularioViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crear_fragmento_uno, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FormularioViewModel.class);

        edtTitulo = view.findViewById(R.id.edtTitulo);
        edtFechaCreacion = view.findViewById(R.id.edtFechaCreacion);
        edtFechaObjetivo = view.findViewById(R.id.edtFechaObjetivo);
        spinnerProgreso = view.findViewById(R.id.spinnerProgreso);
        cbPrioritaria = view.findViewById(R.id.cbPrioritaria);

        // Configurar Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"No iniciada", "Iniciada", "Avanzada", "Casi finalizada", "Finalizada"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgreso.setAdapter(adapter);

        // DatePickers
        edtFechaCreacion.setOnClickListener(v -> mostrarDatePicker(edtFechaCreacion));
        edtFechaObjetivo.setOnClickListener(v -> mostrarDatePicker(edtFechaObjetivo));

        // Botón siguiente
        view.findViewById(R.id.btnSiguiente).setOnClickListener(v -> {

            if (edtTitulo.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Escribe un título", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.titulo.setValue(edtTitulo.getText().toString());
            viewModel.prioritaria.setValue(cbPrioritaria.isChecked());

            // Progreso → convertir a número
            int[] valores = {0, 25, 50, 75, 100};
            viewModel.progreso.setValue(valores[spinnerProgreso.getSelectedItemPosition()]);

            ((CrearTareaActivity) requireActivity()).cargarPaso2();
        });
    }

    private void mostrarDatePicker(EditText campo) {
        DatePickerFragment picker = new DatePickerFragment(date -> {
            campo.setText(date.toString());
            if (campo == edtFechaCreacion) viewModel.fechaCreacion.setValue(date);
            if (campo == edtFechaObjetivo) viewModel.fechaObjetivo.setValue(date);
        });
        picker.show(getParentFragmentManager(), "datePicker");
    }
}
