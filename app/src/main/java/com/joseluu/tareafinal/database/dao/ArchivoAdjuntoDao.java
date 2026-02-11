package com.joseluu.tareafinal.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.joseluu.tareafinal.database.entity.ArchivoAdjuntoEntity;

import java.util.List;

@Dao
public interface ArchivoAdjuntoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ArchivoAdjuntoEntity archivo);

    @Update
    void update(ArchivoAdjuntoEntity archivo);

    @Delete
    void delete(ArchivoAdjuntoEntity archivo);

    @Query("DELETE FROM archivos_adjuntos WHERE tarea_id = :tareaId")
    void deleteByTareaId(int tareaId);

    @Query("SELECT * FROM archivos_adjuntos WHERE tarea_id = :tareaId")
    List<ArchivoAdjuntoEntity> getArchivosByTareaId(int tareaId);

    @Query("SELECT * FROM archivos_adjuntos")
    List<ArchivoAdjuntoEntity> getAllArchivos();
}
