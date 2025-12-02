package com.joseluu.tareafinal.adapter;

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
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemcapital,parent,false);
        return new TareaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        //Asignamos el dato del array correspondiente a la posición actual al
        //objeto ViewHolder, de forma que se represente en el RecyclerView.
        holder.bindCapital(tareaData.get(position));
    }

    @Override
    public int getItemCount() {
        //Devolvemos el tamaño de array de datos de Capitales
        return tareaData.size();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class TareaViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView;
        private TextView habitantesTextView;
        private CheckBox favoritaCheckBox;

        //Metodo constructor
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            //Bindings
//            titleTextView = itemView.findViewById(R.id.tvCapital);
//            habitantesTextView = itemView.findViewById(R.id.tvHabitantes);
//            favoritaCheckBox = itemView.findViewById(R.id.cbFavorita);
        }

        //Metodo que nos permitirá dar valores a cada campo del objeto ViewHolder y que
        //el mismo pueda ser mostrado en el RecyclerView
        public void bindCapital(Tarea t) {
            titleTextView.setText(t.getTitulo());
            habitantesTextView.setText(t.);
            favoritaCheckBox.setChecked(t.isFavorita());
        }
    }
}
