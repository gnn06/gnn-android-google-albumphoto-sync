package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.AppScheduler;
import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallPaperWorker;

/**
 * Schedule Synchronizer through Worker via Android WorkManager API
 */
public class SyncScheduler extends AppScheduler {

    private static final String TAG = "Scheduler";

    public SyncScheduler(Context context) {
        super(context);
    }

    @Override
    public String getWorkName() {
        return "GNN-WORK-SYNC";
    }

    void schedule(String album, String destinationFolder, String rename, int quantity, int intervalHour, ApplicationContext appContext) {
        Data data = new Data.Builder()
                .putString(WallPaperWorker.PARAM_CACHE_ABSOLUTE_PATH, appContext.getCachePath())
                .putString(WallPaperWorker.PARAM_PROCESS_ABSOLUTE_PATH, appContext.getProcessPath())
                .putString(WallPaperWorker.PARAM_ALBUM, album)
                .putString(WallPaperWorker.PARAM_FOLDER_PATH, destinationFolder)
                .putString(WallPaperWorker.PARAM_RENAME, rename)
                .putInt(WallPaperWorker.PARAM_QUANTITY, quantity)

                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(SyncWorker.class, intervalHour, TimeUnit.HOURS)
                .setInputData(data)
                .addTag("gnn")
                .build();
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(getWorkName(), ExistingPeriodicWorkPolicy.KEEP, work);
    }

    public void schedule(int intervalHour) {
        PersistPrefMain persist = ServiceLocator.getInstance().getPersistMain();
        String album = persist.getAlbum();
        String folder = persist.getPhotoPath();
        String rename = persist.getRename();
        int quantity = persist.getQuantity();
        ApplicationContext appContext = ApplicationContext.getInstance(context);
        schedule(album, folder, rename, quantity, intervalHour, appContext);
    }

    public void cancel() {
        WorkManager.getInstance(context.getApplicationContext())
            .cancelUniqueWork(getWorkName());
        Log.i(TAG, "work canceled");
    }

    WorkInfo getState() {
        ListenableFuture<List<WorkInfo>> futureInfo = WorkManager.getInstance(context.getApplicationContext())
                .getWorkInfosForUniqueWork(getWorkName());
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

    void dumpWorker() {
        try {
            // "e248680d-778f-41b8-9992-79da52fb8d83"
            WorkManager workManager = WorkManager.getInstance(context.getApplicationContext());
            ListenableFuture<List<WorkInfo>> infos = workManager.getWorkInfosByTag("gnn.com.googlealbumdownloadappnougat.service.MyWorker");
            Log.i(TAG, "getWorkInfosByTag.size= " + infos.get().size());
            for (Iterator<WorkInfo> it = infos.get().iterator(); it.hasNext(); ) {
                WorkInfo info = it.next();
                Log.i(TAG, String.valueOf(info.getState().isFinished()));
            }
//            workManager.cancelAllWorkByTag("gnn.com.googlealbumdownloadappnougat.service.MyWorker");
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "onCreate: worker statut", e);
        }
    }
}