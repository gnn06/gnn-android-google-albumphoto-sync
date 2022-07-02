package gnn.com.googlealbumdownloadappnougat.ui.presenter;

public class ScheduleFromFreq {

    private final WallpaperSchedulerWithPermission wlppScheduler;
    private final PresenterFrequencies presenter;

    public ScheduleFromFreq(PresenterFrequencies presenter, WallpaperSchedulerWithPermission wlppSchedulerMock) {
        this.presenter = presenter;
        this.wlppScheduler = wlppSchedulerMock;
    }

    public void scheduleOrCancel() {
        int frequency = presenter.getFrequencyWallpaper();
        if (frequency > 0) {
            wlppScheduler.schedule(frequency, -1, -1);
        }
    }
}
