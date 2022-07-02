package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ScheduleFromFreq {

    private int frequencyWallpaper;

    private final WallpaperScheduler wlppScheduler;

    public ScheduleFromFreq(int frequencyWallpaper, WallpaperScheduler wlppSchedulerMock) {
        this.frequencyWallpaper = frequencyWallpaper;
        this.wlppScheduler = wlppSchedulerMock;
    }

    public void setFrequencyWallpaper(int frequency) {
        this.frequencyWallpaper = frequency;
    }
}
