package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;

public class SchedulerWallpaper {

    private static final String WORK_NAME_WALLPAPER = "GNN-WORK_WALLPAPER";
    private static final String TAG = "SchedulerWapper";

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
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(WallPaperWorker.class, intervalHour, TimeUnit.HOURS)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME_WALLPAPER, ExistingPeriodicWorkPolicy.KEEP, work);
    }

    public void cancel() {
        WorkManager.getInstance(context.getApplicationContext())
                .cancelUniqueWork(WORK_NAME_WALLPAPER);
        Log.i(TAG, "work canceled");
    }
}
