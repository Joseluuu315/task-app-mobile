package com.joseluu.tareafinal.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.EditarTareaActivity;
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
        holder.bindTarea(tareaData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return tareaData.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {

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
        public void bindTarea(Tarea t, int position) {

            txtTitle.setText(t.getTitulo());
            txtDescripcion.setText(t.getDescripcion());

            cbPrioritario.setChecked(t.isPrioritario());

            txtTitle.setTypeface(null, t.isPrioritario() ? Typeface.BOLD : Typeface.NORMAL);

            progressBar.setProgress(t.getProgreso());

            LocalDate fechaObj = t.getFechaObjectivo().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate hoy = LocalDate.now();
            long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaObj);
            txtDiasRestantes.setText(diasRestantes + " días restantes");
            txtDiasRestantes.setTextColor(diasRestantes < 0 ? Color.RED : Color.BLACK);

            if (t.getProgreso() == 100) {
                txtTitle.setPaintFlags(txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtDiasRestantes.setText("0 días");
            } else {
                txtTitle.setPaintFlags(0);
            }

            // Click normal para mostrar descripción en Toast
            itemView.setOnClickListener(v ->
                    Toast.makeText(v.getContext(), t.getDescripcion(), Toast.LENGTH_LONG).show()
            );

            // Long click para mostrar menú contextual
            itemView.setOnLongClickListener(v -> {
                showPopupMenu(v, position);
                return true;
            });
        }

        private void showPopupMenu(View view, int position) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenu().add(0, 1, 0, "Editar");
            popupMenu.getMenu().add(0, 2, 1, "Borrar");

            popupMenu.setOnMenuItemClickListener(item -> {
                Tarea tarea = tareaData.get(position);
                switch (item.getItemId()) {

                    case 1: // Editar
                        if (editListener != null) {
                            editListener.onEdit(tarea, position, view);
                        }
                        return true;

                    case 2: // Borrar
                        if (deleteListener != null) {
                            deleteListener.onDelete(position);
                        }
                        return true;
                }
                return false;
            });


            popupMenu.show();
        }
    }

    public interface OnEditListener {
        void onEdit(Tarea tarea, int position, View view);
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public void setOnEditListener(OnEditListener listener) {
        this.editListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(Tarea tarea, int position);
    }

}
