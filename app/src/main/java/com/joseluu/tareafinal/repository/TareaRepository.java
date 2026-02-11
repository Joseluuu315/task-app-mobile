package com.joseluu.tareafinal.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.joseluu.tareafinal.database.AppDatabase;
import com.joseluu.tareafinal.database.dao.ArchivoAdjuntoDao;
import com.joseluu.tareafinal.database.dao.TareaDao;
import com.joseluu.tareafinal.database.entity.ArchivoAdjuntoEntity;
import com.joseluu.tareafinal.database.entity.TareaEntity;
import com.joseluu.tareafinal.model.ArchivoAdjunto;
import com.joseluu.tareafinal.model.Tarea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class TareaRepository {

    private static TareaRepository INSTANCE;
    private final TareaDao tareaDao;
    private final ArchivoAdjuntoDao archivoAdjuntoDao;
    private final ExecutorService executor;
    private final Handler mainHandler;

    private TareaRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        tareaDao = db.tareaDao();
        archivoAdjuntoDao = db.archivoAdjuntoDao();
        executor = AppDatabase.databaseWriteExecutor;
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static TareaRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TareaRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TareaRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public interface DataCallback<T> {
        void onResult(T result);
    }

    
    public void getTareas(int sortCriterion, boolean ascending, boolean onlyPriority,
            DataCallback<List<Tarea>> callback) {
        executor.execute(() -> {
            
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tareas");

            if (onlyPriority) {
                queryBuilder.append(" WHERE prioritaria = 1");
            }

            queryBuilder.append(" ORDER BY ");

            switch (sortCriterion) {
                case 1: 
                    queryBuilder.append("titulo COLLATE NOCASE");
                    break;
                case 2: 
                    queryBuilder.append("fecha_creacion");
                    break;
                case 3: 
                    
                    queryBuilder.append("fecha_objetivo");
                    break;
                case 4: 
                    queryBuilder.append("progreso");
                    break;
                default:
                    queryBuilder.append("fecha_creacion");
            }

            if (ascending) {
                queryBuilder.append(" ASC");
            } else {
                queryBuilder.append(" DESC");
            }

            SupportSQLiteQuery query = new SimpleSQLiteQuery(queryBuilder.toString());
            List<TareaEntity> entities = tareaDao.getTareas(query);

            
            List<Tarea> tareas = new ArrayList<>();
            for (TareaEntity entity : entities) {
                Tarea tarea = mapEntityToTarea(entity);

                
                List<ArchivoAdjuntoEntity> attachmentEntities = archivoAdjuntoDao.getArchivosByTareaId(entity.getId());
                List<ArchivoAdjunto> attachments = new ArrayList<>();
                for (ArchivoAdjuntoEntity attEntity : attachmentEntities) {
                    attachments.add(mapEntityToAttachment(attEntity));
                }
                tarea.setArchivosAdjuntos(attachments);

                tareas.add(tarea);
            }

            mainHandler.post(() -> callback.onResult(tareas));
        });
    }

    public void addTarea(Tarea tarea, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                TareaEntity entity = mapTareaToEntity(tarea);
                long id = tareaDao.insert(entity);
                tarea.setId((int) id);

                
                if (tarea.getArchivosAdjuntos() != null) {
                    for (ArchivoAdjunto adjunto : tarea.getArchivosAdjuntos()) {
                        adjunto.setTareaId((int) id);
                        archivoAdjuntoDao.insert(mapAttachmentToEntity(adjunto));
                    }
                }

                mainHandler.post(() -> callback.onResult(true));
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> callback.onResult(false));
            }
        });
    }

    public void updateTarea(Tarea tarea, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                TareaEntity entity = mapTareaToEntity(tarea);
                tareaDao.update(entity);

                
                
                
                
                
                archivoAdjuntoDao.deleteByTareaId(tarea.getId());

                if (tarea.getArchivosAdjuntos() != null) {
                    for (ArchivoAdjunto adjunto : tarea.getArchivosAdjuntos()) {
                        adjunto.setTareaId(tarea.getId());
                        archivoAdjuntoDao.insert(mapAttachmentToEntity(adjunto));
                    }
                }

                mainHandler.post(() -> callback.onResult(true));
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> callback.onResult(false));
            }
        });
    }

    public void deleteTarea(Tarea tarea, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                
                tareaDao.delete(mapTareaToEntity(tarea));
                mainHandler.post(() -> callback.onResult(true));
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> callback.onResult(false));
            }
        });
    }

    
    public void getStatistics(DataCallback<StatisticsData> callback) {
        executor.execute(() -> {
            StatisticsData stats = new StatisticsData();
            stats.totalTasks = tareaDao.getTotalTaskCount();
            stats.priorityTasks = tareaDao.getPriorityTaskCount();
            stats.averageProgress = tareaDao.getAverageProgress();

            stats.pendingTasks = tareaDao.getTotalTaskCount() - tareaDao.getCompletedTaskCount();
            stats.completedTasks = tareaDao.getCompletedTaskCount();

            Long avgDate = tareaDao.getAverageTargetDateTimestamp();
            stats.averageTargetDate = avgDate != null ? avgDate : 0;

            stats.count0to25 = tareaDao.getCountByProgressRange(0, 25);
            stats.count26to50 = tareaDao.getCountByProgressRange(26, 50);
            stats.count51to75 = tareaDao.getCountByProgressRange(51, 75);
            stats.count76to100 = tareaDao.getCountByProgressRange(76, 100);

            mainHandler.post(() -> callback.onResult(stats));
        });
    }

    public static class StatisticsData {
        public int totalTasks;
        public int priorityTasks;
        public float averageProgress;
        public int pendingTasks;
        public int completedTasks;
        public long averageTargetDate;

        public int count0to25;
        public int count26to50;
        public int count51to75;
        public int count76to100;
    }

    
    private TareaEntity mapTareaToEntity(Tarea tarea) {
        TareaEntity entity = new TareaEntity(
                tarea.getTitulo(),
                tarea.getFechaCreacion(),
                tarea.getFechaObjectivo(),
                tarea.getProgreso(),
                tarea.isPrioritario(),
                tarea.getDescripcion(),
                
                null, null, null, null);
        if (tarea.getId() != 0) {
            entity.setId(tarea.getId());
        }
        return entity;
    }

    private Tarea mapEntityToTarea(TareaEntity entity) {
        Tarea tarea = new Tarea(
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getProgreso(),
                entity.getFechaCreacion(),
                entity.getFechaObjetivo(),
                entity.isPrioritaria());
        tarea.setId(entity.getId());
        return tarea;
    }

    private ArchivoAdjuntoEntity mapAttachmentToEntity(ArchivoAdjunto adjunto) {
        ArchivoAdjuntoEntity entity = new ArchivoAdjuntoEntity(
                adjunto.getTareaId(),
                adjunto.getTipo(),
                adjunto.getNombreArchivo(),
                adjunto.getRutaArchivo());
        if (adjunto.getId() != 0) {
            entity.setId(adjunto.getId());
        }
        return entity;
    }

    private ArchivoAdjunto mapEntityToAttachment(ArchivoAdjuntoEntity entity) {
        return new ArchivoAdjunto(
                entity.getId(),
                entity.getTareaId(),
                entity.getTipo(),
                entity.getNombreArchivo(),
                entity.getRutaArchivo());
    }
}
