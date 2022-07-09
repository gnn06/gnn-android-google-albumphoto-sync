package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;

public class ScheduleFromFreq {

    private final WallpaperSchedulerWithPermission wlppScheduler;
    private final SyncScheduler syncScheduler;
    private final PresenterFrequencies presenter;

    public ScheduleFromFreq(PresenterFrequencies presenter) {
        this.presenter = presenter;
        this.wlppScheduler = ServiceLocator.getInstance().getWallpaperSchedulerWithPermission();
        this.syncScheduler = ServiceLocator.getInstance().getSyncScheduler();
    }

    // For Test
    public ScheduleFromFreq(PresenterFrequencies presenter, WallpaperSchedulerWithPermission wlppSchedulerMock, SyncScheduler syncSchedulerMock) {
        this.presenter = presenter;
        this.wlppScheduler = wlppSchedulerMock;
        this.syncScheduler = syncSchedulerMock;
    }

    public void scheduleOrCancel() {
        if (presenter.getFrequencyWallpaper() != Frequency.NEVER) {
            wlppScheduler.schedule(
                    presenter.getFrequencyWallpaper(),
                    presenter.getFrequencySyncMinute(),
                    presenter.getFrequencyUpdatePhotosHour());
        } else if (presenter.getFrequencySyncHour() != Frequency.NEVER) {
            syncScheduler.schedule(presenter.getFrequencySyncHour());
        } else {
            wlppScheduler.cancel();
        }
    }
}
