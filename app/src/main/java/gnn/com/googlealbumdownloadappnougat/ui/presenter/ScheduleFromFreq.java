package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ScheduleFromFreq {

    private int frequencyWallpaper;

    private final WallpaperSchedulerWithPermission wlppScheduler;

    public ScheduleFromFreq(int frequencyWallpaper, WallpaperSchedulerWithPermission wlppSchedulerMock) {
        this.frequencyWallpaper = frequencyWallpaper;
        this.wlppScheduler = wlppSchedulerMock;
    }

    public void setFrequencyWallpaper(int frequency) {
        this.frequencyWallpaper = frequency;
        if (frequency > 0) {
            wlppScheduler.schedule(this.frequencyWallpaper, -1, -1);
        }
    }
}
