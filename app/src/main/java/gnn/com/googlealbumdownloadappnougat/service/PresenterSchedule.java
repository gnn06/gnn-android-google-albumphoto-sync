package gnn.com.googlealbumdownloadappnougat.service;

import androidx.work.WorkInfo;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistenceMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.SyncData;
import gnn.com.googlealbumdownloadappnougat.wallpaper.SchedulerWallpaper;

public class PresenterSchedule implements IPresenterSchedule {

    private static final String TAG = "Schedule";
    public static int DefaultInterval = 24;

    private final IScheduleView view;
    private final ActivitySchedule activity;

    public PresenterSchedule(ActivitySchedule activity)
    {
        this.activity = activity;
        this.view = activity;
    }

    @Override
    public void onInit() {
        new PersistenceSchedule(this.activity).restore(this);
        WorkInfo info = new SchedulerSync(this.activity).getState();
        view.setState(info);
    }

    @Override
    public void onScheduleSync() {
        SchedulerSync sched = new SchedulerSync(activity);
        SyncData data = new PersistenceMain(activity).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(activity);
        int interval = view.getIntervalSync();
        sched.schedule(data.getAlbum(),
                data.getFolderHuman(),
                data.getRename(),
                data.getQuantity(),
                interval,
                appContext);
    }

    @Override
    public void onCancelSync() {
        SchedulerSync sched = new SchedulerSync(activity);
        sched.cancel();
    }

    @Override
    public void onScheduleWallpaper() {
        SchedulerWallpaper sched = new SchedulerWallpaper(activity);
        SyncData data = new PersistenceMain(activity).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(activity);
        // TODO get interval from UI
        int interval = 1;
        sched.schedule(data.getFolderHuman(), interval, appContext);
    }

    @Override
    public void onCancelWallpaper() {
        SchedulerWallpaper sched = new SchedulerWallpaper(activity);
        sched.cancel();
    }


    @Override
    public int getIntervalSync() {
        return view.getIntervalSync();
    }

    @Override
    public void setIntervalSync(int interval) {
        view.setIntervalSync(interval);
    }
}

