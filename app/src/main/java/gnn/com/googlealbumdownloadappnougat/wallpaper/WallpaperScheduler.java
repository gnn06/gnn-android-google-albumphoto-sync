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

import gnn.com.googlealbumdownloadappnougat.AppScheduler;
import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class WallpaperScheduler extends AppScheduler {

    private static final String TAG = "GNNAPP";

    public WallpaperScheduler(Context context) {
        super(context);
    }

    @Override
    public String getWorkName() {
        return "GNN-WORK-WALLPAPER";
    }

    /**
     * maxAge in minutes
     */
    public void schedule(long wallpaperMaxAgeMinute,
                         ApplicationContext appContext) {
        Logger logger = Logger.getLogger();
        logger.fine("schedule");
        // TODO envoi des argmunent trop laborieux
        Data data = new Data.Builder()
                .putString(WallPaperWorker.PARAM_CACHE_ABSOLUTE_PATH, appContext.getCachePath())
                .putString(WallPaperWorker.PARAM_PROCESS_ABSOLUTE_PATH, appContext.getProcessPath())
                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(WallPaperWorker.class, wallpaperMaxAgeMinute, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag("gnn")
                .build();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(getWorkName(), ExistingPeriodicWorkPolicy.KEEP, work);
        Log.i(TAG,"work enqueued");
    }

    public void cancel() {
        WorkManager.getInstance(context.getApplicationContext())
                .cancelUniqueWork(getWorkName());
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

    @Override
    public void onWorkerChanged(List<WorkInfo> workInfos, PresenterHome presenter) {
        if (workInfos.size() > 0) {
            if (workInfos.get(0).getState().equals(WorkInfo.State.ENQUEUED)) {
                presenter.refreshLastTime();
            }
        }
    }
}
