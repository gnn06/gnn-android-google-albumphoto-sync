package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ScheduleFromFreq {

    private final WallpaperSchedulerWithPermission wlppScheduler;
    private final PresenterFrequencies presenter;
    private final WallpaperScheduler scheduler;

    public ScheduleFromFreq(PresenterFrequencies presenter, WallpaperSchedulerWithPermission wlppSchedulerMock, WallpaperScheduler scheduler) {
        this.presenter = presenter;
        this.wlppScheduler = wlppSchedulerMock;
        this.scheduler = scheduler;
    }

    public void scheduleOrCancel() {
        if (presenter.getFrequencyWallpaper() > 0
        || presenter.getFrequencySyncMinute() > 0
        || presenter.getFrequencyUpdatePhotosHour() > 0) {
            wlppScheduler.schedule(
                    presenter.getFrequencyWallpaper(),
                    presenter.getFrequencySyncMinute(),
                    presenter.getFrequencyUpdatePhotosHour());
        } else {
            scheduler.cancel();
        }
    }
}
