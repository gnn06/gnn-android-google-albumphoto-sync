package gnn.com.googlealbumdownloadappnougat.ui.presenter;

public class ScheduleFromFreq {

    private final WallpaperSchedulerWithPermission wlppScheduler;
    private final PresenterFrequencies presenter;

    public ScheduleFromFreq(PresenterFrequencies presenter, WallpaperSchedulerWithPermission wlppSchedulerMock) {
        this.presenter = presenter;
        this.wlppScheduler = wlppSchedulerMock;
    }

    public void scheduleOrCancel() {
        if (presenter.getFrequencyWallpaper() > 0) {
            wlppScheduler.schedule(
                    presenter.getFrequencyWallpaper(),
                    presenter.getFrequencySyncMinute(),
                    presenter.getFrequencyUpdatePhotosHour());
        }
    }
}
