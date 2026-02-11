package com.joseluu.tareafinal.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.joseluu.tareafinal.database.entity.TareaEntity;

import java.util.List;

@Dao
public interface TareaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TareaEntity tarea);

    @Update
    void update(TareaEntity tarea);

    @Delete
    void delete(TareaEntity tarea);

    @Query("SELECT * FROM tareas WHERE id = :id")
    TareaEntity getTareaById(int id);

    
    @RawQuery
    List<TareaEntity> getTareas(SupportSQLiteQuery query);

    

    @Query("SELECT COUNT(*) FROM tareas")
    int getTotalTaskCount();

    @Query("SELECT COUNT(*) FROM tareas WHERE prioritaria = 1")
    int getPriorityTaskCount();

    @Query("SELECT AVG(progreso) FROM tareas")
    float getAverageProgress();

    
    @Query("SELECT COUNT(*) FROM tareas WHERE progreso >= :min AND progreso <= :max")
    int getCountByProgressRange(int min, int max);

    @Query("SELECT AVG(fecha_objetivo) FROM tareas")
    Long getAverageTargetDateTimestamp();

    @Query("SELECT COUNT(*) FROM tareas WHERE progreso = 100")
    int getCompletedTaskCount();

    @Query("SELECT COUNT(*) FROM tareas WHERE fecha_objetivo < :timestamp AND progreso < 100")
    int getOverdueTaskCount(long timestamp);

    @Query("SELECT * FROM tareas")
    List<TareaEntity> getAllTareasList();
}
