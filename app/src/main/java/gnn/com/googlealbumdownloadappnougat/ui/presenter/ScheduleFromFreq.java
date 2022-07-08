package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;

public class ScheduleFromFreq {

    private final WallpaperSchedulerWithPermission wlppScheduler;
    private final PresenterFrequencies presenter;

    public ScheduleFromFreq(PresenterFrequencies presenter) {
        this.presenter = presenter;
        this.wlppScheduler = ServiceLocator.getInstance().getWallpaperSchedulerWithPermission();
    }

    // For Test
    public ScheduleFromFreq(PresenterFrequencies presenter, WallpaperSchedulerWithPermission wlppSchedulerMock) {
        this.presenter = presenter;
        this.wlppScheduler = wlppSchedulerMock;
    }

    public void scheduleOrCancel() {
        if (presenter.getFrequencyWallpaper() != Frequency.NEVER
        || presenter.getFrequencySyncHour() != Frequency.NEVER
        || presenter.getFrequencyUpdatePhotos() != Frequency.NEVER) {
            wlppScheduler.schedule(
                    presenter.getFrequencyWallpaper(),
                    presenter.getFrequencySyncMinute(),
                    presenter.getFrequencyUpdatePhotosHour());
        } else {
            wlppScheduler.cancel();
        }
    }
}
