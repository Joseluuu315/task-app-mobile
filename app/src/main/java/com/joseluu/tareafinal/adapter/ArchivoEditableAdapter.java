package com.joseluu.tareafinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.model.ArchivoAdjunto;

import java.util.ArrayList;
import java.util.List;


public class ArchivoEditableAdapter extends RecyclerView.Adapter<ArchivoEditableAdapter.ArchivoViewHolder> {

    private List<ArchivoAdjunto> archivos;
    private OnArchivoDeleteListener deleteListener;

    public interface OnArchivoDeleteListener {
        void onArchivoDelete(ArchivoAdjunto archivo, int position);
    }

    public ArchivoEditableAdapter(OnArchivoDeleteListener deleteListener) {
        this.archivos = new ArrayList<>();
        this.deleteListener = deleteListener;
    }

    public void setArchivos(List<ArchivoAdjunto> archivos) {
        this.archivos = archivos != null ? archivos : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<ArchivoAdjunto> getArchivos() {
        return archivos;
    }

    @NonNull
    @Override
    public ArchivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_archivo_editable, parent, false);
        return new ArchivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivoViewHolder holder, int position) {
        holder.bind(archivos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return archivos.size();
    }

    class ArchivoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFileIcon;
        TextView txtFileName;
        ImageButton btnDelete;

        public ArchivoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFileIcon = itemView.findViewById(R.id.imgFileIcon);
            txtFileName = itemView.findViewById(R.id.txtFileName);
            btnDelete = itemView.findViewById(R.id.btnDeleteFile);
        }

        public void bind(ArchivoAdjunto archivo, int position) {
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

            
            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onArchivoDelete(archivo, position);
                }
            });
        }
    }
}
