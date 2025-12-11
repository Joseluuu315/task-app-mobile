package com.joseluu.tareafinal.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.model.Tarea;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private final ArrayList<Tarea> tareaData;

    public TareaAdapter(ArrayList<Tarea> tareaData) {
        this.tareaData = tareaData;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_tarea, parent, false);

        return new TareaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        holder.bindTarea(tareaData.get(position));
    }

    @Override
    public int getItemCount() {
        return tareaData.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtDescripcion;
        private final ProgressBar progressBar;
        private final CheckBox cbPrioritario;
        private final TextView txtDiasRestantes;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            progressBar = itemView.findViewById(R.id.progressBar);
            cbPrioritario = itemView.findViewById(R.id.cbPrioritario);
            txtDiasRestantes = itemView.findViewById(R.id.txtDiasRestantes);
        }

        @SuppressLint("SetTextI18n")
        public void bindTarea(Tarea t) {

            txtTitle.setText(t.getTitulo());
            txtDescripcion.setText(t.getDescripcion());

            // PRIORIDAD (CheckBox)
            cbPrioritario.setChecked(t.isPrioritario());

            if (t.isPrioritario()) {
                txtTitle.setTypeface(null, Typeface.BOLD);
            } else {
                txtTitle.setTypeface(null, Typeface.NORMAL);
            }

            // PROGRESO
            progressBar.setProgress(t.getProgreso());

            // FECHA -> días restantes
            LocalDate fechaObj = t.getFechaObjectivo().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate hoy = LocalDate.now();
            long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaObj);

            txtDiasRestantes.setText(diasRestantes + " días restantes");

            // Color rojo si atrasada
            txtDiasRestantes.setTextColor(diasRestantes < 0 ? Color.RED : Color.BLACK);

            // TACHADO si finalizada
            if (t.getProgreso() == 100) {
                txtTitle.setPaintFlags(txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtDiasRestantes.setText("0 días");
            } else {
                txtTitle.setPaintFlags(0);
            }

            // Mostrar descripción completa en Toast
            itemView.setOnClickListener(v ->
                    Toast.makeText(
                            v.getContext(),
                            t.getDescripcion(),
                            Toast.LENGTH_LONG
                    ).show()
            );
        }
    }
}
