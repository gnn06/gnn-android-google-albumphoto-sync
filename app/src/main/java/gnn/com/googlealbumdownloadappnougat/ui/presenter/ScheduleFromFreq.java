package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ScheduleFromFreq {

    private final WallpaperSchedulerWithPermission wlppScheduler;
    private final PresenterFrequencies presenter;

    public ScheduleFromFreq(PresenterFrequencies presenter, WallpaperSchedulerWithPermission wlppSchedulerMock) {
        this.presenter = presenter;
        this.wlppScheduler = wlppSchedulerMock;
    }

    public void scheduleOrCancel() {
        if (presenter.getFrequencyWallpaper() > 0
        || presenter.getFrequencySyncHour() > 0
        || presenter.getFrequencyUpdatePhotos() > 0) {
            wlppScheduler.schedule(
                    presenter.getFrequencyWallpaper(),
                    presenter.getFrequencySyncMinute(),
                    presenter.getFrequencyUpdatePhotosHour());
        } else {
            wlppScheduler.cancel();
        }
    }
}
