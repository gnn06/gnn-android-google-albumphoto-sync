package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class WallpaperScheduler {

    private static final String WORK_NAME_WALLPAPER = "GNN-WORK_WALLPAPER";
    private static final String TAG = "SchedulerWapper";

    final private Context context;

    public WallpaperScheduler(Context context) {
        this.context = context;
    }

    /**
     * maxAge in minutes
     */
    public void schedule(String destinationFolder, long wallpaperMaxAgeMinute,
                         String album, int quantity, String rename,
                         ApplicationContext appContext) {
        Logger logger = Logger.getLogger();
        logger.fine("schedule");
        // TODO envoi des argmunent trop laborieux
        Data data = new Data.Builder()
                .putString(WallPaperWorker.PARAM_CACHE_ABSOLUTE_PATH, appContext.getCachePath())
                .putString(WallPaperWorker.PARAM_PROCESS_ABSOLUTE_PATH, appContext.getProcessPath())
                .putString(WallPaperWorker.PARAM_FOLDER_PATH, destinationFolder)
                .putString(WallPaperWorker.PARAM_ALBUM, album)
                .putString(WallPaperWorker.PARAM_FOLDER_PATH, destinationFolder)
                .putInt(WallPaperWorker.PARAM_QUANTITY, quantity)
                .putString(WallPaperWorker.PARAM_RENAME, rename)
                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(WallPaperWorker.class, wallpaperMaxAgeMinute, TimeUnit.MINUTES)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME_WALLPAPER, ExistingPeriodicWorkPolicy.KEEP, work);
        Log.i(TAG,"work enqueued");
    }

    public void cancel() {
        WorkManager.getInstance(context.getApplicationContext())
                .cancelUniqueWork(WORK_NAME_WALLPAPER);
        Log.i(TAG, "work canceled");
    }

    public boolean isScheduled() {
        WorkInfo state = getState();
        if (state != null) {
            return WorkInfo.State.ENQUEUED.toString().equals(state.getState().name())
                    || WorkInfo.State.RUNNING.toString().equals(state.getState().name());
        } else {
            return false;
        }
    }

    public WorkInfo getState() {
        ListenableFuture<List<WorkInfo>> futureInfo = WorkManager.getInstance(context.getApplicationContext())
                .getWorkInfosForUniqueWork(WORK_NAME_WALLPAPER);
        try {
            if (futureInfo.get().size() >= 1) {
                WorkInfo info = futureInfo.get().get(0);
                return info;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
