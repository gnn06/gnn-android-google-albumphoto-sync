package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;

public abstract class AppScheduler {

    protected final Context context;

    public AppScheduler(Context context) {
        this.context = context;
    }

    public void registerWorkerObserver(PresenterHome presenter, LifecycleOwner owner) {
        // REgister OBserver
        WorkManager.getInstance(context)
                .getWorkInfosForUniqueWorkLiveData(getWorkName())
                .observe(owner, new Observer<List<WorkInfo>>() {
                    @Override
                    public void onChanged(List<WorkInfo> workInfos) {
                        onWorkerChanged(workInfos, presenter);
                    }
                });
    }

    // When work is RUNNING and finish, last setProgressAsync with FINISHED state is bypasssed
    // workInfo directly contains state ENQUEUED with no outputData

    // When app starts, and register work observer,
    // when enqueued => onWorkedChanged is immediatly called whatever the state (CANCELLED or ENQUEUED)

    // OnChanged is executed immediately after getWorkInfos even for the last Work.
    abstract public void onWorkerChanged(List<WorkInfo> workInfos, PresenterHome presenter);

    abstract public  String getWorkName();
}
