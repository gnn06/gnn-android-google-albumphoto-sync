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

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;

/**
 * Schedule Synchronizer through Worker via Android WorkManager API
 */
public class Scheduler {

    private static final String TAG = "Scheduler";
    static final String WORK_NAME = "mywork";

    private final Context context;

    public Scheduler(Context context) {
        this.context = context;
    }

    // TODO: 19/03/21 comment récupérer le statut d'un uniquePeriodic
    // TODO: 19/03/21 que se passe-t-il si on change la fréquence d'un uniquePeriodic

    public void schedule(String album, String destinationFolder, String rename, int quantity, int intervalHour, ApplicationContext appContext) {
        Data data = new Data.Builder()
                .putString("cacheAbsolutePath", appContext.getCachePath())
                .putString("processAbsolutePath", appContext.getProcessPath())
                .putLong("cacheMaxAge", -1)
                .putString("album", album)
                .putString("folderPath", destinationFolder)
                .putString("rename", rename)
                .putInt("quantity", quantity)

                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(MyWorker.class, intervalHour, TimeUnit.HOURS)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, work);
    }

    public void cancel() {
        WorkManager.getInstance(context.getApplicationContext())
            .cancelUniqueWork(WORK_NAME);
        Log.i(TAG, "work canceled");
    }

    WorkInfo getState() {
        ListenableFuture<List<WorkInfo>> futureInfo = WorkManager.getInstance(context.getApplicationContext())
                .getWorkInfosForUniqueWork(WORK_NAME);
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