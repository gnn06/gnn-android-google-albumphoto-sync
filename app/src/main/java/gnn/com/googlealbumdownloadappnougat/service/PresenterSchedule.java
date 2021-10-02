package gnn.com.googlealbumdownloadappnougat.service;

import androidx.work.WorkInfo;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistenceMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.SyncData;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

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
        WorkInfo info = new SyncScheduler(this.activity).getState();
        view.setStateSync(info);
        WorkInfo stateWallpaper = new WallpaperScheduler(this.activity).getState();
        view.setStateWallpaper(stateWallpaper);
    }

    @Override
    public void onScheduleSync() {
        SyncScheduler sched = new SyncScheduler(activity);
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
        SyncScheduler sched = new SyncScheduler(activity);
        sched.cancel();
    }

    @Override
    public void onScheduleWallpaper() {
        WallpaperScheduler sched = new WallpaperScheduler(activity);
        SyncData data = new PersistenceMain(activity).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(activity);
        // TODO get interval from UI
        int interval = 1;
        sched.schedule(data.getFolderHuman(), interval, appContext);
    }

    @Override
    public void onCancelWallpaper() {
        WallpaperScheduler sched = new WallpaperScheduler(activity);
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

