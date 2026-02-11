package com.joseluu.tareafinal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.adapter.ArchivoAdapter;
import com.joseluu.tareafinal.model.Tarea;


public class DescripcionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        
        Tarea tarea = getIntent().getParcelableExtra("TAREA");
        if (tarea == null) {
            finish();
            return;
        }

        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalle de la Tarea");
        }

        
        TextView txtTitulo = findViewById(R.id.txtTituloDescripcion);
        TextView txtDescripcion = findViewById(R.id.txtDescripcion);
        RecyclerView rvArchivos = findViewById(R.id.rvArchivosAdjuntos);
        TextView txtNoAttachments = findViewById(R.id.txtNoAttachments);

        
        txtTitulo.setText(tarea.getTitulo());
        txtDescripcion.setText(tarea.getDescripcion());

        
        ArchivoAdapter adapter = new ArchivoAdapter();
        rvArchivos.setLayoutManager(new LinearLayoutManager(this));
        rvArchivos.setAdapter(adapter);

        
        if (tarea.getArchivosAdjuntos() != null && !tarea.getArchivosAdjuntos().isEmpty()) {
            adapter.setArchivos(tarea.getArchivosAdjuntos());
            rvArchivos.setVisibility(View.VISIBLE);
            txtNoAttachments.setVisibility(View.GONE);
        } else {
            rvArchivos.setVisibility(View.GONE);
            txtNoAttachments.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
