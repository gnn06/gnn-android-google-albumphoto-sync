package gnn.com.googlealbumdownloadappnougat.service;

import androidx.work.WorkInfo;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class PresenterSchedule implements IPresenterSchedule {

    private final IScheduleView view;
    private final ActivitySchedule activity;

    public PresenterSchedule(ActivitySchedule activity)
    {
        this.activity = activity;
        this.view = activity;
    }

    @Override
    public void onInit() {
        WorkInfo info = new SyncScheduler(this.activity).getState();
        view.setStateSync(info);
        WorkInfo stateWallpaper = new WallpaperScheduler(this.activity).getState();
        view.setStateWallpaper(stateWallpaper);
    }

    @Override
    public void onCancelSync() {
        SyncScheduler sched = new SyncScheduler(activity);
        sched.cancel();
    }

    @Override
    public void onCancelWallpaper() {
        WallpaperScheduler sched = new WallpaperScheduler(activity);
        sched.cancel();
    }
}

