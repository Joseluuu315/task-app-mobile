package com.joseluu.tareafinal;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.joseluu.tareafinal.repository.TareaRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EstadisticasActivity extends AppCompatActivity {

    private TextView txtTotalTareas, txtPendientes, txtCompletadas, txtPrioritarias;
    private TextView txtPromedioProgreso, txtFechaPromedio;
    private TextView txtCount0to25, txtCount26to50, txtCount51to75, txtCount76to100;
    private ProgressBar progress0to25, progress26to50, progress51to75, progress76to100;

    private TareaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        setContentView(R.layout.activity_estadisticas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Estadísticas");
        }

        repository = TareaRepository.getInstance(this);

        initViews();
        loadStatistics();
    }

    private void initViews() {
        txtTotalTareas = findViewById(R.id.txtTotalTareas);
        txtPendientes = findViewById(R.id.txtPendientes);
        txtCompletadas = findViewById(R.id.txtCompletadas);
        txtPrioritarias = findViewById(R.id.txtPrioritarias);
        txtPromedioProgreso = findViewById(R.id.txtPromedioProgreso);
        txtFechaPromedio = findViewById(R.id.txtFechaPromedio);

        txtCount0to25 = findViewById(R.id.txtCount0to25);
        txtCount26to50 = findViewById(R.id.txtCount26to50);
        txtCount51to75 = findViewById(R.id.txtCount51to75);
        txtCount76to100 = findViewById(R.id.txtCount76to100);

        progress0to25 = findViewById(R.id.progress0to25);
        progress26to50 = findViewById(R.id.progress26to50);
        progress51to75 = findViewById(R.id.progress51to75);
        progress76to100 = findViewById(R.id.progress76to100);
    }

    private void loadStatistics() {
        repository.getStatistics(stats -> {
            if (stats == null)
                return;

            txtTotalTareas.setText(String.valueOf(stats.totalTasks));
            txtPendientes.setText(String.valueOf(stats.pendingTasks));
            txtCompletadas.setText(String.valueOf(stats.completedTasks));
            txtPrioritarias.setText(String.valueOf(stats.priorityTasks));

            txtPromedioProgreso.setText(String.format(Locale.getDefault(), "%.1f%%", stats.averageProgress));

            if (stats.averageTargetDate > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtFechaPromedio.setText(sdf.format(new Date(stats.averageTargetDate)));
            } else {
                txtFechaPromedio.setText("-");
            }

            
            updateProgressSection(txtCount0to25, progress0to25, stats.count0to25, stats.totalTasks);
            updateProgressSection(txtCount26to50, progress26to50, stats.count26to50, stats.totalTasks);
            updateProgressSection(txtCount51to75, progress51to75, stats.count51to75, stats.totalTasks);
            updateProgressSection(txtCount76to100, progress76to100, stats.count76to100, stats.totalTasks);
        });
    }

    private void updateProgressSection(TextView txtCount, ProgressBar progressBar, int count, int total) {
        txtCount.setText(String.valueOf(count));
        if (total > 0) {
            progressBar.setProgress((count * 100) / total);
        } else {
            progressBar.setProgress(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
