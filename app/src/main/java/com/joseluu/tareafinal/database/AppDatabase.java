package com.joseluu.tareafinal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.joseluu.tareafinal.database.dao.ArchivoAdjuntoDao;
import com.joseluu.tareafinal.database.dao.TareaDao;
import com.joseluu.tareafinal.database.entity.ArchivoAdjuntoEntity;
import com.joseluu.tareafinal.database.entity.TareaEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { TareaEntity.class, ArchivoAdjuntoEntity.class }, version = 1, exportSchema = false)
@TypeConverters({ Converters.class })
public abstract class AppDatabase extends RoomDatabase {

    public abstract TareaDao tareaDao();

    public abstract ArchivoAdjuntoDao archivoAdjuntoDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "tareas_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
