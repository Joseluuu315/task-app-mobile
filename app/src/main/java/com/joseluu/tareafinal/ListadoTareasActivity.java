package com.joseluu.tareafinal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.adapter.TareaAdapter;
import com.joseluu.tareafinal.model.Tarea;

import java.util.ArrayList;
import java.util.Date;

public class ListadoTareasActivity extends AppCompatActivity {
    private final ArrayList<Tarea> datos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);
        init();

        TareaAdapter adaptador = new TareaAdapter(datos);
        RecyclerView rv = findViewById(R.id.rvTareas);
        rv.setAdapter(adaptador);
        rv.setLayoutManager( new LinearLayoutManager(this, RecyclerView.VERTICAL,true));


        rv.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
            }
        });

    }

    /*
    In this methods i can create data to see RecyclerView
     */
    public void init(){
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
        datos.add(new Tarea("Penelope", "Juega", 2, new Date(), new Date(), true));
    }
}