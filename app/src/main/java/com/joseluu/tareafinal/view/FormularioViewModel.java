package com.joseluu.tareafinal.view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.joseluu.tareafinal.model.ArchivoAdjunto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormularioViewModel extends ViewModel {

    public MutableLiveData<String> titulo = new MutableLiveData<>();
    public MutableLiveData<Date> fechaCreacion = new MutableLiveData<>();
    public MutableLiveData<Date> fechaObjetivo = new MutableLiveData<>();
    public MutableLiveData<Integer> progreso = new MutableLiveData<>();
    public MutableLiveData<Boolean> prioritaria = new MutableLiveData<>();
    public MutableLiveData<String> descripcion = new MutableLiveData<>();
    public MutableLiveData<List<ArchivoAdjunto>> archivosAdjuntos = new MutableLiveData<>(new ArrayList<>());

}
