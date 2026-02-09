package com.joseluu.tareafinal.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluu.tareafinal.CrearTareaActivity;
import com.joseluu.tareafinal.EditarTareaActivity;
import com.joseluu.tareafinal.R;
import com.joseluu.tareafinal.adapter.ArchivoEditableAdapter;
import com.joseluu.tareafinal.model.ArchivoAdjunto;
import com.joseluu.tareafinal.model.Tarea;
import com.joseluu.tareafinal.view.FormularioViewModel;
import com.joseluu.tareafinal.manager.FileStorageHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentoPasoDos extends Fragment {
    private EditText edtDescripcion;
    private TextView txtSelectedFiles;
    private RecyclerView rvExistingFiles;
    private FormularioViewModel viewModel;
    private ArchivoEditableAdapter existingFilesAdapter;

    // Map to store selected file URIs by type (for newly selected files)
    private Map<ArchivoAdjunto.TipoArchivo, Uri> selectedFiles = new HashMap<>();
    private Map<ArchivoAdjunto.TipoArchivo, String> selectedFileNames = new HashMap<>();

    // List of files to delete (existing files marked for deletion)
    private List<ArchivoAdjunto> filesToDelete = new ArrayList<>();

    // Activity result launchers for file pickers
    private ActivityResultLauncher<Intent> documentPickerLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> audioPickerLauncher;
    private ActivityResultLauncher<Intent> videoPickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFilePickers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crear_fragmento_dos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FormularioViewModel.class);
        edtDescripcion = view.findViewById(R.id.edtDescripcion);
        txtSelectedFiles = view.findViewById(R.id.txtSelectedFiles);
        rvExistingFiles = view.findViewById(R.id.rvExistingFiles);

        setupExistingFilesRecyclerView();
        precargarDescripcion();
        precargarArchivosExistentes();
        setupFileButtons(view);

        view.findViewById(R.id.btnVolver).setOnClickListener(v -> volverPaso1());
        view.findViewById(R.id.btnGuardar).setOnClickListener(v -> guardar());
    }

    private void setupExistingFilesRecyclerView() {
        existingFilesAdapter = new ArchivoEditableAdapter((archivo, position) -> {
            // Show confirmation dialog before deleting
            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar archivo")
                    .setMessage("¿Eliminar " + archivo.getNombreArchivo() + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        // Mark for deletion
                        filesToDelete.add(archivo);

                        // Remove from current list
                        List<ArchivoAdjunto> currentFiles = viewModel.archivosAdjuntos.getValue();
                        if (currentFiles != null) {
                            currentFiles.remove(archivo);
                            viewModel.archivosAdjuntos.setValue(currentFiles);
                            existingFilesAdapter.setArchivos(currentFiles);
                        }

                        Toast.makeText(requireContext(), "Archivo marcado para eliminar", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        rvExistingFiles.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvExistingFiles.setAdapter(existingFilesAdapter);
    }

    private void precargarArchivosExistentes() {
        List<ArchivoAdjunto> archivos = viewModel.archivosAdjuntos.getValue();
        if (archivos != null && !archivos.isEmpty()) {
            existingFilesAdapter.setArchivos(archivos);
            rvExistingFiles.setVisibility(View.VISIBLE);
        } else {
            rvExistingFiles.setVisibility(View.GONE);
        }
    }

    private void setupFilePickers() {
        documentPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleFileSelection(result.getData(), ArchivoAdjunto.TipoArchivo.DOCUMENTO));

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleFileSelection(result.getData(), ArchivoAdjunto.TipoArchivo.IMAGEN));

        audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleFileSelection(result.getData(), ArchivoAdjunto.TipoArchivo.AUDIO));

        videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleFileSelection(result.getData(), ArchivoAdjunto.TipoArchivo.VIDEO));
    }

    private void setupFileButtons(View view) {
        ImageButton btnDoc = view.findViewById(R.id.btnAttachDocument);
        ImageButton btnImg = view.findViewById(R.id.btnAttachImage);
        ImageButton btnAudio = view.findViewById(R.id.btnAttachAudio);
        ImageButton btnVideo = view.findViewById(R.id.btnAttachVideo);

        btnDoc.setOnClickListener(v -> openFilePicker(ArchivoAdjunto.TipoArchivo.DOCUMENTO));
        btnImg.setOnClickListener(v -> openFilePicker(ArchivoAdjunto.TipoArchivo.IMAGEN));
        btnAudio.setOnClickListener(v -> openFilePicker(ArchivoAdjunto.TipoArchivo.AUDIO));
        btnVideo.setOnClickListener(v -> openFilePicker(ArchivoAdjunto.TipoArchivo.VIDEO));
    }

    private void openFilePicker(ArchivoAdjunto.TipoArchivo tipo) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(getMimeType(tipo));
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            switch (tipo) {
                case DOCUMENTO:
                    documentPickerLauncher.launch(intent);
                    break;
                case IMAGEN:
                    imagePickerLauncher.launch(intent);
                    break;
                case AUDIO:
                    audioPickerLauncher.launch(intent);
                    break;
                case VIDEO:
                    videoPickerLauncher.launch(intent);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "No se pudo abrir el selector de archivos", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMimeType(ArchivoAdjunto.TipoArchivo tipo) {
        switch (tipo) {
            case DOCUMENTO:
                return "application/*";
            case IMAGEN:
                return "image/*";
            case AUDIO:
                return "audio/*";
            case VIDEO:
                return "video/*";
            default:
                return "*/*";
        }
    }

    private void handleFileSelection(Intent data, ArchivoAdjunto.TipoArchivo tipo) {
        if (data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            String fileName = FileStorageHelper.getFileName(requireContext(), fileUri);

            selectedFiles.put(tipo, fileUri);
            selectedFileNames.put(tipo, fileName);

            updateSelectedFilesDisplay();
            Toast.makeText(getContext(), fileName + " seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSelectedFilesDisplay() {
        if (selectedFiles.isEmpty()) {
            txtSelectedFiles.setText("Ningún archivo nuevo seleccionado");
        } else {
            StringBuilder sb = new StringBuilder("Archivos nuevos:\n");
            for (Map.Entry<ArchivoAdjunto.TipoArchivo, String> entry : selectedFileNames.entrySet()) {
                sb.append("• ").append(getTipoNombre(entry.getKey())).append(": ")
                        .append(entry.getValue()).append("\n");
            }
            txtSelectedFiles.setText(sb.toString().trim());
        }
    }

    private String getTipoNombre(ArchivoAdjunto.TipoArchivo tipo) {
        switch (tipo) {
            case DOCUMENTO:
                return "Documento";
            case IMAGEN:
                return "Imagen";
            case AUDIO:
                return "Audio";
            case VIDEO:
                return "Vídeo";
            default:
                return "Archivo";
        }
    }

    private void precargarDescripcion() {
        if (viewModel.descripcion.getValue() != null)
            edtDescripcion.setText(viewModel.descripcion.getValue());
    }

    private void volverPaso1() {
        if (requireActivity() instanceof CrearTareaActivity) {
            ((CrearTareaActivity) requireActivity()).volverPaso1();
        } else if (requireActivity() instanceof EditarTareaActivity) {
            ((EditarTareaActivity) requireActivity()).volverPaso1();
        }
    }

    private void guardar() {
        String titulo = viewModel.titulo.getValue();
        String descripcion = edtDescripcion.getText().toString();
        int progreso = viewModel.progreso.getValue();
        Date fechaCreacion = viewModel.fechaCreacion.getValue();
        Date fechaObjetivo = viewModel.fechaObjetivo.getValue();
        boolean prioritaria = viewModel.prioritaria.getValue();

        Tarea tarea = new Tarea(titulo, descripcion, progreso, fechaCreacion, fechaObjetivo, prioritaria);

        // Add existing files (not marked for deletion)
        List<ArchivoAdjunto> existingFiles = viewModel.archivosAdjuntos.getValue();
        if (existingFiles != null) {
            for (ArchivoAdjunto archivo : existingFiles) {
                if (!filesToDelete.contains(archivo)) {
                    tarea.addArchivoAdjunto(archivo);
                }
            }
        }

        // Delete files marked for deletion
        for (ArchivoAdjunto archivo : filesToDelete) {
            FileStorageHelper.deleteFile(archivo.getRutaArchivo());
        }

        // Save newly selected files to storage and add to tarea
        for (Map.Entry<ArchivoAdjunto.TipoArchivo, Uri> entry : selectedFiles.entrySet()) {
            String fileName = selectedFileNames.get(entry.getKey());
            String savedPath = FileStorageHelper.saveFileToStorage(
                    requireContext(),
                    entry.getValue(),
                    fileName);

            if (savedPath != null) {
                ArchivoAdjunto archivo = new ArchivoAdjunto(
                        0, // tareaId will be set later
                        entry.getKey(),
                        fileName,
                        savedPath);
                tarea.addArchivoAdjunto(archivo);
            }
        }

        if (requireActivity() instanceof CrearTareaActivity) {
            ((CrearTareaActivity) requireActivity()).guardarTareaYSalir(tarea);
        } else if (requireActivity() instanceof EditarTareaActivity) {
            ((EditarTareaActivity) requireActivity()).guardarTareaEditada(tarea);
        }
    }
}
