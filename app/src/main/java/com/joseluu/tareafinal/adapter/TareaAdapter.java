package com.joseluu.tareafinal.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.DescripcionActivity;
import com.joseluu.tareafinal.EditarTareaActivity;
import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.manager.PreferenciasHelper;
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

        
        float fontSize = PreferenciasHelper.getTamanoFuente(holder.itemView.getContext());
        float titleSize = PreferenciasHelper.getTamanoFuenteTitulo(holder.itemView.getContext());

        holder.txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        holder.txtDescripcion.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        holder.txtDiasRestantes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
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
        private final ImageView imageView;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            progressBar = itemView.findViewById(R.id.progressBar);
            cbPrioritario = itemView.findViewById(R.id.cbPrioritario);
            txtDiasRestantes = itemView.findViewById(R.id.txtDiasRestantes);
            imageView = itemView.findViewById(R.id.imgPrioritaria);
        }

        @SuppressLint("SetTextI18n")
        public void bindTarea(Tarea t, int position) {

            txtTitle.setText(t.getTitulo());
            txtDescripcion.setText(t.getDescripcion());

            cbPrioritario.setChecked(t.isPrioritario());

            
            if (t.isPrioritario()) {
                imageView.setImageResource(R.drawable.ic_star_filled);
                txtTitle.setTypeface(null, Typeface.BOLD);
            } else {
                imageView.setImageResource(R.drawable.ic_star_outline);
                txtTitle.setTypeface(null, Typeface.NORMAL);
            }

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

            
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DescripcionActivity.class);
                intent.putExtra("TAREA", t);
                v.getContext().startActivity(intent);
            });

            
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

                    case 1: 
                        if (editListener != null) {
                            editListener.onEdit(tarea, position, view);
                        }
                        return true;

                    case 2: 
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

}
