package com.joseluu.tareafinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.model.ArchivoAdjunto;

import java.util.ArrayList;
import java.util.List;


public class ArchivoAdapter extends RecyclerView.Adapter<ArchivoAdapter.ArchivoViewHolder> {

    private List<ArchivoAdjunto> archivos;

    public ArchivoAdapter() {
        this.archivos = new ArrayList<>();
    }

    public void setArchivos(List<ArchivoAdjunto> archivos) {
        this.archivos = archivos != null ? archivos : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArchivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_archivo, parent, false);
        return new ArchivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivoViewHolder holder, int position) {
        holder.bind(archivos.get(position));
    }

    @Override
    public int getItemCount() {
        return archivos.size();
    }

    static class ArchivoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFileIcon;
        TextView txtFileName;

        public ArchivoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFileIcon = itemView.findViewById(R.id.imgFileIcon);
            txtFileName = itemView.findViewById(R.id.txtFileName);
        }

        public void bind(ArchivoAdjunto archivo) {
            txtFileName.setText(archivo.getNombreArchivo());

            
            int iconRes;
            switch (archivo.getTipo()) {
                case DOCUMENTO:
                    iconRes = R.drawable.ic_document;
                    break;
                case IMAGEN:
                    iconRes = R.drawable.ic_image_file;
                    break;
                case AUDIO:
                    iconRes = R.drawable.ic_audio;
                    break;
                case VIDEO:
                    iconRes = R.drawable.ic_video;
                    break;
                default:
                    iconRes = R.drawable.ic_document;
            }
            imgFileIcon.setImageResource(iconRes);
        }
    }
}
