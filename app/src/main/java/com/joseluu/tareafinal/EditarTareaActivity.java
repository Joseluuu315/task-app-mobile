package com.joseluu.tareafinal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.joseluu.tareafinal.manager.ManagerMethods;
import com.joseluu.tareafinal.model.Tarea;

import java.util.Date;

public class EditarTareaActivity extends AppCompatActivity {

    private Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        // Obtener posición de la tarea desde el Intent
        int tareaIndex = getIntent().getIntExtra("tareaIndex", -1);
        if (tareaIndex == -1) {
            finish();
            return;
        }

        tarea = ManagerMethods.getInstance().getDatos().get(tareaIndex);


        // Referencias UI
        EditText etTitulo = findViewById(R.id.etTituloEditar);
        EditText etFechaCreacion = findViewById(R.id.etFechaCreacionEditar);
        EditText etFechaObjetivo = findViewById(R.id.etFechaObjetivoEditar);
        Spinner spProgreso = findViewById(R.id.spProgresoEditar);
        CheckBox cbPrioritaria = findViewById(R.id.cbPrioritariaEditar);
        EditText etDescripcion = findViewById(R.id.etDescripcionEditar);
        Button btnGuardar = findViewById(R.id.btnGuardarEditar);

        // Cargar datos actuales
        etTitulo.setText(tarea.getTitulo());
        etFechaCreacion.setText(tarea.getFechaCreacion().toString());  // usa String, no toString()
        etFechaObjetivo.setText(tarea.getFechaObjectivo().toString());
        etDescripcion.setText(tarea.getDescripcion());
        cbPrioritaria.setChecked(tarea.isPrioritario());

        // Spinner progreso
        String[] opcionesProgreso = {
                "No iniciada", "Iniciada", "Avanzada", "Casi finalizada", "Finalizada"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,   // ❌ antes: requireContext() → solo existe en Fragment
                android.R.layout.simple_spinner_item,
                opcionesProgreso
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProgreso.setAdapter(adapter);

        // Valores reales del progreso
        int[] valoresProgreso = {0, 25, 50, 75, 100};

        // Preseleccionar según la tarea
        int posProgreso = 0;
        switch (tarea.getProgreso()) {
            case 25: posProgreso = 1; break;
            case 50: posProgreso = 2; break;
            case 75: posProgreso = 3; break;
            case 100: posProgreso = 4; break;
        }
        spProgreso.setSelection(posProgreso);

        // Guardar cambios
        btnGuardar.setOnClickListener(v -> {

            tarea.setTitulo(etTitulo.getText().toString());
            tarea.setFechaObjectivo(new Date(String.valueOf(etFechaObjetivo)));
            tarea.setDescripcion(etDescripcion.getText().toString());
            tarea.setPrioritario(cbPrioritaria.isChecked());

            tarea.setProgreso(valoresProgreso[ spProgreso.getSelectedItemPosition() ]);

            Toast.makeText(this, getString(R.string.msgTareaActualizada), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
