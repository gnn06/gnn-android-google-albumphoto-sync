package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.service.SyncWorker;

public class SchedulerWallpaper {

    private static final String WORK_NAME_WALLPAPER = "GNN-WORK_WALLPAPER";
    final private Context context;

    public SchedulerWallpaper(Context context) {
        this.context = context;
    }

    public void schedule(String destinationFolder, int intervalHour, ApplicationContext appContext) {
        // TODO put cacheMax in params
        Data data = new Data.Builder()
                .putString("cacheAbsolutePath", appContext.getCachePath())
                .putString("processAbsolutePath", appContext.getProcessPath())
                .putLong("cacheMaxAge", -1)
                .putString("folderPath", destinationFolder)

                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(SyncWorker.class, intervalHour, TimeUnit.HOURS)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME_WALLPAPER, ExistingPeriodicWorkPolicy.KEEP, work);
    }
}
