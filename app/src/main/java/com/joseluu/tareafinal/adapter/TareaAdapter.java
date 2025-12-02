package com.joseluu.tareafinal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.model.Tarea;

import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder>{
    private final ArrayList<Tarea> tareaData;

    public TareaAdapter(ArrayList<Tarea> tareaData) {
        this.tareaData = tareaData;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tarea,parent,false);
        TareaViewHolder tareaViewHolder = new TareaViewHolder(item);

        return tareaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        //Asignamos el dato del array correspondiente a la posición actual al
        //objeto ViewHolder, de forma que se represente en el RecyclerView.
        holder.bindTarea(tareaData.get(position));
    }

    @Override
    public int getItemCount() {
        //Devolvemos el tamaño de array de datos de Capitales
        return tareaData.size();
    }


    public static class TareaViewHolder extends RecyclerView.ViewHolder{

        private final TextView titleTextView;
        private final TextView descripcionTextView;
        private TextView progresoTextView;
        private TextView dateTextView1;
        private TextView dateTextView2;
        private final CheckBox prioritarioCheckBox;

        //Metodo constructor
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.txtTitle);
            descripcionTextView = itemView.findViewById(R.id.txtDescripcion);
            prioritarioCheckBox = itemView.findViewById(R.id.cbPrioritario);
        }

        //Metodo que nos permitirá dar valores a cada campo del objeto ViewHolder y que
        //el mismo pueda ser mostrado en el RecyclerView
        @SuppressLint("SetTextI18n")
        public void bindTarea(Tarea t) {
            titleTextView.setText(t.getTitulo());
            descripcionTextView.setText(t.getDescripcion());
            prioritarioCheckBox.setChecked(t.isPrioritario());
        }
    }
}
