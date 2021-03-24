package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Schedule Synchronizer through Worker via Android WorkManager API
 */
public class Scheduler {

    private static final String TAG = "Scheduler";
    public static final String WORK_NAME = "mywork";

    private final Context context;

    public Scheduler(Context context) {
        this.context = context;
    }

    // TODO: 19/03/21 comment récupérer le statut d'un uniquePeriodic
    // TODO: 19/03/21 que se passe-t-il si on change la fréquence d'un uniquePeriodic

    void schedule() {
        // TODO rajouter les paramétres
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(context.getApplicationContext())
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, work);
    }

    void cancel() {
        WorkManager.getInstance(context.getApplicationContext())
            .cancelUniqueWork(WORK_NAME);
    }

    void getState() {
        WorkManager.getInstance(context.getApplicationContext())
                .getWorkInfosForUniqueWork(WORK_NAME);
    }

    void dumpWorker() {
        try {
            // "e248680d-778f-41b8-9992-79da52fb8d83"
            WorkManager workManager = WorkManager.getInstance(context.getApplicationContext());
            ListenableFuture<List<WorkInfo>> infos = workManager.getWorkInfosByTag("gnn.com.googlealbumdownloadappnougat.MyWorker");
            Log.i(TAG, "getWorkInfosByTag.size= " + infos.get().size());
            for (Iterator<WorkInfo> it = infos.get().iterator(); it.hasNext(); ) {
                WorkInfo info = it.next();
                Log.i(TAG, String.valueOf(info.getState().isFinished()));
            }
//            workManager.cancelAllWorkByTag("gnn.com.googlealbumdownloadappnougat.MyWorker");
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "onCreate: worker statut", e);
        }
    }
}