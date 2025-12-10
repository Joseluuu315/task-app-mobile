package com.joseluu.tareafinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.joseluu.tareafinal.manager.IntentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEmpezar =findViewById(R.id.btnEmpezar);
        Button btnCrearActividad = findViewById(R.id.btnCrearTarea);

        btnEmpezar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListadoTareasActivity.class);
            /*
            TODO: migrate intent to new manager class
            new IntentManager().startActivityWithIntent(MainActivity.this, ListadoTareasActivity.class);
             */

            startActivity(intent);
        });

        btnCrearActividad.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, CrearTareaActivity.class);

            startActivity(intent);
        });

    }
}